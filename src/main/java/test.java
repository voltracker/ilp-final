import com.google.common.graph.EndpointPair;
import com.google.common.graph.ImmutableValueGraph;
import com.google.common.graph.MutableValueGraph;
import command.DeliverOrders;
import command.GeoJsonWriter;
import command.OrderValidation;
import command.RestClient;
import logging.LogItem;
import logging.Logger;
import model.*;
import pathfinding.AStar;
import pathfinding.VisibilityGraph;

import java.util.ArrayList;
import java.util.List;

import static pathfinding.LineApproximation.approximatePath;

public class test {
    public static void main(String[] args){
        DeliverOrders main = new DeliverOrders("2023-01-01", "https://ilp-rest.azurewebsites.net/");
        main.go();
    }
}
