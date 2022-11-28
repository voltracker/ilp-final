package pathfinding;

import com.google.common.graph.MutableValueGraph;
import model.LineSegment;
import model.Point;

import java.util.*;

public class AStar {

    public static List<LineSegment> AStar(Point goal, Point startingPoint, MutableValueGraph<Point, Double> graph){
        List<Point> openList = new ArrayList<>();
        openList.add(startingPoint);
        Map<Point, Point> cameFrom = new HashMap<>();
        Map<Point, Double> gscore = new HashMap<>();
        gscore.put(startingPoint, 0.0);
        Map<Double, Point> fscore = new HashMap<>();
        fscore.put(startingPoint.distanceTo(goal), startingPoint);

        while(!openList.isEmpty()){
            double minF = Double.POSITIVE_INFINITY;
            for (double value : fscore.keySet()){
                if(value < minF && openList.contains(fscore.get(value))){
                    minF = value;
                }
            }
            Point n = fscore.get(minF);
            if (n.equals(goal)){
                return getPath(cameFrom, n);
            }
            openList.remove(n);
            var neighbours = graph.adjacentNodes(n);
            for (var neighbour : neighbours){
                double tentative_gScore = gscore.get(n) + graph.edgeValue(n, neighbour).get();
                if (tentative_gScore < gscore.getOrDefault(neighbour, Double.POSITIVE_INFINITY)){
                    cameFrom.put(neighbour, n);
                    gscore.put(neighbour, tentative_gScore);
                    fscore.put(tentative_gScore + neighbour.distanceTo(goal), neighbour);
                    if (!openList.contains(neighbour)){
                        openList.add(neighbour);
                    }
                }
            }
        }
        return null;
    }

    public static List<LineSegment> getPath(Map<Point, Point> path, Point end){
        List<Point> totalPath = new ArrayList<>();
        totalPath.add(end);
        var current = end;
        while (path.keySet().contains(current)){
            current = path.get(current);
            totalPath.add(current);
        }

        List<LineSegment> finalPath = new ArrayList<>();
        for (int i = totalPath.size()-1; i > 0; i--) {
            finalPath.add(new LineSegment(totalPath.get(i), totalPath.get(i-1)));
        }
        return finalPath;
    }

}
