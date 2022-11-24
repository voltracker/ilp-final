import command.OrderValidation;
import command.RestClient;
import logging.LogItem;
import logging.Logger;
import model.*;

import java.util.List;

public class test {
    public static void main(String[] args){
        RestClient cli = new RestClient("https://ilp-rest.azurewebsites.net");
        List<Polygon> nfz = cli.getNoFlyZones();
        for (Polygon noFlyZone : nfz){
            System.out.println(noFlyZone.getName());
            System.out.println(noFlyZone.toString());
        }
    }
}
