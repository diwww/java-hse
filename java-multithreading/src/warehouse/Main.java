package warehouse;

import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        System.out.println("-------WAREHOUSE-------");

        Scanner in = new Scanner(System.in);
        int M, N, p;
        float t1, t2, T1, T2;

        System.out.print("Enter N: ");
        N = in.nextInt();
        System.out.print("Enter M: ");
        M = in.nextInt();
        System.out.print("Enter t1: ");
        t1 = in.nextFloat();
        System.out.print("Enter t2: ");
        t2 = in.nextFloat();
        System.out.print("Enter T1: ");
        T1 = in.nextFloat();
        System.out.print("Enter T2: ");
        T2 = in.nextFloat();
        System.out.print("Enter p: ");
        p = in.nextInt();

        Warehouse warehouse = new Warehouse(N, M, t1);
        warehouse.start();
        System.out.println("-------STARTED-------");
        System.out.println("To interrupt all threads enter any character and press ENTER.");


        Thread[] farmers = new Thread[N];
        for (int i = 0; i < N; i++) {
            farmers[i] = new Thread(new Farmer(warehouse, T1, p), "F_" + i);
            farmers[i].start();
        }

        Thread[] sellers = new Thread[M];
        for (int i = 0; i < M; i++) {
            sellers[i] = new Thread(new Seller(warehouse, t2, T2), "S_" + i);
            sellers[i].start();
        }


        String exit;
        do {
            exit = in.nextLine();
        }
        while (exit.isEmpty());

        for (int i = 0; i < N; i++) {
            farmers[i].interrupt();
        }

        for (int i = 0; i < M; i++) {
            sellers[i].interrupt();
        }

        warehouse.stop();
    }
}