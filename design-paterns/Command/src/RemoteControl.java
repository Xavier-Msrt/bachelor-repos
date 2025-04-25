public class RemoteControl {
    private Command command;

    public RemoteControl(Command command) {
        this.command = command;
    }

    public void pressBotton(){
        command.execute();
    }

    public void setCommand(Command command) {
        this.command = command;
    }
}
