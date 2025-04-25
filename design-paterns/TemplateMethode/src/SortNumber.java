import java.util.ArrayList;

public abstract class SortNumber {

    ArrayList<Number> numbers;
    public SortNumber(ArrayList<Number> numbers) {
        this.numbers = numbers;
    }

    void sort(){
        //do stuff
        for (Number element : numbers) {
            if(sortingMethod(element)){
                System.out.println(element);
            }
        }
        //do stuff
    }

    abstract boolean sortingMethod(Number number);
}
