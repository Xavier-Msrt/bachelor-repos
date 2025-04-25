import java.util.ArrayList;

public class SortingNumberPair extends SortNumber{
    public SortingNumberPair(ArrayList<Number> numbers) {
        super(numbers);
    }

    @Override
    boolean sortingMethod(Number number) {
        return number.getNumber() % 2 == 0;
    }
}
