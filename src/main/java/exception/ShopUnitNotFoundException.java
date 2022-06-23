package exception;

public class ShopUnitNotFoundException extends RuntimeException {

    public ShopUnitNotFoundException() {
        super("Item not found");
    }
}
