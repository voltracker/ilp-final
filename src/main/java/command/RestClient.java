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

    public RestClient(String baseURL){
        if (!baseURL.endsWith("/")){
            baseURL += "/";
        }
        this.baseURL = baseURL;
    }

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

   public List<Order> getOrders(String date){
        Logger log = Logger.getInstance();
        try {
            LocalDate date1 = LocalDate.parse(date);
            URL url = new URL(baseURL + "orders/" + date1.toString());
            List<Order> orders = new ObjectMapper()
                    .readValue(url, new TypeReference<>(){});
            if (orders.size() != 0){
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

   public Polygon getCentralArea(){
        Logger log = Logger.getInstance();
        try {
            URL url = new URL(baseURL + "centralArea");
            List<CentralArea> centralArea = new ObjectMapper()
                    .readValue(url, new TypeReference<>() {});
            log.logAction("RestClient.getCentralArea()", LogStatus.GET_CENTRAL_AREA_SUCCESS);
            List<Point> points = new ArrayList<>();
            for (CentralArea area : centralArea) {
                points.add(new Point(area.lng(), area.lat()));
            }
            return new Polygon("CentralArea", points);
        } catch (IOException e){
            System.err.println(e);
            log.logAction("RestClient.getCentralArea()", LogStatus.IOEXCEPTION);
       }
        return null;
   }

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
                    vertices.add(new Point(point.get(0), point.get(1)));
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
