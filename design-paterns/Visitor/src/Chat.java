public class Chat implements Animal{
    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
