package command;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.LineString;
import com.mapbox.geojson.Point;
import com.mapbox.geojson.Polygon;
import model.Delivery;
import model.LineSegment;
import model.OrderOutcome;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class GeoJsonWriter {
    public static void writeVisGraph(List<model.Point> points, List<LineSegment> edges){
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
            file.write(output.toJson());
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeNoFlyZones(List<Polygon> noFlyZones){
      List<Feature> features = new ArrayList<>();
      for (var nfz : noFlyZones){
        features.add(Feature.fromGeometry(nfz));
      }
      FeatureCollection output = FeatureCollection.fromFeatures(features);
      try {
        FileWriter file = new FileWriter("noFlyZones.geojson");
        file.write(output.toJson());
        file.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    public static void writeDeliveries(List<Delivery> deliveries){
        System.out.println(deliveries.stream().filter(d -> d.outcome() == OrderOutcome.Delivered).collect(Collectors.toList()).size());
        ObjectMapper mapper = new ObjectMapper();
        try {
            mapper.writeValue(new File("deliveries.json"), deliveries);
        } catch (IOException e){
            System.err.println(e);
        }
    }
}
