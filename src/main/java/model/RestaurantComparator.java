package model;

import java.util.Comparator;

/**
 * comparator class used for creating a Priority Queue of restaurants
 */
public class RestaurantComparator implements Comparator<Restaurant> {

    /**
     * method used for comparing two Restaurants
     * @param o1 the first object to be compared.
     * @param o2 the second object to be compared.
     * @return int, 1 if o1 requires more moves than o2, -1 if o2 more than o1, 0 if they are equal
     */
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
