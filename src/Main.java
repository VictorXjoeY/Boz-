public class Main{
    public static void main(String[] args) throws Exception{
		Server server = new Server(1337);
		String str = "";

		server.start();

		while (!str.equals("sair")){
			str = EntradaTeclado.leString();
		}

		server.shutdown();

		while (!server.done()){
			Thread.sleep(250);
		}

		System.out.println("Fim");
    }
}