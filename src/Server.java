import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.TreeMap;

public class Server extends Thread{
	private ServerSocket ss;
	private TreeMap<String, Integer> rounds;
	private TreeMap<String, Integer> points;
	private boolean shutdown;
	private boolean done;
	private int numberOfRunningThreads;
	private int port;

	public Server(int port) throws Exception{
		super();

		ss = new ServerSocket(port);

		this.port = port;
		points = new TreeMap<String, Integer>();
		rounds = new TreeMap<String, Integer>();
		numberOfRunningThreads = 0;
		shutdown = false;
		done = false;
	}

	public void add(String user, int score){
		Integer r, p;

		r = rounds.get(user);
		p = points.get(user);

		rounds.put(user, r == null ? (Integer)1 : (Integer)(r + 1));
		points.put(user, p == null ? (Integer)score : (Integer)(p + score));
	}

	public void shutdown(){
		shutdown = true;
	}

	public void close(){
		numberOfRunningThreads--;
	}

	public boolean done(){
		return done;
	}

	private void PrintScores(){
		System.out.println("Pontuação final:");

		for (String user : rounds.keySet()){
			System.out.print(user + " - (" + rounds.get(user) + ") " + points.get(user));
			System.out.printf(" - (%.2f)\n", (double)points.get(user) / (double)rounds.get(user));
		}
	}

	public void run(){
		Socket cliente;
		ServerThread st;

		System.out.println("Porta " + port + " aberta!");

		while (!shutdown){
			try{
				cliente = ss.accept();
				System.out.println("Nova conexão com o cliente " + cliente.getInetAddress().getHostAddress());
				st = new ServerThread(cliente, this);
				st.start();
				numberOfRunningThreads++;
			}
			catch (Exception e){
				System.out.println("Deu ruim.");
			}

		}

		System.out.println("Aguardando as outras Threads...");

		while (numberOfRunningThreads > 0){
			try{
				Thread.sleep(1000);
			}
			catch (Exception e){
				System.out.println("Deu ruim no sleep");
			}
		}

		PrintScores();

		try{
			Thread.sleep(1000);
		}
		catch (Exception e){
			System.out.println("Deu ruim no sleep");
		}

		System.out.println("Porta " + port + " fechada!");

		done = true;
	}

	public static void main(String[] args) throws Exception{
		
	}
}
