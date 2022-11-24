package pathfinding;

import model.LineSegment;
import model.NoFlyZone;
import model.Point;
import model.Polygon;
import model.Restaurant;

import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.List;

public class VisibilityGraph {
    private List<Polygon> noFlyZones;
    private List<Point> goals;

    public VisibilityGraph(List<Polygon> noFlyZones, List<Restaurant> restaurants){
        this.noFlyZones = noFlyZones;
        goals = new ArrayList<>();
        for (Restaurant restaurant : restaurants) {
            goals.add(new Point(restaurant.name(), restaurant.lng(), restaurant.lat()));
        }
        goals.add(new Point("Appleton Tower", -3.186874, 55.944494));
    }

    public boolean doesLineIntersect(LineSegment line1, LineSegment line2){
        return Line2D.linesIntersect(line1.p1().lat(), line1.p1().lng(),
                line1.p2().lat(), line1.p2().lng(),
                line2.p1().lat(), line2.p1().lng(),
                line2.p2().lat(), line2.p2().lng());
    }


}
