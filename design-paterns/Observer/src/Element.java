public class Element implements Observer{
    @Override
    public void update() {
        System.out.println("I am notified");
    }
}
