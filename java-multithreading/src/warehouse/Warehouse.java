package warehouse;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Warehouse {

    public final Lock lock = new ReentrantLock(); // lock for seller and inspector
    public final Condition inspectorCondition = lock.newCondition();
    public final Condition sellerCondition = lock.newCondition();
    BlockingQueue<List<Item>> queue; // where farmers supply items
    List<Item> items; // warehouse's stock
    private boolean opened; // if the warehouse is opened for sellers
    private int N; // only limited number of farmers can be served
    private int M; // number of sellers
    private final Inspector inspector;
    private final Thread inspectorThread;

    public Warehouse(int n, int m, float t1) {
        N = n;
        M = m;
        opened = false;
        queue = new ArrayBlockingQueue<>(N);
        items = new ArrayList<>();
        inspector = new Inspector(t1);
        inspectorThread = new Thread(inspector, "Inspector");
    }

    /**
     * Starts warehouse process
     */
    public void start() {
        inspectorThread.start();
    }

    /**
     * Stops warehouse process
     */
    public void stop() {
        inspectorThread.interrupt();
    }

    /**
     * Returns warehouse open condition
     *
     * @return open flag
     */
    public boolean isOpened() {
        return opened;
    }

    /**
     * Gets size of items list
     *
     * @return size of items list
     */
    public int getSize() {
        return items.size();
    }

    /**
     * Recieves items from a farmer
     *
     * @param temp - list of items to check
     * @throws InterruptedException
     */
    public void recieve(List<Item> temp) throws InterruptedException {
        queue.put(temp);
    }

    /**
     * Picks up an item set by seller
     *
     * @param m - number of items in current set
     * @throws InterruptedException
     */
    public void giveaway(int m) throws InterruptedException {
        for (int i = 0; i < m; i++) {
            items.remove(items.size() - 1);
        }
    }

    private class Inspector implements Runnable {
        private float t1; // item check time

        public Inspector(float t1) {
            this.t1 = t1;
        }

        /**
         * Checks given item set whether it contains
         * spoiled items, or opens warehouse for sellers
         *
         * @throws InterruptedException
         */
        public void checkItems() throws InterruptedException {
            lock.lock();
            try {
                // If there are no farmers and warehouse is not empty
                while (queue.isEmpty() && items.size() > 0) {
                    if (!opened) {
                        System.out.println(Thread.currentThread().getName() + ": " + States.OPENING_WAREHOUSE);
                        opened = true;
                        sellerCondition.signalAll();
                    }
                    inspectorCondition.await();
                }

                opened = false;
                List<Item> temp = queue.take();
                System.out.println(Thread.currentThread().getName() + ": " + States.CHECKING);
                for (Item i : temp) {
//                    System.out.println("\tSpoiled: " + i.isSpoiled());
                    if (!i.isSpoiled()) {
                        items.add(i);
                    }
                    Thread.sleep((int) t1 * 1000);
                }
                System.out.println("\tWarehouse, items count: " + items.size());
            } finally {
                lock.unlock();
            }
        }

        @Override
        public void run() {
            try {
                while (true) {
                    checkItems();
                }
            } catch (InterruptedException e) {
                System.out.println(Thread.currentThread().getName() + ": " + States.INTERRUPTED);
            }
        }
    }
}
