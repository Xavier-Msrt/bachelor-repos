import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        Visitor visitor = new VisitorImp();

        ArrayList<Chien> chiens = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            chiens.add(new Chien());
        }

        for (int i = 0; i < chiens.size(); i++) {
            chiens.get(i).accept(visitor);
        }


    }
}