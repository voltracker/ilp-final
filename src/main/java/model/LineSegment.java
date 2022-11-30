package model;

import java.util.ArrayList;
import java.util.List;

public record LineSegment(Point p1, Point p2) {

    public double distanceFromPoint(Point p){
        double distanceBetweenEnds = this.p1.distanceTo(this.p2());
        // x = lng, y = lat
        double numerator = ((p2.lng() - p1.lng())*(p1.lat() - p.lat())) - ((p1.lng() - p.lng()) * (p2.lat() - p1.lat()));
        return Math.abs(numerator) / distanceBetweenEnds;
    }

    public List<com.mapbox.geojson.Point> asMapBoxPoints(){
        List<com.mapbox.geojson.Point> mbPoints = new ArrayList<>();
        mbPoints.add(com.mapbox.geojson.Point.fromLngLat(this.p1.lng(), this.p1.lat()));
        mbPoints.add(com.mapbox.geojson.Point.fromLngLat(this.p2.lng(), this.p2.lat()));
        return mbPoints;
    }
}
