import java.util.ArrayList;

public class Avion {
    private String name;
    private ArrayList<Personne> passagers;

    private static Avion instance;
    private Avion(String name) {
        this.name = name;
        passagers = new ArrayList<>();
    }

    public static Avion getInstance() {
        if(instance == null) {
            instance = new Avion("Avion-459");
        }
        return instance;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Personne> getPassagers() {
        return passagers;
    }

    public void addPassager(Personne p) {
        passagers.add(p);
    }

    @Override
    public String toString() {
        return "Avion{" +
                "name='" + name + '\'' +
                ", passagers=" + passagers +
                '}';
    }
}
