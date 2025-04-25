public class DiscountEmployee implements DiscountStrategy{
    @Override
    public int discountApply(int price) {
        return (int) (price * 0.65); // 35%
    }
}
