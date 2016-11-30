package wdk.controller;

import java.util.ArrayList;
import java.util.Arrays;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import properties_manager.PropertiesManager;
import wdk.WDK_PropertyType;
import wdk.data.Draft;
import wdk.data.Player;
import wdk.data.Team;
import wdk.error.ErrorHandler;
import wdk.gui.DraftSummaryScreen;
import wdk.gui.FantasyStandingsScreen;
import wdk.gui.FantasyTeamsScreen;
import wdk.gui.MLBTeamsScreen;
import wdk.gui.PlayersScreen;
import wdk.gui.WDK_GUI;

/**
 * This controller class handles the responses to all draft editing input,
 * including verification of data and binding of entered data to the Draft
 * object.
 * 
 * @author Brandon
 */
public class DraftEditController {
    // WE USE THIS TO MAKE SURE OUR PROGRAMMED UPDATES OF UI
    // VALUES DON'T THEMSELVES TRIGGER EVENTS
    private boolean enabled;
    
    FantasyTeamsScreen fantasyTeamsScreen;
    PlayersScreen playersScreen;
    FantasyStandingsScreen fantasyStandingsScreen;
    DraftSummaryScreen draftSummaryScreen;
    MLBTeamsScreen MLBTeamsScreen;
    
    // FOR PROPERTIES
    PropertiesManager props;
    
    // FOR THE AUTO THREAD
    Thread thread;
    boolean done;
    
    /**
     * Constructor that gets this controller ready, not much to initialize
     * as the methods for this function are sent all the objects they need
     * as arguments.
     */
    public DraftEditController() {
        enabled = true;
    }
    
    /**
     * This mutator method lets us enable or disable this controller.
     * 
     * @param enableSetting If false, this controller will not respond to
     * Draft editing.  If true, it will
     */
    public void enable(boolean enableSetting) {
        enabled = enableSetting;
    }
    
    /**
     * This controller function is called in response to the user changing
     * draft details in the UI.  It responds by updating the bound Draft
     * object using all the UI values, including the verification of that data.
     * 
     * @param gui The user interface that requested the change.
     */
    public void handleDraftChangeRequest(WDK_GUI gui) {
        if (enabled) {
            try {
                // UPDATE THE DRAFT, VERIFYING INPUT VALUES
                gui.updateDraftInfo(gui.getDataManager().getDraft());
                
                // THE DRAFT IS NOW DIRTY, MEANING IT'S BEEN
                // CHANGED SINCE IT WAS LAST SAVED, SO MAKE SURE THE SAVE
                // BUTTOM IS ENABLED
                gui.getFileController().markAsEdited(gui);
            }catch (Exception e) {
                // SOMETHING WENT WRONG
                ErrorHandler eH = ErrorHandler.getErrorHandler();
                eH.handleUpdateDraftError();
            }
        }
    }
    
    // THESE ARE FOR SWITCHING SCREENS
    public void handleFantasyTeamsRequest(WDK_GUI gui) {
        fantasyTeamsScreen = new FantasyTeamsScreen(gui);
        gui.updateWorkspace(fantasyTeamsScreen.initWorkspace());
    }
    
    public void handlePlayersRequest(WDK_GUI gui) {
        playersScreen = new PlayersScreen(gui);
        gui.updateWorkspace(playersScreen.initWorkspace());
    }
    
    public void handleFantasyStandingsRequest(WDK_GUI gui) {
        fantasyStandingsScreen = new FantasyStandingsScreen(gui);
        gui.updateWorkspace(fantasyStandingsScreen.initWorkspace());
    }
    public void handleDraftSummaryRequest(WDK_GUI gui) {
        draftSummaryScreen = new DraftSummaryScreen(gui);
        gui.updateWorkspace(draftSummaryScreen.initWorkspace());
    }
    public void handleMLBTeamsRequest(WDK_GUI gui) {
        MLBTeamsScreen = new MLBTeamsScreen(gui);
        gui.updateWorkspace(MLBTeamsScreen.initWorkspace());
    }
    
    // FOR SEARCHING PLAYERS
    public void handleSearchPlayerRequest(WDK_GUI gui, PlayersScreen screen) {
        
        // MAKE AN ITERATOR TO SEARCH THROUGH THE LIST OF VISIBLE PLAYERS
        // AND REMOVE THOSE THAT DON'T FIT THE CRITERIA
        String search = screen.getSearchPlayerField().getText();
        ObservableList<Player> nameArray = FXCollections.observableArrayList();
        nameArray.addAll(gui.getDataManager().getDraft().getVisiblePlayers());
        gui.getDataManager().getDraft().getVisiblePlayers().clear();
        
        for (Player p : nameArray) {
            if(p.getFirstName().toLowerCase().startsWith(search.toLowerCase()) || p.getLastName().toLowerCase().startsWith(search.toLowerCase())) {
                gui.getDataManager().getDraft().addVisiblePlayer(p);
            }
        }
        
        // RESET AND TAKE POSITION SELECTED INTO ACCOUNT
        if(screen.getSearchPlayerField().getText().equals("")) {
            gui.getDataManager().getDraft().resetVisiblePlayers();
            handlePositionsFilterRequest(gui, screen);
        }
               
    }
    
    // FOR FILTERING POSITIONS USING RADIO BUTTONS
    public void handlePositionsFilterRequest(WDK_GUI gui, PlayersScreen screen) {
        // FOR SETTING LABELS LATER

        //  LOCATE PROPERTY
        WDK_PropertyType selection = (WDK_PropertyType)screen.getPositionsGroup().getSelectedToggle().getUserData();
        props = PropertiesManager.getPropertiesManager();
        
        // SEARCH FOR PLAYERS WITH THAT QP
        gui.getDataManager().getDraft().getVisiblePlayers().clear();
        String sel = props.getProperty(selection);
        for(Player p : gui.getDataManager().getDraft().getFreeAgents()) {
            ArrayList<String> qpArray = new ArrayList(Arrays.asList(p.getQP().split("_")));
            if (qpArray.contains(sel))
                gui.getDataManager().getDraft().addVisiblePlayer(p);
        }
        
        // CHANGE TO PITCHER LABELS
        if (sel.equals("P")) {
            screen.getRwColumn().setText(props.getProperty(WDK_PropertyType.W_LABEL));
            screen.getHrswColumn().setText(props.getProperty(WDK_PropertyType.SW_LABEL));
            screen.getRbikColumn().setText(props.getProperty(WDK_PropertyType.K_LABEL));
            screen.getSberaColumn().setText(props.getProperty(WDK_PropertyType.ERA_LABEL));
            screen.getBawhipColumn().setText(props.getProperty(WDK_PropertyType.WHIP_LABEL));
        }
        
        // SHOW ALL AND CHANGE TO NEUTRAL LABELS
        else if (sel.equals("All")) {
            gui.getDataManager().getDraft().resetVisiblePlayers();
            screen.getRwColumn().setText(props.getProperty(WDK_PropertyType.R_LABEL) + "/" + props.getProperty(WDK_PropertyType.W_LABEL));
            screen.getHrswColumn().setText(props.getProperty(WDK_PropertyType.HR_LABEL) + "/" + props.getProperty(WDK_PropertyType.SW_LABEL));
            screen.getRbikColumn().setText(props.getProperty(WDK_PropertyType.RBI_LABEL) + "/" + props.getProperty(WDK_PropertyType.K_LABEL));
            screen.getSberaColumn().setText(props.getProperty(WDK_PropertyType.SB_LABEL) + "/" + props.getProperty(WDK_PropertyType.ERA_LABEL));
            screen.getBawhipColumn().setText(props.getProperty(WDK_PropertyType.BA_LABEL) + "/" + props.getProperty(WDK_PropertyType.WHIP_LABEL));
        }
        
        // CHANGE TO HITTER LABELS
        else {
            screen.getRwColumn().setText(props.getProperty(WDK_PropertyType.R_LABEL));
            screen.getHrswColumn().setText(props.getProperty(WDK_PropertyType.HR_LABEL));
            screen.getRbikColumn().setText(props.getProperty(WDK_PropertyType.RBI_LABEL));
            screen.getSberaColumn().setText(props.getProperty(WDK_PropertyType.SB_LABEL));
            screen.getBawhipColumn().setText(props.getProperty(WDK_PropertyType.BA_LABEL));
        }
        
        if (!screen.getSearchPlayerField().getText().equals("")) {
            handleSearchPlayerRequest(gui, screen);
        }
        
    }
    
    public void handleVisibleTeamRequest(WDK_GUI gui, FantasyTeamsScreen screen) {
        int i = screen.getFantasyTeamComboBox().getSelectionModel().getSelectedIndex();
        if (i != -1) {
            Team t = gui.getDataManager().getDraft().getFantasyTeams().get(i);
            gui.getDataManager().getDraft().setVisibleTeam(t);
            screen.getStartingPlayersTable().setItems(t.getStartingPlayers()); 
            screen.getTaxiPlayersTable().setItems(t.getTaxiPlayers());
        }  
        
    }
    
    public void handleVisibleMLBTeamRequest(WDK_GUI gui, MLBTeamsScreen screen) {
        //  LOCATE STRING
        String selection = (String)screen.getTeamComboBox().getSelectionModel().getSelectedItem();
                
        // SEARCH FOR PLAYERS WITH THAT TEAM
        gui.getDataManager().getDraft().getVisibleMLBPlayers().clear();
        for(Player p : gui.getDataManager().getDraft().getPlayers()) {
            String team = p.getTeam();
            if (team.equals(selection))
                gui.getDataManager().getDraft().addVisibleMLBPlayer(p);
        }
    }
    
    // AUTO ADDS A SINGLE PLAYER
    // RETURNS TRUE IF ALL STARTING SLOTS ARE FULL
    public boolean handleAutoAddRequest(WDK_GUI gui) {
        
        Draft draft = gui.getDataManager().getDraft();
        
        if (draft.getFantasyTeams().size() == 0) {
            return true;
        }
        
        boolean taxiTime = false;
        for (Team t : draft.getFantasyTeams()) {
            if (t.getStartingPlayers().size() >= 23)
                taxiTime = true;
            else {
                taxiTime = false;
                break;
            }
        }
        
        if (taxiTime) {
            return autoTaxiRequest(gui);
        }
        else {
            return autoRegRequest(gui);
        }
    }
    
    public boolean autoRegRequest(WDK_GUI gui) {
        Draft draft = gui.getDataManager().getDraft();
        
        // GET SIZE OF FREE AGENTS TO CALCULATE RANDOM PLAYER
        int numFreeAgents = gui.getDataManager().getDraft().getFreeAgents().size();
        
        // GET NUMBER TEAMS TO CALCULATE RANDOM TEAM
        int numTeams = gui.getDataManager().getDraft().getFantasyTeams().size();
        
        int numTeamToAdd = 0;
        Team teamToAdd = gui.getDataManager().getDraft().getFantasyTeams().get(numTeamToAdd);
        
        // FIND RANDOM TEAM THAT FITS THE <23 PLAYER REQUIREMENT
        boolean goodTeam = false;
        try {
            while (goodTeam == false) {
                // CHECK NUM PLAYERS
                if (teamToAdd.getStartingPlayers().size() == 23) {
                    numTeamToAdd += 1;
                    teamToAdd = gui.getDataManager().getDraft().getFantasyTeams().get(numTeamToAdd);
        
                    goodTeam = false;
                }
                else {
                    goodTeam = true;
                }
            }
        }
        catch(IndexOutOfBoundsException iobe) {
            return true;
        }
        
        // FIND RANDOM PLAYER THAT FITS THE POSITIONS REQUIREMENT
        boolean goodPlayer = false;
        ArrayList<String> eligiblePos = new ArrayList<String>();
        ArrayList<String> positions = new ArrayList<String>();
        int numPlayerToAdd;
        Player playerToAdd = gui.getDataManager().getDraft().getFreeAgents().get(0);
        while (goodPlayer == false) {
            if (eligiblePos.isEmpty()) {
                // GET RANDOM PLAYER
                numPlayerToAdd = (int)(Math.random()*numFreeAgents);
                playerToAdd = gui.getDataManager().getDraft().getFreeAgents().get(numPlayerToAdd);
                positions.clear();
                positions.addAll(playerToAdd.getPositions());
                
                // CHECK POSITIONS
                ObservableList<String> oP = teamToAdd.openPositions();
                for (String s : positions) {
                    if(oP.contains(s)) {
                        eligiblePos.add(s);
                    }
                }
            }
            else {
                goodPlayer = true;
            }
        }
        
        // EDIT PLAYER INTO THE TEAM
        
        
        // POSITION
        playerToAdd.setCurrentPosition(eligiblePos.get(0));
        
        // CONTRACT
        playerToAdd.setContract("S2");
        
        // SALARY
        playerToAdd.setSalary("1");
        
        // FANTASY TEAM
        int teamPlace = draft.getFantasyTeams().indexOf(teamToAdd);
            if (teamPlace != -1) {  
                draft.getFantasyTeams().get(teamPlace).addStartingPlayer(playerToAdd);
                draft.getFreeAgents().remove(playerToAdd);
                playerToAdd.setFantasyTeam(draft.getFantasyTeams().get(teamPlace));
                draft.getFantasyTeams().get(teamPlace).addPosition(playerToAdd.getCurrentPosition());
                       
                draft.addDraftedPlayer(playerToAdd);
            }
        
       return false;
        
                    
        
    }
    
    public boolean autoTaxiRequest(WDK_GUI gui) {
        Draft draft = gui.getDataManager().getDraft();
        
        // GET SIZE OF FREE AGENTS TO CALCULATE RANDOM PLAYER
        int numFreeAgents = gui.getDataManager().getDraft().getFreeAgents().size();
        
        // GET NUMBER TEAMS TO CALCULATE RANDOM TEAM
        int numTeams = gui.getDataManager().getDraft().getFantasyTeams().size();
        
        int numTeamToAdd = (int)(Math.random()*numTeams);
        Team teamToAdd = gui.getDataManager().getDraft().getFantasyTeams().get(numTeamToAdd);
        
        // FIND RANDOM TEAM THAT FITS THE <23 PLAYER REQUIREMENT
        /*
        boolean goodTeam = false;
        try {
            while (goodTeam == false) {
                // CHECK NUM PLAYERS
                if (teamToAdd.getStartingPlayers().size() == 23) {
                    numTeamToAdd += 1;
                    teamToAdd = gui.getDataManager().getDraft().getFantasyTeams().get(numTeamToAdd);
        
                    goodTeam = false;
                }
                else {
                    goodTeam = true;
                }
            }
        }
        catch(IndexOutOfBoundsException iobe) {
            return true;
        }
        */
        
        // FIND RANDOM PLAYER THAT FITS THE POSITIONS REQUIREMENT
        boolean goodPlayer = false;
        ArrayList<String> eligiblePos = new ArrayList<String>();
        ArrayList<String> positions = new ArrayList<String>();
        int numPlayerToAdd;
        Player playerToAdd = gui.getDataManager().getDraft().getFreeAgents().get(0);
        while (goodPlayer == false) {
            if (eligiblePos.isEmpty()) {
                // GET RANDOM PLAYER
                numPlayerToAdd = (int)(Math.random()*numFreeAgents);
                playerToAdd = gui.getDataManager().getDraft().getFreeAgents().get(numPlayerToAdd);
                positions.clear();
                positions.addAll(playerToAdd.getPositions());
                
                // CHECK POSITIONS
                ObservableList<String> oP = teamToAdd.openPositions();
                for (String s : positions) {
                    if(oP.contains(s)) {
                        eligiblePos.add(s);
                    }
                }
            }
            else {
                goodPlayer = true;
            }
        }
        
        // EDIT PLAYER INTO THE TEAM
        
        
        // POSITION
        playerToAdd.setCurrentPosition(eligiblePos.get(0));
        
        // CONTRACT
        playerToAdd.setContract("S2");
        
        // SALARY
        playerToAdd.setSalary("1");
        
        // FANTASY TEAM
        int teamPlace = draft.getFantasyTeams().indexOf(teamToAdd);
            if (teamPlace != -1) {  
                draft.getFantasyTeams().get(teamPlace).addStartingPlayer(playerToAdd);
                draft.getFreeAgents().remove(playerToAdd);
                playerToAdd.setFantasyTeam(draft.getFantasyTeams().get(teamPlace));
                draft.getFantasyTeams().get(teamPlace).addPosition(playerToAdd.getCurrentPosition());
                       
                draft.addDraftedPlayer(playerToAdd);
            }
        
       return false;
        
                    
        
    }
    
    public void handlePlayRequest(WDK_GUI gui) {
        // SET UP THE TASK FOR ADDING PLAYERS TO TEAMS
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                done = false;
                while (done != true) {
                    // ADD THE PLAYER
                    done = handleAutoAddRequest(gui);
                    
                    // MAKE IT SLEEP
                    Thread.sleep(500);
                    
                }
                return null;
            }
        };
        
        thread = new Thread(task);
        thread.start();
    }
    
    public void handlePauseRequest(WDK_GUI gui) {
        done = true;
    }
    
}
