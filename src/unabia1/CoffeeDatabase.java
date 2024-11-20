package unabia1;

import java.util.ArrayList;
import java.util.List;

public class CoffeeDatabase {
    private List<Coffee> coffeeMenu = new ArrayList<>();
    private List<Order> purchaseHistory = new ArrayList<>();

    public void addCoffee(String name, double price) {
        coffeeMenu.add(new Coffee(name, price));
    }

    public void addOrder(Order order) {
        purchaseHistory.add(order);
    }

    public List<Coffee> getCoffeeMenu() {
        return coffeeMenu; // Ensures no duplicate definitions
    }

    public List<Order> getPurchaseHistory() {
        return purchaseHistory;
    }

    void addOrder(Unabia1.Order newOrder) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}

