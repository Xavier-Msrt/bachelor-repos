public class ComplexCollectionIterator implements Iterator {
    private final ComplexCollection complexCollection;
    private int index;

    public ComplexCollectionIterator(ComplexCollection complexCollection) {
        this.complexCollection = complexCollection;
    }

    @Override
    public boolean hasNext() {
        return complexCollection.getNames().length > index;
    }

    @Override
    public Object next() {
        index++;
        return complexCollection.getNames()[index-1];
    }
}
