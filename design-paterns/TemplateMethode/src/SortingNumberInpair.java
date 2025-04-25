import java.util.ArrayList;

public class SortingNumberInpair extends SortNumber{
    public SortingNumberInpair(ArrayList<Number> numbers) {
        super(numbers);
    }

    @Override
    boolean sortingMethod(Number number) {
        return number.getNumber() % 2 != 0;
    }
}
