
public class Main {
    public static void main(String[] args) {

        Avion avion = Avion.getInstance();

        Personne p1 = new Personne("Marchal", "Lucas", 12);
        Personne p2 = new Personne("Noel", "Julie", 12);

        avion.addPassager(p1);
        avion.addPassager(p2);

        System.out.println(avion);
    }
}