package model;

/**
 * Record used to represent a geographical Point
 * @param name String containing name of Point
 * @param lng double containing longitude of Point
 * @param lat double containing latitude of Point
 */
public record Point(String name, double lng, double lat){

    /**
     * method used to get the euclidean distance to the other point
     * @param other Point to find distance to
     * @return double containing euclidean distance
     */
    public double distanceTo(Point other){
        return Math.sqrt(Math.pow(this.lng - other.lng, 2.0) + Math.pow(this.lat - other.lat, 2.0));
    }

    /**
     * method used to calculate the resulting Point after moving in direction that was passed in
     * @param direction CompassDirection from enum
     * @return resulting Point after moving in direction
     */
    public Point makeMove(CompassDirection direction){
        double radians = Math.toRadians(direction.bearing);
        double newLat = this.lat + (0.00015 * Math.sin(radians));
        double newLng = this.lng + (0.00015 * Math.cos(radians));
        return new Point(this.name, newLng, newLat);
    }

    /**
     * method that returns true if point is close to the other point, false otherwise
     * @param p Point to determine if we are close to
     * @return true if close, false otherwise
     */
    public boolean closeTo(Point p){
        return this.distanceTo(p) < 0.00015;
    }
    @Override
    public String toString(){
        return "(" + this.lng + "," + this.lat + ")";
    }
    public boolean equals(Point p){
        return (this.lng() == p.lng() && this.lat() == p.lat());
    }
}
