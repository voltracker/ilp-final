package command;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.LineString;
import com.mapbox.geojson.Point;
import com.mapbox.geojson.Polygon;
import logging.Logger;
import model.Delivery;
import model.LineSegment;
import model.OrderOutcome;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GeoJsonWriter {
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
            logger.logAction("GeoJsonWriter.writeVisGraph(points, edges)", LogStatus.WRITE_VISGRAPH_SUCCESS);
            file.write(output.toJson());
            file.close();
        } catch (IOException e) {
            logger.logAction("GeoJsonWriter.writeVisGraph(points, edges)", LogStatus.GJSON_WRITER_IOEXCEPTION);
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
          logger.logAction("GeoJsonWriter.writeNoFlyZones(points, edges)", LogStatus.WRITE_NOFLYZONES_SUCCESS);
      } catch (IOException e) {
          logger.logAction("GeoJsonWriter.writeVisGraph(points, edges)", LogStatus.GJSON_WRITER_IOEXCEPTION);
          e.printStackTrace();
      }
    }

    public static void writeDeliveries(List<Delivery> deliveries){
        Logger logger = Logger.getInstance();
        System.out.println(deliveries.stream().filter(d -> d.outcome() == OrderOutcome.Delivered).toList().size());
        ObjectMapper mapper = new ObjectMapper();
        try {
            mapper.writeValue(new File("deliveries.json"), deliveries);
            logger.logAction("GeoJsonWriter.writeDeliveries(points, edges)", LogStatus.WRITE_DELIVERIES_SUCCESS);
        } catch (IOException e){
            logger.logAction("GeoJsonWriter.writeDeliveries(points, edges)", LogStatus.GJSON_WRITER_IOEXCEPTION);
            System.err.println(e);
        }
    }

    private enum LogStatus{
        GJSON_WRITER_IOEXCEPTION,
        WRITE_VISGRAPH_SUCCESS,
        WRITE_NOFLYZONES_SUCCESS,
        WRITE_DELIVERIES_SUCCESS
    }

}
