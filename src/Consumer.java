public class Consumer implements Runnable {

    private Remove buffer;

    public Consumer(Remove buffer) {
        this.buffer = buffer;
    }

    @Override
    public void run() {
        boolean isDone = false;

        while(!isDone){
            isDone = buffer.remove();
        }

    }
}
