package wdk.data;

import java.util.Comparator;

/**
 * This class is for taking a string, parsing a double, and comparing them
 * using the double value
 * 
 * @author Brandon
 */
public class DoubleColumnComparator implements Comparator<String>{
    
    @Override
    public int compare(String s0, String s1) {
        double ds0 = Double.parseDouble(s0);
        double ds1 = Double.parseDouble(s1);
        Double dS0 = new Double(ds0);
        Double dS1 = new Double(ds1);
        return dS0.compareTo(dS1);
    }
    
}
