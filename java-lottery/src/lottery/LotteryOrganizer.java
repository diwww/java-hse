package lottery;

import java.util.ArrayList;
import java.util.Random;

public class LotteryOrganizer implements Organizer {

    private ArrayList<Player> players = new ArrayList<>();
    private Random random = new Random();

    @Override
    public void register(Player player) {
        players.add(player);
    }

    @Override
    public void unregister(Player player) {
        players.remove(player);
    }

    public void extractNumber() {
        int number = random.nextInt(10);
        System.out.println("Организатор: Cледующая цифра - " + number);
        for (Player p : players) {
            p.acceptDigit(number);
        }
    }

    private LotteryPlayer convert(Player p) {
        if (p instanceof LotteryPlayer)
            return (LotteryPlayer) p;
        else return null;
    }

    private void defineWinners() {
        int max = 0;

        // Find max count
        for (Player p : players) {
            LotteryPlayer temp = convert(p);
            if (temp.getCount() > max)
                max = temp.getCount();
        }

        // Unregister players with count < max
        for (int i = 0; i < players.size(); i++) {
            LotteryPlayer temp = convert(players.get(i));
            if (temp.getCount() < max) {
                unregister(temp);
                i--;
            }
        }
    }

    public void showResults(int prize) {
        defineWinners();
        if (players.size() < 1) {
            System.out.println("Все вы - лохи!");
        } else {
            System.out.println("Организатор: Победили игроки:");
            prize = prize / players.size();
            // Organizer's announcement
            for (Player p : players) {
                System.out.println(p + " выиграл " + prize + "$");
                LotteryPlayer temp = convert(p);
                temp.setGain(prize);
            }
            // Players' cheers
            System.out.println();
            for (Player p : players) {
                p.gameOver();
            }
        }
    }
}