import java.util.ArrayList;

public class Buffer implements Put,Remove{
    private static final int BOUND = 100000;
    private static final boolean ENABLE_PRINTS = false;

    private ArrayList<Integer> mBuffer;
    private boolean mTaskDone;

    public Buffer(){
        mBuffer = new ArrayList<>();
        mTaskDone = false;
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
        if(mTaskDone){
            return true;
        }

        while (getSize() >= BOUND){
            notifyAll();
            try{
                wait();
            }
            catch (InterruptedException e){
                Thread.currentThread().interrupt();
                System.out.println("Thread interrupted "+Thread.currentThread().getName());
            }

            if(mTaskDone){
                return true;
            }
        }

        if(ENABLE_PRINTS){
            System.out.println("Putting: " + val);
        }
        mBuffer.add(val);
        return false;
    }

    @Override
    public boolean isDone() {
        return mTaskDone;
    }

    @Override
    public synchronized boolean remove() {
        while(getSize() < BOUND){
            notifyAll();
            try{
                wait();
            }
            catch (InterruptedException e){
                Thread.currentThread().interrupt();
                System.out.println("Thread interrupted "+Thread.currentThread().getName());
            }
        }
        if(mTaskDone){
            return true;
        }
        removeDuplicates();

        if(getSize() >= BOUND){
            System.out.println("Task done!");
            if(ENABLE_PRINTS){
                printBuffer();
            }
            mTaskDone = true;
            notifyAll();
            return true;
        }

        else{
            return false;
        }
    }
}
