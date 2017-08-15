/**
 * Esta classe representa um dado, ela possui as funções
 * básicas de um dado, como rolar um número aleatório entre
 * 1 e seu número de lados.
 * @author Victor Forbes - 9293394 / Gabriel Simmel Nascimento - 9050232
 */
public class Dado{
    private Random rnd;
    private int last;
    private int sides;

    /**
     * Cria um dado com 6 lados (um cubo).
     */
    public Dado(){
        last = -1;
        sides = 6;
        rnd = new Random();
    }

    /**
     * Cria objeto com um número qualquer de lados.
     * @param n Número de lados do dado.
     */
    public Dado(int n){
        if (n > 0){
            last = -1;
            sides = n;
            rnd = new Random();
        }
    }

    /**
     * Recupera o último número selecionado.
     * @return o número do último lado selecionado.
     */
    public int getLado(){
        return last;
    }

    /**
     * Simula a rolagem do dado por meio de um gerador
     * aleatório. O número selecionado pode posteriormente
     * ser recuperado com a chamada a getLado().
     * @return o número que foi sorteado.
     */
    public int rolar(){
        return last = rnd.getIntRand(sides) + 1;
    }

    /**
     * Transforma representação do dado em String. É mostrada
     * uma representação do dado que está para cima. Note que
     * só funciona corretamente para dados de 6 lados. Exemplo:
     * +-----+    
     * |*   *|    
     * |  *  |    
     * |*   *|    
     * +-----+
     */
    @Override
    public java.lang.String toString(){
        java.lang.String str = "+-----+\n";

        switch (last){
            case 1:
                str += "|     |\n";
                str += "|  *  |\n";
                str += "|     |\n";
                break;
            case 2:
                str += "|*    |\n";
                str += "|     |\n";
                str += "|    *|\n";
                break;
            case 3:
                str += "|*    |\n";
                str += "|  *  |\n";
                str += "|    *|\n";
                break;
            case 4:
                str += "|*   *|\n";
                str += "|     |\n";
                str += "|*   *|\n";
                break;
            case 5:
                str += "|*   *|\n";
                str += "|  *  |\n";
                str += "|*   *|\n";
                break;
            case 6:
                str += "|*   *|\n";
                str += "|*   *|\n";
                str += "|*   *|\n";
                break;
            default:
                break;
        }

        return str + "+-----+";
    }

    /**
     * Não tem função real dentro da classe. Foi usada
     * apenas para testar os métodos implementados.
     * @param args Sem uso.
     */
    public static void main(java.lang.String[] args){

    }
}
