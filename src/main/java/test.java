import command.OrderValidation;
import command.RestClient;
import logging.LogItem;
import logging.Logger;
import model.Delivery;
import model.Order;
import model.Restaurant;

import java.util.List;

public class test {
    public static void main(String[] args){
        RestClient cli = new RestClient("https://ilp-rest.azurewebsites.net");
        List<Restaurant> restaurants = cli.getRestaurants();
        List<Order> orders = cli.getOrders("2023-01-10");
        List<Delivery> deliveries = OrderValidation.process(orders, restaurants);
        for (Delivery delivery : deliveries) {
            System.out.println(delivery.orderNo() + " | " + delivery.outcome() + " | " + delivery.costInPence());
        }
        Logger log = Logger.getInstance();
        for (LogItem item : log.getLog()){
            System.out.println(item.toString());
        }
    }
}
