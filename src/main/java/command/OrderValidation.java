package command;

import logging.Logger;
import model.*;
import model.MenuItem;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

public class OrderValidation {

    public static List<Delivery> process(List<Order> orders, List<Restaurant> restaurants){
        List<Delivery> deliveries = new ArrayList<>();
        for (Order order: orders) {
            deliveries.add(new Delivery(order.orderNo(), validate(order, restaurants), order.orderTotal()));
        }
        return deliveries;
    }

    private static OrderOutcome validate(Order order, List<Restaurant> restaurants){
        if (!validCardNumber(order.creditCardNumber())){
            return OrderOutcome.InvalidCardNumber;
        }
        if (!validExpiryDate(order.creditCardExpiry())){
            return OrderOutcome.InvalidExpiryDate;
        }
        if (!validCvv(order.cvv())){
            return OrderOutcome.InvalidCvv;
        }
        return checkPizzaOrder(order, restaurants);
    }

    private static boolean validCardNumber(String cardNumber){
        if (cardNumber.length() < 16){
            return false;
        } else {
            return true;
        }
    }

    private static boolean validExpiryDate(String expiryDate){
        Logger log = Logger.getInstance();
        System.out.println(expiryDate);
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/yy");
            YearMonth expiry = YearMonth.parse(expiryDate, formatter);
            // true if expiry date in the future, false if expiry date has passed
            return expiry.isAfter(YearMonth.now());
        } catch (DateTimeParseException e) {
            log.logAction("OrderValidation.validExpiryDate(expiryDate)", LogStatus.VALID_EXPIRY_DATE_PARSE_EXCEPTION);
            return false;
        }
    }

    private static boolean validCvv(String cvv){
        // matches if there are either 3 or 4 digits in string ONLY
        String regex = "^\\d{3,4}$";
        Pattern pattern = Pattern.compile(regex);
        // returns true if cvv is valid, false otherwise
        return pattern.matcher(cvv).find();
    }

    private static OrderOutcome checkPizzaOrder(Order order, List<Restaurant> restaurants){
    	if (order.orderItems().size() > 4 || order.orderItems().size() < 1){
            return OrderOutcome.InvalidPizzaCount;
        }

        List<String> allPizzas = new ArrayList<>();
	    for (Restaurant restaurant : restaurants){
		    allPizzas.addAll(restaurant.getPizzaNames());
	    }

        if (!allPizzas.containsAll(order.orderItems())){
            return OrderOutcome.InvalidPizzaNotDefined;
        }

        int validRestaurants = 0;
        int restaurantIndex = 0;
        int deliveryCost = 100;
        while (restaurantIndex < restaurants.size() && validRestaurants <= 1){
            if (restaurants.get(restaurantIndex).getPizzaNames().containsAll(order.orderItems())){
                validRestaurants += 1;
            }
            restaurantIndex++;
        }
        if (validRestaurants > 1){
            return OrderOutcome.InvalidPizzaCombinationMultipleSuppliers;
        } else {
            restaurantIndex--;
            for (String pizza : order.orderItems()){
                for(MenuItem item : restaurants.get(restaurantIndex).menu()){
                    if (Objects.equals(pizza, item.name())){
                        deliveryCost += item.price();
                    }
                }
            }
            if (deliveryCost != order.orderTotal()){
                return OrderOutcome.InvalidTotal;
            } else {
                return OrderOutcome.ValidButNotDelivered;
            }
        }
    }

    // TODO: implement luhn algorithm
    private boolean luhnCheck(String cardNumber){
        return false;
    }

    private enum LogStatus{
        VALID_EXPIRY_DATE_PARSE_EXCEPTION
    }

}
