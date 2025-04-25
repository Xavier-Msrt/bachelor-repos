public class ComplexCollection {
    private final String[] names = {"Hello", "world"};
    public Iterator createIterator(){
        return new ComplexCollectionIterator(this);
    }

    public String[] getNames() {
        return names;
    }
}
