import java.util.ArrayList;

public class Buffer implements Put,Remove{
    private static final int BOUND = 100000;
    private static final boolean ENABLE_PRINTS = false;

    private ArrayList<Integer> mBuffer;

    private boolean taskDone;


    public Buffer(){
        mBuffer = new ArrayList<>();
        taskDone = false;
    }

    public void printBuffer(){
        for(Integer elem: mBuffer){
            System.out.print(elem + " ");
        }
    }

    private void removeDuplicates(){
        ArrayList<Integer> tempList = new ArrayList<>();

        for(Integer e : mBuffer){
            boolean notContains = true;

            for(Integer e1 : tempList){
                if(e1.equals(e)){
                    notContains = false;
                }
            }

            if(notContains){
                tempList.add(e);
            }

            else{
                if(ENABLE_PRINTS){
                    System.out.println("Removing: " + e);
                }
            }
        }
        mBuffer = tempList;
    }

    public synchronized int getSize(){
        return mBuffer.size();
    }

    @Override
    public synchronized boolean put(int val) {
        if(taskDone){
            return true;
        }

        while (getSize() >= BOUND){



            try{
                wait();
            }
            catch (InterruptedException e){
                Thread.currentThread().interrupt();
                System.out.println("Thread interrupted "+Thread.currentThread().getName());
            }

            if(taskDone){
                return true;
            }
        }

        if(getSize() < BOUND){
            if(ENABLE_PRINTS){
                System.out.println("Putting: " + val);
            }
            mBuffer.add(val);
        }
        return false;
    }

    @Override
    public boolean isDone() {
        return taskDone;
    }

    @Override
    public synchronized boolean remove() {
        if(taskDone){
            return true;
        }
        removeDuplicates();

        if(getSize() < BOUND){
            notifyAll();
            return false;
        }

        else{
            System.out.println("Task done!");
            if(ENABLE_PRINTS){
                printBuffer();
            }
            taskDone = true;
            notifyAll();
            return true;
        }
    }
}
