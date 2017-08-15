/**
 * Esta classe representa o placar de um jogo de Bozó. Permite que
 * combinações de dados sejam alocadas às posições e mantém o
 * escore de um jogador.
 * @author Victor Forbes - 9293394 / Gabriel Simmel Nascimento - 9050232
 */
public class Placar{
	int[] points;

	/**
	 * Construtor padrão do Placar que inicializa o vetor de pontos com -1.
	 */
	public Placar(){
		int i;

		points = new int[11];

		for (i = 1; i <= 10; i++){
			points[i] = -1;
		}
	}

	/**
	 * Adiciona uma sequencia de dados em uma determinada posição do placar.
	 * Após a chamada, aquela posição torna-se ocupada e não pode ser usada
	 * uma segunda vez.
	 * @param posicao Posição a ser preenchida. As posições 1 a 6 correspondem
	 * às quantidas de uns até seis, ou seja, as laterais do placar. As outas
	 * posições são: 7 - full hand; 8 - sequencia; 9 - quadra; e 10 - quina
	 * @param dados um array de inteiros, de tamanho 5. Cada posição corresponde
	 * a um valor de um dado. Supões-se que cada dado pode ter valor entre 1 e 6.
	 * @throws java.lang.IllegalArgumentException Se a posição estiver ocupada ou
	 * se for passado um valor de posição inválido, essa exceção é lançada. Não é
	 * feita nenhuma verificação quanto ao tamanho do array nem quanto ao seu conteúdo.
	 */
	public void add(int posicao, int[] dados) throws IllegalArgumentException{
		int[] occurrence = new int[7];
		int[] identifier = new int[6];
		int i;

		if (posicao >= 1 && posicao <= 10){
			if (points[posicao] == -1){
				for (i = 0; i < 5; i++){
					occurrence[dados[i]]++;
				}

				for (i = 1; i <= 6; i++){
					identifier[occurrence[i]]++;
				}

				if (posicao <= 6){
					points[posicao] = occurrence[posicao] * posicao;
				}
				else if (posicao == 7 && identifier[3] == 1 && identifier[2] == 1){
					points[posicao] = 15;
				}
				else if (posicao == 8 && identifier[1] == 5 && (occurrence[1] == 0 || occurrence[6] == 0)){
					points[posicao] = 20;
				}
				else if (posicao == 9 && (identifier[4] == 1 || identifier[5] == 1)){
					points[posicao] = 30;
				}
				else if (posicao == 10 && identifier[5] == 1){
					points[posicao] = 40;
				}
				else{
					points[posicao] = 0;
				}
			}
			else{
				throw new IllegalArgumentException("Posição ocupada.");
			}
		}
		else{
			throw new IllegalArgumentException("Posição inválida.");
		}
	}

	/**
	 * Computa a soma dos valores obtidos, considerando apenas as posições
	 * que já estão ocupadas.
	 * @return O valor da soma.
	 */
	public int getScore(){
		int soma = 0;
		int i;

		for (i = 1; i <= 10; i++){
			soma += points[i] == -1 ? 0 : points[i];
		}

		return soma;
	}

	/**
	 * A representação na forma de string, mostra o placar completo,
	 * indicando quais são as posições livres (com seus respectivos 
	 * números) e o valor obtido nas posições já ocupadas. Por exemplo: 
	 *
 	 * (1)    |   (7)    |   (4) 
 	 * --------------------------
 	 * (2)    |   20     |   (5) 
 	 * --------------------------
 	 * (3)    |   30     |   (6) 
	 * --------------------------
 	 *        |   (10)   |
 	 *        +----------+ 
	 *
	 * mostra as posições 8 (sequencia) e 9 (quadra) ocupadas.
	 */
	@Override
	public java.lang.String toString(){
		java.lang.String str = "";

		if (points[1] == -1){
			str += "(1)    |   ";
		}
		else{
			str += (points[1] < 10 ? "0" : "") + points[1] + "     |   ";
		}

		if (points[7] == -1){
			str += "(7)    |   ";
		}
		else{
			str += points[7] + "     |   ";
		}

		if (points[4] == -1){
			str += "(4) ";
		}
		else{
			str += (points[4] < 10 ? "0" : "") + points[4] + "  ";
		}

		str += "\n--------------------------\n";

		if (points[2] == -1){
			str += "(2)    |   ";
		}
		else{
			str += (points[2] < 10 ? "0" : "") + points[2] + "     |   ";
		}

		if (points[8] == -1){
			str += "(8)    |   ";
		}
		else{
			str += points[8] + "     |   ";
		}

		if (points[5] == -1){
			str += "(5) ";
		}
		else{
			str += (points[5] < 10 ? "0" : "") + points[5] + "  ";
		}

		str += "\n--------------------------\n";

		if (points[3] == -1){
			str += "(3)    |   ";
		}
		else{
			str += (points[3] < 10 ? "0" : "") + points[3] + "     |   ";
		}

		if (points[9] == -1){
			str += "(9)    |   ";
		}
		else{
			str += points[9] + "     |   ";
		}

		if (points[6] == -1){
			str += "(6) ";
		}
		else{
			str += (points[6] < 10 ? "0" : "") + points[6] + "  ";
		}

		str += "\n--------------------------\n";
		str += "       |   ";

		if (points[10] == -1){
			str += "(10)   |";
		}
		else{
			str += " " + points[10] + "    |";
		}

		str += "\n       +----------+\n";

		return str;
	}

    public static void main(String[] args) throws Exception{
    	
    }
}