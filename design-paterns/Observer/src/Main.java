public class Main {
    public static void main(String[] args) {
        Subject subject = new SubjectImpl();
        Element element = new Element();
        subject.attach(element);

        // all observer notify
        subject.notifyObserver();
    }
}