import java.io.*;
import java.net.*;

// Jogo é um pedra papel e tesoura com uma diferença, ganhar com certas jogadas valem mais pontos do que ganhar com outras, sendo assim:
// ganhar com tesoura garante 3 pontos, ganhar com papel 2 pontos, e ganhar com pedra somente 1 ponto.
//O objetivo é alcançar 5 pontos para ganhar.

public class ServidorJogo {
    private static final int PORT = 10000;

    public static void main(String[] args) {
        try {
            ServerSocket servidor = new ServerSocket(PORT);
            System.out.println("Servidor de Pedra, Papel e Tesoura iniciado... Aguardando jogadores...");

            // Conectar Jogador 1
            Socket jogador1Socket = servidor.accept();
            System.out.println("Jogador 1 conectado.");
            PrintWriter jogador1Saida = new PrintWriter(jogador1Socket.getOutputStream(), true);
            BufferedReader jogador1Entrada = new BufferedReader(new InputStreamReader(jogador1Socket.getInputStream()));

            jogador1Saida.println("Digite seu nome, Jogador 1: ");
            String nomeJogador1 = jogador1Entrada.readLine();
            jogador1Saida.println("Bem-vindo, " + nomeJogador1 + "! Você está conectado como Jogador 1.");

            // Conectar Jogador 2
            Socket jogador2Socket = servidor.accept();
            System.out.println("Jogador 2 conectado.");
            PrintWriter jogador2Saida = new PrintWriter(jogador2Socket.getOutputStream(), true);
            BufferedReader jogador2Entrada = new BufferedReader(new InputStreamReader(jogador2Socket.getInputStream()));

            jogador2Saida.println("Digite seu nome, Jogador 2: ");
            String nomeJogador2 = jogador2Entrada.readLine();
            jogador2Saida.println("Bem-vindo, " + nomeJogador2 + "! Você está conectado como Jogador 2.");

            String lanceJogador1, lanceJogador2;
            int pontosJogador1 = 0, pontosJogador2 = 0;

            while (pontosJogador1 < 5 && pontosJogador2 < 5) {
                // Recebe a jogada do Jogador 1 com validação
                jogador2Saida.println("Aguarde " + nomeJogador1 + " fazer sua jogada...");
                lanceJogador1 = obterJogadaValida(jogador1Saida, jogador1Entrada, nomeJogador1);

                // Recebe a jogada do Jogador 2 com validação
                jogador1Saida.println("Aguarde " + nomeJogador2 + " fazer sua jogada...");
                lanceJogador2 = obterJogadaValida(jogador2Saida, jogador2Entrada, nomeJogador2);

                // Calcula o vencedor da rodada
                String resultadoRodada = determinaVencedor(lanceJogador1, lanceJogador2, nomeJogador1, nomeJogador2);
                jogador1Saida.println(resultadoRodada);
                jogador2Saida.println(resultadoRodada);

                // Atualiza os pontos dos jogadores
                if (resultadoRodada.contains(nomeJogador1 + " venceu")) {
                    pontosJogador1 += calculaPontos(lanceJogador1);
                } else if (resultadoRodada.contains(nomeJogador2 + " venceu")) {
                    pontosJogador2 += calculaPontos(lanceJogador2);
                }

                jogador1Saida.println("Sua pontuação: " + pontosJogador1);
                jogador2Saida.println("Sua pontuação: " + pontosJogador2);
            }

            // Determina o vencedor final
            if (pontosJogador1 >= 5) {
                jogador1Saida.println("Parabéns! " + nomeJogador1 + " ganhou o jogo!");
                jogador2Saida.println(nomeJogador1 + " venceu o jogo.");
            } else {
                jogador1Saida.println(nomeJogador2 + " venceu o jogo.");
                jogador2Saida.println("Parabéns! " + nomeJogador2 + " ganhou o jogo!");
            }

            // Fecha as conexões
            System.out.println("Fim do jogo!");
            jogador1Saida.println("Fim do jogo!");
            jogador2Saida.println("Fim do jogo!");
            jogador1Socket.close();
            jogador2Socket.close();
            servidor.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
// ------------------Funções------------------------

    // Função para obter uma jogada válida de um jogador
    private static String obterJogadaValida(PrintWriter jogadorSaida, BufferedReader jogadorEntrada, String nomeJogador)
            throws IOException {
        String jogada;
        while (true) {
            jogadorSaida.println(nomeJogador + ", digite seu lance (pedra, papel ou tesoura): ");
            jogadorSaida.flush(); // Garante que o cliente receba a mensagem
            jogada = jogadorEntrada.readLine();
            if (jogada.equals("pedra") || jogada.equals("papel") || jogada.equals("tesoura")) {
                break;
            } else {
                jogadorSaida.println("Lance inválido! Por favor, digite apenas 'pedra', 'papel' ou 'tesoura'.");
                jogadorSaida.flush();
            }
        }
        return jogada;
    }

    // Função que determina o vencedor da rodada
    private static String determinaVencedor(String lanceJogador1, String lanceJogador2, String nomeJogador1,
            String nomeJogador2) {
        if (lanceJogador1.equals(lanceJogador2)) {
            return "Empate!";
        } else if ((lanceJogador1.equals("pedra") && lanceJogador2.equals("tesoura")) ||
                (lanceJogador1.equals("tesoura") && lanceJogador2.equals("papel")) ||
                (lanceJogador1.equals("papel") && lanceJogador2.equals("pedra"))) {
            return nomeJogador1 + " venceu esta rodada!";
        } else {
            return nomeJogador2 + " venceu esta rodada!";
        }
    }

    // Função que calcula os pontos com base no lance
    private static int calculaPontos(String lance) {
        switch (lance) {
            case "tesoura":
                return 3;
            case "papel":
                return 2;
            case "pedra":
                return 1;
            default:
                return 0;
        }
    }
}