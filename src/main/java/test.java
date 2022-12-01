import com.google.common.graph.EndpointPair;
import com.google.common.graph.ImmutableValueGraph;
import com.google.common.graph.MutableValueGraph;
import command.GeoJsonWriter;
import command.OrderValidation;
import command.RestClient;
import logging.LogItem;
import logging.Logger;
import model.*;
import pathfinding.AStar;
import pathfinding.VisibilityGraph;

import java.util.ArrayList;
import java.util.List;

import static pathfinding.LineApproximation.approximatePath;

public class test {
    public static void main(String[] args){
        RestClient cli = new RestClient("https://ilp-rest.azurewebsites.net");
        List<Polygon> nfz = cli.getNoFlyZones();
        List<Restaurant> restaurants = cli.getRestaurants();
        List<MenuItem> menu = new ArrayList<>();
        menu.add(new MenuItem("swag", 2000));
        restaurants.add(new Restaurant("Matthew's Pizzeria", -3.187461675406197, 55.945243692575374, menu));
        List<Order> orders = cli.getOrders("2023-01-04");
        List<Delivery> deliveries = OrderValidation.process(orders, restaurants);
        VisibilityGraph graph = new VisibilityGraph(nfz, restaurants);
        graph.buildGraph();
        MutableValueGraph<Point, Double> outGraph = graph.getGraph();
        var points = outGraph.nodes().stream().toList();
        var edges = outGraph.edges();
        var outputEdges = new ArrayList<LineSegment>();
        for(EndpointPair edge : edges){
            outputEdges.add(new LineSegment((Point) edge.nodeU(), (Point) edge.nodeV()));
        }
        List<LineSegment> endPath = new ArrayList<>();
        Point appleton = new Point("Appleton Tower", -3.186874, 55.944494);
        var goals = graph.getGoals();
        List<List<LineSegment>> lines = new ArrayList<>();
        for(var goal : goals){
            lines.add(AStar.AStar(goal, appleton, outGraph));
            lines.add(AStar.AStar(appleton, goal, outGraph));
        }

        for(var line : lines){
            endPath.addAll(approximatePath(line, nfz));
        }

        endPath.addAll(graph.getNoFlySegments());
        GeoJsonWriter.writeVisGraph(points, endPath);
    }
}
