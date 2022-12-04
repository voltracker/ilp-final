package model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

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

        public List<DroneMove> getPathFromAppleton() {
                return pathFromAppleton;
        }

        public List<DroneMove> getPathToAppleton() {
                return pathToAppleton;
        }

        public int getNumberOfMoves(){
                return this.pathToAppleton.size() + this.pathFromAppleton.size() + 1;
        }

        @Override
        public String toString(){
                return this.name;
        }
}

