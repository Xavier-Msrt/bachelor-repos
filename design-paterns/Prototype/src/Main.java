public class Main {
    public static void main(String[] args) {
        Sheep sheep = new Sheep("Gerda", 15);
        System.out.println(sheep);

        Sheep child = sheep.clone();
        System.out.println(child);
    }
}