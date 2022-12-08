package pathfinding;

import com.google.common.graph.MutableValueGraph;
import com.google.common.graph.ValueGraphBuilder;
import logging.Logger;
import model.LineSegment;
import model.Point;
import model.Polygon;
import model.Restaurant;

import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.List;

/**
 * Class used to build a Visibility Graph of the no-fly zones and Restaurants/Appleton tower
 */
public class VisibilityGraph {
    private final List<LineSegment> noFlySegments;
    private final List<Point> goals;
    // Using the Google Guava package which contains Graph classes/utilities
    private final MutableValueGraph<Point, Double> visibilityGraph = ValueGraphBuilder.undirected().allowsSelfLoops(true).build();

    /**
     * Constructs a Visibility Graph object
     * @param noFlyZones List of Polygons containing no-fly zones
     * @param restaurants List of Restaurants
     */
    public VisibilityGraph(List<Polygon> noFlyZones, List<Restaurant> restaurants){
        Logger logger = Logger.getInstance();

        // creates list of LineSegments which represent the edges of all no-fly zones
        noFlySegments = new ArrayList<>();
        for (Polygon noFlyZone : noFlyZones) {
            List<LineSegment> edges = noFlyZone.getLineSegments();
            noFlySegments.addAll(edges);
            // add each of the vertices for the no-fly zone to the visibility graph
            for (Point point : noFlyZone.getPoints()){
                visibilityGraph.addNode(point);
            }
            // add each of the edges for the no-fly zone to the visibility graph
            for (LineSegment edge : edges){
                visibilityGraph.putEdgeValue(edge.p1(), edge.p2(), edge.p1().distanceTo(edge.p2()));
            }
        }
        // add all the restaurants to a "goals" list and the visibility graph
        goals = new ArrayList<>();
        for (Restaurant restaurant : restaurants) {
            Point point = restaurant.getPoint();
            goals.add(point);
            visibilityGraph.addNode(point);
        }
        // add appleton tower to the visibility graph
        Point appleton = new Point("Appleton Tower", -3.186874, 55.944494);
        visibilityGraph.addNode(appleton);
        // build the graph
        this.buildGraph();
        logger.logAction("VisibilityGraph.VisibilityGraph(noFlyZones, restaurants)", LogStatus.VISIBILITY_GRAPH_BUILT);
    }

    /**
     * check if two LineSegments intersect
     * @param line1 LineSegment
     * @param line2 LineSegment
     * @return boolean, true if they intersect, false otherwise
     */
    public static boolean doesLineIntersect(LineSegment line1, LineSegment line2){
        // use the Line2D class from the standard library to check if lines intersect
        boolean intersect = Line2D.linesIntersect(line1.p1().lng(), line1.p1().lat(),
                line1.p2().lng(), line1.p2().lat(),
                line2.p1().lng(), line2.p1().lat(),
                line2.p2().lng(), line2.p2().lat());

        // if the intersection is at a vertex then for our purposes we don't want to count that as an intersection
        if (line1.p1().equals(line2.p1()) || line1.p1().equals(line2.p2()) || line1.p2().equals(line2.p1()) || line1.p2().equals(line2.p2())){
            return false;
        } else {
            // otherwise just return the calculated intersect boolean
            return intersect;
        }
    }

    /**
     * for a given Point p, add an edges to each node in the visibility graph for which
     * a line can be drawn directly from p to that node without intersecting a no-fly zone
     * @param p Point
     */
    public void addEdgesForPoint(Point p){
        for (Point node : visibilityGraph.nodes()){
            // as long as the name of the Point isn't the same as the point passed in
            // this prevents inserting edges within a no-fly zone
            if (!node.name().equals(p.name())){
                // check if the lineSegment between p and the current node intersects any of the no-fly zones
                LineSegment line = new LineSegment(p, node);
                boolean intersect = false;
                for (LineSegment segment : noFlySegments){
                    if (doesLineIntersect(segment, line)){
                        intersect = true;
                        break;
                    }
                }
                // if there are no intersections, add an edge between the node and p, with the euclidean distance as the
                // weight
                if (!intersect) {
                    visibilityGraph.putEdgeValue(p, node, p.distanceTo(node));
                }
            }
        }
    }

    /**
     * add the edges for all nodes in the graph
     */
    public void buildGraph(){
        for (Point p : visibilityGraph.nodes()){
            addEdgesForPoint(p);
        }
    }

    /**
     * get the no-fly zone edges
     * @return List of LineSegment
     */
    public List<LineSegment> getNoFlySegments(){
        return noFlySegments;
    }

    /**
     * get the list of goals
     * @return List of Point
     */
    public List<Point> getGoals() {
        return goals;
    }

    /**
     * get the visibility graph as a MutableValueGraph (from Guava)
     * @return MutableValueGraph
     */
    public MutableValueGraph<Point, Double> getGraph(){
        return visibilityGraph;
    }

    private enum LogStatus {
        VISIBILITY_GRAPH_BUILT
    }

}
