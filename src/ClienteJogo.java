import java.io.*;
import java.net.*;
import java.util.Scanner;

public class ClienteJogo {
    public static void main(String[] args) {
        try {
            Socket socket = new Socket("localhost", 10000);
            BufferedReader entradaDoServidor = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter saidaParaServidor = new PrintWriter(socket.getOutputStream(), true);
            Scanner scanner = new Scanner(System.in);

            String mensagemDoServidor;

            System.err.println("Aguardando resposta do servidor...");
            // Recebe a mensagem do servidor e envia o nome do jogador
            System.out.println(entradaDoServidor.readLine()); // "Digite seu nome"
            String nomeJogador = scanner.nextLine();
            saidaParaServidor.println(nomeJogador);

            while (true) {
                // Recebe a mensagem do servidor
                mensagemDoServidor = entradaDoServidor.readLine();
                if (mensagemDoServidor != null) {
                    System.out.println("Servidor: " + mensagemDoServidor);
                }
                if (mensagemDoServidor != null) {
                    if (mensagemDoServidor.startsWith(nomeJogador + ", digite seu lance")) {
                        // Envia a jogada do jogador para o servidor
                        System.out.print("Sua jogada: ");
                        String jogada = scanner.nextLine();
                        saidaParaServidor.println(jogada);
                        saidaParaServidor.flush(); // Garante que a jogada seja enviada
                    }
                }
                if (mensagemDoServidor != null) {
                    if (mensagemDoServidor.contains("venceu o jogo")) {
                        break;
                    }
                }
            }

            // Fecha as conexões
            entradaDoServidor.close();
            saidaParaServidor.close();
            scanner.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}