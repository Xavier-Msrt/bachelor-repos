public class Main {
    public static void main(String[] args) {
        GUIFactoryWindows GUIWindows = new GUIFactoryWindows();
        GUIWindows.createBouton().pain();

        GUIFactoryMac GUIMac = new GUIFactoryMac();
        GUIMac.createBouton().pain();
    }
}