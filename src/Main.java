import java.sql.Time;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;

public class Main {

    private static int NUM_OF_THREADS = 100;

    /**
     *  Returns number of consumer threads
     *  (~10% of all threads)
     */
    public static int getTreshold(int n){
        int m;
        if( n > 9){
            m = (int)Math.round(n * 0.1);
        }
        else{
            m = 1;
        }
        return m;
    }

    public static void getCurrentTime(){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        System.out.println(dtf.format(now));
    }

    public static void main(String args[]){

        int numOfCons = getTreshold(NUM_OF_THREADS);
        ArrayList<Runnable> runnables = new ArrayList<>();
        ArrayList<Thread> threads = new ArrayList<>();
        Buffer buffer = new Buffer();

        //Creating and adding new producer and consumer threads
        for(int i = 0; i < NUM_OF_THREADS; i++){
            if(i >= numOfCons){
                runnables.add(new Producer(buffer));
            }

            else{
                runnables.add(new Consumer(buffer));
            }
        }

        for(Runnable elem : runnables){
            threads.add(new Thread(elem));
        }

        //Starting
        getCurrentTime();

        for(Thread thread : threads){
            thread.start();
        }


        try{
            for(Thread thread : threads){
                thread.join();
            }
        }
        catch (InterruptedException e){
            System.out.println("InterruptedException in main doing thread.join(): ");
            e.printStackTrace();
        }

        System.out.println("Job finished, size of buffer: "+buffer.getSize());
        getCurrentTime();
        //finished

    }
}
