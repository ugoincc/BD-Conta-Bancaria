CREATE TABLE ddi(
    ddi_id INT PRIMARY KEY AUTO_INCREMENT,
    ddi VARCHAR(5) NOT NULL
);

CREATE TABLE ddd(
    ddd_id INT PRIMARY KEY AUTO_INCREMENT,
    ddd VARCHAR(5) NOT NULL
);

CREATE TABLE banco (
    idBanco INT PRIMARY KEY AUTO_INCREMENT,
    nomeBanco VARCHAR(55) NOT NULL
);

CREATE TABLE UnidadeFederacao (
  siglaUF VARCHAR(2) PRIMARY KEY, 
  nomeUF VARCHAR(50) NOT NULL
);

CREATE TABLE TipoLogradouro (
  siglaLogradouro VARCHAR(10) PRIMARY KEY,
  nomeLogradouro VARCHAR(50) NOT NULL
);

CREATE TABLE Bairro (
  idBairro INT PRIMARY KEY AUTO_INCREMENT,
  nomeBairro VARCHAR(50) NOT NULL
);

CREATE TABLE TipoConta (
  idTipoConta INT PRIMARY KEY AUTO_INCREMENT,
  tipoConta VARCHAR(50) NOT NULL
);

CREATE TABLE TipoTransacao (
  idTipoTransacao INT PRIMARY KEY AUTO_INCREMENT,
  indicador ENUM('credito', 'debito') NOT NULL
);

CREATE TABLE agencia (
    idAgencia INT PRIMARY KEY AUTO_INCREMENT,
    nomeAgencia VARCHAR(55) NOT NULL,
    idBanco INT NOT NULL,
    FOREIGN KEY (idBanco) REFERENCES banco(idBanco)
);

CREATE TABLE Cidade (
  idCidade INT PRIMARY KEY AUTO_INCREMENT,
  nomeCidade VARCHAR(50) NOT NULL,
  UnidadeFederacao_siglaUF VARCHAR(2) NOT NULL,
  FOREIGN KEY (UnidadeFederacao_siglaUF) REFERENCES UnidadeFederacao(siglaUF)
);

CREATE TABLE Logradouro (
  idLogradouro INT PRIMARY KEY AUTO_INCREMENT,
  nomeLogradouro VARCHAR(100) NOT NULL,
  TipoLogradouro_siglaLogradouro VARCHAR(10),
  FOREIGN KEY (TipoLogradouro_siglaLogradouro) REFERENCES TipoLogradouro(siglaLogradouro)
);

CREATE TABLE Endereco (
  idEndereco INT PRIMARY KEY AUTO_INCREMENT,
  CEP VARCHAR(20) NOT NULL,
  idLogradouro INT,
  idCidade INT,
  idBairro INT,
  FOREIGN KEY (idLogradouro) REFERENCES Logradouro(idLogradouro),
  FOREIGN KEY (idCidade) REFERENCES Cidade(idCidade),
  FOREIGN KEY (idBairro) REFERENCES Bairro(idBairro)
);

CREATE TABLE Cliente (
  idCliente INT PRIMARY KEY AUTO_INCREMENT,
  nomeCliente VARCHAR(100) NOT NULL,
  tipoCliente ENUM('fisico', 'juridico') NOT NULL,
  CPF VARCHAR(11),  
  CNPJ VARCHAR(14), 
  idEndereco INT,
  FOREIGN KEY (idEndereco) REFERENCES Endereco(idEndereco)
);

CREATE TABLE Email (
  idEmail INT PRIMARY KEY AUTO_INCREMENT,
  idCliente INT,
  enderecoEmail VARCHAR(100),
  FOREIGN KEY (idCliente) REFERENCES Cliente(idCliente)
);

CREATE TABLE Telefone (
  idTelefone INT PRIMARY KEY AUTO_INCREMENT,
  idCliente INT,
  ddi_id INT,
  ddd_id INT,
  numTelefone VARCHAR(20),
  FOREIGN KEY (ddi_id) REFERENCES ddi(ddi_id),
  FOREIGN KEY (ddd_id) REFERENCES ddd(ddd_id),
  FOREIGN KEY (idCliente) REFERENCES Cliente(idCliente)
);

CREATE TABLE ContaBancaria (
  idConta INT PRIMARY KEY AUTO_INCREMENT,
  idCliente int,
  numeroConta VARCHAR(20) NOT NULL,
  idBanco INT NOT NULL,
  idAgencia INT NOT NULL,
  dataAbertura DATETIME NOT NULL,
  saldoConta FLOAT,
  idTipoConta INT NOT NULL,
  FOREIGN KEY (idBanco) REFERENCES banco(idBanco),
  FOREIGN KEY (idAgencia) REFERENCES agencia(idAgencia),
  FOREIGN KEY (idCliente) REFERENCES Cliente(idCliente),
  FOREIGN KEY (idTipoConta) REFERENCES TipoConta(idTipoConta)
);

CREATE TABLE Transacao (
  idTransacao INT PRIMARY KEY AUTO_INCREMENT,
  codTransacao INT NOT NULL,
  dataTransacao DATE NOT NULL,
  descricaoTransacao VARCHAR(255) NOT NULL,
  valorTransacao DECIMAL(10, 2) NOT NULL,
  observacoes VARCHAR(255),
  idConta INT,  
  idTipoTransacao INT,
  FOREIGN KEY (idConta) REFERENCES ContaBancaria(idConta),
  FOREIGN KEY (idTipoTransacao) REFERENCES TipoTransacao(idTipoTransacao)
);