package model;

public record FlightPath(
        String orderNo,
        double fromLongitude,
        double fromLatitude,
        Double angle,
        double toLongitude,
        double toLatitude,
        long ticksSinceStartOfCalculation
) {}
