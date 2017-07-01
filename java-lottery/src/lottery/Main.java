// Homework assignment: Lottery
// Date:    12.10.2016
// Name:    Surovtsev Maxim
// Group:   BSE151(1)

package lottery;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        int N;

        // Parse N
        try {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Введите количество игроков N: ");
            N = Integer.parseInt(scanner.nextLine());
            if (N <= 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            System.out.println("Нужно ввести целое положительное число!");
            return;
        }

        // Start lottery
        int prize = N * 100;
        LotteryOrganizer organizer = new LotteryOrganizer();
        for (int i = 0; i < N; i++) {
            Player player = new LotteryPlayer();
            player.startGame(organizer, i);
        }

        // Extract lottery numbers
        for (int i = 0; i < LotteryPlayer.AMOUNT; i++) {
            organizer.extractNumber();
        }

        // Finally, show results
        System.out.println();
        organizer.showResults(prize);
    }
}