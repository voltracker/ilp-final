package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Record representing a line from Point 1 to Point 2
 * @param p1 Point containing the start Point
 * @param p2 Point containing the end Point
 */
public record LineSegment(Point p1, Point p2) {

    /**
     * get the distance of a point from the line
     * @param p point to find distance to
     * @return double containing the distance
     */
    public double distanceFromPoint(Point p){
        double distanceBetweenEnds = this.p1.distanceTo(this.p2());
        // x = lng, y = lat
        double numerator = ((p2.lng() - p1.lng())*(p1.lat() - p.lat())) - ((p1.lng() - p.lng()) * (p2.lat() - p1.lat()));
        return Math.abs(numerator) / distanceBetweenEnds;
    }

    /**
     * return the corresponding LineSegment as a List of MapBox Points
     * @return List containing start and end Points as MapBox Points
     */
    public List<com.mapbox.geojson.Point> asMapBoxPoints(){
        List<com.mapbox.geojson.Point> mbPoints = new ArrayList<>();
        mbPoints.add(com.mapbox.geojson.Point.fromLngLat(this.p1.lng(), this.p1.lat()));
        mbPoints.add(com.mapbox.geojson.Point.fromLngLat(this.p2.lng(), this.p2.lat()));
        return mbPoints;
    }
}
