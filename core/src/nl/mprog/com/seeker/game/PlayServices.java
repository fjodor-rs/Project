package nl.mprog.com.seeker.game;

/**
 * Created by Fjodor on 2017/01/19.
 */

public interface PlayServices {
    public void signIn();
    public void signOut();
    public void rateGame();
    public void unlockAchievement();
    public void submitScore(int highScore);
//    public void showAchievement();
    public void showScore();
    public boolean isSignedIn();
}
