package pathfinding;

import model.CompassDirection;
import model.LineSegment;
import model.Point;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LineApproximation {
    public static List<LineSegment> approximateSegment(LineSegment segment, List<LineSegment> noFlySegments){
        List<Point> moves = new ArrayList<>();
        moves.add(segment.p1());
        boolean complete = false;
        var directions = CompassDirection.values();
        Point current = segment.p1();
        while (!complete) {
            Point finalCurrent = current;
            List<Point> possibleMoves = Arrays.stream(directions)
                    .map(finalCurrent::makeMove).toList();

            var bestMove = possibleMoves.get(0);
            for (Point move : possibleMoves) {
                if (move.distanceTo(segment.p2()) < bestMove.distanceTo(segment.p2())){
                    boolean intersects = false;
                    for (var seg : noFlySegments){
                        if(VisibilityGraph.doesLineIntersect(seg, new LineSegment(moves.get(moves.size() - 1), move))){
                            System.out.println("!!! TRUE !!!");
                            intersects = true;
                        }
                    }
                    if (!intersects){
                        bestMove = move;
                    }
                }
            }
            current = bestMove;
            moves.add(bestMove);
            if(bestMove.closeTo(segment.p2())){
                complete = true;
            }
        }
        List<LineSegment> path = new ArrayList<>();
        for (int i = 0; i < moves.size()-1; i++) {
            path.add(new LineSegment(moves.get(i), moves.get(i+1)));
        }
        return path;
    }
}
