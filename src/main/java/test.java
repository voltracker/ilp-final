import com.google.common.graph.EndpointPair;
import com.google.common.graph.ImmutableValueGraph;
import com.google.common.graph.MutableValueGraph;
import command.GeoJsonWriter;
import command.OrderValidation;
import command.RestClient;
import logging.LogItem;
import logging.Logger;
import model.*;
import pathfinding.VisibilityGraph;

import java.util.ArrayList;
import java.util.List;

public class test {
    public static void main(String[] args){
        RestClient cli = new RestClient("https://ilp-rest.azurewebsites.net");
        List<Polygon> nfz = cli.getNoFlyZones();
        List<Restaurant> restaurants = cli.getRestaurants();
        List<Order> orders = cli.getOrders("2023-01-02");
       // List<Delivery> deliveries = OrderValidation.process(orders, restaurants);
        //for (Delivery delivery : deliveries) {
       //     System.out.println(delivery.outcome());
       // }
        VisibilityGraph graph = new VisibilityGraph(nfz, restaurants);
        graph.buildGraph();
        MutableValueGraph<Point, Double> outGraph = graph.getGraph();
        var points = outGraph.nodes().stream().toList();
        for (Point point : points) {
            System.out.println(point.name());
        }
        var edges = outGraph.edges();
        var outputEdges = new ArrayList<LineSegment>();
        for(EndpointPair edge : edges){
            outputEdges.add(new LineSegment((Point) edge.nodeU(), (Point) edge.nodeV()));
        }
        GeoJsonWriter.writeVisGraph(points, outputEdges);
    }
}
