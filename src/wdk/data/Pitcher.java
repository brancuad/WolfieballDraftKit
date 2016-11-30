package wdk.data;

import java.text.DecimalFormat;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;


/**
 * This class serves to represent a pitcher in the Draft.
 * 
 * @author Brandon
 */
public class Pitcher extends Player {
    final StringProperty QP;
    final StringProperty IP;
    final StringProperty ER;
    final StringProperty W;
    final StringProperty SV;
    final StringProperty H;
    final StringProperty BB;
    final StringProperty K;
    final StringProperty WHIP;
    final StringProperty ERA;
    final StringProperty EstimatedValue;
    public static final String DEFAULT = "";
    public static final String DEFAULT_QP = "P";
    private int wRank;
    private int svRank;
    private int kRank;
    private int eraRank;
    private int whipRank;
    private int avgRank;
    
    // FOR LIMITING DECIMAL SPACES
    DecimalFormat df;
    
    public Pitcher() {
        setLastName(DEFAULT);
        setFirstName(DEFAULT);
        setTeam(DEFAULT);
        QP = new SimpleStringProperty(DEFAULT_QP);
        IP = new SimpleStringProperty(DEFAULT);
        ER = new SimpleStringProperty(DEFAULT);
        W = new SimpleStringProperty(DEFAULT);
        SV = new SimpleStringProperty(DEFAULT);
        H = new SimpleStringProperty(DEFAULT);
        BB = new SimpleStringProperty(DEFAULT);
        K = new SimpleStringProperty(DEFAULT);
        ERA = new SimpleStringProperty(DEFAULT);
        WHIP = new SimpleStringProperty(DEFAULT);
        df = new DecimalFormat("0.00");
        setNotes(DEFAULT);
        setYearOfBirth(DEFAULT);
        setNationOfBirth(DEFAULT);
        EstimatedValue = new SimpleStringProperty(DEFAULT);
        setContract(DEFAULT);
        setSalary(DEFAULT);
        setCurrentPosition(DEFAULT);
    }
    
    public void reset() {
        setLastName(DEFAULT);
        setFirstName(DEFAULT);
        setTeam(DEFAULT);
        setQP(DEFAULT_QP);
        setIP(DEFAULT);
        setER(DEFAULT);
        setW(DEFAULT);
        setSV(DEFAULT);
        setH(DEFAULT);
        setBB(DEFAULT);
        setK(DEFAULT);
        setWHIP();
        setERA();
        setNotes(DEFAULT);
        setYearOfBirth(DEFAULT);
        setNationOfBirth(DEFAULT);
    }
    
    public String getIP() {
        return IP.get();
    }
    
    public void setIP(String initIP) {
        IP.set(initIP);
    }
    
    public StringProperty IPProperty() {
        return IP;
    }
    
    public String getER() {
        return ER.get();
    }
    
    public void setER(String initER) {
        ER.set(initER);
    }
    
    public StringProperty ERProperty() {
        return ER;
    }
    
    public String getW() {
        return W.get();
    }
    
    public void setW(String initW) {
        W.set(initW);
    }
    
    public StringProperty WProperty() {
        return W;
    }
    
    public String getSV() {
        return SV.get();
    }
    
    public void setSV(String initSV) {
        SV.set(initSV);
    }
    
    public StringProperty SVProperty() {
        return SV;
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
    
    public String getBB() {
        return BB.get();
    }
    
    public void setBB(String initBB) {
        BB.set(initBB);
    }
    
    public StringProperty BBProperty() {
        return BB;
    }
    
    public String getK() {
        return K.get();
    }
    
    public void setK(String initK) {
        K.set(initK);
    }
    
    public StringProperty KProperty() {
        return K;
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
    
    public String getQP() {
        return QP.get();
    }
    
    public void setQP(String initQP) {
        QP.set(initQP);
    }
    
    public StringProperty QPProperty() {
        return QP;
    }
    
    public String getERA() {
        return ERA.get();
    }
    
    public void setERA() {
        String era = df.format(calcERA());
        ERA.set(era);
    }
    
    public StringProperty ERAProperty() {
        return ERA;
    }
    
    public double calcERA() {
        double ip = Double.parseDouble(getIP());
        if (ip == 0)
            return 0;
        else {
            double ip9 = 9/ip;
            double er = Double.parseDouble(getER());
            double era = er * (ip9);
            return era;
        }
        
    }
    
    public String getWHIP() {
        return WHIP.get();
    }
    
    public void setWHIP() {
        String whip = df.format(calcWHIP());
        WHIP.set(whip);
    }
    
    public StringProperty WHIPProperty() {
        return WHIP;
    }
    
    public double calcWHIP() {
        double bb = Double.parseDouble(getBB());
        double h = Double.parseDouble(getH());
        double ip = Double.parseDouble(getIP());
        if (ip == 0)
            return 0;
        else {
            double bbh = bb+h;
            double whip = bbh/ip;
            return whip;
        }
        
    }
    
    // THE FOLLOWING ARE FOR BINDING THE PROPERTIES BY THE PROPERTY NAMES OF 
    // THE HITTER STATS
    public StringProperty RProperty() {
        return WProperty();
    }
    
    public StringProperty HRProperty() {
        return SVProperty();
    }
    
    public StringProperty RBIProperty() {
        return KProperty();
    }
    
    public StringProperty SBProperty() {
        return ERAProperty();
    }
    
    public StringProperty BAProperty() {
        return WHIPProperty();
    }

    /**
     * @return the wRank
     */
    public int getWRank() {
        return wRank;
    }

    /**
     * @param wRank the wRank to set
     */
    public void setWRank(int wRank) {
        this.wRank = wRank;
    }

    /**
     * @return the svRank
     */
    public int getSvRank() {
        return svRank;
    }

    /**
     * @param svRank the svRank to set
     */
    public void setSvRank(int svRank) {
        this.svRank = svRank;
    }

    /**
     * @return the kRank
     */
    public int getKRank() {
        return kRank;
    }

    /**
     * @param kRank the kRank to set
     */
    public void setKRank(int kRank) {
        this.kRank = kRank;
    }

    /**
     * @return the eraRank
     */
    public int getEraRank() {
        return eraRank;
    }

    /**
     * @param eraRank the eraRank to set
     */
    public void setEraRank(int eraRank) {
        this.eraRank = eraRank;
    }

    /**
     * @return the whipRank
     */
    public int getWhipRank() {
        return whipRank;
    }

    /**
     * @param whipRank the whipRank to set
     */
    public void setWhipRank(int whipRank) {
        this.whipRank = whipRank;
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
