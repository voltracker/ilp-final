package model;

import java.util.List;

/**
 * record used to deserialize no-fly zones from REST Server
 * @param name String containing name of no-fly zone
 * @param coordinates List containing pairs of doubles which represent a list of points
 */
public record NoFlyZone(String name, List<List<Double>> coordinates) {}
