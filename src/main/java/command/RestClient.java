package command;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import logging.Logger;
import model.Order;
import model.Restaurant;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
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

   public List<Object> getCentralArea(){
        Logger log = Logger.getInstance();
        try {
            URL url = new URL(baseURL + "centralArea");
            List<Object> centralArea = new ObjectMapper()
                    .readValue(url, new TypeReference<>() {});
            log.logAction("RestClient.getCentralArea()", LogStatus.GET_CENTRAL_AREA_SUCCESS);
            return centralArea;
        } catch (IOException e){
            System.err.println(e);
            log.logAction("RestClient.getCentralArea()", LogStatus.IOEXCEPTION);
       }
        return null;
   }

    public List<Object> getNoFlyZones(){
        Logger log = Logger.getInstance();
        try {
            URL url = new URL(baseURL + "noFlyZones");
            List<Object> noFlyZones = new ObjectMapper()
                    .readValue(url, new TypeReference<>() {});
            log.logAction("RestClient.getCentralArea()", LogStatus.GET_NO_FLY_ZONES_SUCCESS);
            return noFlyZones;
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
