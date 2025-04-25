public class Chien implements Animal{
    @Override
    public void accept(Visitor visitor) { // fait appel a la methode specifique
        visitor.visit(this);
    }
}
