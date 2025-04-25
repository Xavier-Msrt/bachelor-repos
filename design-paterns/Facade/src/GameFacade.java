import ClassComplex.Game;
import ClassComplex.Player;

public class GameFacade {
    private Game game;

    public void start() throws InterruptedException {
        game = new Game(new Player());
        game.lauchGame();
    }

    public void stop(){
        game.stopGame();
    }
}
