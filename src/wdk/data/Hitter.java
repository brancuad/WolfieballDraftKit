package wdk.data;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * This class is for representing Hitters in the Draft.
 * 
 * @author Brandon
 */
public class Hitter extends Player {
    final StringProperty QP;
    final StringProperty AB;
    final StringProperty R;
    final StringProperty H;
    final StringProperty HR;
    final StringProperty RBI;
    final StringProperty SB;
    final StringProperty BA;
    final StringProperty EstimatedValue;
    public static final String DEFAULt = "";
    private int rRank;
    private int hrRank;
    private int rbiRank;
    private int sbRank;
    private int baRank;
    private int avgRank;
    
    // FOR LIMITING DECIMAL SPACES
    DecimalFormat df;
    
    public Hitter() {
        setLastName(DEFAULT);
        setFirstName(DEFAULT);
        setTeam(DEFAULT);
        QP = new SimpleStringProperty(DEFAULT);
        AB = new SimpleStringProperty(DEFAULT);
        R = new SimpleStringProperty(DEFAULT);
        H = new SimpleStringProperty(DEFAULT);
        HR = new SimpleStringProperty(DEFAULT);
        RBI = new SimpleStringProperty(DEFAULT);
        SB = new SimpleStringProperty(DEFAULT);
        BA = new SimpleStringProperty(DEFAULT);
        EstimatedValue = new SimpleStringProperty(DEFAULT);
        setContract(DEFAULT);
        setSalary(DEFAULT);
        setCurrentPosition(DEFAULT);
        df = new DecimalFormat("0.000");
    }
    
    public void reset() {
        setLastName(DEFAULT);
        setFirstName(DEFAULT);
        setTeam(DEFAULT);
        setQP(DEFAULT);
        setAB(DEFAULT);
        setR(DEFAULT);
        setH(DEFAULT);
        setHR(DEFAULT);
        setRBI(DEFAULT);
        setSB(DEFAULT);
        setBA();
        setNotes(DEFAULT);
        setYearOfBirth(DEFAULT);
        setNationOfBirth(DEFAULT);
        setEstimatedValue(DEFAULT);
    }
    
    public String getQP() {
        return QP.get();
    }
    
    public void setQPSave(String initQP) {
        QP.set(initQP);
    }
    
    public void setQP(String initQP) {
        //String[] QParray = initQP.split("_");
        
        // MAKE AN ARRAY LIST OF QP
        ArrayList<String> qpArray = new ArrayList(Arrays.asList(initQP.split("_")));
        
        // ADD CI IF 1B OR 3B
        if(qpArray.contains("1B") || qpArray.contains("3B")) {
            qpArray.add("CI");
        }
        if(qpArray.contains("SS") || qpArray.contains("2B")) {
            qpArray.add("MI");
        }
        // ADD U FOR ALL HITTERS
        qpArray.add("U");
        
        // MAKE NEW QP STRING WITH UPDATES POSITIONS
        String newQP = "";
        for(String qP : qpArray) {
            newQP += qP + "_";
        }
        newQP = newQP.substring(0, newQP.length()-1); // REMOVE ERONEOUS "_"
        QP.set(newQP);
    }
    
    public StringProperty QPProperty() {
        return QP;
    }
    
    public String getAB() {
        return AB.get();
    }
    
    public void setAB(String initAB) {
        AB.set(initAB);
    }
    
    public StringProperty ABProperty() {
        return AB;
    }
    
    public String getR() {
        return R.get();
    }
    
    public void setR(String initR) {
        R.set(initR);
    }
    
    public StringProperty RProperty() {
        return R;
    }
    
    public String getH() {
        return H.get();
    }
    
    public void setH(String initH) {
        H.set(initH);
    }
    
    public StringProperty HProperty() {
        return H;
    }
    
    public String getHR() {
        return HR.get();
    }
    
    public void setHR(String initHR) {
        HR.set(initHR);
    }
    
    public StringProperty HRProperty() {
        return HR;
    }
    
    public String getRBI() {
        return RBI.get();
    }
    
    public void setRBI(String initRBI) {
        RBI.set(initRBI);
    }
    
    public StringProperty RBIProperty() {
        return RBI;
    }
    
    public String getSB() {
        return SB.get();
    }
    
    public void setSB(String initSB) {
        SB.set(initSB);
    }
    
    public StringProperty SBProperty() {
        return SB;
    }
    
    public String getEstimatedValue() {
        return EstimatedValue.get();
    }
    
    public void setEstimatedValue(String initEV) {
        EstimatedValue.set(initEV);
    }
    
    public StringProperty EstimatedValueProperty() {
        return EstimatedValue;
    }
    
    public String getBA() {
        return BA.get();
    }
    
    public void setBA() {
        String ba = df.format(calcBA());
        BA.set(ba);
    }
    
    public StringProperty BAProperty() {
        return BA;
    }
    
    public double calcBA() {
        double h = Double.parseDouble(getH());
        double ab = Double.parseDouble(getAB());
        if (ab == 0)
            return 0;
        else {
            double ba = h/ab;
            return ba;
        }
    }

    /**
     * @return the rRank
     */
    public int getRRank() {
        return rRank;
    }

    /**
     * @param rRank the rRank to set
     */
    public void setRRank(int rRank) {
        this.rRank = rRank;
    }

    /**
     * @return the hrRank
     */
    public int getHrRank() {
        return hrRank;
    }

    /**
     * @param hrRank the hrRank to set
     */
    public void setHrRank(int hrRank) {
        this.hrRank = hrRank;
    }

    /**
     * @return the rbiRank
     */
    public int getRbiRank() {
        return rbiRank;
    }

    /**
     * @param rbiRank the rbiRank to set
     */
    public void setRbiRank(int rbiRank) {
        this.rbiRank = rbiRank;
    }

    /**
     * @return the sbRank
     */
    public int getSbRank() {
        return sbRank;
    }

    /**
     * @param sbRank the sbRank to set
     */
    public void setSbRank(int sbRank) {
        this.sbRank = sbRank;
    }

    /**
     * @return the baRank
     */
    public int getBaRank() {
        return baRank;
    }

    /**
     * @param baRank the baRank to set
     */
    public void setBaRank(int baRank) {
        this.baRank = baRank;
    }

    /**
     * @return the avgRank
     */
    public int getAvgRank() {
        return avgRank;
    }

    /**
     * @param avgRank the avgRank to set
     */
    public void setAvgRank(int avgRank) {
        this.avgRank = avgRank;
    }
    
    
}  
