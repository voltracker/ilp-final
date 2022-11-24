package model;

import java.util.ArrayList;
import java.util.List;

public class Polygon {

    private String name;
    private List<Point> points;

    public Polygon(String name, List<Point> points){
        this.name = name;
        this.points = points;
    }

    public String getName(){
        return this.name;
    }

    public List<Point> getPoints() {
        return points;
    }

    public List<LineSegment> getLineSegments(){
        List<LineSegment> segments = new ArrayList<>();
        for (int i = 0; i < this.points.size()-1; i++) {
            segments.add(new LineSegment(this.points.get(i), this.points.get(i+1)));
        }
        segments.add(new LineSegment(this.points.get(this.points.size()-1), this.points.get(0)));
        return segments;
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
