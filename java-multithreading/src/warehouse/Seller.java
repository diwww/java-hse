package warehouse;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class Seller implements Runnable {

    private Warehouse warehouse;
    private float t2, T2;

    public Seller(Warehouse warehouse, float t2, float T2) {
        this.warehouse = warehouse;
        this.t2 = t2;
        this.T2 = T2;
    }

    private void pickup(int m) throws InterruptedException {
        warehouse.lock.lock();
        try {
            while (m > warehouse.getSize() || !warehouse.isOpened()) {
                System.out.println(Thread.currentThread().getName() + ": " + States.WAITING_FOR_OPENING);
                warehouse.sellerCondition.await();
            }
            System.out.println(Thread.currentThread().getName() + ": " + States.PICKING_UP + " " + m + " items");
            warehouse.giveaway(m);
            Thread.sleep((int) t2 * 1000);
            System.out.println(Thread.currentThread().getName() + ": " + States.QUITING_WAREHOUSE);
            warehouse.inspectorCondition.signalAll();
            warehouse.sellerCondition.await((int) T2 * 1000, TimeUnit.MILLISECONDS);
        } finally {
            warehouse.lock.unlock();
        }
    }


    @Override
    public void run() {
        try {
            while (true) {
                pickup(ThreadLocalRandom.current().nextInt(10, 101));
            }
        } catch (InterruptedException e) {
            System.out.println(Thread.currentThread().getName() + ": " + States.INTERRUPTED);
        }
    }
}
