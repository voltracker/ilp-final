package model;

import java.util.List;

/**
 * Record to represent a restaurant
 * @param name name of restaurant
 * @param lng longitude
 * @param lat latitude
 * @param menu list of MenuItems containing pizzas
 */
public record Restaurant(String name, double lng, double lat, List<MenuItem> menu){

}
