package model;

import java.util.ArrayList;
import java.util.List;

/**
 * class used to represent a Polygon, which is merely a list of points.
 * also contains methods which may be useful when using Polygons
 */
public class Polygon {

    private String name;
    private List<Point> points;

    /**
     * Construct a Polygon object
     * @param name String containing name of Polygon
     * @param points List of Points representing a vertex of the Polygon (clockwise or counter-clockwise)
     */
    public Polygon(String name, List<Point> points){
        this.name = name;
        this.points = points;
    }

    /**
     * getter for name
     * @return String containing name
     */
    public String getName(){
        return this.name;
    }

    /**
     * getter for Points
     * @return List of Points representing a vertex of the Polygon (clockwise or counter-clockwise)
     */
    public List<Point> getPoints() {
        return points;
    }

    /**
     * get LineSegments for corresponding Polygon (these represent the edges of the Polygon)
     * @return List of LineSegments
     */
    public List<LineSegment> getLineSegments(){
        List<LineSegment> segments = new ArrayList<>();
        for (int i = 0; i < this.points.size()-1; i++) {
            segments.add(new LineSegment(this.points.get(i), this.points.get(i+1)));
        }
        segments.add(new LineSegment(this.points.get(this.points.size()-1), this.points.get(0)));
        return segments;
    }

    /**
     * get Polygon as a MapBox Polygon
     * @return MapBox Polygon
     */
    public com.mapbox.geojson.Polygon getAsMapboxPolygon(){
        var mbPoints = this.points.stream().map(p -> com.mapbox.geojson.Point.fromLngLat(p.lng(), p.lat())).toList();
        List<List<com.mapbox.geojson.Point>> mbListList = new ArrayList<>();
        mbListList.add(mbPoints);
        return com.mapbox.geojson.Polygon.fromLngLats(mbListList);
    }

    @Override
    public String toString(){
        String out = "";
        for (Point point: this.points) {
            out += point.toString();
            out += ",";
        }
        return out;
    }
}
