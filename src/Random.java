import java.util.Calendar;

/**
 * Gerador simples de números aleatórios.
 * @author Victor Forbes - 9293394 / Gabriel Simmel Nascimento - 9050232
 */
public class Random{
    private long a = 453816693;
    private long m = 843314861;
    private long p = 2147483648l;
    private long xi = 1023;
    Calendar c = Calendar.getInstance();

    /**
     * Construtor que usa uma semente aleatória, adquirida
     * usando o método Calendar.getTimeInMillis().
     */
    public Random(){
        xi = c.getTimeInMillis() % p;
    }

    /**
     * Construtor que permite criar o gerador, especificando o valor inicial da semente.
     * @param k O valor inicial da semente.
     */
    public Random(long k){
        if (k >= 0 && k < p){
            xi = k;
        }
    }

    /**
     * Retorna um número aleatório no intervalo (0,1[
     * @return O número gerado.
     */
    public double getRand(){
        xi = (a + m * xi) % p;

        return (double)xi / (double)p;
    }

    /**
     * Retorna um número aleatório no intervalo [0, INT_MAX]
     * @return O número gerado.
     */
    public int getIntRand(){
        return (int)(getRand() * (p));
    }

    /**
     * Retorna um número aleatório no intervalo [0, max)
     * @param max O valor limite para a geração do número inteiro.
     * @return O número gerado.
     */
    public int getIntRand(int max){
        if (max >= 0){
            return (int)(getRand() * (max));
        }

        return -1;
    }

    /**
     * Retorna um número aleatório no intervalo [min, max)
     * @param min O limite inferior para a geração do número inteiro.
     * @param max O limite superior para a geração do número inteiro.
     * @return O número gerado.
     */
    public int getIntRand(int min, int max){
        if (min < max){
            return (int)(getRand() * (max - min + 1)) + min;
        }

        return -1;
    }

    /**
     * Permite alterar a semente de geração de números aleatórios.
     * Supostamente deve ser chamada antes de iniciar a geração,
     * mas se for chamado a qualquer instante, reseta o valor da
     * semente.
     * @param semente O valor da nova semente de geração.
     */
    public void setSemente(long semente){
        if (semente >= 0 && semente < p){
            xi = semente;
        }
    }
}
