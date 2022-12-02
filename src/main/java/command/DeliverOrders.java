package command;

import com.google.common.graph.MutableValueGraph;
import model.*;
import pathfinding.AStar;
import pathfinding.LineApproximation;
import pathfinding.VisibilityGraph;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DeliverOrders {

    private final List<Restaurant> restaurants;
    private final List<Delivery> orders;
    private final List<Polygon> noFlyZones;

    private final Point appleton = new Point("Appleton Tower", -3.186874, 55.944494);

    public DeliverOrders(String date, String baseURL){
        RestClient cli = new RestClient(baseURL);
        this.restaurants = cli.getRestaurants();
        this.orders = OrderValidation.process(cli.getOrders(date), restaurants);
        this.noFlyZones = cli.getNoFlyZones();
    }

    public void pathFind(){
        VisibilityGraph visGraph = new VisibilityGraph(this.noFlyZones, this.restaurants);
        visGraph.buildGraph();
        MutableValueGraph<Point, Double> outGraph = visGraph.getGraph();
        for (var restaurant : restaurants){
            var from = AStar.AStar(appleton, restaurant.getPoint(), outGraph);
            var to = AStar.AStar(restaurant.getPoint(), appleton, outGraph);
            assert from != null;
            restaurant.setPathFromAppleton(LineApproximation.approximatePath(from, this.noFlyZones));
            assert to != null;
            restaurant.setPathToAppleton(LineApproximation.approximatePath(to, this.noFlyZones));
        }
    }

    public void deliver(){
        Map<Integer, Restaurant> movesPerRestaurant = new HashMap<>();
        for (var restaurant : restaurants){
            movesPerRestaurant.put(restaurant.getPathFromAppleton().size()+restaurant.getPathToAppleton().size() + 1, restaurant);
        }

        var total = 0;
        while(total < 2000){

        }
    }

    public void go(){
        this.pathFind();
        this.deliver();
    }
}
