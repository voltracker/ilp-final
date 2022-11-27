package model;

import java.util.ArrayList;
import java.util.List;

public record LineSegment(Point p1, Point p2) {
    public List<com.mapbox.geojson.Point> asMapBoxPoints(){
        List<com.mapbox.geojson.Point> mbPoints = new ArrayList<>();
        mbPoints.add(com.mapbox.geojson.Point.fromLngLat(this.p1.lng(), this.p1.lat()));
        mbPoints.add(com.mapbox.geojson.Point.fromLngLat(this.p2.lng(), this.p2.lat()));
        return mbPoints;
    }
}
