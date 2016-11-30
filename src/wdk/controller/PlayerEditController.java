package wdk.controller;

import wdk.data.Draft;
import javafx.stage.Stage;
import properties_manager.PropertiesManager;
import static wdk.WDK_PropertyType.*;
import wdk.data.DraftDataManager;
import wdk.data.Player;
import wdk.gui.PlayerDialog;
import wdk.gui.WDK_GUI;
import wdk.gui.YesNoCancelDialog;
import wdk.gui.MessageDialog;

/**
 * The Controller Class for managing Players
 * 
 * @author Brandon
 */
public class PlayerEditController {
    PlayerDialog pd;
    MessageDialog messageDialog;
    YesNoCancelDialog yesNoCancelDialog;
    
    public PlayerEditController(Stage initPrimaryStage, Draft draft, MessageDialog initMessageDialog, YesNoCancelDialog initYesNoCancelDialog) {
        pd = new PlayerDialog(initPrimaryStage, draft);
        messageDialog = initMessageDialog;
        yesNoCancelDialog = initYesNoCancelDialog;
    }
    
    // THESE ARE FOR PLAYERS
        
    public void handleAddPlayerRequest(WDK_GUI gui) {
        DraftDataManager draftManager = gui.getDataManager();
        Draft draft = draftManager.getDraft();
        Player p = pd.showAddPlayerDialog();
        
        // DID THE USER CONFIRM?
        if (pd.wasCompleteSelected()) {
            // AND ADD IT AS A ROW TO THE TABLE
            draft.addPlayer(p);
            draft.addFreeAgent(p);
            draft.resetVisiblePlayers();
            // THE DRAFT IS NOW DIRTY, MEANING IT HAS BEEN CHANGED
            // SINCE IT WAS LAST SAVED, SO MAKE SURE
            // THE SAVE BUTTON IS ENABLED
            gui.getFileController().markAsEdited(gui);
        }
        else {
            // THE USER MUST HAVE PRESSED CANCEL, SO WE DO NOTHING
        }
    }
    
    public void handleRemovePlayerRequest(WDK_GUI gui, Player playerToRemove) {
        // PROMPT THE USER TO SAVE UNSAVED WORK
        yesNoCancelDialog.show(PropertiesManager.getPropertiesManager().getProperty(REMOVE_PLAYER_MESSAGE));
        
        // AND NOW GET THE USER'S SELECTION
        String selection = yesNoCancelDialog.getSelection();
        
        // IF THE USER SAID YES, THEN REMOVE IT
        if (selection.equals(YesNoCancelDialog.YES)) {
            gui.getDataManager().getDraft().removePlayer(playerToRemove);
            gui.getDataManager().getDraft().removeFreeAgent(playerToRemove);
            gui.getDataManager().getDraft().removeVisiblePlayer(playerToRemove);
        }
    }
    
    public void handleEditPlayerRequest(WDK_GUI gui, Player playerToEdit) {
        DraftDataManager ddm = gui.getDataManager();
        Draft draft = ddm.getDraft();
        pd.showEditPlayerDialog(playerToEdit);
        
        // DID THE USER CONFIRM?
        if (pd.wasCompleteSelected()) {
            // UPDATE THE PLAYER
            pd.updateInfo(playerToEdit);
            

            
            // DRAFT IS DIRTY
            gui.getFileController().markAsEdited(gui);
        }
        else {
            // DO NOTHING BECAUSE CANCEL WAS SELECTED
        }
        
    }
    
}
