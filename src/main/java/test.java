import command.RestClient;
import model.Order;

import java.util.List;

public class test {
    public static void main(String[] args){
        RestClient cli = new RestClient("https://ilp-rest.azurewebsites.net");
        List<Order> orders = cli.getOrders();
        for (Order order : orders){
            System.out.println(order.orderNo());
            for (String item : order.orderItems()){
                System.out.print("--" + item + "--");
            }
            System.out.print("\n");
        }
    }
}
