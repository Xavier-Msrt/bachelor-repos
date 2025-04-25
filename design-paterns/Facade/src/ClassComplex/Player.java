package ClassComplex;

public class Player {
    private int health;
    private String nom;

    public String getNom() {
        return nom;
    }

    public int getHealth() {
        return health;
    }

    public void decHealth() {
        health -= 1;
    }

    public void addHealth() {
        health += 1;
    }


}
