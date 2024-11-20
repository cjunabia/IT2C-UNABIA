package unabia1;

public class Order {
    private String coffeeType;
    private String size;
    private String addOns;
    private double totalPrice;

    public Order(String coffeeType, String size, String addOns, double totalPrice) {
        this.coffeeType = coffeeType;
        this.size = size;
        this.addOns = addOns;
        this.totalPrice = totalPrice;
    }

    public String getCoffeeType() {
        return coffeeType;
    }

    public String getSize() {
        return size;
    }

    public String getAddOns() {
        return addOns;
    }

    public double getTotalPrice() {
        return totalPrice;
    }
}
