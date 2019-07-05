import java.util.ArrayList;

public class Buffer implements Put,Remove{
    private static final int BOUND = 10;

    private ArrayList<Integer> mBuffer;
    private boolean taskDone;

    public Buffer(){
        mBuffer = new ArrayList<>();
        taskDone = false;
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
                System.out.println("Removing: " + e);
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

        else{
            System.out.println("Putting: " + val);
            mBuffer.add(val);
            return false;
        }
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

        if(mBuffer.size() < BOUND){
            return false;
        }

        else{
            System.out.println("Task done!");
            taskDone = true;
            return true;
        }
    }
}
