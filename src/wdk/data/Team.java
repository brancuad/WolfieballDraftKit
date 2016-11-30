package wdk.data;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * This class represents a team which contains players and is created during
 * a draft.
 * 
 * @author Brandon
 */
public class Team implements Comparable {
    final StringProperty TEAM_NAME;
    final StringProperty OWNER_NAME;
    ObservableList<Player> startingPlayers;
    ObservableList<Player> taxiPlayers;
    ObservableList<String> positions;
    PositionComparator pc;
    final StringProperty MONEY_LEFT;
    final StringProperty R;
    final StringProperty HR;
    final StringProperty RBI;
    final StringProperty SB;
    final StringProperty BA;
    final StringProperty W;
    final StringProperty SV;
    final StringProperty K;
    final StringProperty ERA;
    final StringProperty WHIP;
    final StringProperty TOTAL_POINTS;
    public static final String DEFAULT = "";
    
    DecimalFormat ba = new DecimalFormat("0.000");
    DecimalFormat df = new DecimalFormat("0.00");
    
    /**
     * Constructor for setting up a Team
     */
    public Team() {
        TEAM_NAME = new SimpleStringProperty(DEFAULT);
        OWNER_NAME = new SimpleStringProperty(DEFAULT);
        startingPlayers = FXCollections.observableArrayList();
        taxiPlayers = FXCollections.observableArrayList();
        positions = FXCollections.observableArrayList();
        pc = new PositionComparator();
        R = new SimpleStringProperty(DEFAULT);
        HR = new SimpleStringProperty(DEFAULT);
        RBI = new SimpleStringProperty(DEFAULT);
        SB = new SimpleStringProperty(DEFAULT);
        BA = new SimpleStringProperty(DEFAULT);
        W = new SimpleStringProperty(DEFAULT);
        SV = new SimpleStringProperty(DEFAULT);
        K = new SimpleStringProperty(DEFAULT);
        ERA = new SimpleStringProperty(DEFAULT);
        WHIP = new SimpleStringProperty(DEFAULT);
        TOTAL_POINTS = new SimpleStringProperty(DEFAULT);
        MONEY_LEFT = new SimpleStringProperty(DEFAULT);
    }
    
    public void reset() {
        setTeamName(DEFAULT);
        setOwnerName(DEFAULT);
        resetStartingPlayers();
        resetTaxiPlayers();
        resetPositions();
    }
    
    public String getTeamName() {
        return TEAM_NAME.get();
    }
    
    public void setTeamName(String initName) {
        TEAM_NAME.set(initName);
    }
    
    public StringProperty teamNameProperty() {
        return TEAM_NAME;
    }
    
    public String getOwnerName() {
        return OWNER_NAME.get();
    }
    
    public void setOwnerName(String initName) {
        OWNER_NAME.set(initName);
    }
    
    public StringProperty ownerNameProperty() {
        return OWNER_NAME;
    }
    
    // CHOOSE BETWEEN STARTING OR TAXI
    public void addPlayer(Player p) {
        // if
        addStartingPlayer(p);
        // else
        // addTaxiPlayer(p);
    }
    
    public void addStartingPlayer(Player p) {
        if (startingPlayers.size() < 23) {
           startingPlayers.add(p);
            Collections.sort(startingPlayers, pc); 
        }
        else {
            taxiPlayers.add(p);
            Collections.sort(taxiPlayers, pc);
        }
        
    }
    
    public ObservableList<Player> getStartingPlayers() {
        return startingPlayers;
    }
    
    public void removeStartingPlayer(Player itemToRemove) {
        startingPlayers.remove(itemToRemove);
        removeTaxiPlayer(itemToRemove);
    }
    
    public void addTaxiPlayer(Player p) {
        taxiPlayers.add(p);
        Collections.sort(taxiPlayers, pc);
    }
    
    public ObservableList<Player> getTaxiPlayers() {
        return taxiPlayers;
    }
    
    public void removeTaxiPlayer(Player itemToRemove) {
        taxiPlayers.remove(itemToRemove);
    }
    
    public void addPosition(String s) {
        positions.add(s);
    }
    
    public ObservableList<String> getPositions() {
        return positions;
    }
    
    public void removePosition(String s) {
        positions.remove(s);
    }
    
    public void resetPositions() {
        positions.clear();
        positions = FXCollections.observableArrayList();
    }
    
    public void resetStartingPlayers() {
        startingPlayers.clear();
        startingPlayers = FXCollections.observableArrayList();
    }
    
    public void resetTaxiPlayers() {
        taxiPlayers.clear();
        taxiPlayers = FXCollections.observableArrayList();
    }
    
    
    
    // RETURNS AN ObservableList OF OPEN POSITIONS
    public ObservableList<String> openPositions() {
        ObservableList<String> openPositions = FXCollections.observableArrayList();
        int n;
        // PUT ALL POSITIONS AND REMOVE AS NECESSARY
        openPositions.add("C");
        openPositions.add("1B");
        openPositions.add("CI");
        openPositions.add("3B");
        openPositions.add("2B");
        openPositions.add("MI");
        openPositions.add("SS");
        openPositions.add("U");
        openPositions.add("OF");
        openPositions.add("P");
        ObservableList<String> toReturn = FXCollections.observableArrayList();
        toReturn.addAll(openPositions);
        if (startingPlayers.size() >= 23) {
            return toReturn;
        }
        else {
            for(String s : openPositions) {
                // CHECK FOR NUMBER OF OCCURENCES OF DESIRED POSITION
                switch (s) {
                    case "C":   n = Collections.frequency(positions, s);
                                if (n >= 2) {
                                    toReturn.remove(s);
                                }
                                break;
                    case "1B":  
                    case "CI":
                    case "3B":
                    case "2B":
                    case "MI":
                    case "SS":
                    case "U":
                                n = Collections.frequency(positions, s);
                                if (n >= 1) {
                                    toReturn.remove(s);
                                }
                                break;
                    case "OF":  n = Collections.frequency(positions, s);
                                if (n >= 5) {
                                    toReturn.remove(s);
                                }
                                break;
                    case "P":   n = Collections.frequency(positions, s);
                                if (n >= 9) {
                                    toReturn.remove(s);
                                }    
                }
            }
        return toReturn;
        }
    }
    
    @Override
    public int compareTo(Object t) {
        Team otherTeam = (Team) t;
        return getTeamName().compareTo(otherTeam.getTeamName());
    }
    
    // RETURNS ONLY THE PITCHERS OF A LIST
    public ArrayList<Pitcher> getPitchers(List<Player> players) {
        ArrayList<Pitcher> pitchers = new ArrayList();
        for (Player p : players) {
            if (p.getCurrentPosition().equals("P")) {
                pitchers.add((Pitcher)p);
            }
        }
        return pitchers;
    }
    
    // RETURNS ONLY THE HITTERS OF A LIST
    public ArrayList<Hitter> getHitters(List<Player> players) {
        ArrayList<Hitter> hitters = new ArrayList();
        for (Player p : players) {
            if (!p.getCurrentPosition().equals("P")) {
                hitters.add((Hitter)p);
            }
        }
        return hitters;
    }
    
    public String getMoneyLeft() {
        updateMoneyLeft();
        return MONEY_LEFT.get();
    }
    
    public void setMoneyLeft() {
        updateMoneyLeft();
    }
    
    public StringProperty moneyLeftProperty() {
        updateMoneyLeft();
        return MONEY_LEFT;
    }
    
    public void updateMoneyLeft() {
        int mL = 260;
        for (Player p : startingPlayers) {
            mL -= Integer.parseInt(p.getSalary());
        }
        for (Player p : taxiPlayers) {
            mL -= Integer.parseInt(p.getSalary());
        }
        MONEY_LEFT.set(mL+"");
    }
    
    public String getR() {
        setR();
        return R.get();
    }
    
    public void setR() {
        R.set(calcR() + "");
    }
    
    public StringProperty RProperty() {
        return R;
    }
    
    public int calcR() {
        int total = 0;
        for (Hitter h : getHitters(startingPlayers)) {
            total += Integer.parseInt(h.getR());
        }
        return total;
    }
    
    public String getHR() {
        setHR();
        return HR.get();
    }
    
    public void setHR() {
        HR.set(calcHR() + "");
    }
    
    public StringProperty HRProperty() {
        return HR;
    }
    
    public int calcHR() {
        int total = 0;
        for (Hitter h : getHitters(startingPlayers)) {
            total += Integer.parseInt(h.getHR());
        }
        return total;
    }
    
    public String getRBI() {
        setRBI();
        return RBI.get();
    }
    
    public void setRBI() {
        RBI.set(calcRBI() + "");
    }
    
    public StringProperty RBIProperty() {
        return RBI;
    }
    
    public int calcRBI() {
        int total = 0;
        for (Hitter h : getHitters(startingPlayers)) {
            total += Integer.parseInt(h.getRBI());
        }
        return total;
    }
    
    public String getSB() {
        setSB();
        return SB.get();
    }
    
    public void setSB() {
        SB.set(calcSB() + "");
    }
    
    public StringProperty SBProperty() {
        return SB;
    }
    
    public int calcSB() {
        int total = 0;
        for (Hitter h : getHitters(startingPlayers)) {
            total += Integer.parseInt(h.getSB());
        }
        return total;
    }
    
    public String getBA() {
        setBA();
        return BA.get();
    }
    
    public void setBA() {
        BA.set(ba.format(calcBA()) + "");
    }
    
    public StringProperty BAProperty() {
        return BA;
    }
    
    public double calcBA() {
        double totalNum = 0;
        double totalDenom = 0;
        double total;
        for (Hitter h : getHitters(startingPlayers)) {
            totalNum += Double.parseDouble(h.getH());
            totalDenom += Double.parseDouble(h.getAB());
        }
        if (totalDenom == 0)
            total = 0;
        else
            total = totalNum / totalDenom;
        return total;
    }
    
    public String getW() {
        setW();
        return W.get();
    }
    
    public void setW() {
        W.set(calcW() + "");
    }
    
    public StringProperty WProperty() {
        return W;
    }
    
    public int calcW() {
        int total = 0;
        for (Pitcher h : getPitchers(startingPlayers)) {
            total += Integer.parseInt(h.getW());
        }
        return total;
    }
    
    public String getSV() {
        setSV();
        return SV.get();
    }
    
    public void setSV() {
        SV.set(calcSV() + "");
    }
    
    public StringProperty SVProperty() {
        return SV;
    }
    
    public int calcSV() {
        int total = 0;
        for (Pitcher h : getPitchers(startingPlayers)) {
            total += Integer.parseInt(h.getSV());
        }
        return total;
    }
    
    public String getK() {
        setK();
        return K.get();
    }
    
    public void setK() {
        K.set(calcK() + "");
    }
    
    public StringProperty KProperty() {
        return K;
    }
    
    public int calcK() {
        int total = 0;
        for (Pitcher h : getPitchers(startingPlayers)) {
            total += Integer.parseInt(h.getK());
        }
        return total;
    }
    
    public String getERA() {
        setERA();
        return ERA.get();
    }
    
    public void setERA() {
        ERA.set(df.format(calcERA()) + "");
    }
    
    public StringProperty ERAProperty() {
        return ERA;
    }
    
    public double calcERA() {
        double totalNum = 0;
        double totalDenom = 0;
        double total;
        for (Pitcher h : getPitchers(startingPlayers)) {
            totalNum += (Double.parseDouble(h.getER()) * 9);
            totalDenom += Double.parseDouble(h.getIP());
        }
        if (totalDenom == 0)
            total = 0;
        else
            total = totalNum / totalDenom;
        return total;
    }
    
    public String getWHIP() {
        setWHIP();
        return WHIP.get();
    }
    
    public void setWHIP() {
        WHIP.set(df.format(calcWHIP()) + "");
    }
    
    public StringProperty WHIPProperty() {
        return WHIP;
    }
    
    public double calcWHIP() {
        double totalNum = 0;
        double totalDenom = 0;
        double total;
        for (Pitcher h : getPitchers(startingPlayers)) {
            totalNum += (Double.parseDouble(h.getBB()) + Double.parseDouble(h.getH()));
            totalDenom += Double.parseDouble(h.getIP());
        }
        if (totalDenom == 0)
            total = 0;
        else
            total = totalNum / totalDenom;
        return total;
    }
    
    public String getTotalPoints() {
        return TOTAL_POINTS.get();
    }
    
    public void setTotalPoints(String t) {
        TOTAL_POINTS.set(t);
    }
    
    public StringProperty totalPointsProperty() {
        return TOTAL_POINTS;
    }
    
}
