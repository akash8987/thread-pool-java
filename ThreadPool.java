import java.util.LinkedList;
import java.util.Queue;

class ThreadPool {
    private final Queue<Runnable> taskQueue = new LinkedList<>();
    private final Worker[] workers;

    public ThreadPool(int size) {
        workers = new Worker[size];
        for (int i = 0; i < size; i++) {
            workers[i] = new Worker();
            workers[i].start();
        }
    }

    public synchronized void submit(Runnable task) {
        taskQueue.offer(task);
        notify();
    }

    private synchronized Runnable take() throws InterruptedException {
        while (taskQueue.isEmpty()) {
            wait();
        }
        return taskQueue.poll();
    }

    private class Worker extends Thread {
        public void run() {
            try {
                while (true) {
                    Runnable task = take();
                    task.run();
                }
            } catch (InterruptedException ignored) {}
        }
    }
}
