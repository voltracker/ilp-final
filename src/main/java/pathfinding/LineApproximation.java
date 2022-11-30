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

            var bestMove = possibleMoves.get(0);
            for (Point move : possibleMoves) {
                if (move.distanceTo(end) < bestMove.distanceTo(end)){
                    boolean inside = false;
                    for (Polygon nfz : noFlyZones) {
                        if (inside(com.mapbox.geojson.Point.fromLngLat(move.lng(), move.lat()), nfz.getAsMapboxPolygon())){
                            inside = true;
                        }
                    }
                    if (!inside){
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
