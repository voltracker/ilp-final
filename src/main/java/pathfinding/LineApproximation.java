package pathfinding;

import logging.Logger;
import model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.mapbox.turf.TurfJoins.inside;

public class LineApproximation {
    private static List<DroneMove> approximateSegment(Point start, Point end, List<Polygon> noFlyZones){
        Logger logger = Logger.getInstance();

        if (start == null || end == null || noFlyZones == null){
            logger.logAction("LineApproximation.approximateSegment(start, end, noFlyZones", LogStatus.LINE_APPROXIMATION_NULL_ARGUMENT);
            return new ArrayList<>();
        }

        List<DroneMove> moves = new ArrayList<>();
        List<DroneMove> badMoves = new ArrayList<>();
        boolean complete = false;
        var directions = CompassDirection.values();
        Point current = start;
        while (!complete) {
            //TODO: make sure that there is a "betterMove or a valid Move"
            //TODO: if there isn't, go back and find the next best move
            //boolean bestMoveFound

            List<CompassDirection> validAngles = new ArrayList<>();
            for (var angle : directions) {
                Point move = current.makeMove(angle);
                var segment = new LineSegment(move, current);
                var doesIntersect = intersectsNoFlyZone(noFlyZones, segment);
                var isInside = insideNoFlyZone(noFlyZones, move);
                if (!(doesIntersect ||
                        isInside ||
                        moreThanOneOccurrence(moves, new DroneMove(current, move, angle.bearing)) ||
                        badMoves.contains(new DroneMove(current, move, angle.bearing)))
                ) {
                    validAngles.add(angle);
                }
            }

            if (validAngles.size() > 0) {
                CompassDirection bestAngle = validAngles.get(0);
                Point bestMove = current.makeMove(bestAngle);
                for (var angle : validAngles) {
                    Point move = current.makeMove(angle);
                    if (move.distanceTo(end) < bestMove.distanceTo(end)) {
                        bestMove = move;
                        bestAngle = angle;
                    }
                }
                moves.add(new DroneMove(current, bestMove, bestAngle.bearing));
                current = bestMove;
                if(bestMove.closeTo(end)){
                    complete = true;
                }
            } else {
                current = moves.get(moves.size() - 1).from();
                badMoves.add(moves.get(moves.size() - 1));
                moves.remove(moves.size() - 1);
            }

        }
        return moves;
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

    private static boolean insideNoFlyZone(List<Polygon> noFlyZones, Point move){
        var isInside = false;
        for(Polygon nfz : noFlyZones) {
            isInside = isInside || inside(
                    com.mapbox.geojson.Point.fromLngLat(move.lng(), move.lat()),
                    nfz.getAsMapboxPolygon());
        }
        return isInside;
    }

    private static boolean moreThanOneOccurrence(List<DroneMove> moves, DroneMove newMove){
        int occurrences = moves.stream().filter(p -> p.equals(newMove)).toList().size();
        return occurrences > 1;
    }

    public static List<DroneMove> approximatePath(List<LineSegment> exactPath, List<Polygon> noFlyZones){
        Logger logger = Logger.getInstance();
        if (exactPath == null || noFlyZones == null){
            logger.logAction("LineApproximation.approximatePath(exactPath, noFlyZones", LogStatus.LINE_APPROXIMATION_NULL_ARGUMENT);
            return new ArrayList<>();
        }

        List<DroneMove> approximatePath = new ArrayList<>(Objects.requireNonNull(approximateSegment(exactPath.get(0).p1(), exactPath.get(0).p2(), noFlyZones)));
        for (int i = 1; i < exactPath.size(); i++) {
            var prevEndPoint = approximatePath.get(approximatePath.size()-1).to();
            var segmentsToAdd = approximateSegment(prevEndPoint, exactPath.get(i).p2(), noFlyZones);
            approximatePath.addAll(segmentsToAdd);
        }
        logger.logAction("LineApproximation.approximatePath(exactPath, noFlyZones", LogStatus.LINE_APPROXIMATION_APPROXIMATE_PATH_SUCCESS);
        return approximatePath;
    }

    private enum LogStatus{
        LINE_APPROXIMATION_NULL_ARGUMENT,
        LINE_APPROXIMATION_APPROXIMATE_PATH_SUCCESS
    }
}
