package ClassComplex;

public class Game {
    private Player player;
    private String state = "idle";

    public Game(Player player) {
        this.player = player;
    }

    public void lauchGame() throws InterruptedException {
        state = "started";
        for (int i = 1; i <= 10; i++) {
            if(!state.equals("stop")){
                System.out.println("Game start in " + i);
                Thread.sleep(1000);
            }else{
                return;
            }
        }
        player.decHealth();
    }

    public void stopGame(){
        state = "stop";
        System.out.println("Game stop");
    }

}
