import java.util.ArrayList;

public class GuildOfPlayer implements EntityAttack {

    ArrayList<Player> players = new ArrayList<>();

    public GuildOfPlayer(ArrayList<Player> players) {
        this.players = players;
    }

    @Override
    public void attack() {
        System.out.println("Guild of player attack");
        for (int i = 0; i < players.size(); i++) {
            players.get(i).attack();
        }
    }
}
