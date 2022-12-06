package model;

/**
 * Record used for serializing the list of drone moves to the json file
 * @param orderNo String containing the order number
 * @param fromLongitude double containing the longitude the drone is moving from
 * @param fromLatitude double containing the latitude the drone is moving from
 * @param angle double containing the bearing on which the drone is moving
 * @param toLongitude double containing the longitude the drone is moving to
 * @param toLatitude double containing the latitude the drone is moving to
 * @param ticksSinceStartOfCalculation long containing the number of nanoseconds since computation began
 */
public record FlightPath(
        String orderNo,
        double fromLongitude,
        double fromLatitude,
        Double angle,
        double toLongitude,
        double toLatitude,
        long ticksSinceStartOfCalculation
) {}
