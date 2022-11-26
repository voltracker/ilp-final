import command.OrderValidation;
import command.RestClient;
import logging.LogItem;
import logging.Logger;
import model.*;
import pathfinding.VisibilityGraph;

import java.util.List;

public class test {
    public static void main(String[] args){
        RestClient cli = new RestClient("https://ilp-rest.azurewebsites.net");
        List<Polygon> nfz = cli.getNoFlyZones();
        List<Restaurant> restaurants = cli.getRestaurants();
        List<Order> orders = cli.getOrders("2023-01-02");
        List<Delivery> deliveries = OrderValidation.process(orders, restaurants);
        for (Delivery delivery : deliveries) {
            System.out.println(delivery.outcome());
        }
        VisibilityGraph graph = new VisibilityGraph(nfz, restaurants);
        graph.buildGraph();
        System.out.println(graph.getGraph());
    }
}
