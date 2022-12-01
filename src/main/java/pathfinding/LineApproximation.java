package pathfinding;

import model.CompassDirection;
import model.LineSegment;
import model.Point;
import model.Polygon;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.mapbox.turf.TurfJoins.inside;

public class LineApproximation {
    private static List<LineSegment> approximateSegment(Point start, Point end, List<Polygon> noFlyZones){
        List<Point> moves = new ArrayList<>();
        moves.add(start);
        boolean complete = false;
        var directions = CompassDirection.values();
        Point current = start;
        while (!complete) {
            Point finalCurrent = current;
            List<Point> possibleMoves = Arrays.stream(directions)
                    .map(finalCurrent::makeMove).toList();

            //TODO: make sure that there is a "betterMove or a valid Move"
            //TODO: if there isn't, go back and find the next best move
            //boolean bestMoveFound
            var bestMove = possibleMoves.get(0);
            for (Point move : possibleMoves) {
                if (move.distanceTo(end) < bestMove.distanceTo(end)){
                    boolean valid = true;
                    for (Polygon nfz : noFlyZones) {
                        var segment = new LineSegment(move, finalCurrent);
                        var doesIntersect = intersectsNoFlyZone(noFlyZones, segment);
                        var isInside = inside(
                                com.mapbox.geojson.Point.fromLngLat(move.lng(), move.lat()),
                                nfz.getAsMapboxPolygon());
                        if (doesIntersect || isInside || moreThanOneOccurrence(moves, move)){
                            valid = false;
                        }
                    }
                    if (valid){
                        bestMove = move;
                    }
                }
            }
            current = bestMove;
            moves.add(bestMove);
            if(bestMove.closeTo(end)){
                complete = true;
            }
        }
        List<LineSegment> path = new ArrayList<>();
        for (int i = 0; i < moves.size()-1; i++) {
            path.add(new LineSegment(moves.get(i), moves.get(i+1)));
        }
        return path;
    }

    private static boolean intersectsNoFlyZone(List<Polygon> noFlyZones, LineSegment segment){
        boolean intersect = false;
        for (var noflyzone : noFlyZones){
            List<LineSegment> edges = noflyzone.getLineSegments();
            for (LineSegment edge : edges){
                if (VisibilityGraph.doesLineIntersect(edge, segment)){
                    intersect = true;
                }
            }
        }
        return intersect;
    }

    private static boolean moreThanOneOccurrence(List<Point> moves, Point newMove){
        int occurrences = moves.stream().filter(p -> p.equals(newMove)).toList().size();
        return occurrences > 1;
    }

    public static List<LineSegment> approximatePath(List<LineSegment> exactPath, List<Polygon> noFlyZones){
        List<LineSegment> approximatePath = new ArrayList<>();

        approximatePath.addAll(approximateSegment(exactPath.get(0).p1(), exactPath.get(0).p2(), noFlyZones));
        for (int i = 1; i < exactPath.size(); i++) {
            var prevEndPoint = approximatePath.get(approximatePath.size()-1).p2();
            var segmentsToAdd = approximateSegment(prevEndPoint, exactPath.get(i).p2(), noFlyZones);
            approximatePath.addAll(segmentsToAdd);
        }

        return approximatePath;
    }
}
