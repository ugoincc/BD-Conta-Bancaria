package application;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

import db.DB;

public class Program {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean continuarBusca = true;

        while (continuarBusca) {
            System.out.println("Você deseja buscar por CPF ou CNPJ?");
            String tipoBusca = scanner.nextLine().toLowerCase();

            if (tipoBusca.equals("cpf")) {
                System.out.println("Digite o CPF do cliente:");
                String cpf = scanner.nextLine();
                buscarClientePorCpfOuCnpj(cpf, "CPF");
            } else if (tipoBusca.equals("cnpj")) {
                System.out.println("Digite o CNPJ da empresa:");
                String cnpj = scanner.nextLine();
                buscarClientePorCpfOuCnpj(cnpj, "CNPJ");
            } else {
                System.out.println("Opção inválida, por favor escolha 'cpf' ou 'cnpj'.");
                continue;
            }

            System.out.println("Deseja buscar outro cliente ou empresa? (sim/nao)");
            String resposta = scanner.nextLine().toLowerCase();

            if (!resposta.equals("sim")) {
                continuarBusca = false;
            }
        }

        System.out.println("Encerrando o programa.");
        scanner.close();
        DB.closeConnecction(); // Fechando a conexão ao encerrar o programa
    }

    private static void buscarClientePorCpfOuCnpj(String documento, String tipoDocumento) {
        Connection conn = DB.getConnection();
        String sql = "SELECT c.nomeCliente, tr.codTransacao, tr.dataTransacao, tr.descricaoTransacao, tr.valorTransacao, t.tipoConta, tt.indicador " +
                     "FROM Cliente c " +
                     "JOIN ContaBancaria cb ON c.idCliente = cb.idCliente " +
                     "JOIN Transacao tr ON cb.idConta = tr.idConta " +
                     "JOIN TipoConta t ON cb.idTipoConta = t.idTipoConta " +
                     "JOIN TipoTransacao tt ON tr.idTipoTransacao = tt.idTipoTransacao " +
                     "WHERE c." + tipoDocumento + " = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, documento);
            ResultSet rs = stmt.executeQuery();

            if (!rs.next()) {
                System.out.println("Nenhum cliente ou empresa encontrado com este " + tipoDocumento + ".");
                return;
            }

            System.out.println(tipoDocumento.equals("CPF") ? "Cliente encontrado:" : "Empresa encontrada:");
            do {
                String nomeCliente = rs.getString("nomeCliente");
                int codTransacao = rs.getInt("codTransacao");
                String dataTransacao = rs.getString("dataTransacao");
                String descricaoTransacao = rs.getString("descricaoTransacao");
                String tipoConta = rs.getString("tipoConta");
                String indicador = rs.getString("indicador");
                double valorTransacao = rs.getDouble("valorTransacao");

                System.out.println("Nome do Cliente/Empresa: " + nomeCliente);
                System.out.println("Código da Transação: " + codTransacao);
                System.out.println("Data: " + dataTransacao);
                System.out.println("Descrição: " + descricaoTransacao);
                System.out.println("Tipo de Conta: " + tipoConta);
                System.out.println("Tipo de Transação: " + indicador);
                System.out.println("Valor da Transação: " + valorTransacao);
                System.out.println("---------------------------------------------------");
            } while (rs.next());

        } catch (SQLException e) {
            System.out.println("Erro ao realizar operação no banco de dados: " + e.getMessage());
        }
    }
}
