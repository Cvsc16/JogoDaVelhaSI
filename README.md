# Jogo da Velha com RMI

## Visão Geral
Este é um simples jogo da velha implementado em Java utilizando RMI (Remote Method Invocation). O jogo permite que dois jogadores joguem entre si em diferentes máquinas através de uma comunicação remota.

## Como Executar
Para rodar o jogo, siga as instruções abaixo:

### Servidor
1. Abra um terminal ou prompt de comando.
2. Navegue até o diretório onde o arquivo `GameServer.jar` está localizado.
3. Execute o comando abaixo, substituindo `<ip>` e `<porta>` pelo endereço IP e porta desejados:
java -jar GameServer.jar <ip> <porta>
4. O servidor estará agora rodando e aguardando a conexão dos jogadores.

### Cliente
1. Abra um terminal ou prompt de comando.
2. Navegue até o diretório onde o arquivo `GameClient.jar` está localizado.
3. Execute o comando abaixo, substituindo `<nome>` pelo nome desejado, `<ip>` pelo endereço IP do servidor e `<porta>` pela porta do servidor:
4. O cliente irá se conectar ao servidor e você poderá começar a jogar.

### Exemplo
Para rodar o servidor na máquina local na porta 8080:
java -jar GameServer.jar localhost 8080

Para rodar o cliente na mesma máquina, conectando-se ao servidor local:
java -jar GameClient.jar jogador1 localhost 8080
java -jar GameClient.jar jogador2 localhost 8080
