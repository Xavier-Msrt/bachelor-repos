public class CashRegister {
    DiscountStrategy discountStrategy;

    public CashRegister(DiscountStrategy discountStrategy) {
        this.discountStrategy = discountStrategy;
    }

    void use(int price){
        System.out.println("check ...");
        int finalPrice = discountStrategy.discountApply(price);
        System.out.println("The price is "+finalPrice);

    }
}
