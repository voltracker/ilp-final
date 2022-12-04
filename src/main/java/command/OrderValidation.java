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
import java.util.Optional;
import java.util.regex.Pattern;

public class OrderValidation {

    private record pickupRestaurantOutcome(OrderOutcome outcome, Optional<Restaurant> restaurant){
    }

    public static List<Delivery> process(List<Order> orders, List<Restaurant> restaurants){
        List<Delivery> deliveries = new ArrayList<>();
        for (Order order: orders) {
            var outcome = validate(order, restaurants);
            if (outcome.restaurant().isPresent()){
                deliveries.add(new Delivery(order.orderNo(), outcome.outcome(), outcome.restaurant().get(), order.orderTotal()));
            } else {
                deliveries.add(new Delivery(order.orderNo(), outcome.outcome(), null, order.orderTotal()));
            }

        }
        return deliveries;
    }

    private static pickupRestaurantOutcome validate(Order order, List<Restaurant> restaurants){
        if (!validCardNumber(order.creditCardNumber())){
            return new pickupRestaurantOutcome(OrderOutcome.InvalidCardNumber, Optional.empty());
        }
        if (!validExpiryDate(order.creditCardExpiry())){
            return new pickupRestaurantOutcome(OrderOutcome.InvalidExpiryDate, Optional.empty());
        }
        if (!validCvv(order.cvv())){
            return new pickupRestaurantOutcome(OrderOutcome.InvalidCvv, Optional.empty());
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

    private static pickupRestaurantOutcome checkPizzaOrder(Order order, List<Restaurant> restaurants){
    	if (order.orderItems().size() > 4 || order.orderItems().size() < 1){
            return new pickupRestaurantOutcome(OrderOutcome.InvalidPizzaCount, Optional.empty());
        }

        List<String> allPizzas = new ArrayList<>();
	    for (Restaurant restaurant : restaurants){
		    allPizzas.addAll(restaurant.getPizzaNames());
	    }

        if (!allPizzas.containsAll(order.orderItems())){
            return new pickupRestaurantOutcome(OrderOutcome.InvalidPizzaNotDefined, Optional.empty());
        }

        int totalOfAllPizzas = 0;
        for (String pizza : order.orderItems()) {
            totalOfAllPizzas += allPizzas.stream()
                    .filter(a -> a.equals(pizza))
                    .toList().size();
        }

        if (totalOfAllPizzas >= 2 * order.orderItems().size()) {
            return new pickupRestaurantOutcome(OrderOutcome.InvalidPizzaCombinationMultipleSuppliers, Optional.empty());
        }
        int deliveryCost = 100;
        int restaurantIndex = 0;
        boolean found = false;

        while (restaurantIndex < restaurants.size() && !found) {
            if (restaurants.get(restaurantIndex).getPizzaNames().containsAll(order.orderItems())) {
                found = true;
            } else {
                restaurantIndex++;
            }
        }
        if(found){
            for (String pizza : order.orderItems()) {
                for (MenuItem item : restaurants.get(restaurantIndex).menu()) {
                    if (item.name().equals(pizza)) {
                        deliveryCost += item.price();
                    }
                 }
            }

            if (deliveryCost != order.orderTotal()) {
              return new pickupRestaurantOutcome(OrderOutcome.InvalidTotal, Optional.empty());
            } else {
             return new pickupRestaurantOutcome(OrderOutcome.ValidButNotDelivered, Optional.of(restaurants.get(restaurantIndex)));
         }
        } else {
            return new pickupRestaurantOutcome(OrderOutcome.InvalidPizzaCombinationMultipleSuppliers, Optional.empty());
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
