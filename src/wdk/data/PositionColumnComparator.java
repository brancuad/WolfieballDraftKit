package wdk.data;

import java.util.Comparator;
import java.util.Arrays;
import java.util.List;

/**
 * This class is for taking a string representation of position
 * and organizing them in a 
 * specified way
 * 
 * @author Brandon
 */
public class PositionColumnComparator implements Comparator<String> {
    
    @Override
    public int compare(String s0, String s1) {
        List order = Arrays.asList("C", "1B", "CI", "3B", "2B", "MI", "SS", "OF", "U", "P");
        Integer a = order.indexOf(s0);
        Integer b = order.indexOf(s1);
        return a.compareTo(b);
    }
}
