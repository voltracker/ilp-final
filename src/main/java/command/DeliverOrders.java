package command;

import com.google.common.graph.MutableValueGraph;
import model.*;
import pathfinding.AStar;
import pathfinding.LineApproximation;
import pathfinding.VisibilityGraph;

import java.util.*;

public class DeliverOrders {

    private final List<Restaurant> restaurants;
    private final List<Delivery> orders;
    private final List<Polygon> noFlyZones;
    private final String date;

    private final long startingTick;
    private final Point appleton = new Point("Appleton Tower", -3.186874, 55.944494);

    public DeliverOrders(String date, String baseURL){
        this.startingTick = System.nanoTime();
        RestClient cli = new RestClient(baseURL);
        this.date = date;
        this.restaurants = cli.getRestaurants();
        this.orders = OrderValidation.process(cli.getOrders(date), restaurants);
        this.noFlyZones = cli.getNoFlyZones();
    }

    public void pathFind(){
        VisibilityGraph visGraph = new VisibilityGraph(this.noFlyZones, this.restaurants);
        MutableValueGraph outGraph = visGraph.getGraph();
        for (var restaurant : restaurants){
            List<LineSegment> from = AStar.AStar(appleton, restaurant.getPoint(), outGraph);
            List<LineSegment> to = AStar.AStar(restaurant.getPoint(), appleton, outGraph);
            assert from != null;
            restaurant.setPathFromAppleton(LineApproximation.approximatePath(from, this.noFlyZones));
            assert to != null;
            restaurant.setPathToAppleton(LineApproximation.approximatePath(to, this.noFlyZones));
        }
    }

    public void deliver(){
        // initialise list of FlightPath moves
        List<FlightPath> flightPath = new ArrayList<>();
        List<LineSegment> droneMoves = new ArrayList<>();
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
            flightPath.addAll(currentRestaurant.generateFlightPath(currentDelivery.orderNo(), this.startingTick, this.appleton));
            droneMoves.addAll(currentRestaurant.getDronePath());
            if (!deliveries.containsKey(currentRestaurant.name())){
                currentRestaurant = restaurantQueue.poll();
            }
        }

        for (var del : deliveries.values()){
            orders.addAll(del);
        }
        System.out.println(totalMoves);
        JsonWriter.writeDeliveries(orders, date);
        JsonWriter.writeFlightPathJSON(flightPath, date);
        JsonWriter.writeFlightPathGJSON(droneMoves, date);
    }

    public void go(){
        this.pathFind();
        this.deliver();
    }
}
