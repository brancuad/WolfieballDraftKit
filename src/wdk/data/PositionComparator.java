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
public class PositionComparator implements Comparator<Player> {
    
    @Override
    public int compare(Player p0, Player p1) {
        List order = Arrays.asList("C", "1B", "CI", "3B", "2B", "MI", "SS", "OF", "U", "P");
        Integer a = order.indexOf(p0.getCurrentPosition());
        Integer b = order.indexOf(p1.getCurrentPosition());
        return a.compareTo(b);
    }
}