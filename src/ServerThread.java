import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.Arrays;

public class ServerThread extends Thread{
	private Socket cliente;
	private String user;
	private Server server;
	private Scanner entrada;
	private PrintStream saida;

	public ServerThread(Socket cliente, Server server){
		super();

		this.cliente = cliente;
		this.server = server;
	}

	public void ForceDisconnect() throws Exception{
		System.out.println("Entrada inválida!");
		saida.println("Entrada inválida!");
		server.add(user, 0);
		server.close();
		entrada.close();
		saida.close();
		cliente.close();
	}

	public void run(){
		// Server.
		String in, play;
		String[] aux = null;

		// Bozo.
		Placar placar = new Placar();
        RolaDados dados = new RolaDados(5);
        int[] dadosArray;
        boolean again;
        int i, j, k;

		try{
			entrada = new Scanner(cliente.getInputStream()); // Dados do cliente para o servidor.
			saida = new PrintStream(cliente.getOutputStream()); // Dados do servidor para o cliente.
			
			// I 9293394
			in = entrada.nextLine();
			System.out.println("Client - \"" + in + "\"");

			// Verificando
			if (!in.matches("I .+")){
				ForceDisconnect();
			}

			user = in.split(" ")[1];
			saida.println("Bem vindo, " + user);

	        for (i = 0; i < 10; i++){
				// Ri
				in = entrada.nextLine();
				System.out.println(user + " - \"" + in + "\"");

				// Verificando
				if (!in.matches("R" + ((Integer)(i + 1)).toString())){
					ForceDisconnect();
				}

	            /*System.out.println("Pressione ENTER para iniciar a rodada.");
	            EntradaTeclado.leString();*/
	           
	            dadosArray = dados.rolar();

	            saida.println(Arrays.toString(dadosArray).replaceAll("[\\,,\\[,\\]]", ""));
	            // System.out.println(dados);
	            
	            for (j = 0; j < 2; j++){
		            do{
		                try{
		                    // System.out.println("Escolha os dados a serem rolados.");
		                    again = false;

		                    // T X X X X X
		                    in = entrada.nextLine();
							System.out.println(user + " - \"" + in + "\"");

							// Verificando
							if (!in.matches("T [01] [01] [01] [01] [01]")){
								ForceDisconnect();
							}

							play = "";

							// Transformando em uma jogada
							for (k = 2; k <= 10; k += 2){
								if (in.charAt(k) == '1'){
									play += ((Integer)(k / 2)).toString() + " ";
								}
							}

		                    dadosArray = dados.rolar(play);
		                }
		                catch (Exception e){
		                    saida.println(e.getMessage() + " Tente novamente.");
		                    again = true;
		                }
		            }while (again);

		            saida.println(Arrays.toString(dadosArray).replaceAll("[\\,,\\[,\\]]", ""));
	            }

	            do{
	                try{
						again = false;

	                    // P1 N
	                    in = entrada.nextLine();
						System.out.println(user + " - \"" + in + "\"");

						// Verificando
						if (!in.matches("P" + ((Integer)(i + 1)).toString() + " .+")){
							ForceDisconnect();
						}
						else{
							aux = in.split(" ");
							if (aux.length >  2 || Integer.parseInt(aux[1]) < 1 || Integer.parseInt(aux[1]) > 10){
								ForceDisconnect();
							}
						}

	                    placar.add(Integer.parseInt(aux[1]), dados.rolar(""));
	                }catch (Exception e){
	                    saida.println(e.getMessage() + " Tente novamente.");
	                    again = true;
	                }
	            }while (again);

	            saida.println("Sua pontuação: " + placar.getScore());
	        }

	        in = entrada.nextLine();
			System.out.println(user + " - \"" + in + "\"");

			if (!in.matches("F")){
				ForceDisconnect();
			}

			saida.println("Fim do jogo! Pontuação final do jogador " + user + ": " + placar.getScore());

			server.add(user, placar.getScore());
			server.close();

			entrada.close();
			saida.close();
			cliente.close();
		}
		catch (Exception e){
			System.out.println("Deu ruim");
		}
	}

    public static void main(String[] args) throws Exception{
    	
    }
}