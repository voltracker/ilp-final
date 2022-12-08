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

        /**
         * Get the given Restaurants location as a Point
         * @return Point
         */
        public Point getPoint(){
                return new Point(this.name, this.lng, this.lat);
        }

        /**
         * get longitude of Restaurant
         * @return double
         */
        public double lng(){
                return lng;
        }

        /**
         * get latitude of Restaurant
         * @return double
         */
        public double lat(){
                return lat;
        }

        /**
         * get name of Restaurant
         * @return String
         */
        public String name(){
                return name;
        }

        /**
         * get the Restaurant's menu as a list of MenuItems
         * @return List of MenuItem
         */
        public List<MenuItem> menu(){
                return menu;
        }

        /**
         * set the approximated path for the drone when traveling to Appleton tower from the Restaurant
         * @param pathToAppleton List of DroneMove
         */
        public void setPathToAppleton(List<DroneMove> pathToAppleton) {
                this.pathToAppleton = pathToAppleton;
        }

        /**
         * set the approximated path for the drone when traveling from Appleton tower to the Restaurant
         * @param pathFromAppleton List of DroneMove
         */
        public void setPathFromAppleton(List<DroneMove> pathFromAppleton) {
                this.pathFromAppleton = pathFromAppleton;
        }

        /**
         * get the total number of moves required to complete a journey between the restaurant and appleton tower
         * @return int
         */
        public int getNumberOfMoves(){
                // return -1 if the paths haven't been set
                if (this.pathFromAppleton == null || this.pathToAppleton == null){
                        return -1;
                } else {
                        // add 2 for hover moves
                        return this.pathToAppleton.size() + this.pathFromAppleton.size() + 2;
                }
        }

        /**
         * generate a List of FlightPath, used for serializing the generated paths in the required format
         * @param orderNo String containing order that is being delivered
         * @param startingTicks long containing a timestamp in nanoseconds when execution started
         * @param appleton Point containing the location of Appleton tower
         * @return List of FlightPath
         */
        public List<FlightPath> generateFlightPath(String orderNo, long startingTicks, Point appleton){
              List<FlightPath> flightPath = new ArrayList<>();
              long ticks;
              // add all moves from Appleton tower to the Restaurant
              for (var move : this.pathFromAppleton){
                      ticks = System.nanoTime() - startingTicks;
                      var currentMove = new FlightPath(orderNo, move.from().lng(), move.from().lat(), move.angle(), move.to().lng(), move.to().lat(), ticks);
                      flightPath.add(currentMove);
              }
              // add hover move to pick up order
              ticks = System.nanoTime() - startingTicks;
              flightPath.add(new FlightPath(orderNo, this.lng, this.lat, null, this.lng, this.lat, ticks));
              // add all moves from the Restaurant to Appleton tower
              for (var move : this.pathToAppleton){
                      ticks = System.nanoTime() - startingTicks;
                      var currentMove = new FlightPath(orderNo, move.from().lng(), move.from().lat(), move.angle(), move.to().lng(), move.to().lat(), ticks);
                      flightPath.add(currentMove);
              }
              // add hover move to drop off order at appleton tower
              ticks = System.nanoTime() - startingTicks;
              flightPath.add(new FlightPath(orderNo, appleton.lng(), appleton.lat(), null, appleton.lng(), appleton.lat(), ticks));
              return flightPath;
        }

        /**
         * Get the total List of LineSegments containing the path to and from appleton tower (used for visualising paths)
         * @return List of LineSegment
         */
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

