public class VisitorImp implements Visitor{
    @Override
    public void visit(Chat chat) {
        System.out.println("Visited chat");
    }

    @Override
    public void visit(Chien chien) {
        System.out.println("Visited chien");
    }
}
