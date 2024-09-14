package services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.text.SimpleDateFormat;

public class ClienteService {

    private Connection conn;

    public ClienteService(Connection conn) {
        this.conn = conn;
    }

    public void listarTodosClientes() {
        String sql = "SELECT nomeCliente, CPF, CNPJ FROM Cliente";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();

            System.out.println("Lista de todos os clientes:");
            while (rs.next()) {
                String nomeCliente = rs.getString("nomeCliente");

                // Exibir CPF ou CNPJ apenas se não forem nulos
                String cpf = rs.getString("CPF");
                String cnpj = rs.getString("CNPJ");

                System.out.println("Nome: " + nomeCliente);

                if (cpf != null && !cpf.isEmpty()) {
                    System.out.println("CPF: " + cpf);
                }
                if (cnpj != null && !cnpj.isEmpty()) {
                    System.out.println("CNPJ: " + cnpj);
                }

                System.out.println("--------------------------------");
            }

        } catch (SQLException e) {
            System.out.println("Erro ao listar os clientes: " + e.getMessage());
        }
    }


    public void pesquisarCliente(String documento) {
        String sql = "SELECT c.nomeCliente, c.CPF, c.CNPJ, e.CEP, e.idLogradouro, l.nomeLogradouro, ci.nomeCidade, " +
                     "b.nomeBairro, f.numTelefone, em.enderecoEmail " +
                     "FROM Cliente c " +
                     "JOIN Endereco e ON c.idEndereco = e.idEndereco " +
                     "JOIN Logradouro l ON e.idLogradouro = l.idLogradouro " +
                     "JOIN Cidade ci ON e.idCidade = ci.idCidade " +
                     "JOIN Bairro b ON e.idBairro = b.idBairro " +
                     "LEFT JOIN Telefone f ON c.idCliente = f.idCliente " +
                     "LEFT JOIN Email em ON c.idCliente = em.idCliente " +
                     "WHERE c.CPF = ? OR c.CNPJ = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, documento);
            stmt.setString(2, documento);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                System.out.println("Dados do cliente:");
                System.out.println("Nome: " + rs.getString("nomeCliente"));

                // Exibir CPF ou CNPJ, somente se não for nulo
                String cpf = rs.getString("CPF");
                String cnpj = rs.getString("CNPJ");

                if (cpf != null && !cpf.isEmpty()) {
                    System.out.println("CPF: " + cpf);
                }
                if (cnpj != null && !cnpj.isEmpty()) {
                    System.out.println("CNPJ: " + cnpj);
                }

                // Exibir demais dados do cliente
                System.out.println("Endereço: " + rs.getString("nomeLogradouro") + ", " +
                                   rs.getString("nomeCidade") + ", Bairro: " + rs.getString("nomeBairro"));
                System.out.println("CEP: " + rs.getString("CEP"));

                // Exibir telefone, se disponível
                String telefone = rs.getString("numTelefone");
                if (telefone != null && !telefone.isEmpty()) {
                    System.out.println("Telefone: " + telefone);
                }

                // Exibir e-mail, se disponível
                String email = rs.getString("enderecoEmail");
                if (email != null && !email.isEmpty()) {
                    System.out.println("Email: " + email);
                }

                listarContaBancaria(documento);

            } else {
                System.out.println("Nenhum cliente encontrado com esse CPF ou CNPJ.");
            }

        } catch (SQLException e) {
            System.out.println("Erro ao pesquisar o cliente: " + e.getMessage());
        }
    }


    public void listarContaBancaria(String documento) {
        String sql = "SELECT cb.numeroConta, b.nomeBanco, a.nomeAgencia, t.tipoConta, cb.dataAbertura, cb.saldoConta " +
                     "FROM ContaBancaria cb " +
                     "JOIN Banco b ON cb.idBanco = b.idBanco " +
                     "JOIN Agencia a ON cb.idAgencia = a.idAgencia " +
                     "JOIN TipoConta t ON cb.idTipoConta = t.idTipoConta " +
                     "JOIN Cliente c ON cb.idCliente = c.idCliente " +
                     "WHERE c.CPF = ? OR c.CNPJ = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, documento);
            stmt.setString(2, documento);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                System.out.println("Dados da conta bancária:");
                System.out.println("Número da Conta: " + rs.getString("numeroConta"));
                System.out.println("Banco: " + rs.getString("nomeBanco"));
                System.out.println("Agência: " + rs.getString("nomeAgencia"));
                System.out.println("Tipo de Conta: " + rs.getString("tipoConta"));
                System.out.println("Data de Abertura: " + rs.getString("dataAbertura"));
                System.out.println("Saldo: " + rs.getDouble("saldoConta"));
            }

        } catch (SQLException e) {
            System.out.println("Erro ao listar a conta bancária: " + e.getMessage());
        }
    }

    public void listarTransacoesPorPeriodo(String documento, Date dataInicial, Date dataFinal) {
        String sql = "SELECT tr.codTransacao, tr.dataTransacao, tr.descricaoTransacao, tr.valorTransacao " +
                     "FROM Transacao tr " +
                     "JOIN ContaBancaria cb ON tr.idConta = cb.idConta " +
                     "JOIN Cliente c ON cb.idCliente = c.idCliente " +
                     "WHERE (c.CPF = ? OR c.CNPJ = ?) AND tr.dataTransacao BETWEEN ? AND ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, documento);
            stmt.setString(2, documento);
            stmt.setDate(3, new java.sql.Date(dataInicial.getTime()));
            stmt.setDate(4, new java.sql.Date(dataFinal.getTime()));

            ResultSet rs = stmt.executeQuery();

            System.out.println("Transações no período:");
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            while (rs.next()) {
                System.out.println("Código da Transação: " + rs.getInt("codTransacao"));
                System.out.println("Data: " + sdf.format(rs.getDate("dataTransacao")));
                System.out.println("Descrição: " + rs.getString("descricaoTransacao"));
                System.out.println("Valor: " + rs.getDouble("valorTransacao"));
                System.out.println("---------------------------------------------------");
            }

        } catch (SQLException e) {
            System.out.println("Erro ao listar as transações: " + e.getMessage());
        }
    }
}