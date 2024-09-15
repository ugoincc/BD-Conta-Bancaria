package application;

import java.sql.Connection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

import db.DB;
import services.ClienteService;

public class Program {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Connection conn = DB.getConnection(); // Abrindo a conexão com o banco de dados
        ClienteService clienteService = new ClienteService(conn); // Instanciando o serviço

        boolean continuar = true;
        while (continuar) {
            System.out.println("Menu:");
            System.out.println("1 - Listar todos os clientes");
            System.out.println("2 - Listar todos os clientes/fisico");
            System.out.println("3 - Listar todos os clientes/juridico");
            System.out.println("4 - Listar Bancos do Cliente");
            System.out.println("5 - Emitir Extrato");
            System.out.println("0 - Sair");
            System.out.print("Escolha uma opção: ");
            int opcao = scanner.nextInt();
            scanner.nextLine(); // Limpa o buffer

            switch (opcao) {
                case 1:
                    clienteService.listarTodosClientes();
                    break;
                case 2:
                    clienteService.listarClientesFisicos();
                    break;
                case 3:
                    clienteService.listarClientesJuridicos();
                    break;
                case 4:
                    System.out.println("Digite o CPF ou CNPJ do cliente:");
                    String documentoBanco = scanner.nextLine();
                    clienteService.listarBancosDoCliente(documentoBanco);
                    break;
                case 5:
                	 System.out.println("Digite o CPF ou CNPJ do cliente:");
                     String documento = scanner.nextLine();
                     boolean clienteEncontrado = clienteService.pesquisarCliente(documento);

                     if (clienteEncontrado) {
                    	 boolean contaValida = false;
                         String numeroConta = "";

                         while (!contaValida) {
                             System.out.println("Selecione o número da conta bancária para emitir o extrato:");
                             numeroConta = scanner.nextLine();

                             // Verifica se a conta pertence ao cliente
                             contaValida = clienteService.validarContaBancaria(documento, numeroConta);

                             if (!contaValida) {
                                 System.out.println("Número de conta inválido para o cliente informado. Tente novamente.");
                             }
                         }

                         System.out.println("Digite a data inicial (dd/MM/yyyy):");
                         String dataInicialStr = scanner.nextLine();
                         System.out.println("Digite a data final (dd/MM/yyyy):");
                         String dataFinalStr = scanner.nextLine();

                         try {
                             SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                             Date dataInicial = sdf.parse(dataInicialStr);
                             Date dataFinal = sdf.parse(dataFinalStr);

                             if (dataInicial.before(dataFinal)) {
                                 clienteService.emitirExtrato(documento, numeroConta, dataInicial, dataFinal);
                             } else {
                                 System.out.println("Erro: A data inicial deve ser anterior à data final.");
                             }
                         } catch (ParseException e) {
                             System.out.println("Formato de data inválido.");
                         }
                     }
                     break;
                case 0:
                    continuar = false;
                    break;
                default:
                    System.out.println("Opção inválida.");
                    break;
            }
        }

        System.out.println("Encerrando o programa.");
        DB.closeConnecction(); // Fechando a conexão com o banco de dados
        scanner.close();
    }
}
