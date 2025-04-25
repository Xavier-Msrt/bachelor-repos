public class GUIFactoryWindows implements GUIFactory{
    @Override
    public Boutton createBouton() {
        return new BouttonWindows();
    }

    @Override
    public Checkbox createCheckbox() {
        return null; // TODO
    }
}
