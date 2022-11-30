package model;

public enum CompassDirection {
    /**
     * North
     */
    N(0.0),
    /**
     * North-North-East
     */
    NNE(22.5),
    /**
     * North-East
     */
    NE(45.0),
    /**
     * East-North-East
     */
    ENE(67.5),
    /**
     * East
     */
    E(90.0),
    /**
     * East-South-East
     */
    ESE(112.5),
    /**
     * South-East
     */
    SE(135.0),
    /**
     * South-South-East
     */
    SSE(157.5),
    /**
     * South
     */
    S(180.0),
    /**
     * South-South-West
     */
    SSW(202.5),
    /**
     * South-West
     */
    SW(225.0),
    /**
     * West-South-WEst
     */
    WSW(247.5),
    /**
     * West
     */
    W(270.0),
    /**
     * West-North-West
     */
    WNW(292.5),
    /**
     * North-West
     */
    NW(315.0),
    /**
     * North-North-West
     */
    NNW(337.5);

    /**
     * Double, compass direction represented as the number of degrees from North
     */
    public final double bearing;

    private CompassDirection(double bearing) {
        this.bearing = bearing;
    }
}
