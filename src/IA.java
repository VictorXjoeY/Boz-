import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;
import java.util.Arrays;

public class IA{
	private final int QUINA_POINTS = 40;
    private final int QUADRA_POINTS = 30;
    private final int SEQUENCIA_POINTS = 20;
    private final int FULL_HAND_POINTS = 15;
    private final int QUINA = 10;
    private final int QUADRA = 9;
    private final int SEQUENCIA = 8;
    private final int FULL_HAND = 7;
    private Socket cliente;
    private Scanner entrada;
    private PrintStream saida;
    private Scanner teclado;
    private double[][] probabilidades;
    private double[] pesos;
    private int[][] respostas;
    private int[] dados;
    private int[] constPoints;
    private int[] melhorCaso;
    private int[] identificador;
    private int[] ocorrencias;
    private int[] counter;
    private boolean[] preenchido;
    private boolean flag;
    private int[] points;
    private double myConst = 0.8; // 0.8
    private double normalWeight = 8.0;
    private double sequenciaWeight = 15.0; // 14.22 < x < 17.77
    private double fullHandWeight = 10.4; // 8.88 < x < 10.66
    private double quadraWeight = 30.0;
    private double quinaWeight = 30.0;
    private int[][] pointsMatrix;

    public int GetBestPlay(){
        int counter_i, counter_j, sum_i, sum_j, play, i, j, k;
        double maxPeso, novoPeso;

        for (i = 1; i <= QUINA; i++){
            pesos[i] = 0.0;
        }

        for (i = 0; i < 32; i++){
            // Para as posições normais.
            for (j = 1; j <= 6; j++){
                // Essa fórmula pode ser alterada.
                novoPeso = normalWeight * probabilidades[i][j] * j;

                if (novoPeso > pesos[j]){
                    pesos[j] = novoPeso;
                    melhorCaso[j] = i;
                }
            }

            for (j = FULL_HAND; j <= QUINA; j++){
                novoPeso = probabilidades[i][j]/* * constPoints[j]*/;

                // Diminuindo o peso caso o dado a ser mantido já tiver sua posição preenchida no placar.
                for (k = 1; k <= 5; k++){
                    novoPeso *= (respostas[i][k] == 0 && preenchido[dados[k]]) ? myConst : 1.0;
                }

                if (novoPeso > pesos[j]){
                    pesos[j] = novoPeso;
                    melhorCaso[j] = i;
                }
                else if (novoPeso == pesos[j]){
                    counter_i = 0;
                    counter_j = 0;

                    sum_i = 0;
                    sum_j = 0;

                    // Desempatando pelos dados a serem mantidos.
                    for (k = 1; k <= 5; k++){
                        // Se eu mantiver o dado de índice k e ele não tiver sido preenchido antes.
                        counter_i += (respostas[i][k] == 0 && !preenchido[dados[k]]) ? 1 : 0;

                        // Soma dos dados que eu irei manter.
                        sum_i += respostas[i][k] == 0 ? dados[k] : 0;

                        // Se eu mantiver o dado de índice k e ele não tiver sido preenchido antes.
                        counter_j += (respostas[melhorCaso[j]][k] == 0 && !preenchido[dados[k]]) ? 1 : 0;
                        
                        // Soma dos dados que eu irei manter.
                        sum_j += respostas[melhorCaso[j]][k] == 0 ? dados[k] : 0;
                    }
                    
                    if (counter_i > counter_j){
                        melhorCaso[j] = i;
                    }
                    else if (counter_i == counter_j){
                        if (sum_i > sum_j){
                            melhorCaso[j] = i;
                        }
                    }
                }
            }
        }

        // Definindo um peso mínimo para realizar uma jogada de acordo com esses critérios.
        maxPeso = 0.0;

        // Mudando os pesos de cada especial.
        pesos[FULL_HAND] *= fullHandWeight;
        pesos[SEQUENCIA] *= sequenciaWeight;
        pesos[QUADRA] *= quadraWeight;
        pesos[QUINA] *= quinaWeight;

        play = -1;

        // Escolhendo a jogada de maior peso.
        for (i = QUINA; i >= 1; i--){
            if (!preenchido[i] && pesos[i] > maxPeso){
                maxPeso = pesos[i];
                play = i;
            }
        }

        // O estilo de jogar para conseguir a Quina já engloba a Quadra.
        return play == QUADRA ? QUINA : play;
    }

    // Ao invés de pegar a posição de 1 a 6 que dá o máximo de pontos, pegar a da face que ocorre mais vezes.
    public int GetBestPosition(){
        int i, pos, maxPoints, amount;
        boolean flag = false;

        pos = -1;
        amount = 0;

        // Pegando a face que ocorreu mais vezes.
        for (i = 6; i >= 1; i--){
            if (!preenchido[i] && ocorrencias[i] > amount){
                amount = ocorrencias[i];
                pos = i;
            }
        }

        // NOTA: Testar se muitas posições "normais" do placar já foram preenchidas.
        if (pos == -1){
            for (i = QUINA; i >= 1; i--){
                if (!preenchido[i]){
                    pos = i;
                    break;
                }
            }
        }
        else{
            if (ocorrencias[pos] == 1){
                for (i = 6; i >= 1; i--){
                    if (i != pos && !preenchido[i] && ocorrencias[i] > 1){
                        pos = i;
                        flag = true;
                        break;
                    }
                }

                if (!flag){
                    for (i = 1; i <= 6; i++){
                        if (!preenchido[i] && ocorrencias[i] > 0){
                            pos = i;
                            flag = true;
                            break;
                        }
                    }
                }
            }
        }

        return pos;
    }

    public void setFlag(){
        flag = true;
    }

	public static void main(String[] args) throws Exception{
        IA ia = new IA();

		int i, j, k, l, n, x, total;
		int play, pos;
		String[] aux;
		String in, str;

		System.out.println("Quantas vezes?");
		n = EntradaTeclado.leInt();

        ia.flag = false;
		total = 0;

        InterruptThread it = new InterruptThread(ia);
        it.start();

		for (x = 0; x < n && !ia.flag; x++){
			// Inicializando.
			ia.Initialize();

            // Conectando.
            ia.Connect();

			// I Forbes
			ia.saida.println("I Forbes");

			// Imprimindo o que foi recebido.
			in = ia.entrada.nextLine();
			System.out.println("Server - \"" + in + "\"");

			for (i = 0; i < 10; i++){
				// Ri
				ia.saida.println("R" + ((Integer)(i + 1)).toString());

				// Imprimindo o que foi recebido.
				in = ia.entrada.nextLine();
				System.out.println("Server - \"" + in + "\"");

				ia.GetDados(in);

				for (j = 0; j < 2; j++){
					ia.GeneratePossibilities();

					play = ia.GetBestPlay();

					ia.saida.println("T " + Arrays.toString(ia.respostas[ia.melhorCaso[play]]).replaceAll("[\\,,\\[,\\]]", "").substring(2));

					// Imprimindo
					in = ia.entrada.nextLine();
					System.out.println("Server - \"" + in + "\"");

					ia.GetDados(in);
				}

				ia.SetUp(ia.dados);

				if (!ia.preenchido[ia.QUINA] && ia.IsQuina()){
	                pos = ia.QUINA;
	            }
	            else if (!ia.preenchido[ia.QUADRA] && ia.IsQuadra()){
	                pos = ia.QUADRA;
	            }
	            else if (!ia.preenchido[ia.SEQUENCIA] && ia.IsSequencia()){
	                pos = ia.SEQUENCIA;
	            }
	            else if (!ia.preenchido[ia.FULL_HAND] && ia.IsFullHand()){
	                pos = ia.FULL_HAND;
	            }
	            else{
	                pos = ia.GetBestPosition();
	            }

				// Preenchendo placar.
				ia.preenchido[pos] = true;
				ia.saida.println("P" + ((Integer)(i + 1)).toString() + " " + (Integer)pos);

				// Imprimindo
				in = ia.entrada.nextLine();
				System.out.println("Server - \"" + in + "\"");
			}

			ia.saida.println("F");

			// Imprimindo
			in = ia.entrada.nextLine();
			total += ia.GetScore(in);
			System.out.println("Server - \"" + in + "\"");

			ia.Disconnect();

		    System.out.printf("%d - Média: %.2f\n", x + 1, (double)total / (double)(x + 1));
        }
	}

	public int GetScore(String in){
		String[] aux = in.split(" ");

		return Integer.parseInt(aux[8]);
	}

	public void SetUp(int[] d){
        int i;

        ocorrencias = new int[]{0, 0, 0, 0, 0, 0, 0}; // 7 (de 0 a 6, usando de 1 a 6)
        identificador = new int[]{0, 0, 0, 0, 0, 0}; // 6 (de 0 a 5, usando de 0 a 5)

        // Checando a ocorrência de cada face.
        for (i = 1; i <= 5; i++){
            ocorrencias[d[i]]++;
        }

        // Checando o caso a ser tratado.
        for (i = 1; i <= 6; i++){
            identificador[ocorrencias[i]]++;
        }
    }

    public boolean IsQuina(){
        return identificador[5] == 1;
    }

    public boolean IsQuadra(){
        return identificador[4] == 1 || identificador[5] == 1;
    }

    public boolean IsSequencia(){
        return identificador[1] == 5 && (ocorrencias[1] == 0 || ocorrencias[6] == 0);
    }

    public boolean IsFullHand(){
        return identificador[3] == 1 && identificador[2] == 1;
    }

    public void CountOccurrences(){
        int i;

        for (i = 1; i <= 6; i++){
            if (ocorrencias[i] >= 4){
                counter[i]++;
            }
        }

        counter[FULL_HAND] += IsFullHand() ? 1 : 0;
        counter[SEQUENCIA] += IsSequencia() ? 1 : 0;
        counter[QUADRA] += IsQuadra() ? 1 : 0;
        counter[QUINA] += IsQuina() ? 1 : 0;
    }

    public void CalculateProbabilities(int x, int total){
        int i;

        for (i = 1; i <= QUINA; i++){
            probabilidades[x][i] = (double)counter[i] / (double)total;
        }
    }

    // Número de operações: k * (1 * 6^0 + 5 * 6^1 + 10 * 6^2 + 10 * 6^3 + 5 * 6^4 + 1 * 6^5)
    // 16807 * k = 1.6 * 10^4 * k
    public void GeneratePossibilities(){
        int[] d = new int[6]; // Auxiliar para não alterar o valor dos dados.
        int total, i, j, k, l, m, n, o, p, x;

        // Copiando os dados originais. Usar no final de cada uma das 32 linhas.
        for (i = 1; i <= 5; i++){
            d[i] = dados[i];
        }

        // Para cada configuração diferente de dados tenho que usar o SetUp().
        SetUp(d);

        x = 0;

        // Rolando 0 dados: _ _ _ _ _
        probabilidades[x][FULL_HAND] = (IsFullHand() ? 1.0 : 0.0);
        probabilidades[x][SEQUENCIA] = (IsSequencia() ? 1.0 : 0.0);
        probabilidades[x][QUADRA] = (IsQuadra() ? 1.0 : 0.0);
        probabilidades[x][QUINA] = (IsQuina() ? 1.0 : 0.0);

        x++;

        // Rolando 1 dado: x _ _ _ _
        for (i = 1; i <= 5; i++){ // Indexa o dado (Índice do dado).
            counter = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
            total = 0;

            for (j = 1; j <= 6; j++){
                d[i] = j; // Variando o valor do dado i.
                total++; // Incrementando as possibilidades.

                SetUp(d);

                CountOccurrences();
            }

            // Marcando as probabilidades.
            CalculateProbabilities(x, total);

            // Copiando os dados originais.
            for (j = 1; j <= 5; j++){
                d[j] = dados[j];
            }

            x++;
        }

        // Rolando 2 dados: x x _ _ _
        for (i = 1; i <= 4; i++){ // Indexa o primeiro dado.
            for (j = i + 1; j <= 5; j++){ // Indexa o segundo dado.
                counter = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
                total = 0;

                for (k = 1; k <= 6; k++){ // Varia o primeiro dado.
                    d[i] = k;

                    for (l = 1; l <= 6; l++){ // Varia o segundo dado.
                        d[j] = l;
                        total++;

                        SetUp(d);

                        CountOccurrences();
                    }
                }

                // Marcando as probabilidades.
                CalculateProbabilities(x, total);

                // Copiando os dados originais.
                for (k = 1; k <= 5; k++){
                    d[k] = dados[k];
                }

                x++;
            }
        }

        // Rolando 3 dados: x x x _ _
        for (i = 1; i <= 3; i++){ // Indexando o primeiro dado.
            for (j = i + 1; j <= 4; j++){ // Indexando o segundo dado.
                for (k = j + 1; k <= 5; k++){ // Indexando o terceiro dado.
                    counter = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
                    total = 0;

                    // Variando os dados.
                    for (l = 1; l <= 6; l++){
                        d[i] = l;

                        for (m = 1; m <= 6; m++){
                            d[j] = m;

                            for (n = 1; n <= 6; n++){
                                d[k] = n;
                                total++;

                                SetUp(d);

                                CountOccurrences();
                            }
                        }
                    }

                    // Marcando as probabilidades.
                    CalculateProbabilities(x, total);

                    // Copiando os dados originais.
                    for (l = 1; l <= 5; l++){
                        d[l] = dados[l];
                    }

                    x++;
                }
            }
        }

        // Rolando 4 dados: x x x x _
        for (i = 1; i <= 2; i++){ // Indexando o primeiro dado.
            for (j = i + 1; j <= 3; j++){ // Indexando o segundo dado.
                for (k = j + 1; k <= 4; k++){ // Indexando o terceiro dado.
                    for (l = k + 1; l <= 5; l++){ // Indexando o quarto dado.
                        counter = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
                        total = 0;

                        for (m = 1; m <= 6; m++){
                            d[i] = m;

                            for (n = 1; n <= 6; n++){
                                d[j] = n;

                                for (o = 1; o <= 6; o++){
                                    d[k] = o;

                                    for (p = 1; p <= 6; p++){
                                        d[l] = p;
                                        total++;

                                        SetUp(d);

                                        CountOccurrences();
                                    }
                                }
                            }
                        }

                        // Marcando as probabilidades.
                        CalculateProbabilities(x, total);

                        // Copiando os dados originais.
                        for (m = 1; m <= 5; m++){
                            d[m] = dados[m];
                        }

                        x++;
                    }
                }
            }
        }

        // Rolando 5 dados: x x x x x
        counter = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        total = 0;

        for (i = 1; i <= 6; i++){
            d[1] = i;

            for (j = 1; j <= 6; j++){
                d[2] = j;

                for (k = 1; k <= 6; k++){
                    d[3] = k;

                    for (l = 1; l <= 6; l++){
                        d[4] = l;
                        
                        for (m = 1; m <= 6; m++){
                            d[5] = m;
                            total++;

                            SetUp(d);

                            CountOccurrences();
                        }                       
                    }                   
                }               
            }           
        }

        // Marcando as probabilidades.
        CalculateProbabilities(x, total);

        x++;
    }

    public int[][] GetAnswers(){
        int[][] mat = new int[32][6];
        int x, i, j, k, l;

        x = 1;

        // Rolando 1 dado.
        for (i = 1; i <= 5; i++){
            mat[x][i] = 1;

            x++;
        }

        // Rolando 2 dados.
        for (i = 1; i <= 4; i++){
            for (j = i + 1; j <= 5; j++){
                mat[x][i] = 1;
                mat[x][j] = 1;

                x++;
            }
        }

        // Rolando 3 dados.
        for (i = 1; i <= 3; i++){
            for (j = i + 1; j <= 4; j++){
                for (k = j + 1; k <= 5; k++){
                    mat[x][i] = 1;
                    mat[x][j] = 1;
                    mat[x][k] = 1;
                    
                    x++;
                }
            }
        }

        // Rolando 4 dados.
        for (i = 1; i <= 2; i++){
            for (j = i + 1; j <= 3; j++){
                for (k = j + 1; k <= 4; k++){
                    for (l = k + 1; l <= 5; l++){
                        mat[x][i] = 1;
                        mat[x][j] = 1;
                        mat[x][k] = 1;
                        mat[x][l] = 1;
                        
                        x++;
                    }
                }
            }
        }

        // Rolando 5 dados.
        for (i = 1; i <= 5; i++){
            mat[x][i] = 1;
        }

        return mat;
    }

    public void Initialize(){
        // De 0 a 5, mas usarei apenas os dados de 1 a 5.
        dados = new int[6];

        // Marcando os pontos que eu conseguirei com a probabilidade dada pela matriz de probabilidades para cada jogada.
        pointsMatrix = new int[32][11];

        // Marcando as probabilidades de conseguir uma certa configuração
        // de dados para uma certa quantidade de dados a serem rerolados.
        // De 0 a 5 para a quantidade de dados a serem rerolados.
        // De 1 a 10 para as posições do placar.
        probabilidades = new double[32][11];

        // Pegando a matriz de respostas para cada um dos 32 casos.
        respostas = GetAnswers();

        // Marcando quais posições do placar já foram ocupadas.
        // De 0 a 10, mas usarei apenas de 1 a 10.
        preenchido = new boolean[]{false, false, false, false, false, false, false, false, false, false, false};
        
        // Marcando quantos pontos consigo com a configuração atual dos dados.
        // De 0 a 10, mas usarei apenas 1 a 10.
        points = new int[11];

        // De 0 a 10, mas usarei apenas 1 a 10.
        constPoints = new int[11];

        constPoints[FULL_HAND] = FULL_HAND_POINTS;
        constPoints[SEQUENCIA] = SEQUENCIA_POINTS;
        constPoints[QUADRA] = QUADRA_POINTS;
        constPoints[QUINA] = QUINA;

        // De 0 a 10, mas usarei apenas 1 a 10.
        pesos = new double[11];

        // Guarda valores entre 0 e 31 para cada posição do placar.
        // De 0 a 10, mas usarei apenas 1 a 10.
        melhorCaso = new int[11];
    }

    public void Connect() throws Exception{
        cliente = new Socket("127.0.0.1", 1337);
        // cliente = new Socket("192.168.182.91", 9669);
        entrada = new Scanner(cliente.getInputStream());
        saida = new PrintStream(cliente.getOutputStream());
        teclado = new Scanner(System.in);

        System.out.println("Conectado!");
    }

    public void Disconnect() throws Exception{
        saida.close();
        teclado.close();
        entrada.close();
        cliente.close();

        System.out.println("Desconectado!");
    }

    public void GetDados(String str){
		String[] aux;
		int i;

		aux = str.split(" ");

		// Fazendo parsing.
		for (i = 1; i <= 5; i++){
			dados[i] = Integer.parseInt(aux[i - 1]);
		}
	}
}
