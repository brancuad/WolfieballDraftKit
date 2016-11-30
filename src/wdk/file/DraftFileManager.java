package wdk.file;

import wdk.data.Draft;
import wdk.data.Player;
import java.io.IOException;
import java.util.List;
import javafx.collections.ObservableList;

/**
 * This interface provides an abstraction of what a file manager should do.
 * Note that file managers know how to read and write drafts and players, 
 * but not how to export sites.
 * 
 * @author Brandon
 */
public interface DraftFileManager {
    public void                 saveDraft(Draft draftToSave) throws IOException;
    public void                 loadDraft(Draft draftToLoad, String draftPath) throws IOException;
    public void                 savePlayers(List<Object> players, String filePath) throws IOException;
    public ObservableList<Player>    loadPlayers(String pitcherFilePath, String hitterFilePath) throws IOException;
}
