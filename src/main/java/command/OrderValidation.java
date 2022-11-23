package command;

import logging.Logger;
import model.Delivery;
import model.Order;
import model.OrderOutcome;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.regex.Pattern;

public class OrderValidation {

    public static List<Delivery> process(List<Order> orders){
        return null;
    }

    private static OrderOutcome validate(Order order){
        return null;
    }

    private boolean validCardNumber(String cardNumber){
        if (cardNumber.length() < 16){
            return false;
        } else {
            return true;
        }
    }

    private boolean validExpiryDate(String expiryDate){
        Logger log = Logger.getInstance();
        try {
            LocalDate expiry = LocalDate.parse(expiryDate);
            // true if expiry date in the future, false if expiry date has passed
            return !expiry.isBefore(LocalDate.now());
        } catch (DateTimeParseException e) {
            log.logAction("OrderValidation.validExpiryDate(expiryDate)", LogStatus.VALID_EXPIRY_DATE_PARSE_EXCEPTION);
            return false;
        }
    }

    private boolean validCvv(String cvv){
        // matches if there are either 3 or 4 digits in string ONLY
        String regex = "^\\d{3,4}$";
        Pattern pattern = Pattern.compile(regex);
        // returns true if cvv is valid, false otherwise
        return pattern.matcher(cvv).find();
    }

    // TODO: implement luhn algorithm
    private boolean luhnCheck(String cardNumber){
        return false;
    }

    private enum LogStatus{
        VALID_EXPIRY_DATE_PARSE_EXCEPTION
    }

}
