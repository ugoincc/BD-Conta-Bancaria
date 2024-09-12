INSERT INTO ddi (ddi) VALUES ('+55'); -- Brasil
INSERT INTO ddd (ddd) VALUES ('11'), ('21'), ('31'); -- São Paulo, Rio de Janeiro, Belo Horizonte

INSERT INTO UnidadeFederacao (siglaUF, nomeUF) VALUES ('SP', 'São Paulo'), ('RJ', 'Rio de Janeiro'), ('MG', 'Minas Gerais');

INSERT INTO TipoLogradouro (siglaLogradouro, nomeLogradouro) VALUES ('R', 'Rua'), ('AV', 'Avenida');

INSERT INTO Bairro (nomeBairro) VALUES ('Centro'), ('Jardins'), ('Copacabana');

INSERT INTO TipoConta (tipoConta) VALUES ('Corrente'), ('Poupança');

INSERT INTO TipoTransacao (indicador) VALUES ('credito'), ('debito');

INSERT INTO banco (nomeBanco) VALUES ('Banco do Brasil'), ('Itaú'), ('Santander');

INSERT INTO agencia (nomeAgencia, idBanco) VALUES ('Agência Centro', 1), ('Agência Paulista', 2), ('Agência Copacabana', 3);

INSERT INTO Logradouro (nomeLogradouro, TipoLogradouro_siglaLogradouro) VALUES ('Paulista', 'AV'), ('Nossa Senhora de Copacabana', 'AV');
INSERT INTO Cidade (nomeCidade, UnidadeFederacao_siglaUF) VALUES ('São Paulo', 'SP'), ('Rio de Janeiro', 'RJ'), ('Belo Horizonte', 'MG');

INSERT INTO Endereco (CEP, idLogradouro, idCidade, idBairro) 
VALUES ('01000-000', 1, 1, 1), ('22000-000', 2, 2, 3);

INSERT INTO Cliente (nomeCliente, tipoCliente, CPF, CNPJ, idEndereco) 
VALUES ('João Silva', 'fisico', '12345678901', NULL, 1),
       ('Maria Souza', 'fisico', '98765432100', NULL, 2),
       ('Empresa XYZ Ltda', 'juridico', NULL, '11222333000181', 1);

INSERT INTO ContaBancaria (idCliente, numeroConta, idBanco, idAgencia, dataAbertura, saldoConta, idTipoConta) 
VALUES (1, '12345-6', 1, 1, '2023-01-01', 1000.00, 1),
       (1, '65432-1', 2, 2, '2024-02-01', 2000.00, 2),
       (2, '54321-9', 3, 3, '2024-05-15', 1500.00, 1),
       (2, '98765-2', 1, 1, '2023-06-20', 3000.00, 2),
       (3, '67890-0', 2, 2, '2023-03-30', 5000.00, 1);


INSERT INTO Transacao (codTransacao, dataTransacao, descricaoTransacao, valorTransacao, observacoes, idConta, idTipoTransacao)
VALUES (1001, '2023-08-01', 'Compra Mercado', 100.00, NULL, 1, 2),
       (1002, '2023-08-05', 'Depósito', 500.00, NULL, 1, 1),
       (1003, '2023-08-10', 'Pagamento Aluguel', 1200.00, NULL, 2, 2),
       (1004, '2023-08-15', 'Transferência', 300.00, 'Para conta Maria', 3, 2),
       (1005, '2023-08-20', 'Compra Online', 200.00, NULL, 4, 2),
       (1006, '2023-08-25', 'Depósito', 1000.00, NULL, 5, 1);
