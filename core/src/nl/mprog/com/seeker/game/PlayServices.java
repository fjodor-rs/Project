package nl.mprog.com.seeker.game;

/**
 * Fjodor van Rijsselberg
 * Student number: 11409231
 *
 * Holds all the methods that work with Google Play Services
 */

public interface PlayServices {
    public void signIn();
    public void signOut();
    public void rateGame();
    public void unlockAchievement();
    public void submitTime(int highScore);
    public void showAchievement();
    public void showTime();
    public boolean isSignedIn();
}
