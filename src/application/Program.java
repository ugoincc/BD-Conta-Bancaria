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
        while (true) {
            Scanner scanner = new Scanner(System.in);
            System.out.println("\nEscolha uma opção:");
            System.out.println("1 - Listar Clientes e Empresas");
            System.out.println("2 - Listar Bancos do Cliente");
            System.out.println("3 - Emitir Extrato");
            System.out.println("0 - Fechar Programa");
            String menuOption = scanner.next();

            if (menuOption.equals("1")) {
                listarClientes();
            } else if (menuOption.equals("2")) {
                listarContas(solicitarDoc());
            } else if (menuOption.equals("3")) {
                emitirExtrato();
            }
            if (menuOption.equals("0")) {
                System.out.println("Encerrando o programa.");
                DB.closeConnecction(); // Fechando a conexão ao encerrar o programa
                break;
            }
        }


    }

    private static void listarContas(String documento) {
        String tipoDocumento = "";
        if (documento.length() == 11) {
            tipoDocumento = "cpf";
        } else if (documento.length() == 14) {
            tipoDocumento = "cnpj";
        }

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

            System.out.printf("%-20s | %-15s | %-15s | %-20s | %-15s | %-20s | %-10s%n",
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

                System.out.printf("%-20s | %-15s | %-15s | %-20s | %-15s | %-20s | %-10.2f%n",
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
        String documento = "";

        while (true) {
            System.out.println("Digite o número do documento(CPF ou CNPJ)");
            documento = scanner.nextLine();
            if (documento.length() == 11 || documento.length() == 14) {
                return documento;
            } else {
                System.out.println("Documento inválido digite um CPF ou um CNPJ");
            }
        }
    }


    private static void emitirExtrato() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Digite o número da conta: ");
        String numeroConta = scanner.nextLine();

        System.out.print("Digite a data de início (yyyy-MM-dd): ");
        String dataInicio = scanner.nextLine();

        System.out.print("Digite a data de fim (yyyy-MM-dd): ");
        String dataFim = scanner.nextLine();

        // Converte as strings para java.sql.Date
        java.sql.Date sqlDataInicio = java.sql.Date.valueOf(dataInicio);
        java.sql.Date sqlDataFim = java.sql.Date.valueOf(dataFim);

        Connection conn = DB.getConnection();
        String sql = "SELECT \n" +
                "    t.dataTransacao AS Data, \n" +
                "    t.codTransacao AS CodTransacao, \n" +
                "    tt.indicador AS Tipo, \n" +
                "    t.descricaoTransacao AS Descrição, \n" +
                "    t.valorTransacao AS Valor \n" +
                "FROM Transacao t\n" +
                "JOIN ContaBancaria cb ON t.idConta = cb.idConta\n" +
                "JOIN Cliente c ON cb.idCliente = c.idCliente\n" +
                "JOIN TipoTransacao tt ON t.idTipoTransacao = tt.idTipoTransacao\n" +
                "WHERE cb.numeroConta = ? \n" +
                "AND t.dataTransacao BETWEEN ? AND ?\n" +
                "ORDER BY t.dataTransacao;";

        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, numeroConta);
            stmt.setDate(2, sqlDataInicio);
            stmt.setDate(3, sqlDataFim);

            ResultSet rs = stmt.executeQuery();

            // Cabeçalho da tabela
            System.out.printf("%-10s | %-12s | %-15s | %-20s | %-10s%n",
                    "Data",
                    "Cod Transacao",
                    "Tipo",
                    "Descrição",
                    "Valor");

            while (rs.next()) {
                Date dataTransacao = rs.getDate("Data");
                int codTransacao = rs.getInt("CodTransacao");
                String tipo = rs.getString("Tipo");
                String descricao = rs.getString("Descrição");
                float valor = rs.getFloat("Valor");

                System.out.printf("%-10s | %-12d | %-15s | %-20s | %-10.2f%n",
                        dataTransacao.toString(),
                        codTransacao,
                        tipo,
                        descricao,
                        valor);
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}