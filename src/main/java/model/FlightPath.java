package model;

public record FlightPath(
        String orderNo,
        double fromLongitude,
        double fromLatitude,
        double angle,
        double toLongitude,
        double toLatitude,
        int ticksSinceStartOfCalculation
) {}
