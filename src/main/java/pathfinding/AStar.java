package pathfinding;

import com.google.common.graph.MutableValueGraph;
import logging.Logger;
import model.LineSegment;
import model.Point;

import java.util.*;

/**
 * Class used to path find on the visibility graph
 * Adapted from the Wikipedia article on <a href="https://en.wikipedia.org/wiki/A*_search_algorithm">A*</a>
 */
public class AStar {

    /**
     * Implementation of the A* search algorithm, using the euclidean distance to the goal as the heuristic
     * @param startingPoint Point to start from
     * @param goal Point to find a path to
     * @param graph MutableValueGraph from the Guava package
     * @return List of LineSegment, representing the path taken
     */
    public static List<LineSegment> AStar(Point startingPoint, Point goal, MutableValueGraph<Point, Double> graph){

        Logger logger = Logger.getInstance();
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
                logger.logAction("AStar.AStar(startingPoint, goal, graph", LogStatus.ASTAR_PATH_FOUND);
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
        logger.logAction("AStar.AStar(startingPoint, goal, graph", LogStatus.ASTAR_NO_PATH_TO_GOAL);
        return null;
    }

    public static List<LineSegment> getPath(Map<Point, Point> path, Point end){
        List<Point> totalPath = new ArrayList<>();
        totalPath.add(end);
        var current = end;
        while (path.containsKey(current)){
            current = path.get(current);
            totalPath.add(current);
        }

        List<LineSegment> finalPath = new ArrayList<>();
        for (int i = totalPath.size()-1; i > 0; i--) {
            finalPath.add(new LineSegment(totalPath.get(i), totalPath.get(i-1)));
        }
        return finalPath;
    }

    private enum LogStatus{
        ASTAR_PATH_FOUND,
        ASTAR_NO_PATH_TO_GOAL,
    }

}
