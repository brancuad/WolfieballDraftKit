package wdk.gui;

import wdk.data.Player;
import static wdk.gui.WDK_GUI.CLASS_HEADING_LABEL;
import static wdk.gui.WDK_GUI.CLASS_PROMPT_LABEL;
import static wdk.gui.WDK_GUI.PRIMARY_STYLE_SHEET;
import wdk.data.DraftDataManager;
import wdk.data.Draft;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import properties_manager.PropertiesManager;
import wdk.WDK_PropertyType;
import wdk.data.Hitter;
import wdk.data.Pitcher;
import wdk.data.Team;


/**
 *
 * @author Brandon
 */
public class TeamDialog extends Stage {
    // THIS IS THE OBJECT DATA BEHIND THIS UI
    Team team;
    
    Draft draft;
    
    // FOR INTERACTING WITH DRAFT
    DraftDataManager draftManager;
    
    // FOR PROPERTIES
    PropertiesManager props = PropertiesManager.getPropertiesManager();
    
    // GUI CONTROLS FOR OUR DIALOG
    GridPane gridPane;
    Scene dialogScene;
    Label headingLabel;
    Label teamNameLabel;
    Label teamOwnerLabel;
    TextField teamNameTextField;
    TextField teamOwnerTextField;
    
    Button completeButton;
    Button cancelButton;
    
    // THIS IS FOR KEEPING TRACK OF WHICH BUTTON THE USER PRESSED
    String selection;
    
    // CONSTANTS FOR OUR UI
    public static final String COMPLETE = "Complete";
    public static final String CANCEL = "Cancel";
    public static final String TEAM_NAME_PROMPT = "Name: ";
    public static final String TEAM_OWNER_PROMPT = "Owner: ";
    public static final String TEAM_HEADING = "Fantasy Team Details";
    public static final String ADD_TEAM_TITLE = "Add New Fantasy Team";
    public static final String EDIT_TEAM_TITLE = "Edit Fantasy Team";
    
    /**
     * Initializes this dialog so that it can be used for either adding
     * new teams or editing existing ones.
     * 
     * @param primaryStage The owner of everything here
     * @param draft The draft for this
     */
    public TeamDialog(Stage primaryStage, Draft draft) {
        // FIRST MAKE OUR TEAM AND INITIALIZE
        // IT WITH DEFAULT VALUES
        team = new Team();
        this.draft = draft;
        
        // MAKE THIS DIALOG MODAL, WHICH MEANS OTHERS WILL WAIT
        // FOR IT WHEN IT IS DISPLAYED
        initModality(Modality.WINDOW_MODAL);
        initOwner(primaryStage);
        
        gridPane = new GridPane();
    }
    
    public void initAddTeamDialog() {
        
        // PUT THE HEADING IN THE GRID, NOTE THAT THE TEXT WILL DEPEND
        // ON WHETHER WE'RE ADDING OR EDITING
        headingLabel = new Label(TEAM_HEADING);
        headingLabel.getStyleClass().add(CLASS_HEADING_LABEL);
        
        // NOW THE NAME
        teamNameLabel = new Label(TEAM_NAME_PROMPT);
        teamNameLabel.getStyleClass().add(CLASS_PROMPT_LABEL);
        teamNameTextField = new TextField();
        teamNameTextField.textProperty().addListener((obervable, oldValue, newValue) -> {
            team.setTeamName(newValue);
        });
        
        // NOW THE OWNER
        teamOwnerLabel = new Label(TEAM_OWNER_PROMPT);
        teamOwnerLabel.getStyleClass().add(CLASS_PROMPT_LABEL);
        teamOwnerTextField = new TextField();
        teamOwnerTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            team.setOwnerName(newValue);
        });
        
        // COMPLETE OR CANCEL
        completeButton = new Button(COMPLETE);
        cancelButton = new Button(CANCEL);
        
        // REGISTER EVENT HANDLERS FOR THE BUTTONS
        EventHandler completeCancelHandler = (EventHandler<ActionEvent>) (ActionEvent ae) -> {
            Button sourceButton = (Button)ae.getSource();
            TeamDialog.this.selection = sourceButton.getText();
            TeamDialog.this.hide();
        };
        completeButton.setOnAction(completeCancelHandler);
        cancelButton.setOnAction(completeCancelHandler);
        
        // PUT EVERYTHING IN THE GRIDPANE
        gridPane = new GridPane();
        gridPane.add(headingLabel, 1, 1, 2, 1);
        gridPane.add(teamNameLabel, 1, 2, 1, 1);
        gridPane.add(teamNameTextField, 2, 2, 1, 1);
        gridPane.add(teamOwnerLabel, 1, 3, 1, 1);
        gridPane.add(teamOwnerTextField, 2, 3, 1, 1);
        gridPane.add(completeButton, 1, 4, 1, 1);
        gridPane.add(cancelButton, 2, 4, 1, 1);
        
        gridPane.setPadding(new Insets(10, 20, 20, 20));
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        
        // RETURN GRID PANE
        dialogScene = new Scene(gridPane);
        dialogScene.getStylesheets().add(PRIMARY_STYLE_SHEET);
        this.setScene(dialogScene);
    }
    
    /**
     * Accessor method for getting the selection the user made.
     * 
     * @return Either YES, NO, or CANCEL, depending on which button the
     * user selected when this dialog was presented.
     */
    public String getSelection() {
        return selection;
    }
    
    public Team getTeam() {
        return team;
    }
    
    /**
     * This method loads a custom message into the label and then
     * pops open the dialog.
     * 
     * @param message Message to appear inside the dialog.
     */
    public Team showAddTeamDialog() {
        // SET THE DIALOG TITLE
        setTitle(ADD_TEAM_TITLE);
        
        // RESET THE TEAM OBJECT WITH DEFAULT VALUES
        team = new Team();
        
        // LOAD THE UI STUFF
        initAddTeamDialog();
        
        // OPEN IT
        this.showAndWait();
        
        return team;
    }
    
    public Team showEditTeamDialog(Team teamToEdit) {
        // SET THE DIALOG TITLE
        setTitle(EDIT_TEAM_TITLE);
        
        // SHOW DIALOG
        initAddTeamDialog();
        
        // LOAD GUI OF CURRENT Team
        loadGUIData(teamToEdit);
        
        // OPEN IT
        this.showAndWait();
        
        return team;
    }
    
    public boolean wasCompleteSelected() {
        return selection.equals(COMPLETE);
    }
    
    public void loadGUIData(Team t) {
        // LOAD THE UI STUFF
        String temp = t.getTeamName();
        teamNameTextField.setText(temp);
        
        temp = t.getOwnerName();
        teamOwnerTextField.setText(temp);
    }
    
}
