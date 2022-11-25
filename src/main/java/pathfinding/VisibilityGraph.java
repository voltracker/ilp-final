package pathfinding;

import com.google.common.graph.ImmutableValueGraph;
import com.google.common.graph.MutableValueGraph;
import com.google.common.graph.ValueGraphBuilder;
import model.LineSegment;
import model.NoFlyZone;
import model.Point;
import model.Polygon;
import model.Restaurant;

import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.List;

public class VisibilityGraph {
    private List<LineSegment> noFlySegments;
    private List<Polygon> noFlyZones;
    private List<Point> goals;
    private MutableValueGraph<Point, Double> visibilityGraph = ValueGraphBuilder.undirected().allowsSelfLoops(true).build();

    public VisibilityGraph(List<Polygon> noFlyZones, List<Restaurant> restaurants){
        this.noFlyZones = noFlyZones;
        noFlySegments = new ArrayList<>();
        for (Polygon noFlyZone : noFlyZones) {
            List<LineSegment> edges = noFlyZone.getLineSegments();
            noFlySegments.addAll(edges);
            for (Point point : noFlyZone.getPoints()){
                visibilityGraph.addNode(point);
            }
            for (LineSegment edge : edges){
                visibilityGraph.putEdgeValue(edge.p1(), edge.p2(), edge.p1().distanceTo(edge.p2()));
            }
        }
        goals = new ArrayList<>();
        for (Restaurant restaurant : restaurants) {
            Point point = new Point(restaurant.name(), restaurant.lng(), restaurant.lat());
            goals.add(point);
            visibilityGraph.addNode(point);
        }
        Point appleton = new Point("Appleton Tower", -3.186874, 55.944494);
        goals.add(appleton);
        visibilityGraph.addNode(appleton);
    }

    public boolean doesLineIntersect(LineSegment line1, LineSegment line2){
        return Line2D.linesIntersect(line1.p1().lat(), line1.p1().lng(),
                line1.p2().lat(), line1.p2().lng(),
                line2.p1().lat(), line2.p1().lng(),
                line2.p2().lat(), line2.p2().lng());
    }

    public void addEdgesForPoint(Point p){
        for(Point node : visibilityGraph.nodes()){
            if (node.name().equals(p.name())){
                break;
            }
            LineSegment line = new LineSegment(p, node);
            boolean intersection = false;
            int currentNoFlySegment = 0;
            while (!intersection && currentNoFlySegment < noFlySegments.size()){
                if (doesLineIntersect(line, noFlySegments.get(currentNoFlySegment))){
                    intersection = true;
                } else {
                    currentNoFlySegment++;
                }
            }
            if (!intersection){
                visibilityGraph.putEdgeValue(p, node, p.distanceTo(node));
            }
        }
    }

    public void buildGraph(){
        for (Point p : visibilityGraph.nodes()){
            addEdgesForPoint(p);
        }
    }

    public MutableValueGraph getGraph(){
        return visibilityGraph;
    }

}
