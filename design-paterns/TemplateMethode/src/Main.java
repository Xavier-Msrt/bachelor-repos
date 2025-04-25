import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        ArrayList<Number> numbers = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            numbers.add(new Number(i));
        }

        SortingNumberInpair sortingNumberInpair = new SortingNumberInpair(numbers);
        sortingNumberInpair.sort();
        System.out.println("---");
        SortingNumberPair sortingNumberPair = new SortingNumberPair(numbers);
        sortingNumberPair.sort();

    }
}