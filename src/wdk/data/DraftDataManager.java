package wdk.data;

import wdk.file.DraftFileManager;
import java.io.IOException;
import wdk.gui.WDK_GUI;

/**
 * This class manages a Draft, which means it knows how to reset one with 
 * default values.
 * 
 * @author Brandon
 */
public class DraftDataManager {
    // THIS IS THE DRAFT BEING EDITED
    Draft draft;
    
    // THIS IS THE UI, WHICH MUST BE UPDATED WHENEVER OUR MODEL'S DATA CHANGES
    DraftDataView view;
    
    // THIS HELPS US LOAD THINGS FOR OUR DRAFT
    DraftFileManager fileManager;
    
    public DraftDataManager(DraftDataView initView) throws IOException {
        view = initView;
        draft = new Draft();
    }
    
    /**
     * Accessor method for getting the Draft that this class manages
     */
    public Draft getDraft() {
        return draft;
    }
    
    /**
     * Accessor method for getting the file manager, which knows how to read
     * and write draft data from/to files
     */
    public DraftFileManager getFileManager() {
        return fileManager;
    }
    
    /**
     * Resets the draft to its default initialized settings, triggering the UI
     * to reflect these changes
     */
    public void reset(WDK_GUI gui) throws IOException{
        // CLEAR ALL THE DRAFT VALUES
        draft.resetPlayers();
        draft.resetVisiblePlayers();
        draft.resetTeams();
        
        // THEN FORCES THE UI TO RELOAD THE UPDATED DRAFT
        gui.reloadDraft(draft);
    }
    
}
