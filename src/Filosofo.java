import java.util.concurrent.Semaphore;


public class Filosofo extends Thread {

    // Declara��o de vari�veis e objetos
    private final int id;
    private final String nome;
    private Estado estado;
    private final Semaphore semaforo = new Semaphore(0);

    // Construtor
    Filosofo(int id, String nome) {
        this.id = id;
        this.nome = nome;
        estado = Estado.PENSANDO;
    }
    
    public void run() {
        try {
            while (true) {
                imprime();
                switch(estado) {
                    case PENSANDO:
                        // pausa a thread
                        pensando();
                        // Exclus�o m�tua - Come�a a executar
                        JantarFilosofos.mutex.acquire();
                        // Altera o status para faminto
                        estado = Estado.FAMINTO; 
                        break;
                    case FAMINTO:
                        // Executa o m�todo que verifica se os fil�sofos a direita e esquerda est�o comendo
                        teste(this);
                        // Exclus�o m�tua - Termina a execu��o
                        JantarFilosofos.mutex.release();
                        // Bloqueia o sem�foro
                        semaforo.acquire();
                        // Altera o estado para comendo
                        estado = Estado.COMENDO;
                        break;
                    case COMENDO:
                        comendo();
                        // Exclus�o m�tua - Come�a a executar
                        JantarFilosofos.mutex.acquire();
                        // Altera o estado para pensando
                        estado = Estado.PENSANDO;
                        // Executa o m�todo que verifica se os fil�sofos a direita e esquerda est�o comendo
                        // Como s�o passados os vizinhos como par�metro serve como uma cutucada para que os vizinhos comam
                        // Caso estejam famintos e seus vizinhos n�o estejam comendo
                        teste(esquerda());
                        teste(direita());
                        // Exclus�o m�tua - Termina a execu��o
                        JantarFilosofos.mutex.release();
                        break;          
                }
            }
        } catch(InterruptedException e) {}
    }
    
    private static void teste(Filosofo f) {
    	
    	if (f.esquerda().estado != Estado.COMENDO
    			&& f.estado == Estado.FAMINTO
    			&& f.direita().estado != Estado.COMENDO) {
    		f.estado = Estado.COMENDO;
    		f.semaforo.release();
    	}
    	
    }
    
    // M�todo que pega o fil�sofo a esquerda
    public Filosofo esquerda() {
      return JantarFilosofos.filosofos.get(id == 0 ? JantarFilosofos.quantidadeFilosofos - 1 : id - 1);
    }

    // M�todo que pega o fil�sofo a direita
    public Filosofo direita() {
        return JantarFilosofos.filosofos.get((id + 1) % JantarFilosofos.quantidadeFilosofos);
    }

    // M�todo que verifica se o fil�sofo da esquerda ou direita est� comendo, caso esteja libera o sem�foro

    // Defini��o do tempo que o fil�sofo fica pensando antes de ficar faminto
    private void pensando() {
        try {
            Thread.sleep((long) Math.round(Math.random() * 5000));
        } catch (InterruptedException e) {}
    }
    
    // Defini��o do tempo que o fil�sofo fica comendo antes de voltar a ficar pensando
    private void comendo() {
        try {
            Thread.sleep((long) Math.round(Math.random() * 5000));
        } catch (InterruptedException e) {}
    }

    // M�todo que faz a impress�o
    private void imprime() {
        System.out.println("(" + (id + 1) + ") " + nome + " est� " + estado.getDescricao());
    }
    
}
