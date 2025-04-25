import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        ComplexCollection complexCollection = new ComplexCollection();

        //System.out.println(Arrays.toString(complexCollection.getNames()));

        Iterator iterator = complexCollection.createIterator();
            while (iterator.hasNext()){

                System.out.println(iterator.next().toString());
            }
    }
}