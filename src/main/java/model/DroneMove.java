package model;

/**
 * Record representing a move by the drone
 * @param from Point that the drone is moving from
 * @param to Point that the drone is moving to
 * @param angle double containing the bearing on which the drone is moving
 */
public record DroneMove (
        Point from, Point to, double angle
){
}
