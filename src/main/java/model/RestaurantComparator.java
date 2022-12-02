package model;

import java.util.Comparator;

public class RestaurantComparator implements Comparator<Restaurant> {

    @Override
    public int compare(Restaurant o1, Restaurant o2) {
        int size1 = o1.getNumberOfMoves();
        int size2 = o2.getNumberOfMoves();
        if (size1 > size2) {
            return 1;
        } else if (size1 < size2) {
            return -1;
        } else {
            return 0;
        }
    }
}
