package wdk.gui;

import java.util.ArrayList;
import wdk.data.Player;
import static wdk.gui.WDK_GUI.CLASS_HEADING_LABEL;
import static wdk.gui.WDK_GUI.CLASS_PROMPT_LABEL;
import static wdk.gui.WDK_GUI.PRIMARY_STYLE_SHEET;
import wdk.data.DraftDataManager;
import wdk.data.Draft;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import properties_manager.PropertiesManager;
import wdk.WDK_PropertyType;
import wdk.data.Hitter;
import wdk.data.Pitcher;
import static wdk.WDK_StartupConstants.PATH_IMAGES;
import wdk.data.Team;
import static wdk.gui.WDK_GUI.CLASS_SUBHEADING_LABEL;

/**
 *
 * @author Brandon
 */
public class PlayerDialog extends Stage {
    // THIS IS THE OBJECT DATA BEHIND THIS UI
    Player player;
    
    // FOR INTERACTING WITH DRAFT
    DraftDataManager draftManager;
    
    // FOR PROPERTIES
    PropertiesManager props = PropertiesManager.getPropertiesManager();
    
    // GUI CONTROLS FOR OUR DIALOG
    GridPane gridPane;
    Scene dialogScene;
    Label headingLabel;
    Label firstNameLabel;
    Label lastNameLabel;
    Label teamLabel;
    Label fantasyTeamLabel;
    Label positionLabel;
    Label contractLabel;
    Label salaryLabel;
    TextField firstNameTextField;
    TextField lastNameTextField;
    ComboBox teamComboBox;
    HBox positionsBox;
    CheckBox CButton;
    CheckBox firstButton;
    CheckBox thirdButton;
    CheckBox secondButton;
    CheckBox ssButton;
    CheckBox ofButton;
    CheckBox pButton;
    
    Draft draft;
    
    // EDIT PLAYER STUFF
    ImageView playerImageView;
    Image playerImage;
    ImageView nationImageView;
    Image nationImage;
    Label playerNameLabel;
    Label playerPositionsLabel;
    
    
    ComboBox fantasyTeamComboBox;
    ComboBox positionComboBox;
    ComboBox contractComboBox;
    TextField salaryTextField;
    Button completeButton;
    Button cancelButton;
    
    // THIS IS FOR KEEPING TRACK OF WHICH BUTTON THE USER PRESSED
    String selection;
    
    // CONSTANTS FOR OUR UI
    public static final String COMPLETE = "Complete";
    public static final String CANCEL = "Cancel";
    public static final String FIRST_NAME_PROMPT = "First Name: ";
    public static final String LAST_NAME_PROMPT = "Last Name: ";
    public static final String TEAM_PROMPT = "Pro Team: ";
    public static final String FANTASY_TEAM_PROMPT = "Fantasy Team: ";
    public static final String POSITION_PROMPT = "Position: ";
    public static final String CONTRACT_PROMPT = "Contract: ";
    public static final String SALARY_PROMPT = "Salary ($): ";
    public static final String PLAYER_HEADING = "Player Details";
    public static final String ADD_PLAYER_TITLE = "Add New Player";
    public static final String EDIT_PLAYER_TITLE = "Edit Player";

    
    /**
     * Initializes this dialog so that it can be used for either adding
     * new players or editing existing ones.
     * 
     * @param primaryStage The owner of everything here
     * @param draft The draft for this 
     */
    public PlayerDialog(Stage primaryStage, Draft draft) {
        // FIRST MAKE OUR PLAYER AND INITIALIZE
        // IT WITH DEFAULT VALUES
        player = new Player();
        this.draft = draft;
        
        // MAKE THIS DIALOG MODAL, WHICH MEANS OTHERS WILL WAIT
        // FOR IT WHEN IT IS DISPLAYED
        initModality(Modality.WINDOW_MODAL);
        initOwner(primaryStage);
        
        // FIRST OUR CONTAINER
        gridPane = new GridPane();
    }
    
    public void initAddPlayerDialog() {
        // PUT THE HEADING IN THE GRID, NOTE THAT THE TEXT WILL DEPEND
        // ON WHETHER WE'RE ADDING OR EDITING
        headingLabel = new Label(PLAYER_HEADING);
        headingLabel.getStyleClass().add(CLASS_HEADING_LABEL);
        
        // NOW THE FIRST NAME
        firstNameLabel = new Label(FIRST_NAME_PROMPT);
        firstNameLabel.getStyleClass().add(CLASS_PROMPT_LABEL);
        firstNameTextField = new TextField();
        firstNameTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            player.setFirstName(newValue);
        });
        
        // NOW THE LAST NAME
        lastNameLabel = new Label(LAST_NAME_PROMPT);
        lastNameLabel.getStyleClass().add(CLASS_PROMPT_LABEL);
        lastNameTextField = new TextField();
        lastNameTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            player.setLastName(newValue);
        });
        
        // MLB TEAM BOX
        teamLabel = new Label(TEAM_PROMPT);
        teamLabel.getStyleClass().add(CLASS_PROMPT_LABEL);
        teamComboBox = new ComboBox();
        teamComboBox.getItems().addAll(draft.getTeams());
        teamComboBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
          @Override
          public void changed(ObservableValue observable, Object oldValue, Object newValue) {
              player.setTeam((String) newValue);
          }
        });
        
        // POSITIONS BUTTONS
        positionsBox = new HBox();
        CButton = initChildCheckBox(positionsBox, WDK_PropertyType.C_LABEL);
        firstButton = initChildCheckBox(positionsBox, WDK_PropertyType.FIRST_LABEL);
        thirdButton = initChildCheckBox(positionsBox, WDK_PropertyType.THIRD_LABEL);
        secondButton = initChildCheckBox(positionsBox, WDK_PropertyType.SECOND_LABEL);
        ssButton = initChildCheckBox(positionsBox, WDK_PropertyType.SS_LABEL);
        ofButton = initChildCheckBox(positionsBox, WDK_PropertyType.OF_LABEL);
        pButton = initChildCheckBox(positionsBox, WDK_PropertyType.P_LABEL);
        
        EventHandler checkBoxHandler = (EventHandler<ActionEvent>) (ActionEvent ae) -> {
            CheckBox chk = (CheckBox) ae.getSource();
            updatePositions(chk);
        };
        
        CButton.setOnAction(checkBoxHandler);
        firstButton.setOnAction(checkBoxHandler);
        thirdButton.setOnAction(checkBoxHandler);
        secondButton.setOnAction(checkBoxHandler);
        ssButton.setOnAction(checkBoxHandler);
        ofButton.setOnAction(checkBoxHandler);
        pButton.setOnAction(checkBoxHandler);
        
        // COMPLETE OR CANCEL
        completeButton = new Button(COMPLETE);
        cancelButton = new Button(CANCEL);
        
        // REGISTER EVENT HANDLERS FOR THE BUTTONS
        EventHandler completeCancelHandler = (EventHandler<ActionEvent>) (ActionEvent ae) -> {
            Button sourceButton = (Button)ae.getSource();
            PlayerDialog.this.selection = sourceButton.getText();
            PlayerDialog.this.hide();
        };
        completeButton.setOnAction(completeCancelHandler);
        cancelButton.setOnAction(completeCancelHandler);
        
        // PUT EVERYTHING IN THE GRIDPANE
        gridPane = new GridPane();
        gridPane.add(headingLabel, 1, 1, 2, 1);
        gridPane.add(firstNameLabel, 1, 2, 1, 1);
        gridPane.add(firstNameTextField, 2, 2, 1, 1);
        gridPane.add(lastNameLabel, 1, 3, 1, 1);
        gridPane.add(lastNameTextField, 2, 3, 1, 1);
        gridPane.add(teamLabel, 1, 4, 1, 1);
        gridPane.add(teamComboBox, 2, 4, 1, 1);
        gridPane.add(positionsBox, 1, 5, 2, 1);
        gridPane.add(completeButton, 1, 6, 1, 1);
        gridPane.add(cancelButton, 2, 6, 1, 1);
        
        gridPane.setPadding(new Insets(10, 20, 20, 20));
        positionsBox.setSpacing(10);
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
    
    public Player getPlayer() {
        return player;
    }
    
    /**
     * This method loads a custom message into the label and then
     * pops open the dialog.
     * 
     * @param message Message to appear inside the dialog.
     */
    public Player showAddPlayerDialog() {
        // SET THE DIALOG TITLE
        setTitle(ADD_PLAYER_TITLE);
        
        // RESET THE PLAYER OBJECT WITH DEFAULT VALUES
        player = new Player();
        
        // LOAD THE UI STUFF
        initAddPlayerDialog();
        
        // OPEN IT
        this.showAndWait();
        
        if (player.getQP().equals("P_")) {
            Pitcher p = new Pitcher();
            p.setFirstName(player.getFirstName());
            p.setLastName(player.getLastName());
            p.setQP(player.getQP().substring(0, player.getQP().length()-1));
            p.setTeam(player.getTeam());
            return p;
        }
        else {
            Hitter h = new Hitter();
            h.setFirstName(player.getFirstName());
            h.setLastName(player.getLastName());
            h.setQP(player.getQP());
            h.setTeam(player.getTeam());
            return h;
        }
    }
    
    public void initEditPlayerDialog(Player playerToEdit) {

        
        // PUT THE HEADING IN THE GRID, NOTE THAT THE TEXT WILL DEPEND
        // ON WHETHER WE'RE ADDING OR EDITING
        headingLabel = new Label(PLAYER_HEADING);
        headingLabel.getStyleClass().add(CLASS_HEADING_LABEL);
        
        // NOW THE IMAGE OF THE PLAYER
        String playerImagePath = "file:" + PATH_IMAGES + playerToEdit.getLastName() + playerToEdit.getFirstName() + ".jpg";
        playerImage = new Image(playerImagePath);
        playerImageView = new ImageView(playerImage);
        
        // NOW THE IMAGE OF THE FLAG
        String nationImagePath = "file:" + PATH_IMAGES + playerToEdit.getNationOfBirth() + ".png";
        nationImage = new Image(nationImagePath);
        nationImageView = new ImageView(nationImage);
        
        // NOW THE PLAYER NAME LABEL
        playerNameLabel = new Label(playerToEdit.getFirstName() + " " + playerToEdit.getLastName());
        playerNameLabel.getStyleClass().add(CLASS_SUBHEADING_LABEL);
        
        // NOW THE PLAYER POSITIONS LABEL
        playerPositionsLabel = new Label(playerToEdit.getQP());
        playerPositionsLabel.getStyleClass().add(CLASS_SUBHEADING_LABEL);
        
        // NOW THE FANTASY TEAM
        fantasyTeamLabel = new Label(FANTASY_TEAM_PROMPT);
        fantasyTeamLabel.getStyleClass().add(CLASS_PROMPT_LABEL);
        fantasyTeamComboBox = new ComboBox();
        fantasyTeamComboBox.getItems().add("Free Agent");
        fantasyTeamComboBox.getItems().addAll(draft.getFantasyTeamNames());
        fantasyTeamComboBox.getSelectionModel().select("Free Agent");
        fantasyTeamComboBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                initPositionComboBox(playerToEdit);
            }
        });
        
        // NOW THE POSITION
        positionLabel = new Label(POSITION_PROMPT);
        positionLabel.getStyleClass().add(CLASS_PROMPT_LABEL);
        positionComboBox = new ComboBox();
        
        // FIND TEAM ON COMBOBOX
        initPositionComboBox(playerToEdit);
        
        // NOW THE CONTRACT
        contractLabel = new Label(CONTRACT_PROMPT);
        contractLabel.getStyleClass().add(CLASS_PROMPT_LABEL);
        contractComboBox = new ComboBox();
        contractComboBox.getItems().addAll("S2", "S1", "X");
        
        // NOW THE SALARY
        salaryLabel = new Label(SALARY_PROMPT);
        salaryLabel.getStyleClass().add(CLASS_PROMPT_LABEL);
        salaryTextField = new TextField();
        
        // NOW THE BUTTONS
        completeButton = new Button(COMPLETE);
        cancelButton = new Button(CANCEL);
        
        // REGISTER EVENT HANDLERS FOR OUR BUTTONS
        EventHandler completeCancelHandler = (EventHandler<ActionEvent>) (ActionEvent ae) -> {
            Button sourceButton = (Button)ae.getSource();
            PlayerDialog.this.selection = sourceButton.getText();
            String tempName = (String)fantasyTeamComboBox.getSelectionModel().getSelectedItem();
            int teamIndex = draft.getFantasyTeamNames().indexOf(tempName);
            Team temp = new Team();
            if (teamIndex >= 0)
                temp = draft.getFantasyTeams().get(teamIndex);
            int playersNeeded = 23 - temp.getStartingPlayers().size();
            int moneyResult = 0;
            if (!salaryTextField.getText().equals(""))
                moneyResult = Integer.parseInt(temp.getMoneyLeft()) - 
                    Integer.parseInt(salaryTextField.getText());
                
                
            if (PlayerDialog.this.selection.equals(COMPLETE) ) {
                // CHECK IF BOXES HAVE BEEN INITIALIZED
                if (fantasyTeamComboBox.getSelectionModel().getSelectedItem() == null || 
                        positionComboBox.getSelectionModel().getSelectedItem() == null ||
                        contractComboBox.getSelectionModel().getSelectedItem() == null ||
                        salaryTextField.getText().equals("")) {
                    MessageDialog incomplete = new MessageDialog(PlayerDialog.this, "Close");
                    incomplete.show("Initialize every field.");
                }
                // CHECK IF SALARY IS TOO HIGH
                else if (moneyResult < playersNeeded  && temp.getStartingPlayers().size() < 23) {
                    MessageDialog tooMuchMoney = new MessageDialog(PlayerDialog.this, "Close");
                    tooMuchMoney.show("You are paying too much for this player!");
                }
                else {
                    PlayerDialog.this.hide();
                }
            }
            else {
                PlayerDialog.this.hide();
            }
        };
        completeButton.setOnAction(completeCancelHandler);
        cancelButton.setOnAction(completeCancelHandler);
        
        // NOW WE PUT THEM IN THE GRID PANE
        gridPane = new GridPane();
        gridPane.add(headingLabel, 1, 1, 2, 1);
        gridPane.add(playerImageView, 1, 2, 1, 3);
        gridPane.add(nationImageView, 2, 2, 1, 1);
        gridPane.add(playerNameLabel, 2, 3, 1, 1);
        gridPane.add(playerPositionsLabel, 2, 4, 1, 1);
        gridPane.add(fantasyTeamLabel, 1, 5, 1, 1);
        gridPane.add(fantasyTeamComboBox, 2, 5, 1, 1);
        gridPane.add(positionLabel, 1, 6, 1, 1);
        gridPane.add(positionComboBox, 2, 6, 1, 1);
        gridPane.add(contractLabel, 1, 7, 1, 1);
        gridPane.add(contractComboBox, 2, 7, 1, 1);
        gridPane.add(salaryLabel, 1, 8, 1, 1);
        gridPane.add(salaryTextField, 2, 8, 1, 1);
        gridPane.add(completeButton, 1, 9, 1, 1);
        gridPane.add(cancelButton, 2, 9, 1, 1);
        
        gridPane.setPadding(new Insets(10, 20, 20, 20));
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        
        // PUT CURRENT INFO INTO GUI
        loadGUIData(playerToEdit);
        
        // RETURN GRID PANE
        dialogScene = new Scene(gridPane);
        dialogScene.getStylesheets().add(PRIMARY_STYLE_SHEET);
        this.setScene(dialogScene);
  
    }
    
    /**
     * This method loads a custom message into the label and then
     * pops open the dialog.
     * 
     * @param message Message to appear inside the dialog.
     */
    public Player showEditPlayerDialog(Player playerToEdit) {
        // SET THE DIALOG TITLE
        setTitle(EDIT_PLAYER_TITLE);
               
        // LOAD THE UI STUFF
        initEditPlayerDialog(playerToEdit);
        
        // OPEN IT
        this.showAndWait();
        
        return playerToEdit;
    }
    
    public void loadGUIData(Player p) {
        // LOAD THE UI STUFF
        String t = p.getFantasyTeam().getTeamName();
        fantasyTeamComboBox.getSelectionModel().select(t);
        
        t = p.getCurrentPosition();
        positionComboBox.getSelectionModel().select(t);
        
        t = p.getContract();
        contractComboBox.getSelectionModel().select(t);
        
        t = p.getSalary();
        salaryTextField.setText(t);
    }

    
    public boolean wasCompleteSelected() {
        return selection.equals(COMPLETE);
    }
    
    // FOR ADDING RADIO BUTTONS TO A TOGGLEGROUP
    private CheckBox initChildCheckBox(HBox box, WDK_PropertyType labelProperty) {
        CheckBox cB = new CheckBox(props.getProperty(labelProperty.toString()));
        box.getChildren().add(cB);
        return cB;
    } 
    
    private void updatePositions(CheckBox chk) {
        String pos = "";
        pos += chk.getText() + "_";
        player.setQP(pos);
    }
    
    private void initPositionComboBox(Player playerToEdit) {
        String t = (String)fantasyTeamComboBox.getSelectionModel().getSelectedItem();
        int teamPlace = draft.getFantasyTeamNames().indexOf(t);
        Team tempTeam = new Team();
        if (teamPlace != -1)
            tempTeam = draft.getFantasyTeams().get(teamPlace);
        ArrayList<String> eligiblePos = new ArrayList<String>();
        ArrayList<String> positions = new ArrayList<String>();
        positions.addAll(playerToEdit.getPositions());
        
        // FIND ELIGIBLE POSITIONS
        if (t.equals("Free Agent"))
            eligiblePos.addAll(positions);
        else {
            ObservableList<String> oP = tempTeam.openPositions();
            for (String s : positions) {
                if(oP.contains(s)) {
                    eligiblePos.add(s);
                }
            }
        }
                
        positionComboBox.getItems().clear();
        positionComboBox.getItems().addAll(eligiblePos);
    }
    
    // SETS THE NEW FANTASY TEAM IF COMPLETE IS SELECTED
    private void handleFantasyTeamSetRequest(Player playerToEdit) {
        String newTeam = (String)fantasyTeamComboBox.getSelectionModel().getSelectedItem();
        if (newTeam.equals("Free Agent")) {
                    if (!draft.getFreeAgents().contains(playerToEdit)) {
                        draft.getVisibleTeam().removeStartingPlayer(playerToEdit);
                        draft.getVisibleTeam().removePosition(playerToEdit.getCurrentPosition());
                        draft.addFreeAgent(playerToEdit);
                        draft.removeDraftedPlayer(playerToEdit);
                        
                        boolean wasS2 = false;
                        if (playerToEdit.getContract().equals("S2")) {
                            wasS2 = true;
                        }
                        if (!wasS2 && playerToEdit.getContract().equals("S2")) {
                            draft.addDraftedPlayer(playerToEdit);
                        }
                    }
                    
                }
        else {
                int teamPlace = draft.getFantasyTeamNames().indexOf(newTeam);
                    if (teamPlace != -1) {  
                        playerToEdit.getFantasyTeam().removeStartingPlayer(playerToEdit);
                        playerToEdit.getFantasyTeam().removePosition(playerToEdit.getCurrentPosition());
                        draft.getFantasyTeams().get(teamPlace).addStartingPlayer(playerToEdit);
                        draft.getFreeAgents().remove(playerToEdit);
                        playerToEdit.setFantasyTeam(draft.getFantasyTeams().get(teamPlace));
                        draft.getFantasyTeams().get(teamPlace).addPosition((String)positionComboBox.getSelectionModel().getSelectedItem());
                       
                        draft.addDraftedPlayer(playerToEdit);
                    }
                }
    }
    
    // SET THE NEW POSITION IF COMPLETE IS SELECTED
    private void handlePositionSetRequest(Player playerToEdit) {
        String position = (String) positionComboBox.getSelectionModel().getSelectedItem();
        playerToEdit.setCurrentPosition(position);
    }
    
    // SET NEW CONTRACT IF COMPLETE IS SELECTED
    private void handleContractSetRequest(Player playerToEdit) {
        String contract = (String) contractComboBox.getSelectionModel().getSelectedItem();
        
        playerToEdit.setContract(contract);
        if (!playerToEdit.getContract().equals("S2")) {
            draft.removeDraftedPlayer(playerToEdit);
        }
        
        
    }
    
    // SET NEW SALARY IF COMPLETE IS SELECTED
    private void handleSalarySetRequest(Player playerToEdit) {
        String salary = (String) salaryTextField.getText();
        playerToEdit.setSalary(salary);
    }
    
    public void updateInfo(Player playerToEdit) {
        handlePositionSetRequest(playerToEdit);
        handleContractSetRequest(playerToEdit);
        handleSalarySetRequest(playerToEdit);
        handleFantasyTeamSetRequest(playerToEdit);
    }
}
