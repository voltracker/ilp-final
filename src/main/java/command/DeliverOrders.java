package command;

import com.google.common.graph.MutableValueGraph;
import model.*;
import pathfinding.AStar;
import pathfinding.LineApproximation;
import pathfinding.VisibilityGraph;

import java.sql.Array;
import java.util.*;
import java.util.stream.Collectors;

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
        // add all restaurants to priority queue in order of moves required descending
        PriorityQueue<Restaurant> restaurantQueue = new PriorityQueue<>(new RestaurantComparator());
        restaurantQueue.addAll(this.restaurants);
        // hashmap of restaurant name to delivery
        Map<String, List<Delivery>> deliveries = new HashMap<>();
        // add orders that are valid but not delivered to hashmap
        for (var order : orders){
            if (order.outcome() == OrderOutcome.ValidButNotDelivered) {
                deliveries.computeIfAbsent(order.pickupRestaurant().name(), k -> new ArrayList<>());
                deliveries.get(order.pickupRestaurant().name()).add(order);
            }
        }

        orders.removeAll(orders.stream().filter(o -> o.outcome() == OrderOutcome.ValidButNotDelivered).toList());

        var totalMoves = 0;
        var currentRestaurant = restaurantQueue.poll();
        while (totalMoves + currentRestaurant.getNumberOfMoves() < 2000 && !restaurantQueue.isEmpty() && !deliveries.isEmpty()){

            var currentDelivery = deliveries.get(currentRestaurant.name()).remove(0);
            if (deliveries.get(currentRestaurant.name()).isEmpty()){
                deliveries.remove(currentRestaurant.name());
            }
            totalMoves += currentRestaurant.getNumberOfMoves();
            orders.add(new Delivery(currentDelivery.orderNo(), OrderOutcome.Delivered, currentRestaurant, currentDelivery.costInPence()));
            //System.out.println(deliveries.keySet());
            //System.out.println(currentRestaurant.name());

            if (!deliveries.containsKey(currentRestaurant.name())){
                currentRestaurant = restaurantQueue.poll();
            }
        }

        for (var del : deliveries.values()){
            orders.addAll(del);
        }
        System.out.println(totalMoves);
        GeoJsonWriter.writeDeliveries(orders);
    }

    public void go(){
        this.pathFind();
        this.deliver();
    }
}
