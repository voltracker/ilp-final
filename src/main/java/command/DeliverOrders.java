package command;

import com.google.common.graph.MutableValueGraph;
import model.*;
import pathfinding.AStar;
import pathfinding.LineApproximation;
import pathfinding.VisibilityGraph;

import java.util.*;

/**
 * class that executes each of the independent functions and creates the necessary files
 */
public class DeliverOrders {

    // restaurants, orders and no-fly zones retrieved from REST server
    private final List<Restaurant> restaurants;
    private final List<Delivery> orders;
    private final List<Polygon> noFlyZones;
    private final String date;
    // used for calculating "TicksSinceStartOfCalculation"
    private final long startingTick;
    // Point that will be used to represent the location of Appleton Tower (remains constant)
    private final Point appleton = new Point("Appleton Tower", -3.186874, 55.944494);

    /**
     * Retrieves necessary data from the REST server and processes the orders using the appropriate method from the
     * OrderValidation class.
     *
     * Both parameters are assumed to be valid and in the correct format
     * @param date to be passed in by a command line argument (in the format yyyy-MM-dd)
     * @param baseURL also to be passed in by a command line argument
     */
    public DeliverOrders(String date, String baseURL){
        // get current system time in nanoseconds
        this.startingTick = System.nanoTime();
        RestClient cli = new RestClient(baseURL);
        this.date = date;
        this.restaurants = cli.getRestaurants();
        this.orders = OrderValidation.process(cli.getOrders(date), restaurants);
        this.noFlyZones = cli.getNoFlyZones();
    }

    /**
     * Uses the appropriate classes to build a visibility graph, path find to and from appleton tower for each of the
     * restaurants using A* and then approximate each of the line segments on the graph using my line approximation
     * class
     */
    public void pathFind(){
        // build visibility graph
        VisibilityGraph visGraph = new VisibilityGraph(this.noFlyZones, this.restaurants);
        // get graph for use in A*
        MutableValueGraph outGraph = visGraph.getGraph();
        // for each restaurant find the path to and from appleton tower and set the appropriate restaurant's path to
        // that the approximated version of that path
        for (var restaurant : restaurants){
            List<LineSegment> from = AStar.AStar(appleton, restaurant.getPoint(), outGraph);
            List<LineSegment> to = AStar.AStar(restaurant.getPoint(), appleton, outGraph);
            assert from != null;
            restaurant.setPathFromAppleton(LineApproximation.approximatePath(from, this.noFlyZones));
            assert to != null;
            restaurant.setPathToAppleton(LineApproximation.approximatePath(to, this.noFlyZones));
        }
    }

    /**
     * using the generated paths and the list of orders, maximise the number of delivered orders to the number
     * of necessary drone moves.
     */
    public void deliver(){
        // initialise list of FlightPath moves
        List<FlightPath> flightPath = new ArrayList<>();
        // initialise list of LineSegments, used to represent the path the drone takes, for drone-(date).geojson
        List<LineSegment> droneMoves = new ArrayList<>();

        // add all restaurants to priority queue in order of moves required descending
        PriorityQueue<Restaurant> restaurantQueue = new PriorityQueue<>(new RestaurantComparator());
        restaurantQueue.addAll(this.restaurants);

        // hashmap of restaurant name to a list of orders that will go to that restaurant
        Map<String, List<Delivery>> deliveries = new HashMap<>();

        // add orders that are valid but not delivered to hashmap
        for (var order : orders){
            if (order.outcome() == OrderOutcome.ValidButNotDelivered) {
                deliveries.computeIfAbsent(order.pickupRestaurant().name(), k -> new ArrayList<>());
                deliveries.get(order.pickupRestaurant().name()).add(order);
            }
        }

        // remove the valid but not delivered orders from the list, as they will be re-added after we have determined
        // which of these will ultimately be delivered
        orders.removeAll(orders.stream().filter(o -> o.outcome() == OrderOutcome.ValidButNotDelivered).toList());

        // get the first restaurant from the priority queue (restaurant with fewest required moves
        var currentRestaurant = restaurantQueue.poll();
        var totalMoves = 0;

        // repeat till all possible moves have been used or there are no orders left to deliver
        while (totalMoves + currentRestaurant.getNumberOfMoves() < 2000
                && !restaurantQueue.isEmpty() && !deliveries.isEmpty()){
            // get the first occurrence of an order with the current restaurant's name and remove it from the list
            var currentDelivery = deliveries.get(currentRestaurant.name()).remove(0);

            // if there are no deliveries left in the list for that restaurant, remove it from the hashmap
            if (deliveries.get(currentRestaurant.name()).isEmpty()){
                deliveries.remove(currentRestaurant.name());
            }
            totalMoves += currentRestaurant.getNumberOfMoves();

            // re-add the corresponding order to the final order list
            orders.add(new Delivery(currentDelivery.orderNo(), OrderOutcome.Delivered,
                    currentRestaurant, currentDelivery.costInPence()));
            // record the flightPath moves and the drone moves to the corresponding lists
            flightPath.addAll(currentRestaurant.generateFlightPath(currentDelivery.orderNo(), this.startingTick, this.appleton));
            droneMoves.addAll(currentRestaurant.getDronePath());
            // if there are no more orders to the currentRestaurant, move on to the restaurant with the next fewest required
            // moves
            if (!deliveries.containsKey(currentRestaurant.name())){
                currentRestaurant = restaurantQueue.poll();
            }
        }

        // read the orders that were valid but not delivered
        for (var del : deliveries.values()){
            orders.addAll(del);
        }
        System.out.println(totalMoves);
        // write the required data to the co
        JsonWriter.writeDeliveries(orders, date);
        JsonWriter.writeFlightPathJSON(flightPath, date);
        JsonWriter.writeFlightPathGJSON(droneMoves, date);
    }

    /**
     * run the required methods to complete pathfinding and order delivery
     */
    public void go(){
        this.pathFind();
        this.deliver();
    }
}
