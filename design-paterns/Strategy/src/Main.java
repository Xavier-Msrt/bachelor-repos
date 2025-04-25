public class Main {
    public static void main(String[] args) {

        DiscountStrategy discountEmployee = new DiscountEmployee();
        DiscountStrategy discountShopKeeper = new DiscountShopKeeper();

        // caisse 1
        CashRegister register1 = new CashRegister(discountEmployee);
        register1.use(10);


        CashRegister register2 = new CashRegister(discountShopKeeper);
        register2.use(10);

    }

}