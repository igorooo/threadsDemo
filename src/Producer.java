import java.util.Random;

public class Producer implements Runnable {
    private static final int MAX_INT = 100;

    private Put buffer;
    private Random mGenerator;

    public Producer(Put buffer){
        this.buffer = buffer;
        mGenerator = new Random();
    }

    private boolean isPalindrome(int val){
        String tempVal = Integer.toString(val);
        StringBuilder reversedVal = new StringBuilder(tempVal);
        reversedVal = reversedVal.reverse();

        return tempVal.equals(reversedVal.toString());
    }

    @Override
    public void run() {
        boolean isDone = false;

        while(!isDone){
            int n = mGenerator.nextInt(MAX_INT);

            if(isPalindrome(n)){
                isDone = buffer.put(n);
            }

            // to avoid not necessary activity
            else{
                isDone = buffer.isDone();
            }
        }
    }
}