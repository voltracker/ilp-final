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
import java.util.ArrayList;
import java.util.List;

public class JsonWriter {
    public static void writeVisGraph(List<model.Point> points, List<LineSegment> edges){
        Logger logger = Logger.getInstance();
        List<Feature> features = new ArrayList<>();
        for (model.Point point : points) {
            features.add(Feature.fromGeometry(Point.fromLngLat(point.lng(), point.lat())));
        }
        for (LineSegment edge : edges){
            features.add(Feature.fromGeometry(LineString.fromLngLats(edge.asMapBoxPoints())));
        }
        FeatureCollection output = FeatureCollection.fromFeatures(features);
        try {
            FileWriter file = new FileWriter("visGraph.geojson");
            logger.logAction("JsonWriter.writeVisGraph(points, edges)", LogStatus.WRITE_VISGRAPH_SUCCESS);
            file.write(output.toJson());
            file.close();
        } catch (IOException e) {
            logger.logAction("JsonWriter.writeVisGraph(points, edges)", LogStatus.JSON_WRITER_IOEXCEPTION);
            e.printStackTrace();
        }
    }

    public static void writeNoFlyZones(List<Polygon> noFlyZones){
        Logger logger = Logger.getInstance();
      List<Feature> features = new ArrayList<>();
      for (var nfz : noFlyZones){
        features.add(Feature.fromGeometry(nfz));
      }
      FeatureCollection output = FeatureCollection.fromFeatures(features);
      try {
          FileWriter file = new FileWriter("noFlyZones.geojson");
          file.write(output.toJson());
          file.close();
          logger.logAction("JsonWriter.writeNoFlyZones(points, edges)", LogStatus.WRITE_NOFLYZONES_SUCCESS);
      } catch (IOException e) {
          logger.logAction("JsonWriter.writeVisGraph(points, edges)", LogStatus.JSON_WRITER_IOEXCEPTION);
          e.printStackTrace();
      }
    }

    public static void writeDeliveries(List<Delivery> deliveries, String date){
        Logger logger = Logger.getInstance();
        //System.out.println(deliveries.stream().filter(d -> d.outcome() == OrderOutcome.Delivered).toList().size());
        ObjectMapper mapper = new ObjectMapper();
        try {
            mapper.writeValue(new File("deliveries-" + date + ".json"), deliveries);
            logger.logAction("JsonWriter.writeDeliveries(points, edges)", LogStatus.WRITE_DELIVERIES_SUCCESS);
        } catch (IOException e){
            logger.logAction("JsonWriter.writeDeliveries(points, edges)", LogStatus.JSON_WRITER_IOEXCEPTION);
            System.err.println(e);
        }
    }

    public static void writeFlightPathJSON(List<FlightPath> flightPath, String date){
        Logger logger = Logger.getInstance();
        ObjectMapper mapper = new ObjectMapper();
        try {
            mapper.writeValue(new File("flightpath-" + date + ".json"), flightPath);
            logger.logAction("JsonWriter.writeFlightPathJSON(flightPath, date)", LogStatus.WRITE_FLIGHT_PATH_SUCCESS);
        } catch (IOException e) {
            logger.logAction("JsonWriter.writeFlightPathJSON(flightPath, date)", LogStatus.JSON_WRITER_IOEXCEPTION);
            System.err.println(e);
        }
    }

    public static void writeFlightPathGJSON(List<LineSegment> path, String date){
        Logger logger = Logger.getInstance();
        List<Feature> features = new ArrayList<>();
        for (LineSegment edge : path){
            features.add(Feature.fromGeometry(LineString.fromLngLats(edge.asMapBoxPoints())));
        }
        FeatureCollection output = FeatureCollection.fromFeatures(features);
        try {
            FileWriter file = new FileWriter("drone-" + date + ".geojson");
            file.write(output.toJson());
            file.close();
            logger.logAction("JsonWriter.writeFlightPathGJSON(path)", LogStatus.WRITE_GJ_FLIGHT_PATH_SUCCESS);
        } catch (IOException e) {
            logger.logAction("JsonWriter.writeFlightPathGJSON(path)", LogStatus.JSON_WRITER_IOEXCEPTION);
            System.err.println(e);
        }

    }

    private enum LogStatus{
        JSON_WRITER_IOEXCEPTION,
        WRITE_VISGRAPH_SUCCESS,
        WRITE_NOFLYZONES_SUCCESS,
        WRITE_DELIVERIES_SUCCESS,
        WRITE_FLIGHT_PATH_SUCCESS,
        WRITE_GJ_FLIGHT_PATH_SUCCESS
    }
}
