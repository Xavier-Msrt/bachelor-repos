import java.util.ArrayList;

public class ArmyOfMonster implements EntityAttack {

    ArrayList<Monster> monsters = new ArrayList<>();

    public ArmyOfMonster(ArrayList<Monster> monsters) {
        this.monsters = monsters;
    }

    @Override
    public void attack() {
        System.out.println("Army of monster attack");
        for (int i = 0; i < monsters.size(); i++) {
            monsters.get(i).attack();
        }
    }
}


