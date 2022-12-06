package command;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import logging.Logger;
import model.*;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * retrieves data from rest server for use in pathfinding and order verification
 */
public class RestClient {

    private final String baseURL;

    /**
     * Constructor, sets the base URL for the server
     * @param baseURL String containing the baseURL for the REST server
     */
    public RestClient(String baseURL){
        // if the base url doesn't end with a '/', add one
        if (!baseURL.endsWith("/")){
            baseURL += "/";
        }
        this.baseURL = baseURL;
    }

    /**
     * Retrieves the list of all orders from the server
     * @return List of Orders
     */
    public List<Order> getOrders(){
        Logger log = Logger.getInstance();
        try {
            URL url = new URL(baseURL + "orders");
            List<Order> orders = new ObjectMapper()
                    .readValue(url, new TypeReference<>(){});
            log.logAction("RestClient.getOrders()", LogStatus.GET_ALL_ORDERS_SUCCESS);
            return orders;
        } catch (IOException e){
            log.logAction("RestClient.getOrders()", LogStatus.IOEXCEPTION);
            System.err.println(e);
        }
        return null;
    }

    /**
     * Retrieves the list of all orders for a given date from the server
     * @param date date to retrieve orders for
     * @return List of Orders for that day
     */
   public List<Order> getOrders(String date){
        Logger log = Logger.getInstance();
        try {
            // try and parse the given date
            LocalDate date1 = LocalDate.parse(date);
            URL url = new URL(baseURL + "orders/" + date1.toString());
            List<Order> orders = new ObjectMapper()
                    .readValue(url, new TypeReference<>(){});
            if (orders.size() > 0){
                log.logAction("RestClient.getOrders(date)", LogStatus.GET_ORDERS_BY_DATE_SUCCESS);
            } else {
                log.logAction("RestClient.getOrders(date)", LogStatus.GET_ORDERS_BY_DATE_NO_ORDERS_FOR_DATE);
            }
            return orders;
        } catch (IOException e) {
            log.logAction("RestClient.getOrders(date)", LogStatus.IOEXCEPTION);
            System.err.println(e);
        } catch (DateTimeParseException e) {
            log.logAction("RestClient.getOrders(date)", LogStatus.GET_ORDERS_BY_DATE_BAD_DATE);
            System.err.println(e);
        }
        return null;
   }

    /**
     * Retrieve List of Restaurants from REST Server
     * @return List of Restaurants
     */
   public List<Restaurant> getRestaurants(){
        Logger log = Logger.getInstance();
        try {
            URL url = new URL(baseURL + "restaurants");
            List<Restaurant> restaurants = new ObjectMapper()
                    .readValue(url, new TypeReference<>(){});
            log.logAction("RestClient.getRestaurants()", LogStatus.GET_RESTAURANTS_SUCCESS);
            return restaurants;
        } catch (IOException e){
            System.err.println(e);
            log.logAction("RestClient.getRestaurants()", LogStatus.IOEXCEPTION);
        }
        return null;
   }

    /**
     * Retrieve central area Polygon from REST Server
     * @return Polygon containing central area
     */
   public Polygon getCentralArea(){
       Logger log = Logger.getInstance();
       try {
           URL url = new URL(baseURL + "centralArea");
           List<CentralArea> centralArea = new ObjectMapper()
                    .readValue(url, new TypeReference<>() {});
           log.logAction("RestClient.getCentralArea()", LogStatus.GET_CENTRAL_AREA_SUCCESS);
           // for each of the retrieved corners of the central area, add them to a Polygon
           List<Point> points = new ArrayList<>();
           for (CentralArea area : centralArea) {
                points.add(new Point(getCentralArea().getName(), area.lng(), area.lat()));
           }
           return new Polygon("CentralArea", points);
       } catch (IOException e){
           System.err.println(e);
           log.logAction("RestClient.getCentralArea()", LogStatus.IOEXCEPTION);
       }
       return null;
   }

    /**
     * Retrieve no-fly zones from REST Server
     * @return List of Polygons representing a no-fly zone
     */
    public List<Polygon> getNoFlyZones(){
        Logger log = Logger.getInstance();
        try {
            URL url = new URL(baseURL + "noFlyZones");
            List<NoFlyZone> noFlyZones = new ObjectMapper()
                    .readValue(url, new TypeReference<>() {});
            log.logAction("RestClient.getCentralArea()", LogStatus.GET_NO_FLY_ZONES_SUCCESS);
            List<Polygon> polygons = new ArrayList<>();
            for (NoFlyZone nfz : noFlyZones) {
                List<Point> vertices = new ArrayList<>();
                for (List<Double> point : nfz.coordinates()) {
                    vertices.add(new Point(nfz.name(), point.get(0), point.get(1)));
                }
                polygons.add(new Polygon(nfz.name(), vertices));
            }
            return polygons;
        } catch (IOException e){
            System.err.println(e);
            log.logAction("RestClient.getCentralArea()", LogStatus.IOEXCEPTION);
        }
        return null;
    }

   private enum LogStatus {
        GET_ALL_ORDERS_SUCCESS,
       GET_ORDERS_BY_DATE_NO_ORDERS_FOR_DATE,
       GET_ORDERS_BY_DATE_BAD_DATE,
       GET_ORDERS_BY_DATE_SUCCESS,
       GET_RESTAURANTS_SUCCESS,
       IOEXCEPTION,
       GET_CENTRAL_AREA_SUCCESS,
       GET_NO_FLY_ZONES_SUCCESS
   }
}
