package command;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import logging.Logger;
import model.Order;

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
            log.logAction("RestClient.getOrders()", LogStatus.GET_ALL_ORDERS_IOEXCEPTION);
            System.err.print(e);
        }
        return null;
    }

   public List<Order> getOrders(String date){
        Logger log = Logger.getInstance();
        try {
            LocalDate date1 = LocalDate.parse(date);
            URL url = new URL(baseURL + "orders/" + date1.toString());
            List<Order> orders = new ObjectMapper()
                    .readValue(url, new TypeReference<>() {});
            if (orders.size() != 0){
                log.logAction("RestClient.getOrders(date)", LogStatus.GET_ORDERS_BY_DATE_SUCCESS);
            } else {
                log.logAction("RestClient.getOrders(date)", LogStatus.GET_ORDERS_BY_DATE_NO_ORDERS_FOR_DATE);
            }
            return orders;
        } catch (IOException e) {
            log.logAction("RestClient.getOrders(date)", LogStatus.GET_ORDERS_BY_DATE_IOEXCEPTION);
            System.err.print(e);
        } catch (DateTimeParseException e) {
            log.logAction("RestClient.getOrders(date)", LogStatus.GET_ORDERS_BY_DATE_BAD_DATE);
            System.err.print(e);
        } catch (NoClassDefFoundError e){
            log.logAction("RestClient.getOrders(date)", LogStatus.GET_ORDERS_BY_DATE_CLASS_DEF_EXCEPTION);
            System.err.print(e);
        }
        return null;
   }

   private enum LogStatus {
        GET_ALL_ORDERS_SUCCESS,
        GET_ALL_ORDERS_IOEXCEPTION,
        GET_ORDERS_BY_DATE_NO_ORDERS_FOR_DATE,
        GET_ORDERS_BY_DATE_BAD_DATE,
        GET_ORDERS_BY_DATE_IOEXCEPTION,
        GET_ORDERS_BY_DATE_SUCCESS,
        GET_ORDERS_BY_DATE_CLASS_DEF_EXCEPTION
   }
}
