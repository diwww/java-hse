package lottery;

import java.util.ArrayList;
import java.util.Random;

public class LotteryPlayer implements Player {

    // Amount of lottery numbers
    public static final int AMOUNT = 6;

    private String name = "player_";
    private int id = -1;
    private int gain = 0;
    private ArrayList<Integer> numbers = new ArrayList<>(AMOUNT);
    private ArrayList<Integer> guessedNumbers = new ArrayList<>(AMOUNT);
    private Random random = new Random();


    @Override
    public void startGame(Organizer organizer, int i) {
        id = i + 1;
        name = name + id;

        for (int j = 0; j < AMOUNT; j++) {
            numbers.add(random.nextInt(10));
        }

        organizer.register(this);
    }

    @Override
    public void acceptDigit(int digit) {
        if (numbers.contains(digit)) {
            numbers.remove(Integer.valueOf(digit));
            guessedNumbers.add(digit);
            System.out.println("\tЯ – " + name + " угадал "
                    + guessedNumbers.size() + "-й раз: " + digit);
        }
    }

    @Override
    public void gameOver() {
        System.out.println(name + ": Ура я выиграл " + gain + "$");
    }

    @Override
    public String toString() {
        return name;
    }

    // Returns an amount of guessed numbers
    public int getCount() {
        return guessedNumbers.size();
    }

    public void setGain(int gain) {
        this.gain = gain;
    }
}