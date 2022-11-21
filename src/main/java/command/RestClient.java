package command;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import model.Order;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * retrieves data from rest server for use in pathfinding and order verification
 */
public class RestClient {

    private String baseURL;

    public RestClient(String baseURL){
        if (!baseURL.endsWith("/")){
            baseURL += "/";
        }
        this.baseURL = baseURL;
    }

    public List<Order> getOrders(){
        try {
            URL url = new URL(baseURL + "orders");
            List<Order> orders = new ObjectMapper().readValue(url, new TypeReference<List<Order>>(){});
            return orders;
        } catch (IOException e){
            System.out.println("shanners");
        }

        return null;
    }

//    public List<Order> getOrders(String date){
//        return
//    }
}
