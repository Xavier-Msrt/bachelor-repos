import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        Player p1 = new Player("Furax");
        Player p2 = new Player("SP1N0X");
        Player p3 = new Player("Skinzer");

        ArrayList<Player> players = new ArrayList<>();
        players.add(p1);
        players.add(p2);
        players.add(p3);
        GuildOfPlayer guildOfPlayer = new GuildOfPlayer(players);

        Monster m1 = new Monster();
        Monster m2 = new Monster();

        ArrayList<Monster> monsters = new ArrayList<>();
        monsters.add(m1);
        monsters.add(m2);
        ArmyOfMonster armyOfMonster = new ArmyOfMonster(monsters);


        // game
        p1.attack();
        p3.attack();

        armyOfMonster.attack();

        guildOfPlayer.attack();




    }
}