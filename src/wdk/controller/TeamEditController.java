package wdk.controller;

import javafx.stage.Stage;
import wdk.data.Draft;
import properties_manager.PropertiesManager;
import static wdk.WDK_PropertyType.*;
import wdk.gui.WDK_GUI;
import wdk.gui.PlayerDialog;
import wdk.gui.TeamDialog;
import wdk.gui.MessageDialog;
import wdk.gui.YesNoCancelDialog;
import wdk.data.DraftDataManager;
import wdk.data.Team;

/**
 * This controller class handles the responses to all draft editing input,
 * including verification of data and binding of entered data to the Draft
 * object.
 * 
 * @author Brandon
 */
public class TeamEditController {
    PlayerDialog pd;
    TeamDialog td;
    MessageDialog messageDialog;
    YesNoCancelDialog yesNoCancelDialog;
    
    DraftEditController draftController;
    
    public TeamEditController(Stage initPrimaryStage, Draft draft, MessageDialog initMessageDialog, YesNoCancelDialog initYesNoCancelDialog) {
        pd = new PlayerDialog(initPrimaryStage, draft);
        td = new TeamDialog(initPrimaryStage, draft);
        messageDialog = initMessageDialog;
        yesNoCancelDialog = initYesNoCancelDialog;
    }
    
    // THESE ARE FOR FANTASY TEAMS
    
    public void handleAddTeamRequest(WDK_GUI gui) {
        DraftDataManager draftManager = gui.getDataManager();
        Draft draft = draftManager.getDraft();
        Team t = td.showAddTeamDialog();
        
        // DID THE USER CONFIRM?
        if (td.wasCompleteSelected()) {
            // AND SET IT AS THE CURRENT VISIBLE TEAM
            draft.addFantasyTeam(t);
            

            // THE DRAFT IS NOW DIRTY
            gui.getFileController().markAsEdited(gui);
        }
        else {
            // THE USER MUST HAVE PRESSED CANCEL, SO WE DO NOTHING
        }
        
    }
    
    public void handleRemoveTeamRequest(WDK_GUI gui, Team teamToRemove) {
        // PROMPT THE USER TO SAVE UNSAVED WORK
        yesNoCancelDialog.show(PropertiesManager.getPropertiesManager().getProperty(REMOVE_TEAM_MESSAGE));
        
        // AND NOW GET THE USER'S SELECTION
        String selection = yesNoCancelDialog.getSelection();
        
        // IF THE USER SAID YES, THEN REMOVE IT
        if (selection.equals(YesNoCancelDialog.YES)) {
            gui.getDataManager().getDraft().getFreeAgents().addAll(teamToRemove.getStartingPlayers());
            gui.getDataManager().getDraft().getFreeAgents().addAll(teamToRemove.getTaxiPlayers());
            gui.getDataManager().getDraft().removeFantasyTeam(teamToRemove);
            gui.getDataManager().getDraft().removeFantasyTeamName(teamToRemove.getTeamName());
            teamToRemove.reset();
        }
    }
    
    public void handleEditTeamRequest(WDK_GUI gui, Team teamToEdit) {
        DraftDataManager ddm = gui.getDataManager();
        draftController = new DraftEditController();
        Draft draft = ddm.getDraft();
        td.showEditTeamDialog(teamToEdit);
        
        // DID THE USER CONFIRM?
        if (td.wasCompleteSelected()) {
            int i = draft.getFantasyTeams().indexOf(teamToEdit);
            draft.getFantasyTeamNames().remove(i);
            Team t = td.getTeam();
            teamToEdit.setTeamName(t.getTeamName());
            teamToEdit.setOwnerName(t.getOwnerName());
            draft.getFantasyTeamNames().add(t.getTeamName());
            // THE DRAFT IS DIRTY
            gui.getFileController().markAsEdited(gui);
        }
        else {
            // CANCEL, SO DO NOTHING
        }
        
    }
    
    
    
}
