import command.RestClient;
import logging.LogItem;
import logging.Logger;
import model.Order;

import java.util.List;

public class test {
    public static void main(String[] args){
        RestClient cli = new RestClient("https://ilp-rest.azurewebsites.net");
        List<Order> orders = cli.getOrders("2023-01-10");
        for (Order order : orders){
            System.out.println(order.creditCardNumber());
            System.out.println(order.creditCardExpiry());
            System.out.println(order.cvv());
            System.out.println("------------------");
        }
        Logger log = Logger.getInstance();
        for (LogItem item : log.getLog()){
            System.out.println(item.toString());
        }
    }
}
