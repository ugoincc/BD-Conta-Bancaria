package application;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Scanner;

import db.DB;

public class Program {

    public static void main(String[] args) {
                while ( true )
                {
                Scanner scanner = new Scanner(System.in);
                    System.out.println("\nEscolha uma opção:");
                    System.out.println("1 - Listar Clientes e Empresas");
                    System.out.println("2 - Listar Bancos do Cliente");
                    System.out.println("3 - Emitir Extrato");
                    System.out.println("0 - Fechar Programa");
                    String menuOption = scanner.next();

                    if (menuOption.equals("1"))
                    {
                        listarClientes();
                    }
                    else if(menuOption.equals("2"))
                    {
                        listarContas(solicitarDoc());
                    }
                    else if(menuOption.equals("3"))
                    {
                        solicitarDoc();
                    }
                    if (menuOption.equals("0"))
                    {
                        System.out.println("Encerrando o programa.");
                        DB.closeConnecction(); // Fechando a conexão ao encerrar o programa
                        break;
                    }
                }


    }

    private static void listarContas(String documento) {
        String tipoDocumento = documento.length() == 11 ? "cpf" : "cnpj";
        System.out.println("Listando Contas:");
        Connection conn = DB.getConnection();
        String sql = "SELECT \n" +
                "    c.nomeCliente, \n" +
                "    cb.numeroConta,\n" +
                "    b.nomeBanco AS Banco,\n" +
                "    a.nomeAgencia AS Agencia,\n" +
                "    tc.tipoConta AS TipoDaConta,\n" +
                "    cb.dataAbertura AS DataDeAbertura,\n" +
                "    cb.saldoConta AS SaldoAtual\n" +
                "FROM Cliente c\n" +
                "JOIN ContaBancaria cb ON c.idCliente = cb.idCliente\n" +
                "JOIN banco b ON cb.idBanco = b.idBanco\n" +
                "JOIN agencia a ON cb.idAgencia = a.idAgencia\n" +
                "JOIN TipoConta tc ON cb.idTipoConta = tc.idTipoConta\n" +
                "WHERE c." + tipoDocumento + " = ?";

        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, documento);  // Substitua pelo CPF/CNPJ desejado
            ResultSet rs = stmt.executeQuery();

            System.out.printf("%-20s | %-15s | %-15s | %-10s | %-15s | %-20s | %-10s%n",
                    "Nome",
                    "Numero Conta",
                    "Banco",
                    "Agencia",
                    "TipoDaConta",
                    "DataDeAbertura",
                    "SaldoAtual");

            while (rs.next()) {
                String nomeCliente = rs.getString("nomeCliente");
                String numeroConta = rs.getString("numeroConta");
                String banco = rs.getString("Banco");
                String agencia = rs.getString("Agencia");
                String tipoDaConta = rs.getString("TipoDaConta");
                Date dataAbertura = rs.getDate("DataDeAbertura");
                float saldoAtual = rs.getFloat("SaldoAtual");

                System.out.printf("%-20s | %-15s | %-15s | %-10s | %-15s | %-20s | %-10.2f%n",
                        nomeCliente,
                        numeroConta,
                        banco,
                        agencia,
                        tipoDaConta,
                        (dataAbertura != null ? dataAbertura.toString() : "N/A"),
                        saldoAtual);
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void listarClientes() {
        System.out.println("Listando Clientes:");
        Connection conn = DB.getConnection();
        String sql = "SELECT \n" +
                "    c.idCliente, \n" +
                "    c.nomeCliente, \n" +
                "    c.tipoCliente, \n" +
                "    c.CPF, \n" +
                "    c.CNPJ\n" +
                "FROM Cliente c\n" +
                "ORDER BY c.idCliente;\n";

        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            System.out.printf("%-10s | %-20s | %-15s | %-15s | %-20s%n", "ID Cliente", "Nome", "Tipo de Cliente", "CPF", "CNPJ");
            while (rs.next()) {
                int idCliente = rs.getInt("idCliente");
                String nomeCliente = rs.getString("nomeCliente");
                String tipoCliente = rs.getString("tipoCliente");
                String CPF = rs.getString("CPF");
                String CNPJ = rs.getString("CNPJ");
                System.out.printf("%-10d | %-20s | %-15s | %-15s | %-20s%n",
                        idCliente,
                        nomeCliente,
                        tipoCliente,
                        (CPF != null ? CPF : "N/A"),
                        (CNPJ != null ? CNPJ : "N/A"));

            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static String solicitarDoc() {
        Scanner scanner = new Scanner(System.in);
        String tipoBusca = "";

        while (true) {
            System.out.println("Você deseja buscar por CPF ou CNPJ?");
            tipoBusca = scanner.nextLine().toLowerCase();

            if (tipoBusca.equals("cpf")) {
                System.out.println("Digite o CPF do cliente:");
                String cpf = scanner.nextLine();
                return cpf;
            } else if (tipoBusca.equals("cnpj")) {
                System.out.println("Digite o CNPJ da empresa:");
                String cnpj = scanner.nextLine();
                return cnpj;
            } else {
                System.out.println("Opção inválida, por favor escolha 'cpf' ou 'cnpj'.");
            }
        }
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
