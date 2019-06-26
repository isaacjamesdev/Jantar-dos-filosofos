import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

public class JantarFilosofos  {

    public static final int quantidadeFilosofos = 5;
    public static final Semaphore mutex = new Semaphore(1);
    public static final List<String> nomes = new ArrayList<String>();
    public static final List<Filosofo> filosofos = new ArrayList<Filosofo>();
    
    public static void main(String[] args) {
        
        // Nomeando filosofos
    	nomes.add("Karnal");
    	nomes.add("Cortella");
    	nomes.add("Clovis");
        nomes.add("Jefferson");
        nomes.add("James");
        
        // instanciando os pensandores
        for (int i = 0; i < quantidadeFilosofos; i++) {
            filosofos.add(new Filosofo(i, nomes.get(i)));
        }
        
        // Inicializando as threads
        for (Thread t : filosofos) {
            t.start();
        }   
    }
}