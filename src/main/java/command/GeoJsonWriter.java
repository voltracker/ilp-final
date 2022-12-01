package command;

import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.LineString;
import com.mapbox.geojson.Point;
import com.mapbox.geojson.Polygon;
import model.LineSegment;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
}
