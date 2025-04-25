
public class Main {
    public static void main(String[] args) {
        Window main = new SimpleWindow();
        main.render();
        main = new FullScreenWindowDecorator();
        main.render();
        main = new SimpleWindow();
        main.render();

    }
}