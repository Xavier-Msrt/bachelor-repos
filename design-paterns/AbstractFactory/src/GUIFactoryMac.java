public class GUIFactoryMac implements GUIFactory {
    @Override
    public Boutton createBouton() {
        return new BouttonMac();
    }

    @Override
    public Checkbox createCheckbox() {
        return null; // TODO
    }
}
