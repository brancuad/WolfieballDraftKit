package wdk.gui;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import properties_manager.PropertiesManager;
import wdk.WDK_PropertyType;
import static wdk.WDK_StartupConstants.PATH_IMAGES;
import wdk.controller.DraftEditController;
import wdk.controller.PlayerEditController;
import wdk.data.DoubleColumnComparator;
import wdk.data.Player;

/**
 *This class is used to initialize the Players Screen of the Wolfieball Draft
 * Kit application.
 * 
 * @author Brandon
 */
public class PlayersScreen {
    
    // THESE CONSTANTS ARE FOR TYING THE PRESENTATION STYLE OF THIS GUI'S
    // COMPONENTS TO A STYLE SHEET THAT IT USES
    static final String CLASS_BORDERED_PANE = "bordered_pane";
    static final String CLASS_HEADING_LABEL = "heading_label";
    static final String CLASS_INNER_PANE = "inner_pane";
    static final String CLASS_SUBHEADING_LABEL = "subheading_label";
    static final String CLASS_PROMPT_LABEL = "prompt_label";
    static final String EMPTY_TEXT = "";
    static final int LARGE_TEXT_FIELD_LENGTH = 50;
    static final int SMALL_TEXT_FIELD_LENGTH = 5;
    
    // MAIN GUI
    WDK_GUI gui;
    
    // FOR ACCESSING PROPERTIES
    PropertiesManager props = PropertiesManager.getPropertiesManager();
    
    // FOR SORTING DOUBLE VALUES
    DoubleColumnComparator dcc;
    
    
    
    // FOR HANDLING REQUESTS
    PlayerEditController playerController;
    DraftEditController draftController;
    
    // THE FOLLOWING ARE FOR INITIALIZING THE PAGE WITH A LABEL
    BorderPane playersBorderPane;
    VBox playersBox;
    Label playersLabel;
    
    // THESE ARE FOR THE ADD, MINUS, AND SEARCH FIELD
    HBox playersSearchBox;
    HBox playersButtonBox;
    GridPane playersControlToolbar;
    Button addPlayerButton;
    Button removePlayerButton;
    TextField searchPlayerField;
    Label searchPlayerLabel;
    
    // THESE ARE FOR THE QUALIFYING POSITIONS BUTTONS
    HBox positionsBox;
    ToggleGroup positionsGroup;
    RadioButton allButton;
    RadioButton cButton;
    RadioButton firstButton;
    RadioButton ciButton;
    RadioButton thirdButton;
    RadioButton secondButton;
    RadioButton miButton;
    RadioButton ssButton;
    RadioButton ofButton;
    RadioButton uButton;
    RadioButton pButton;
    
    // THESE ARE FOR TABLEVIEW
    VBox playersTableBox;
    TableView<Player> playersTable;
    TableColumn firstNameColumn;
    TableColumn lastNameColumn;
    TableColumn teamColumn;
    TableColumn qpColumn;
    TableColumn yearOfBirthColumn;
    private TableColumn rwColumn;
    private TableColumn hrswColumn;
    private TableColumn rbikColumn;
    private TableColumn sberaColumn;
    private TableColumn bawhipColumn;
    TableColumn evColumn;
    TableColumn notesColumn;
       
    
    
    public PlayersScreen(WDK_GUI wdkgui) {
        gui = wdkgui;
    }
    
    public BorderPane initWorkspace() {
        // INITIALIZE BORDER PANE WITH LABEL
        
        gui.getDataManager().getDraft().resetVisiblePlayers();
        
        playersBorderPane = new BorderPane();
        playersBox = new VBox();
        playersBox.getStyleClass().add(CLASS_INNER_PANE);
        playersLabel = initChildLabel(playersBox, WDK_PropertyType.PLAYERS_LABEL, CLASS_HEADING_LABEL);
        
        // THIS SETS THE TOOLBAR FOR CONTROLS
        playersSearchBox = new HBox();
        playersButtonBox = new HBox();
        playersControlToolbar = new GridPane();
        addPlayerButton = initChildButton(playersButtonBox, WDK_PropertyType.ADD_ICON, WDK_PropertyType.ADD_ITEM_TOOLTIP, false);
        removePlayerButton = initChildButton(playersButtonBox, WDK_PropertyType.MINUS_ICON, WDK_PropertyType.REMOVE_ITEM_TOOLTIP, false);
        searchPlayerLabel = initChildLabel(playersSearchBox, WDK_PropertyType.SEARCH_LABEL, CLASS_SUBHEADING_LABEL);
        searchPlayerField = initChildTextField(playersSearchBox, LARGE_TEXT_FIELD_LENGTH, EMPTY_TEXT, true);
        playersButtonBox.setSpacing(5);
        
        // PUT THE BUTTONS, LABELS, AND TEXT FIELDS WITHIN THE PANES;
        playersControlToolbar.add(playersButtonBox, 1, 1, 1, 1);
        playersControlToolbar.add(playersSearchBox, 2, 1, 1, 1);
        playersControlToolbar.getColumnConstraints().add(new ColumnConstraints(0));
        playersControlToolbar.getColumnConstraints().add(new ColumnConstraints(150));
        
        // PUT THE RADIO BUTTONS IN THE POSITIONS GROUP THEN POSITIONS PANE
        positionsBox = new HBox();
        positionsGroup = new ToggleGroup();
        allButton = initChildRadioButton(positionsBox, positionsGroup, WDK_PropertyType.ALL_LABEL);
        cButton = initChildRadioButton(positionsBox, positionsGroup, WDK_PropertyType.C_LABEL);
        firstButton = initChildRadioButton(positionsBox, positionsGroup, WDK_PropertyType.FIRST_LABEL);
        ciButton = initChildRadioButton(positionsBox, positionsGroup, WDK_PropertyType.CI_LABEL);
        thirdButton = initChildRadioButton(positionsBox, positionsGroup, WDK_PropertyType.THIRD_LABEL);
        secondButton = initChildRadioButton(positionsBox, positionsGroup, WDK_PropertyType.SECOND_LABEL);
        miButton = initChildRadioButton(positionsBox, positionsGroup, WDK_PropertyType.MI_LABEL);
        ssButton = initChildRadioButton(positionsBox, positionsGroup, WDK_PropertyType.SS_LABEL);
        ofButton = initChildRadioButton(positionsBox, positionsGroup, WDK_PropertyType.OF_LABEL);
        uButton = initChildRadioButton(positionsBox, positionsGroup, WDK_PropertyType.U_LABEL);
        pButton = initChildRadioButton(positionsBox, positionsGroup, WDK_PropertyType.P_LABEL);
        allButton.setSelected(true);
        positionsBox.getStyleClass().add(CLASS_BORDERED_PANE);
        
        // PUT THE TABLEVIEW SHOWING VISIBLEPLAYERS
        playersTableBox = new VBox();
        playersTable = new TableView();
        playersTable.setEditable(true);
        dcc = new DoubleColumnComparator();
        playersTableBox.getChildren().add(playersTable);
        playersTableBox.getStyleClass().add(CLASS_BORDERED_PANE);
        
        // SET UP TABLE COLUMNS
        firstNameColumn = new TableColumn(props.getProperty(WDK_PropertyType.FIRST_NAME_LABEL));
        lastNameColumn = new TableColumn(props.getProperty(WDK_PropertyType.LAST_NAME_LABEL));
        teamColumn = new TableColumn(props.getProperty(WDK_PropertyType.TEAM_LABEL));
        qpColumn = new TableColumn(props.getProperty(WDK_PropertyType.QP_LABEL));
        yearOfBirthColumn = new TableColumn(props.getProperty(WDK_PropertyType.YEAR_OF_BIRTH_LABEL));
        rwColumn = new TableColumn(props.getProperty(WDK_PropertyType.R_LABEL) + "/" + props.getProperty(WDK_PropertyType.W_LABEL));
        hrswColumn = new TableColumn(props.getProperty(WDK_PropertyType.HR_LABEL) + "/" + props.getProperty(WDK_PropertyType.SW_LABEL));
        rbikColumn = new TableColumn(props.getProperty(WDK_PropertyType.RBI_LABEL) + "/" + props.getProperty(WDK_PropertyType.K_LABEL));
        sberaColumn = new TableColumn(props.getProperty(WDK_PropertyType.SB_LABEL) + "/" + props.getProperty(WDK_PropertyType.ERA_LABEL));
        bawhipColumn = new TableColumn(props.getProperty(WDK_PropertyType.BA_LABEL) + "/" + props.getProperty(WDK_PropertyType.WHIP_LABEL));
        evColumn = new TableColumn(props.getProperty(WDK_PropertyType.EV_LABEL));
        notesColumn = new TableColumn(props.getProperty(WDK_PropertyType.NOTES_LABEL));
        
        gui.getDataManager().getDraft().calcEV();
        
        // AND LINK THE COLUMNS TO THE DATA
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<String, String>("firstName"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<String, String>("lastName"));
        teamColumn.setCellValueFactory(new PropertyValueFactory<String, String>("team"));
        qpColumn.setCellValueFactory(new PropertyValueFactory<String, String>("QP"));
        yearOfBirthColumn.setCellValueFactory(new PropertyValueFactory<String, String>("yearOfBirth"));
        getRwColumn().setCellValueFactory(new PropertyValueFactory<String, String>("R"));
        getHrswColumn().setCellValueFactory(new PropertyValueFactory<String, String>("HR"));
        getRbikColumn().setCellValueFactory(new PropertyValueFactory<String, String>("RBI"));
        getSberaColumn().setCellValueFactory(new PropertyValueFactory<String, String>("SB"));
        getBawhipColumn().setCellValueFactory(new PropertyValueFactory<String, String>("BA"));
        evColumn.setCellValueFactory(new PropertyValueFactory<String, String>("EstimatedValue"));
        notesColumn.setCellValueFactory(new PropertyValueFactory<String, String>("Notes"));
        notesColumn.setEditable(true);
        
        yearOfBirthColumn.setComparator(dcc);
        getRwColumn().setComparator(dcc);
        getHrswColumn().setComparator(dcc);
        getRbikColumn().setComparator(dcc);
        getSberaColumn().setComparator(dcc);
        getBawhipColumn().setComparator(dcc);
        evColumn.setComparator(dcc);
        
        playersTable.getColumns().add(firstNameColumn);
        playersTable.getColumns().add(lastNameColumn);
        playersTable.getColumns().add(teamColumn);
        playersTable.getColumns().add(qpColumn);
        playersTable.getColumns().add(yearOfBirthColumn);
        playersTable.getColumns().add(getRwColumn());
        playersTable.getColumns().add(getHrswColumn());
        playersTable.getColumns().add(getRbikColumn());
        playersTable.getColumns().add(getSberaColumn());
        playersTable.getColumns().add(getBawhipColumn());
        playersTable.getColumns().add(evColumn);
        playersTable.getColumns().add(notesColumn);
        playersTable.setItems(gui.getDataManager().getDraft().getVisiblePlayers());
        playersTable.setPrefHeight(1000);
        
        //PUT THINGS INSIDE THE VBOX
        playersBox.getChildren().add(playersControlToolbar);
        playersBox.getChildren().add(positionsBox);
        playersBox.getChildren().add(playersTableBox);
        
        playersBorderPane.setCenter(playersBox);
        
        initEventHandlers();
        
        return playersBorderPane;
    }
    
    // BELOW ARE GETTERS FOR ACCESSING DURING HANDLING EVENTS
    public TableColumn getNotesColumn() {
        return notesColumn;
    }
    
    public TableColumn getRwColumn() {
        return rwColumn;
    }

    public TableColumn getHrswColumn() {
        return hrswColumn;
    }

    public TableColumn getRbikColumn() {
        return rbikColumn;
    }

    public TableColumn getSberaColumn() {
        return sberaColumn;
    }

    public TableColumn getBawhipColumn() {
        return bawhipColumn;
    }
    
    public TextField getSearchPlayerField() {
        return searchPlayerField;
    }
    
    public TableView getPlayersTable() {
        return playersTable;
    }
    
    public ToggleGroup getPositionsGroup() {
        return positionsGroup;
    }
    
    // EVENT HANDLERS ARE HERE
    private void initEventHandlers() {    
        // CONTROLS FOR ADDING, REMOVING, AND SEARCHING PLAYERS
        playerController = new PlayerEditController(gui.getWindow(), gui.getDataManager().getDraft(), gui.getMessageDialog(), gui.getYesNoCancelDialog());
        draftController = new DraftEditController();
        addPlayerButton.setOnAction(e -> {
            playerController.handleAddPlayerRequest(gui);
        });
        removePlayerButton.setOnAction(e -> {
            playerController.handleRemovePlayerRequest(gui, playersTable.getSelectionModel().getSelectedItem());
        });
        searchPlayerField.textProperty().addListener(e -> {
            draftController.handleSearchPlayerRequest(gui, this); 
        });
        
        // FILTERS POSITION
        registerRadioButtonController(positionsGroup);
        
        // DOUBLE CLICK ON NOTES
        notesColumn.setCellFactory(TextFieldTableCell.<Player>forTableColumn());
        notesColumn.setOnEditCommit(e -> {
            final TableColumn.CellEditEvent t = (TableColumn.CellEditEvent) e;
            ((Player) t.getTableView().getItems().get(t.getTablePosition().getRow())).setNotes((String)t.getNewValue());
        });
        
        // DOUBLE CLICK TO EDIT PLAYER
        playersTable.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2) {
                // OPEN UP EDITOR
                Player p = playersTable.getSelectionModel().getSelectedItem();
                playerController.handleEditPlayerRequest(gui, p);
                gui.getDataManager().getDraft().resetVisiblePlayers();
                draftController.handleSearchPlayerRequest(gui, this);
                draftController.handlePositionsFilterRequest(gui, this);
                gui.getDataManager().getDraft().calcEV();
                playersTable.setItems(gui.getDataManager().getDraft().getVisiblePlayers());
            }
        });
        
        
    }
        
    // REGISTER THE EVENT LISTENER FOR A TOGGLE GROUP
    private void registerRadioButtonController(ToggleGroup group) {
        group.selectedToggleProperty().addListener(e -> {
            draftController.handlePositionsFilterRequest(gui, this);
        });
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
    
    private RadioButton initChildRadioButton(HBox box, ToggleGroup group, WDK_PropertyType labelProperty) {
        RadioButton button = new RadioButton(props.getProperty(labelProperty.toString()));
        button.setToggleGroup(group);
        button.setUserData(labelProperty);
        button.setSelected(false);
        box.getChildren().add(button);
        return button;
    }
    
}
