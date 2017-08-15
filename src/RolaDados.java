/**
 * Essa é uma classe auxiliar que permite gerencia um conjunto 
 * de vários dados simultaneamente. Operações como rolar alguns 
 * dos dados ou exibir o resultado de todos eles são implementadas.
 * @author Victor Forbes - 9293394 / Gabriel Simmel Nascimento - 9050232
 */
public class RolaDados{
	private Dado[] dado;
	private int amount;

	/**
     * Construtor que cria e armazena vários objetos do tipo Dado.
     * @param n que sera a quantidade de dados a serem criados
     */
	public RolaDados(int n){
		int i;

        if (n > 0){
    		dado = new Dado[n];
    		amount = n;
    		
    		for (i = 0; i < n; i++){
    			dado[i] = new Dado();
    			try{
    				Thread.sleep(1);
    			}
    			catch (InterruptedException ex){
    				Thread.currentThread().interrupt();
    			}
    		}
        }
	}

	/**
     * Rola todos os dados.
     * @return Retorna o valor de cada um dos dados, inclusive os que não foram rolados.
     * Nesse caso, o valor retornado é o valor anterior que ele já possuia.
     *
     */
	public int[] rolar(){
		int[] v = new int[amount];
		int i;

		for (i = 0; i < amount; i++){
			v[i] = dado[i].rolar();
		}

		return v;
	}

	/**
     * Rola alguns dos dados.
     * @param quais - É um array de booleanos que indica quais dados devem ser rolados.
     * Cada posição representa um dos dados. Ou seja, a posição 0 do array indica se o
     * dado 1 deve ser rolado ou não, e assim por diante.
     * @return Retorna o valor de cada um dos dados, inclusive os que não foram rolados.
     * Nesse caso, o valor retornado é o valor anterior que ele já possuia.
     *
     */
	public int[] rolar(boolean[] quais){
        int[] v = new int[amount];
        int i;

        for(i = 0; i < amount ; i++){
            if (quais[i]){
            	v[i] = dado[i].rolar();
            }
            else{
            	v[i] = dado[i].getLado();
            }
        }

        return v;
    }

    /**
     * Rola alguns dos dados.
     * @param s - É um String que possui o número dos dados a serem rolados.
     * Por exemplo "1 4 5" indica que os dados 1 4 e cinco devem ser rolados.
     * Os números devem ser separados por espaços. Se o valor passado no string
     * estiver fora do intervalo válido, ele é ignorado simplesmente.
     * @return vetor que inteiros que cada posiçao representa o numero que deu em cada dado
     */
    public int[] rolar(java.lang.String s){
        boolean[] quais = new boolean[amount];
        boolean flag;
        int i, j, x;

        s += " ";
        x = -1;

        for (i = 0; i < s.length(); i++){
        	flag = true;

            if (s.charAt(i) >= '1' && s.charAt(i) <= '9' && s.charAt(i + 1) == ' '){	
            	for (j = i; s.charAt(j) >= '0' && s.charAt(j) <= '9'; j++);

            	try{
            		x = Integer.parseInt(s.substring(i, j));
            	}
            	catch (Exception e){
            		flag = false;
            	}

            	if (flag){
            		quais[x - 1] = true;
            	}
            }
        }

        return rolar(quais);
    }

    /**
     * Método auxiliar que retorna uma string com as partições de x a y - 1 de todos
     * de todos os dados.
     * @return A String auxiliar.
	 */
	private java.lang.String toStringAux(int x, int y){
		java.lang.String str = "";
		int i;

		for (i = 0; i < amount; i++){
			str += dado[i].toString().substring(x, y) + "     ";
		}
		str += "\n";

		return str;
	}

	/**
     * Usa a representação em string do dados, para mostrar o valor de todos os dados
     * do conjunto. Exibe os dados horisontalmente, por exemplo:
     *
     * 1          2          3          4          5
     * +-----+    +-----+    +-----+    +-----+    +-----+    
     * |*   *|    |     |    |*    |    |*    |    |*   *|    
     * |  *  |    |  *  |    |     |    |  *  |    |     |    
     * |*   *|    |     |    |    *|    |    *|    |*   *|    
     * +-----+    +-----+    +-----+    +-----+    +-----+  
     */
	@Override
	public java.lang.String toString(){
		java.lang.String str = "";
		int i;

		for (i = 0; i < amount; i++){
			str += " " + (i + 1) + "          ";
		}
		str += "\n";

		str += toStringAux(0, 7);
		str += toStringAux(8, 15);
		str += toStringAux(16, 23);
		str += toStringAux(24, 31);
		str += toStringAux(32, 39);

		return str;
	}

    public static void main(String[] args) throws java.lang.Exception{

    }
}