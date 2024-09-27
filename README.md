# BD-Conta-Bancaria
Projeto do Trabalho 1B da Disciplina Banco de Dados.


# Sistema de Gestão de Contas Bancárias

Este é um sistema de gerenciamento de contas bancárias que permite listar clientes, consultar suas contas e gerar extratos com base em períodos definidos. O programa é interativo e funciona via linha de comando, permitindo a consulta de informações sobre clientes físicos e jurídicos.

## Funcionalidades

O sistema oferece as seguintes funcionalidades:

1. **Listar todos os clientes**: Exibe uma lista de todos os clientes cadastrados no sistema, sejam eles físicos ou jurídicos.
2. **Listar todos os clientes físicos**: Exibe uma lista dos clientes do tipo pessoa física (CPF).
3. **Listar todos os clientes jurídicos**: Exibe uma lista dos clientes do tipo pessoa jurídica (CNPJ).
4. **Listar bancos do cliente**: Mostra os bancos em que o cliente possui contas.
5. **Emitir extrato bancário**: Exibe um extrato de transações bancárias para uma conta específica de um cliente dentro de um intervalo de datas.
6. **Sair**: Encerra o programa.

## Como Executar

1. Inicie o programa.
2. Escolha uma das opções do menu.
3. Siga as instruções para realizar consultas ou gerar extratos.

## Cenários de Teste

### Cenário 1: Listar Todos os Clientes

**Entrada**:
```
Escolha uma opção: 1
```

**Saída**:
```
Lista de todos os clientes:
Nome: João Silva
CPF: 12345678901
--------------------------------
Nome: Maria Oliveira
CPF: 12345678902
--------------------------------
Nome: Carlos Souza
CPF: 12345678903
--------------------------------
Nome: Ana Pereira
CPF: 12345678904
--------------------------------
Nome: Pedro Costa
CPF: 12345678905
--------------------------------
Nome: Empresa XPTO
CNPJ: 98765432000101
--------------------------------
Nome: Empresa Alfa
CNPJ: 98765432000201
--------------------------------
Nome: Empresa Beta
CNPJ: 98765432000301
--------------------------------
Nome: Empresa Gama
CNPJ: 98765432000401
--------------------------------
Nome: Empresa Delta
CNPJ: 98765432000501
--------------------------------
```

### Cenário 2: Emitir Extrato de um Cliente Físico

**Entrada**:
```
Escolha uma opção: 5
Digite o CPF ou CNPJ do cliente:
12345678901

Selecione o número da conta bancária para emitir o extrato:
12345-6

Digite a data inicial (dd/MM/yyyy):
01/07/2023
Digite a data final (dd/MM/yyyy):
31/07/2023
```

**Saída**:
```
Extrato da conta 12345-6 no período:
--------------------------------------------------
Código da Transação: 1001
Data: 05/07/2023
Descrição: Pagamento de boleto
Tipo de Transação: debito
Valor: R$ 200.00
--------------------------------------------------
Código da Transação: 1002
Data: 10/07/2023
Descrição: Compra em mercado
Tipo de Transação: debito
Valor: R$ 350.00
--------------------------------------------------
Código da Transação: 1003
Data: 15/07/2023
Descrição: Recebimento de salário
Tipo de Transação: credito
Valor: R$ 3000.00
--------------------------------------------------
```

### Cenário 3: Emitir Extrato de um Cliente Jurídico (Empresa Delta)

**Entrada**:
```
Escolha uma opção: 5
Digite o CPF ou CNPJ do cliente:
98765432000501

Selecione o número da conta bancária para emitir o extrato:
12345-5

Digite a data inicial (dd/MM/yyyy):
01/07/2023
Digite a data final (dd/MM/yyyy):
31/07/2023
```

**Saída**:
```
Extrato da conta 12345-5 no período:
--------------------------------------------------
Código da Transação: 9001
Data: 01/07/2023
Descrição: Pagamento de fornecedor
Tipo de Transação: debito
Valor: R$ 5000.00
--------------------------------------------------
Código da Transação: 9002
Data: 05/07/2023
Descrição: Recebimento de pagamento
Tipo de Transação: credito
Valor: R$ 10000.00
--------------------------------------------------
```

### Cenário 4: Tentativa de Emitir Extrato com Conta Inválida

**Entrada**:
```
Escolha uma opção: 5
Digite o CPF ou CNPJ do cliente:
12345678901

Selecione o número da conta bancária para emitir o extrato:
99999-9
```

**Saída**:
```
Número de conta inválido para o cliente informado. Tente novamente.
```

### Cenário 5: Tentativa de Emitir Extrato com Data Inválida

**Entrada**:
```
Escolha uma opção: 5
Digite o CPF ou CNPJ do cliente:
12345678901

Selecione o número da conta bancária para emitir o extrato:
12345-6

Digite a data inicial (dd/MM/yyyy):
31/07/2023
Digite a data final (dd/MM/yyyy):
01/07/2023
```

**Saída**:
```
Erro: A data inicial deve ser anterior à data final.
```

### Cenário 6: Nenhuma Transação Encontrada no Período

**Entrada**:
```
Escolha uma opção: 5
Digite o CPF ou CNPJ do cliente:
98765432000101

Selecione o número da conta bancária para emitir o extrato:
12345-1

Digite a data inicial (dd/MM/yyyy):
01/01/2000
Digite a data final (dd/MM/yyyy):
01/01/2020
```

**Saída**:
```
Nenhuma transação encontrada para o período selecionado.
```

## Tecnologias Utilizadas

- Java 8+
- JDBC para conexão com banco de dados SQL
- MySQL DBMS para consultas e manipulação de dados

## Antes de iniciar a aplicação:

1. Configure um banco de dados SQL com os dados necessários.
2. Utilize o script SQL de inserção fornecido (../sql_files) para criar as tabelas e popular as tabelas com dados.
3. Certifique-se de que a aplicação está conectada corretamente ao banco de dados.
