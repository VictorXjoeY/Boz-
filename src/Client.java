import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

public class Client{
	public static void main(String[] args) throws Exception{
		// Socket cliente = new Socket("www.labes.icmc.usp.br", 22000);
		Socket cliente = new Socket("127.0.0.1", 1337);
		String in;

		System.out.println("Conectado!");
		
		Scanner entrada = new Scanner(cliente.getInputStream());
		PrintStream saida = new PrintStream(cliente.getOutputStream());
		Scanner teclado = new Scanner(System.in);
		
		while (teclado.hasNextLine()){
			saida.println(teclado.nextLine());
			System.out.println(entrada.nextLine());
		}

		System.out.println("Fim do cliente");
		saida.close();
		teclado.close();
		entrada.close();
		cliente.close();
	}
}
