package command;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.LineString;
import com.mapbox.geojson.Point;
import com.mapbox.geojson.Polygon;
import logging.Logger;
import model.Delivery;
import model.FlightPath;
import model.LineSegment;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Class providing methods to write data structures to the corresponding json or geojson files
 */
public class JsonWriter {

    /**
     * Method that writes the generated visibility graph to a geojson file
     * @param points list of Points, which are the nodes of the visgraph
     * @param edges list of LineSegments, which are the edges of the visgraph
     */
    public static void writeVisGraph(List<model.Point> points, List<LineSegment> edges){
        Logger logger = Logger.getInstance();

        // list of MapBox features, used for easy serialisation to geojson
        List<Feature> features = new ArrayList<>();

        // add all visgraph nodes to list of MapBox features
        for (model.Point point : points) {
            features.add(Feature.fromGeometry(Point.fromLngLat(point.lng(), point.lat())));
        }

        // add all visgraph edges to list of MapBox features
        for (LineSegment edge : edges){
            features.add(Feature.fromGeometry(LineString.fromLngLats(edge.asMapBoxPoints())));
        }

        // form a MapBox FeatureCollection from list of Features
        FeatureCollection output = FeatureCollection.fromFeatures(features);

        // write to file
        try {
            // create directory code from stackoverflow: https://stackoverflow.com/questions/3634853/how-to-create-a-directory-in-java
            Path path = Paths.get("resultsfiles/");
            Files.createDirectories(path);
            FileWriter file = new FileWriter("resultsfiles/visGraph.geojson");
            logger.logAction("JsonWriter.writeVisGraph(points, edges)", LogStatus.WRITE_VISGRAPH_SUCCESS);
            file.write(output.toJson());
            file.close();
        } catch (IOException e) {
            logger.logAction("JsonWriter.writeVisGraph(points, edges)", LogStatus.JSON_WRITER_IOEXCEPTION);
            e.printStackTrace();
        }
    }

    /**
     * method used to write the no-fly zones to a geojson file
     * @param noFlyZones List of Polygons containing no-fly zones
     */
    public static void writeNoFlyZones(List<Polygon> noFlyZones){
        Logger logger = Logger.getInstance();

        // add all no-fly zones to a list of MapBox Features
        List<Feature> features = new ArrayList<>();
        for (var nfz : noFlyZones){
            features.add(Feature.fromGeometry(nfz));
        }

        // create a FeatureCollection using list of Features
        FeatureCollection output = FeatureCollection.fromFeatures(features);

        // write to file
        try {
            // create directory code from stackoverflow: https://stackoverflow.com/questions/3634853/how-to-create-a-directory-in-java
            Path path = Paths.get("resultsfiles/");
            Files.createDirectories(path);
            FileWriter file = new FileWriter("resultsfiles/noFlyZones.geojson");
            file.write(output.toJson());
            file.close();
            logger.logAction("JsonWriter.writeNoFlyZones(points, edges)", LogStatus.WRITE_NOFLYZONES_SUCCESS);
        } catch (IOException e) {
            logger.logAction("JsonWriter.writeVisGraph(points, edges)", LogStatus.JSON_WRITER_IOEXCEPTION);
            e.printStackTrace();
        }
    }

    /**
     * method for writing the final state of the deliveries to a json file with the corresponding date
     * @param deliveries List of Deliveries
     * @param date String containing the date on which the deliveries are going to take place
     */
    public static void writeDeliveries(List<Delivery> deliveries, String date){
        Logger logger = Logger.getInstance();

        // create new Jackson ObjectMapper
        ObjectMapper mapper = new ObjectMapper();

        // write to file
        try {
            // create directory code from stackoverflow: https://stackoverflow.com/questions/3634853/how-to-create-a-directory-in-java
            Path path = Paths.get("resultsfiles/");
            Files.createDirectories(path);
            // serialise using Jackson ObjectMapper
            mapper.writeValue(new File("resultsfiles/deliveries-" + date + ".json"), deliveries);
            logger.logAction("JsonWriter.writeDeliveries(points, edges)", LogStatus.WRITE_DELIVERIES_SUCCESS);
        } catch (IOException e){
            logger.logAction("JsonWriter.writeDeliveries(points, edges)", LogStatus.JSON_WRITER_IOEXCEPTION);
            System.err.println(e);
        }
    }

    /**
     * Method for writing the flight path to a json file
     * @param flightPath List of FlightPath objects, which are used to represent a drone move
     * @param date String containing the date on which the given deliveries are going to take place
     */
    public static void writeFlightPathJSON(List<FlightPath> flightPath, String date){
        Logger logger = Logger.getInstance();
        ObjectMapper mapper = new ObjectMapper();
        try {
            // create directory code from stackoverflow: https://stackoverflow.com/questions/3634853/how-to-create-a-directory-in-java
            Path path = Paths.get("resultsfiles/");
            Files.createDirectories(path);
            mapper.writeValue(new File("resultsfiles/flightpath-" + date + ".json"), flightPath);
            logger.logAction("JsonWriter.writeFlightPathJSON(flightPath, date)", LogStatus.WRITE_FLIGHT_PATH_SUCCESS);
        } catch (IOException e) {
            logger.logAction("JsonWriter.writeFlightPathJSON(flightPath, date)", LogStatus.JSON_WRITER_IOEXCEPTION);
            System.err.println(e);
        }
    }

    /**
     * Method for writing the FlightPath to a geojson file
     * @param flightPath List of LineSegments representing each move the drone completes
     * @param date String containing the date on which the given deliveries are going to take place
     */
    public static void writeFlightPathGJSON(List<LineSegment> flightPath, String date){
        Logger logger = Logger.getInstance();
        List<Feature> features = new ArrayList<>();
        for (LineSegment edge : flightPath){
            features.add(Feature.fromGeometry(LineString.fromLngLats(edge.asMapBoxPoints())));
        }
        FeatureCollection output = FeatureCollection.fromFeatures(features);
        try {
            // create directory code from stackoverflow: https://stackoverflow.com/questions/3634853/how-to-create-a-directory-in-java
            Path path = Paths.get("resultsfiles/");
            Files.createDirectories(path);
            FileWriter file = new FileWriter("resultsfiles/drone-" + date + ".geojson");
            file.write(output.toJson());
            file.close();
            logger.logAction("JsonWriter.writeFlightPathGJSON(flightPath)", LogStatus.WRITE_GJ_FLIGHT_PATH_SUCCESS);
        } catch (IOException e) {
            logger.logAction("JsonWriter.writeFlightPathGJSON(flightPath)", LogStatus.JSON_WRITER_IOEXCEPTION);
            System.err.println(e);
        }

    }

    // enum used for representing log statuses
    private enum LogStatus{
        JSON_WRITER_IOEXCEPTION,
        WRITE_VISGRAPH_SUCCESS,
        WRITE_NOFLYZONES_SUCCESS,
        WRITE_DELIVERIES_SUCCESS,
        WRITE_FLIGHT_PATH_SUCCESS,
        WRITE_GJ_FLIGHT_PATH_SUCCESS
    }
}
