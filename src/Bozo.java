/**
 * Essa é a classe inicial do programa Bozó. Possui apenas o método main,
 *  que cuida da execução do jogo.
 * @author Victor Forbes - 9293394 / Gabriel Simmel Nascimento - 9050232
 */
public class Bozo{
    /**
     * Método inicial do programa. Ele cuida da execução do jogo e possui
     * um laço, no qual cada iteração representa uma rodada do jogo. Em
     * cada rodada, o jogador joga os dados até 3 vezes e depois escolhe 
     * a posição do placar que deseja preencher. No final das rodadas a
     * pontuação total é exibida.
     * @param args -
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws java.io.IOException{
        Placar placar = new Placar();
        RolaDados dados = new RolaDados(5);
        boolean again;
        int i, j;

        for (i = 0; i < 10; i++){
            System.out.println("Pressione ENTER para iniciar a rodada.");
            EntradaTeclado.leString();
           
            dados.rolar();
            System.out.println(dados);
            
            for (j = 0; j < 2; j++){
                do{
                    try{
                        System.out.println("Escolha os dados a serem rolados.");
                        again = false;
                        dados.rolar(EntradaTeclado.leString());
                    }
                    catch (Exception e){
                        System.out.println(e.getMessage() + " Tente novamente.");
                        again = true;
                    }
                }while (again);

                System.out.println(dados);
            }

            System.out.println(placar);

            do{
                try{
                    System.out.println("Selecione a posição do placar que deseja ocupar.");
                    System.out.println("As posições 1 a 6 correspondem às quantidades de um até seis, ou seja, as laterais do placar.");
                    System.out.println("As outas posições são: 7 - full hand; 8 - sequencia; 9 - quadra; e 10 - quina");
                    again = false;
                    placar.add(EntradaTeclado.leInt(), dados.rolar(""));
                }catch (Exception e){
                    System.out.println(e.getMessage() + "Tente novamente.");
                    again = true;
                }
            }while (again);

            System.out.println(placar);
        }

        System.out.println("Fim! Número de pontos obtidos: " + placar.getScore());
    }
}
