import command.DeliverOrders;
public class test {
    public static void main(String[] args){
        DeliverOrders main = new DeliverOrders("2023-01-01", "https://ilp-rest.azurewebsites.net/");
        main.go();
    }
}
