import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.Arrays;

public class InterruptThread extends Thread{
	IA ia;

	public InterruptThread(IA ia){
		this.ia = ia;
	}

	public void run(){
		try{
			String tmp = EntradaTeclado.leString();
		}
		catch (Exception e){
			
		}

		ia.setFlag();
	}

    public static void main(String[] args) throws Exception{
    	
    }
}