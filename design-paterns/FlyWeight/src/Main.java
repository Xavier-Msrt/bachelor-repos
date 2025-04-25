
public class Main {
    public static void main(String[] args) {
        Team teamBlue = new Team("Blue"); // 1 instance pour 2 player
        Team teamRed= new Team("Red");


        Player p1 = new Player("Lucas", teamBlue);
        Player p2 = new Player("John", teamRed);
        Player p3 = new Player("Bryan", teamBlue);
        Player p4 = new Player("Pierre", teamRed);

        System.out.println(p1);
        System.out.println(p2);
        System.out.println(p3);
        System.out.println(p4);

    }
}