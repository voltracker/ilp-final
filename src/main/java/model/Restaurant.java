package model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * class to represent a restaurant
 */
@JsonIgnoreProperties(value={"pathToAppleton", "pathFromAppleton"})
public class Restaurant {
        @JsonProperty("name")
        private String name;
        @JsonProperty("longitude")
        private double lng;
        @JsonProperty("latitude")
        private double lat;
        @JsonProperty("menu")
        private List<MenuItem> menu;

        private List<DroneMove> pathToAppleton;
        private List<DroneMove> pathFromAppleton;

        /**
         * Construct a Restaurant object
         * @param name String containing name of restaurant
         * @param lng double containing longitude of restaurant
         * @param lat double containing latitude of restaurant
         * @param menu List of MenuItems containing restaurant's menu
         */
        public Restaurant(@JsonProperty("name")
                          String name,
                          @JsonProperty("longitude")
                          double lng,
                          @JsonProperty("latitude")
                          double lat,
                          @JsonProperty("menu")
                          List<MenuItem> menu){
                this.name = name;
                this.lng = lng;
                this.lat = lat;
                this.menu = menu;
        }

        /**
         * gets the names of all the restaurant's menu items
         * @return List of Strings
         */
        public List<String> getPizzaNames(){
                List<String> pizzaNames = new ArrayList<>();
                for (MenuItem menuItem : this.menu){
                        pizzaNames.add(menuItem.name());
                }
                return pizzaNames;
        }

        public Point getPoint(){
                return new Point(this.name, this.lng, this.lat);
        }

        public double lng(){
                return lng;
        }

        public double lat(){
                return lat;
        }

        public String name(){
                return name;
        }

        public List<MenuItem> menu(){
                return menu;
        }

        public void setPathToAppleton(List<DroneMove> pathToAppleton) {
                this.pathToAppleton = pathToAppleton;
        }

        public void setPathFromAppleton(List<DroneMove> pathFromAppleton) {
                this.pathFromAppleton = pathFromAppleton;
        }

        public int getNumberOfMoves(){
                if (this.pathFromAppleton == null || this.pathToAppleton == null){
                        return -1;
                } else {
                        return this.pathToAppleton.size() + this.pathFromAppleton.size() + 2;
                }
        }

        public List<FlightPath> generateFlightPath(String orderNo, long startingTicks, Point appleton){
              List<FlightPath> flightPath = new ArrayList<>();
              long ticks;
              for (var move : this.pathFromAppleton){
                      ticks = System.nanoTime() - startingTicks;
                      var currentMove = new FlightPath(orderNo, move.from().lng(), move.from().lat(), move.angle(), move.to().lng(), move.to().lat(), ticks);
                      flightPath.add(currentMove);
              }
              ticks = System.nanoTime() - startingTicks;
              flightPath.add(new FlightPath(orderNo, this.lng, this.lat, null, this.lng, this.lat, ticks));
              for (var move : this.pathToAppleton){
                      ticks = System.nanoTime() - startingTicks;
                      var currentMove = new FlightPath(orderNo, move.from().lng(), move.from().lat(), move.angle(), move.to().lng(), move.to().lat(), ticks);
                      flightPath.add(currentMove);
              }
              ticks = System.nanoTime() - startingTicks;
              flightPath.add(new FlightPath(orderNo, appleton.lng(), appleton.lat(), null, appleton.lng(), appleton.lat(), ticks));
              return flightPath;
        }

        public List<LineSegment> getDronePath(){
                List<LineSegment> moves = new ArrayList<>();
                for (var move : this.pathFromAppleton){
                        moves.add(new LineSegment(move.from(), move.to()));
                }
                for (var move : this.pathToAppleton){
                        moves.add(new LineSegment(move.from(), move.to()));
                }
                return moves;
        }

        @Override
        public String toString(){
                return this.name;
        }
}

