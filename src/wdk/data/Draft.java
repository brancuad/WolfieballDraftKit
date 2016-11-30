package wdk.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import wdk.file.JsonDraftFileManager;
import static wdk.WDK_StartupConstants.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.io.IOException;
import java.text.DecimalFormat;

/**
 *This class represents a draft to be edited and then used to generate the
 * Wolfieball Draft Simulation.
 * @author Brandon
 */
public class Draft {
    // THESE DRAFT DETIALS DESCRIBE WHAT IS REQUIRED BY THE DRAFT SIMULATION
    String name;
    ObservableList<Player> players;
    ObservableList<Player> freeAgents;
    ObservableList<Player> visiblePlayers;
    ObservableList<Player> visibleMLBPlayers;
    ObservableList<Player> draftedPlayers;
    ObservableList<Team> fantasyTeams;
    ObservableList<String> fantasyTeamNames;
    ObservableList<String> teams;
    Team visibleTeam;
    JsonDraftFileManager jsonFileManager = new JsonDraftFileManager();
    
    DecimalFormat df = new DecimalFormat("0.00");
    
    /**
     * Constructor for setting up a Draft
     */
    public Draft() throws IOException { 
        // INITIALIZES THIS OBJECT'S DATA STRUCTURES
        name = "";
        players = FXCollections.observableArrayList();
        freeAgents = FXCollections.observableArrayList();
        visiblePlayers = FXCollections.observableArrayList();
        visibleMLBPlayers = FXCollections.observableArrayList();
        draftedPlayers = FXCollections.observableArrayList();
        fantasyTeams = FXCollections.observableArrayList();
        fantasyTeamNames = FXCollections.observableArrayList();
        teams = FXCollections.observableArrayList();
        visibleTeam = new Team();
        resetTeams();
    }
    
    // BELOW ARE ALL THE ACCESSOR METHODS FOR A DRAFT
    // AND THE MUTATOR METHODS.  NOTE THAT WE'LL NEED TO CALL TEHSE AS
    // USERS INTERACT WITH THE GUI
    
    public String getName() {
        return name;
    }
    
    public void setName(String n) {
        name = n;
    }
    
    public void addPlayer(Player p) {
        players.add(p);
        Collections.sort(players);
    }
    
    public ObservableList<Player> getPlayers() {
        return players;
    }
    
    public void removePlayer(Player itemToRemove) {
        players.remove(itemToRemove);
    }
    
    public void addFreeAgent(Player p) {
        freeAgents.add(p);
        Collections.sort(freeAgents);
    }
    
    public ObservableList<Player> getFreeAgents() {
        return freeAgents;
    }
    
    public void removeFreeAgent(Player itemToRemove) {
        freeAgents.remove(itemToRemove);
    }
    
    public void addVisiblePlayer(Player p) {
        visiblePlayers.add(p);
        Collections.sort(visiblePlayers);
    }
    
    public ObservableList<Player> getVisiblePlayers() {
        return visiblePlayers;
    }
    
    public void removeVisiblePlayer(Player itemToRemove) {
        visiblePlayers.remove(itemToRemove);
    }
    
    public void addVisibleMLBPlayer(Player p) {
        visibleMLBPlayers.add(p);
        Collections.sort(visibleMLBPlayers);
    }
    
    public ObservableList<Player> getVisibleMLBPlayers() {
        return visibleMLBPlayers;
    }
    
    public void removeVisibleMLBPlayer(Player itemToRemove) {
        visibleMLBPlayers.remove(itemToRemove);
    }
    
    public void addDraftedPlayer(Player p) {
        draftedPlayers.add(p);
        if (!p.getContract().equals("S2"))
            removeDraftedPlayer(p);
    }
    
    public ObservableList<Player> getDraftedPlayers() {
        return draftedPlayers;
    }
    
    public void removeDraftedPlayer(Player itemToRemove) {
        draftedPlayers.remove(itemToRemove);
    }
    
    public void addFantasyTeamName(String s) {
        fantasyTeamNames.add(s);
        Collections.sort(fantasyTeamNames);
    }
    
    public ObservableList<String> getFantasyTeamNames() {
        return fantasyTeamNames;
    }
    
    public void removeFantasyTeamName(String s) {
        fantasyTeamNames.remove(s);
        Collections.sort(fantasyTeamNames);
    }
    
    public void addTeam(String t) {
        teams.add(t);
        Collections.sort(teams);
    }
    
    public ObservableList<String> getTeams(){
        return teams;
    }
    
    public void removeTeam(Team teamToRemove) {
        teams.remove(teamToRemove);
    }
    
    public void addFantasyTeam(Team t) {
        fantasyTeams.add(t);
        Collections.sort(fantasyTeams);
        addFantasyTeamName(t.getTeamName());
    }
    
    public ObservableList<Team> getFantasyTeams() {
        return fantasyTeams;
    }
    
    public void removeFantasyTeam(Team teamToRemove) {
        fantasyTeams.remove(teamToRemove);
        removeFantasyTeamName(teamToRemove.getTeamName());
    }
    
    public Team getVisibleTeam() {
        return visibleTeam;
    }
    
    public void setVisibleTeam(Team t) {
        visibleTeam = t;
    }
    
    // BELOW ARE METHODS TO RESET THE LISTS OF PLAYERS, VISIBLE PLAYERS, AND
    // TEAMS
    public void resetPlayers() throws IOException {
        try{
            players.clear();
            players = jsonFileManager.loadPlayers(JSON_FILE_PATH_PITCHERS, JSON_FILE_PATH_HITTERS);
            Collections.sort(players);
            freeAgents.addAll(players);
        }
        catch (IOException ioe) {
            System.out.println("IOException happened while reseting players");
        }
    }
    
    public void resetVisiblePlayers() {
        visiblePlayers.clear();
        visiblePlayers.addAll(freeAgents);
    }
    
    public void resetVisibleMLBPlayers() {
        visibleMLBPlayers.clear();
        //visibleMLBPlayers.addAll(players);
    }
    
    public void resetTeams() {
        teams.clear();
        teams = FXCollections.observableArrayList();
        teams.add("ATL"); teams.add("AZ"); teams.add("CHC");
        teams.add("CIN"); teams.add("COL"); teams.add("LAD");
        teams.add("MIA"); teams.add("MIL"); teams.add("NYM");
        teams.add("PHI"); teams.add("PIT"); teams.add("SD");
        teams.add("SF"); teams.add("STL"); teams.add("WAS");
        Collections.sort(teams);
    }
    
    public void resetFantasyTeams() {
        fantasyTeams.clear();
        fantasyTeams = FXCollections.observableArrayList();
    }
    
    // RETURNS ONLY THE PITCHERS OF A LIST
    public ArrayList<Pitcher> getPitchers(List<Player> players) {
        ArrayList<Pitcher> pitchers = new ArrayList();
        for (Player p : players) {
            if (p.getPositions().contains("P")) {
                pitchers.add((Pitcher)p);
            }
        }
        return pitchers;
    }
    
    // RETURNS ONLY THE HITTERS OF A LIST
    public ArrayList<Hitter> getHitters(List<Player> players) {
        ArrayList<Hitter> hitters = new ArrayList();
        for (Player p : players) {
            if (!p.getPositions().contains("P")) {
                hitters.add((Hitter)p);
            }
        }
        return hitters;
    }
    
    public void calcTotalPoints() {
        ArrayList<Double> rankings = new ArrayList<Double>();
        int rank;
        // RESET TOTAL POINTS TO ZERO
        for (Team t : fantasyTeams) {
            t.setTotalPoints(0 + "");
        }
        
        // R
        for (Team t : fantasyTeams) {
            rankings.add(Double.parseDouble(t.getR()));
        }
        Collections.sort(rankings);
        for (Team t : fantasyTeams) {
            rank = rankings.indexOf(Double.parseDouble(t.getR())) + 1;
            t.setTotalPoints((Integer.parseInt(t.getTotalPoints()) + rank) + "");
        }
        
        rankings.clear();
        
        // HR
        for (Team t : fantasyTeams) {
            rankings.add(Double.parseDouble(t.getHR()));
        }
        Collections.sort(rankings);
        for (Team t : fantasyTeams) {
            rank = rankings.indexOf(Double.parseDouble(t.getHR())) + 1;
            t.setTotalPoints((Integer.parseInt(t.getTotalPoints()) + rank) + "");
        }
        
        rankings.clear();
        
        // RBI
        for (Team t : fantasyTeams) {
            rankings.add(Double.parseDouble(t.getRBI()));
        }
        Collections.sort(rankings);
        for (Team t : fantasyTeams) {
            rank = rankings.indexOf(Double.parseDouble(t.getRBI())) + 1;
            t.setTotalPoints((Integer.parseInt(t.getTotalPoints()) + rank) + "");
        }
        
        rankings.clear();
        
        // SB
        for (Team t : fantasyTeams) {
            rankings.add(Double.parseDouble(t.getSB()));
        }
        Collections.sort(rankings);
        for (Team t : fantasyTeams) {
            rank = rankings.indexOf(Double.parseDouble(t.getSB())) + 1;
            t.setTotalPoints((Integer.parseInt(t.getTotalPoints()) + rank) + "");
        }
        
        rankings.clear();
        
        // BA
        for (Team t : fantasyTeams) {
            rankings.add(Double.parseDouble(t.getBA()));
        }
        Collections.sort(rankings);
        for (Team t : fantasyTeams) {
            rank = rankings.indexOf(Double.parseDouble(t.getBA())) + 1;
            t.setTotalPoints((Integer.parseInt(t.getTotalPoints()) + rank) + "");
        }
        
        rankings.clear();
        
        // W
        for (Team t : fantasyTeams) {
            rankings.add(Double.parseDouble(t.getW()));
        }
        Collections.sort(rankings);
        for (Team t : fantasyTeams) {
            rank = rankings.indexOf(Double.parseDouble(t.getW())) + 1;
            t.setTotalPoints((Integer.parseInt(t.getTotalPoints()) + rank) + "");
        }
        
        rankings.clear();
        
        // SV
        for (Team t : fantasyTeams) {
            rankings.add(Double.parseDouble(t.getSV()));
        }
        Collections.sort(rankings);
        for (Team t : fantasyTeams) {
            rank = rankings.indexOf(Double.parseDouble(t.getSV())) + 1;
            t.setTotalPoints((Integer.parseInt(t.getTotalPoints()) + rank) + "");
        }
        
        rankings.clear();
        
        // K
        for (Team t : fantasyTeams) {
            rankings.add(Double.parseDouble(t.getK()));
        }
        Collections.sort(rankings);
        for (Team t : fantasyTeams) {
            rank = rankings.indexOf(Double.parseDouble(t.getK())) + 1;
            t.setTotalPoints((Integer.parseInt(t.getTotalPoints()) + rank) + "");
        }
        
        rankings.clear();
        
        // ERA
        for (Team t : fantasyTeams) {
            rankings.add(Double.parseDouble(t.getERA()));
        }
        Collections.sort(rankings);
        for (Team t : fantasyTeams) {
            rank = rankings.indexOf(Double.parseDouble(t.getERA())) + 1;
            t.setTotalPoints((Integer.parseInt(t.getTotalPoints()) + rank) + "");
        }
        
        rankings.clear();
        
        // WHIP
        for (Team t : fantasyTeams) {
            rankings.add(Double.parseDouble(t.getWHIP()));
        }
        Collections.sort(rankings);
        for (Team t : fantasyTeams) {
            rank = rankings.indexOf(Double.parseDouble(t.getWHIP())) + 1;
            t.setTotalPoints((Integer.parseInt(t.getTotalPoints()) + rank) + "");
        }
    }
    
    public void calcEV() {
        // TOTAL MONEY FOR TEAMS WITH SPOTS TO FILL
        int totalMoney = 0;
        int rank;
        for (Team t : fantasyTeams) {
            if (t.getStartingPlayers().size() < 23)
                totalMoney += Integer.parseInt(t.getMoneyLeft());
        }
        
        ArrayList<Double> rankings = new ArrayList<Double>();
        
        // R
        for (Hitter h : getHitters(freeAgents)) {
            rankings.add(Double.parseDouble(h.getR()));
        }
        Collections.sort(rankings);
        Collections.reverse(rankings);
        for (Hitter h : getHitters(freeAgents)) {
            rank = rankings.indexOf(Double.parseDouble(h.getR())) + 1;
            h.setRRank(rank);
        }
        
        rankings.clear();
        
        // HR
        for (Hitter h : getHitters(freeAgents)) {
            rankings.add(Double.parseDouble(h.getHR()));
        }
        Collections.sort(rankings);
        Collections.reverse(rankings);
        for (Hitter h : getHitters(freeAgents)) {
            rank = rankings.indexOf(Double.parseDouble(h.getHR())) + 1;
            h.setHrRank(rank);
        }
        
        rankings.clear();
        
        // RBI
        for (Hitter h : getHitters(freeAgents)) {
            rankings.add(Double.parseDouble(h.getRBI()));
        }
        Collections.sort(rankings);
        Collections.reverse(rankings);
        for (Hitter h : getHitters(freeAgents)) {
            rank = rankings.indexOf(Double.parseDouble(h.getRBI())) + 1;
            h.setRbiRank(rank);
        }
        
        rankings.clear();
        
        // SB
        for (Hitter h : getHitters(freeAgents)) {
            rankings.add(Double.parseDouble(h.getSB()));
        }
        Collections.sort(rankings);
        Collections.reverse(rankings);
        for (Hitter h : getHitters(freeAgents)) {
            rank = rankings.indexOf(Double.parseDouble(h.getSB())) + 1;
            h.setSbRank(rank);
        }
        
        rankings.clear();
        
        // BA
        for (Hitter h : getHitters(freeAgents)) {
            rankings.add(Double.parseDouble(h.getBA()));
        }
        Collections.sort(rankings);
        Collections.reverse(rankings);
        for (Hitter h : getHitters(freeAgents)) {
            rank = rankings.indexOf(Double.parseDouble(h.getBA())) + 1;
            h.setBaRank(rank);
        }
        
        rankings.clear();
        
        // AvgRank for Hitters
        int totalRankings;
        for (Hitter h : getHitters(freeAgents)) {
            totalRankings = 0;
            totalRankings += h.getBaRank() + h.getHrRank() + h.getRRank() + 
                    h.getRbiRank() + h.getSbRank();
            h.setAvgRank(totalRankings/5);
        }
        
        // W
        for (Pitcher p : getPitchers(freeAgents)) {
            rankings.add(Double.parseDouble(p.getW()));
        }
        Collections.sort(rankings);
        Collections.reverse(rankings);
        for (Pitcher p : getPitchers(freeAgents)) {
            rank = rankings.indexOf(Double.parseDouble(p.getW())) + 1;
            p.setWRank(rank);
        }
        
        rankings.clear();
        
        // SV
        for (Pitcher p : getPitchers(freeAgents)) {
            rankings.add(Double.parseDouble(p.getSV()));
        }
        Collections.sort(rankings);
        Collections.reverse(rankings);
        for (Pitcher p : getPitchers(freeAgents)) {
            rank = rankings.indexOf(Double.parseDouble(p.getSV())) + 1;
            p.setSvRank(rank);
        }
        
        rankings.clear();
        
        // K
        for (Pitcher p : getPitchers(freeAgents)) {
            rankings.add(Double.parseDouble(p.getK()));
        }
        Collections.sort(rankings);
        Collections.reverse(rankings);
        for (Pitcher p : getPitchers(freeAgents)) {
            rank = rankings.indexOf(Double.parseDouble(p.getK())) + 1;
            p.setKRank(rank);
        }
        
        rankings.clear();
        
        // ERA
        for (Pitcher p : getPitchers(freeAgents)) {
            rankings.add(Double.parseDouble(p.getERA()));
        }
        Collections.sort(rankings);
        Collections.reverse(rankings);
        for (Pitcher p : getPitchers(freeAgents)) {
            rank = rankings.indexOf(Double.parseDouble(p.getERA())) + 1;
            p.setEraRank(rank);
        }
        
        rankings.clear();
        
        // WHIP
        for (Pitcher p : getPitchers(freeAgents)) {
            rankings.add(Double.parseDouble(p.getWHIP()));
        }
        Collections.sort(rankings);
        Collections.reverse(rankings);
        for (Pitcher p : getPitchers(freeAgents)) {
            rank = rankings.indexOf(Double.parseDouble(p.getWHIP())) + 1;
            p.setWhipRank(rank);
        }
        
        rankings.clear();
        
        // AvgRank for Pitchers
        
        for (Pitcher p : getPitchers(freeAgents)) {
            totalRankings = 0;
            totalRankings += p.getWRank() + p.getSvRank() + p.getKRank() + 
                    p.getEraRank() + p.getWhipRank();
            p.setAvgRank(totalRankings/5);
        }
        
        // GET NUMBER OF HITTERS AND PITCHERS NEEDED ACROSS ALL TEAMS
        int X = 0;  // NUM HITTERS
        int Y = 0;  // NUM PITCHERS
        for (Team t : fantasyTeams) {
            int numH = t.getStartingPlayers().size() - Collections.frequency(t.getPositions(), "P");
            X += t.getStartingPlayers().size() - numH;
        }
        for (Team t : fantasyTeams) {
            Y += t.getStartingPlayers().size() - Collections.frequency(t.getPositions(), "P");
        }
        
        // CALCULATE EV NOW
        double ev;
        for (Pitcher p : getPitchers(freeAgents)) {
            ev = (double)((double)totalMoney / (double)p.getAvgRank());
            p.setEstimatedValue(df.format(ev) + "");
        }
        for (Hitter h : getHitters(freeAgents)) {
            ev = (double)((double)totalMoney / (double)h.getAvgRank());
            h.setEstimatedValue(df.format(ev) + "");
        }
        
        
        
    }
    
}
