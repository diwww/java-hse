package lottery;

public interface Player {
    void startGame(Organizer organizer, int i);
    void acceptDigit(int digit);
    void gameOver();
}