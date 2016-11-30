package wdk.gui;

import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import properties_manager.PropertiesManager;
import wdk.WDK_PropertyType;
import wdk.WDK_StartupConstants.*;
import static wdk.WDK_StartupConstants.PATH_IMAGES;
import wdk.controller.DraftEditController;
import wdk.controller.PlayerEditController;
import wdk.controller.TeamEditController;
import wdk.data.DoubleColumnComparator;
import wdk.data.Player;
import wdk.data.PositionColumnComparator;
import wdk.data.Team;

/**
 *This class is used to initialize the Fantasy Teams Screen of the
 * Wolfieball Draft Kit application. 
 * @author Brandon
 */
public class FantasyTeamsScreen {
    
    // THESE CONSTANTS ARE FOR TYING THE PRESENTATION STYLE OF
    // THIS GUI'S COMPONENTS TO A STYLE SHEET THAT IT USES
    static final String CLASS_BORDERED_PANE = "bordered_pane";
    static final String CLASS_INNER_PANE = "inner_pane";
    static final String CLASS_HEADING_LABEL = "heading_label";
    static final String CLASS_SUBHEADING_LABEL = "subheading_label";
    static final String CLASS_PROMPT_LABEL = "prompt_label";
    static final String EMPTY_TEXT = "";
    static final int LARGE_TEXT_FIELD_LENGTH = 20;
    static final int SMALL_TEXT_FIELD_LENGTH = 5;
    
    // MAIN GUI
    WDK_GUI gui;
    
    // FOR ACCESSING PROPERTIES, 
    PropertiesManager props = PropertiesManager.getPropertiesManager();
    
        
    // FOR HANDLING REQUESTS
    PlayerEditController playerController;
    DraftEditController draftController;
    TeamEditController teamController;
    
    // FOR ORGANIZING THE PLAYERS
    PositionColumnComparator pcc;
    DoubleColumnComparator dcc;
    
    // THE FOLLOWING ARE FOR INITIALIZING THE PAGE WITH A LABEL
    BorderPane fantasyTeamsBorderPane;
    ScrollPane fantasyTeamsPane;
    VBox fantasyTeamsBox;
    Label fantasyTeamsLabel;
    
    // THESE ARE FOR THE ADD, MINUS, EDIT, DRAFT NAME, AND SELECT FIELDS
    HBox draftNameBox;
    HBox buttonsBox;
    GridPane draftControlToolbar;
    Label draftNameLabel;
    TextField draftNameTextField;
    Button addTeamButton;
    Button removeTeamButton;
    Button editTeamButton;
    Label selectFantasyTeamLabel;
    ComboBox fantasyTeamComboBox;
    
    // THESE ARE FOR TABLEVIEWS
    Label startingLineupLabel;
    Label taxiSquadLabel;
    
    VBox startingTableBox;
    TableView<Player> startingPlayersTable;
    TableColumn positionColumn;
    TableColumn firstNameColumn;
    TableColumn lastNameColumn;
    TableColumn teamColumn;
    TableColumn qpColumn;
    TableColumn rwColumn;
    TableColumn hrswColumn;
    TableColumn rbikColumn;
    TableColumn sberaColumn;
    TableColumn bawhipColumn;
    TableColumn evColumn;
    TableColumn contractColumn;
    TableColumn salaryColumn;
    
    VBox taxiTableBox;
    TableView<Player> taxiPlayersTable;
    TableColumn taxiPositionColumn;
    TableColumn taxiFirstNameColumn;
    TableColumn taxiLastNameColumn;
    TableColumn taxiTeamColumn;
    TableColumn taxiQpColumn;
    TableColumn taxiRwColumn;
    TableColumn taxiHrsvColumn;
    TableColumn taxiRbikColumn;
    TableColumn taxiSberaColumn;
    TableColumn taxiBawhipColumn;
    TableColumn taxiEvColumn;
    TableColumn taxiContractColumn;
    TableColumn taxiSalaryColumn;
    
    
    
    public FantasyTeamsScreen(WDK_GUI wdkgui) {
        gui = wdkgui;
    }
    
    public BorderPane initWorkspace() {
        
        // INITIALIZE BORDER PANE WITH LABEL
        fantasyTeamsBorderPane = new BorderPane();
        fantasyTeamsPane = new ScrollPane();
        fantasyTeamsBox = new VBox();
        fantasyTeamsBox.getStyleClass().add(CLASS_INNER_PANE);
        fantasyTeamsLabel = initChildLabel(fantasyTeamsBox, WDK_PropertyType.FANTASY_TEAMS_LABEL, CLASS_HEADING_LABEL);
        fantasyTeamsBorderPane.setCenter(fantasyTeamsPane);
        fantasyTeamsPane.setContent(fantasyTeamsBox);
        fantasyTeamsPane.setFitToWidth(true);
        // fantasyTeamsBorderPane.getStyleClass().add(CLASS_BORDERED_PANE);
        
        // THIS SETS THE TOOLBAR FOR CONTROLS
        draftNameBox = new HBox();
        buttonsBox = new HBox();
        draftControlToolbar = new GridPane();
        addTeamButton = initChildButton(buttonsBox, WDK_PropertyType.ADD_ICON, WDK_PropertyType.ADD_TEAM_TOOLTIP, false);
        removeTeamButton = initChildButton(buttonsBox, WDK_PropertyType.MINUS_ICON, WDK_PropertyType.REMOVE_TEAM_TOOLTIP, false);
        editTeamButton = initChildButton(buttonsBox, WDK_PropertyType.EDIT_ICON, WDK_PropertyType.EDIT_TEAM_TOOLTIP, false);
        selectFantasyTeamLabel = initChildLabel(buttonsBox, WDK_PropertyType.SELECT_FANTASY_TEAM_LABEL, CLASS_SUBHEADING_LABEL);
        fantasyTeamComboBox = new ComboBox();
        fantasyTeamComboBox.setItems(gui.getDataManager().getDraft().getFantasyTeamNames()); 
        fantasyTeamComboBox.getSelectionModel().select(gui.getDataManager().getDraft().getVisibleTeam().getTeamName());
        buttonsBox.getChildren().add(fantasyTeamComboBox);
        buttonsBox.setSpacing(5);
        
        draftNameLabel = initChildLabel(draftNameBox, WDK_PropertyType.DRAFT_NAME_LABEL, CLASS_SUBHEADING_LABEL);
        draftNameTextField = initChildTextField(draftNameBox, LARGE_TEXT_FIELD_LENGTH, EMPTY_TEXT, true);
        draftNameTextField.setText(gui.getDataManager().getDraft().getName());
        
        // PUT THE TABLEVIEW SHOWING VISIBLETEAM
        startingTableBox = new VBox();
        startingPlayersTable = new TableView();
        startingPlayersTable.setEditable(true);
        pcc = new PositionColumnComparator();
        startingTableBox.getChildren().add(startingPlayersTable);
        startingTableBox.getStyleClass().add(CLASS_BORDERED_PANE);
        
        positionColumn = new TableColumn(props.getProperty(WDK_PropertyType.POSITION_LABEL));
        firstNameColumn = new TableColumn(props.getProperty(WDK_PropertyType.FIRST_NAME_LABEL));
        lastNameColumn = new TableColumn(props.getProperty(WDK_PropertyType.LAST_NAME_LABEL));
        teamColumn = new TableColumn(props.getProperty(WDK_PropertyType.TEAM_LABEL));
        qpColumn = new TableColumn(props.getProperty(WDK_PropertyType.QP_LABEL));
        rwColumn = new TableColumn(props.getProperty(WDK_PropertyType.R_LABEL) + "/" + props.getProperty(WDK_PropertyType.W_LABEL));
        hrswColumn = new TableColumn(props.getProperty(WDK_PropertyType.HR_LABEL) + "/" + props.getProperty(WDK_PropertyType.SW_LABEL));
        rbikColumn = new TableColumn(props.getProperty(WDK_PropertyType.RBI_LABEL) + "/" + props.getProperty(WDK_PropertyType.K_LABEL));
        sberaColumn = new TableColumn(props.getProperty(WDK_PropertyType.SB_LABEL) + "/" + props.getProperty(WDK_PropertyType.ERA_LABEL));
        bawhipColumn = new TableColumn(props.getProperty(WDK_PropertyType.BA_LABEL) + "/" + props.getProperty(WDK_PropertyType.WHIP_LABEL));
        evColumn = new TableColumn(props.getProperty(WDK_PropertyType.EV_LABEL));
        contractColumn = new TableColumn(props.getProperty(WDK_PropertyType.CONTRACT_LABEL));
        salaryColumn = new TableColumn(props.getProperty(WDK_PropertyType.SALARY_LABEL));
        
        // AND LINK THE COLUMNS TO THE DATA
        positionColumn.setCellValueFactory(new PropertyValueFactory<String, String>("currentPosition"));
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<String, String>("firstName"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<String, String>("lastName"));
        teamColumn.setCellValueFactory(new PropertyValueFactory<String, String>("team"));
        qpColumn.setCellValueFactory(new PropertyValueFactory<String, String>("QP"));
        rwColumn.setCellValueFactory(new PropertyValueFactory<String, String>("R"));
        hrswColumn.setCellValueFactory(new PropertyValueFactory<String, String>("HR"));
        rbikColumn.setCellValueFactory(new PropertyValueFactory<String, String>("RBI"));
        sberaColumn.setCellValueFactory(new PropertyValueFactory<String, String>("SB"));
        bawhipColumn.setCellValueFactory(new PropertyValueFactory<String, String>("BA"));
        evColumn.setCellValueFactory(new PropertyValueFactory<String, String>("EstimatedValue"));
        contractColumn.setCellValueFactory(new PropertyValueFactory<String, String>("contract"));
        salaryColumn.setCellValueFactory(new PropertyValueFactory<String, String>("salary"));
        
        positionColumn.setComparator(pcc);
        rwColumn.setComparator(dcc);
        hrswColumn.setComparator(dcc);
        rbikColumn.setComparator(dcc);
        sberaColumn.setComparator(dcc);
        bawhipColumn.setComparator(dcc);
        
        startingPlayersTable.getColumns().add(positionColumn);
        startingPlayersTable.getColumns().add(firstNameColumn);
        startingPlayersTable.getColumns().add(lastNameColumn);
        startingPlayersTable.getColumns().add(teamColumn);
        startingPlayersTable.getColumns().add(qpColumn);
        startingPlayersTable.getColumns().add(rwColumn);
        startingPlayersTable.getColumns().add(hrswColumn);
        startingPlayersTable.getColumns().add(rbikColumn);
        startingPlayersTable.getColumns().add(sberaColumn);
        startingPlayersTable.getColumns().add(bawhipColumn);
        startingPlayersTable.getColumns().add(evColumn);
        startingPlayersTable.getColumns().add(contractColumn);
        startingPlayersTable.getColumns().add(salaryColumn);
        startingPlayersTable.setItems(gui.getDataManager().getDraft().getVisibleTeam().getStartingPlayers());
        startingPlayersTable.setPrefHeight(500);
        
        // PUT THE TABLEVIEW SHOWING VISIBLETEAM TAXI
        taxiTableBox = new VBox();
        taxiPlayersTable = new TableView();
        taxiPlayersTable.setEditable(true);
        pcc = new PositionColumnComparator();
        taxiTableBox.getChildren().add(taxiPlayersTable);
        taxiTableBox.getStyleClass().add(CLASS_BORDERED_PANE);
        
        taxiPositionColumn = new TableColumn(props.getProperty(WDK_PropertyType.POSITION_LABEL));
        taxiFirstNameColumn = new TableColumn(props.getProperty(WDK_PropertyType.FIRST_NAME_LABEL));
        taxiLastNameColumn = new TableColumn(props.getProperty(WDK_PropertyType.LAST_NAME_LABEL));
        taxiTeamColumn = new TableColumn(props.getProperty(WDK_PropertyType.TEAM_LABEL));
        taxiQpColumn = new TableColumn(props.getProperty(WDK_PropertyType.QP_LABEL));
        taxiRwColumn = new TableColumn(props.getProperty(WDK_PropertyType.R_LABEL) + "/" + props.getProperty(WDK_PropertyType.W_LABEL));
        taxiHrsvColumn = new TableColumn(props.getProperty(WDK_PropertyType.HR_LABEL) + "/" + props.getProperty(WDK_PropertyType.SW_LABEL));
        taxiRbikColumn = new TableColumn(props.getProperty(WDK_PropertyType.RBI_LABEL) + "/" + props.getProperty(WDK_PropertyType.K_LABEL));
        taxiSberaColumn = new TableColumn(props.getProperty(WDK_PropertyType.SB_LABEL) + "/" + props.getProperty(WDK_PropertyType.ERA_LABEL));
        taxiBawhipColumn = new TableColumn(props.getProperty(WDK_PropertyType.BA_LABEL) + "/" + props.getProperty(WDK_PropertyType.WHIP_LABEL));
        taxiEvColumn = new TableColumn(props.getProperty(WDK_PropertyType.EV_LABEL));
        taxiContractColumn = new TableColumn(props.getProperty(WDK_PropertyType.CONTRACT_LABEL));
        taxiSalaryColumn = new TableColumn(props.getProperty(WDK_PropertyType.SALARY_LABEL));
        
        // AND LINK THE COLUMNS TO THE DATA
        taxiPositionColumn.setCellValueFactory(new PropertyValueFactory<String, String>("currentPosition"));
        taxiFirstNameColumn.setCellValueFactory(new PropertyValueFactory<String, String>("firstName"));
        taxiLastNameColumn.setCellValueFactory(new PropertyValueFactory<String, String>("lastName"));
        taxiTeamColumn.setCellValueFactory(new PropertyValueFactory<String, String>("team"));
        taxiQpColumn.setCellValueFactory(new PropertyValueFactory<String, String>("QP"));
        taxiRwColumn.setCellValueFactory(new PropertyValueFactory<String, String>("R"));
        taxiHrsvColumn.setCellValueFactory(new PropertyValueFactory<String, String>("HR"));
        taxiRbikColumn.setCellValueFactory(new PropertyValueFactory<String, String>("RBI"));
        taxiSberaColumn.setCellValueFactory(new PropertyValueFactory<String, String>("SB"));
        taxiBawhipColumn.setCellValueFactory(new PropertyValueFactory<String, String>("BA"));
        taxiEvColumn.setCellValueFactory(new PropertyValueFactory<String, String>("EstimatedValue"));
        taxiContractColumn.setCellValueFactory(new PropertyValueFactory<String, String>("contract"));
        taxiSalaryColumn.setCellValueFactory(new PropertyValueFactory<String, String>("salary"));
        
        taxiPositionColumn.setComparator(pcc);
        taxiRwColumn.setComparator(dcc);
        taxiHrsvColumn.setComparator(dcc);
        taxiRbikColumn.setComparator(dcc);
        taxiSberaColumn.setComparator(dcc);
        taxiBawhipColumn.setComparator(dcc);
        
        taxiPlayersTable.getColumns().add(taxiPositionColumn);
        taxiPlayersTable.getColumns().add(taxiFirstNameColumn);
        taxiPlayersTable.getColumns().add(taxiLastNameColumn);
        taxiPlayersTable.getColumns().add(taxiTeamColumn);
        taxiPlayersTable.getColumns().add(taxiQpColumn);
        taxiPlayersTable.getColumns().add(taxiRwColumn);
        taxiPlayersTable.getColumns().add(taxiHrsvColumn);
        taxiPlayersTable.getColumns().add(taxiRbikColumn);
        taxiPlayersTable.getColumns().add(taxiSberaColumn);
        taxiPlayersTable.getColumns().add(taxiBawhipColumn);
        taxiPlayersTable.getColumns().add(taxiEvColumn);
        taxiPlayersTable.getColumns().add(taxiContractColumn);
        taxiPlayersTable.getColumns().add(taxiSalaryColumn);
        taxiPlayersTable.setItems(gui.getDataManager().getDraft().getVisibleTeam().getTaxiPlayers());
        taxiPlayersTable.setPrefHeight(500);
        
        // PUT THINGS INSIDE VBOX
        fantasyTeamsBox.getChildren().add(draftNameBox);
        fantasyTeamsBox.getChildren().add(buttonsBox);
        startingLineupLabel = initChildLabel(fantasyTeamsBox, WDK_PropertyType.STARTING_LINEUP_LABEL, CLASS_HEADING_LABEL);
        fantasyTeamsBox.getChildren().add(startingTableBox);
        taxiSquadLabel = initChildLabel(fantasyTeamsBox, WDK_PropertyType.TAXI_SQUAD_LABEL, CLASS_HEADING_LABEL);
        fantasyTeamsBox.getChildren().add(taxiTableBox);
        
        initEventHandlers();
        
        
        return fantasyTeamsBorderPane;
    }
    
    // EVENT HANDLERS ARE HERE
    private void initEventHandlers() {
        // CONTROLS FOR ADDING, REMOVING, AND EDITING TEAMS
        playerController = new PlayerEditController(gui.getWindow(), gui.getDataManager().getDraft(), gui.getMessageDialog(), gui.getYesNoCancelDialog());
        draftController = new DraftEditController();
        teamController = new TeamEditController(gui.getWindow(), gui.getDataManager().getDraft(), gui.getMessageDialog(), gui.getYesNoCancelDialog());
        addTeamButton.setOnAction(e -> {
            teamController.handleAddTeamRequest(gui);
        });
        removeTeamButton.setOnAction(e -> {
            teamController.handleRemoveTeamRequest(gui, gui.getDataManager().getDraft().getVisibleTeam());
        });
        editTeamButton.setOnAction(e -> {
            teamController.handleEditTeamRequest(gui, gui.getDataManager().getDraft().getVisibleTeam());
        });
        
        // FOR EDITING PLAYERS ON THE TEAM
        startingPlayersTable.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2) {
                // OPEN UP EDITOR
                Player p = startingPlayersTable.getSelectionModel().getSelectedItem();
                playerController.handleEditPlayerRequest(gui, p);
            }
        });
        
        taxiPlayersTable.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2) {
                // OPEN UP EDITOR
                Player p = taxiPlayersTable.getSelectionModel().getSelectedItem();
                playerController.handleEditPlayerRequest(gui, p);
            }
        });
        
        // FOR NAMING THE DRAFT
        draftNameTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            gui.getDataManager().getDraft().setName(newValue);
        });
        
        // FOR SELECTING THE VISIBLE TEAM
        
        fantasyTeamComboBox.setOnAction(e -> {
            draftController.handleVisibleTeamRequest(gui, this);
        });
        
    }
    
    // HELPER METHODS
    
    public ComboBox getFantasyTeamComboBox() {
        return fantasyTeamComboBox;
    }
    
    public TableView getStartingPlayersTable() {
        return startingPlayersTable;
    }
    
    public TableView getTaxiPlayersTable() {
        return taxiPlayersTable;
    }
    
    
    private Label initChildLabel(Pane container, WDK_PropertyType labelProperty, String styleClass) {
        Label label = initLabel(labelProperty, styleClass);
        container.getChildren().add(label);
        return label;
    }
    
    private Label initLabel(WDK_PropertyType labelProperty, String styleClass) {
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        String labelText = props.getProperty(labelProperty);
        Label label =new Label(labelText);
        label.getStyleClass().add(styleClass);
        return label;
    }
    
    private TextField initChildTextField(Pane container, int size, String initText, boolean editable) {
        TextField tf = new TextField();
        tf.setPrefColumnCount(size);
        tf.setText(initText);
        tf.setEditable(editable);
        container.getChildren().add(tf);
        return tf;
    }
    
    private Button initChildButton(Pane toolbar, WDK_PropertyType icon, WDK_PropertyType tooltip, boolean disabled) {
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        String imagePath = "file:" + PATH_IMAGES + props.getProperty(icon.toString());
        Image buttonImage = new Image(imagePath);
        Button button = new Button();
        button.setDisable(disabled);
        button.setGraphic(new ImageView(buttonImage));
        Tooltip buttonTooltip = new Tooltip(props.getProperty(tooltip.toString()));
        button.setTooltip(buttonTooltip);
        toolbar.getChildren().add(button);
        return button;
    }
    
    
    
    
}
