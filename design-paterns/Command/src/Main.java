public class Main {
    public static void main(String[] args) {
        Command commandOn = new TurnOnCommand();
        Command commandOff = new TurnOffCommand();

        RemoteControl control = new RemoteControl(commandOn);
        control.pressBotton();
        control.setCommand(commandOff);
        control.pressBotton();
    }
}