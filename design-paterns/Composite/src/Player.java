public class Player implements EntityAttack{
    private final String name;

    public Player(String name) {
        this.name = name;
    }

    @Override
    public void attack() {
        System.out.println("Player "+name+" Attack");
    }
}
