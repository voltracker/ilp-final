package command;

import command.DeliverOrders;
import logging.Logger;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class App {
    public static void main(String[] args){
        if (args.length != 3){
            System.err.println("Incorrect number of arguments, expected 3 got " + args.length);
        } else {
            var date = args[0];
            var url = args[1];

            try {
                LocalDate parsed = LocalDate.parse(date);
                if (isValidURL(url)){
                    DeliverOrders main = new DeliverOrders(date, url);
                    main.go();
                }
            } catch (DateTimeParseException e){
                System.err.println(e);
                System.err.println("Invalid date.");
            } catch (MalformedURLException | URISyntaxException e){
                System.err.println(e);
                System.err.println("Invalid url.");
            }
        }
    }

    // https://www.baeldung.com/java-validate-url
    private static boolean isValidURL(String url) throws MalformedURLException, URISyntaxException {
        try {
            new URL(url).toURI();
            return true;
        } catch (MalformedURLException e) {
            return false;
        } catch (URISyntaxException e) {
            return false;
        }
    }
}



