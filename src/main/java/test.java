import command.RestClient;
import logging.LogItem;
import logging.Logger;
import model.Order;

import java.util.List;

public class test {
    public static void main(String[] args){
        RestClient cli = new RestClient("https://ilp-rest.azurewebsites.net");
        List<Order> orders = cli.getOrders("2022-01-10");
        for (Order order : orders){
            System.out.println(order.orderNo());
            for (String item : order.orderItems()){
                System.out.print("--" + item + "--");
            }
            System.out.print("\n");
        }
        Logger log = Logger.getInstance();
        for (LogItem item : log.getLog()){
            System.out.println(item.toString());
        }
    }
}
