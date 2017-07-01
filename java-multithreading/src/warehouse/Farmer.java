package warehouse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadLocalRandom;

public class Farmer implements Runnable {

    private Warehouse warehouse;
    private float T1; // latency between generating
    private int p; // probability of spoiled item

    public Farmer(Warehouse w, float T1, int p) {
        warehouse = w;
        this.T1 = T1;
        this.p = p;
    }

    private void supply(int n, int p) throws InterruptedException {
        List<Item> items = new ArrayList<>();
        int count = (int) (p / 100.0 * n); // count of spoiled items
        // Add spoiled items
        for (int i = 0; i < count; i++) {
            items.add(new Item(true));
        }
        // Add unspoiled items
        for (int i = 0; i < n - count; i++) {
            items.add(new Item(false));
        }
        Collections.shuffle(items);
        warehouse.recieve(items);
        System.out.println(Thread.currentThread().getName() + ": " + States.SUPPLIED);
    }

    @Override
    public void run() {
        try {
            while (true) {
                supply(ThreadLocalRandom.current().nextInt(10, 1001), p);
                Thread.sleep((int) T1 * 1000);
            }
        } catch (InterruptedException e) {
            System.out.println(Thread.currentThread().getName() + ": " + States.INTERRUPTED);
        }
    }
}