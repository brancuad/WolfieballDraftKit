package wdk.gui;

import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import properties_manager.PropertiesManager;
import wdk.WDK_PropertyType;
import wdk.WDK_StartupConstants.*;
import wdk.controller.DraftEditController;
import wdk.data.Player;


/**
 *This class is used to initialize the MLB Teams Screen of the Wolfieball
 * Draft Kit application.
 * @author Brandon
 */
public class MLBTeamsScreen {
    
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
    
    // FOR ACCESSING PROPERTIES
    PropertiesManager props = PropertiesManager.getPropertiesManager();
    
    // FOR HANDLING REQUESTS
    DraftEditController draftController;
    
    BorderPane MLBTeamsBorderPane;
    
    VBox MLBTeamsBox;
    Label MLBTeamsLabel;
    
    // THESE ARE FOR THE TEAM COMBO BOX
    HBox teamBox;
    Label teamLabel;
    ComboBox teamComboBox;
    
    // THESE ARE FOR THE TABLEVIEW
    VBox teamTableBox;
    TableView<Player> teamTable;
    TableColumn firstNameColumn;
    TableColumn lastNameColumn;
    TableColumn qpColumn;
    
    
    public MLBTeamsScreen(WDK_GUI wdkgui) {
        gui = wdkgui;
        
    }
    
    public BorderPane initWorkspace() {
        // INITIALIE BORDER PANE WITH LABEL
        
        gui.getDataManager().getDraft().resetVisibleMLBPlayers();
        MLBTeamsBorderPane = new BorderPane();
        MLBTeamsBox = new VBox();
        MLBTeamsBox.getStyleClass().add(CLASS_INNER_PANE);
        MLBTeamsLabel = initChildLabel(MLBTeamsBox, WDK_PropertyType.MLB_TEAMS_LABEL, CLASS_HEADING_LABEL);
        
        // THIS SETS THE LABEL AND COMBOBOX
        teamBox = new HBox();
        teamLabel = initChildLabel(teamBox, WDK_PropertyType.PRO_TEAM_LABEL, CLASS_SUBHEADING_LABEL);
        teamComboBox = new ComboBox();
        teamComboBox.getItems().addAll(gui.getDataManager().getDraft().getTeams());
        teamComboBox.getSelectionModel().select(0);
        draftController = new DraftEditController();
        draftController.handleVisibleMLBTeamRequest(gui, this);
        teamBox.getChildren().add(teamComboBox);
        teamBox.setSpacing(5);
        
        // PUT THE TABLEVIEW SHOWING PLAYERS ON THE TEAM
        teamTableBox = new VBox();
        teamTable = new TableView();
        teamTable.setEditable(true);
        teamTableBox.getChildren().add(teamTable);
        teamTableBox.getStyleClass().add(CLASS_BORDERED_PANE);
        
        // SET UP THE TABLE COLUMNS
        firstNameColumn = new TableColumn(props.getProperty(WDK_PropertyType.FIRST_NAME_LABEL));
        lastNameColumn = new TableColumn(props.getProperty(WDK_PropertyType.LAST_NAME_LABEL));
        qpColumn = new TableColumn(props.getProperty(WDK_PropertyType.QP_LABEL));
        
        // AND LINK THE COLUMNS TO THE DATA
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<String, String>("firstName"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<String, String>("lastName"));
        qpColumn.setCellValueFactory(new PropertyValueFactory<String, String>("QP"));
        
        teamTable.getColumns().add(firstNameColumn);
        teamTable.getColumns().add(lastNameColumn);
        teamTable.getColumns().add(qpColumn);
        teamTable.setItems(gui.getDataManager().getDraft().getVisibleMLBPlayers());
        teamTable.setPrefHeight(500);
        
        // PUT THINGS INSIDE THE VBOX
        MLBTeamsBox.getChildren().add(teamBox);
        MLBTeamsBox.getChildren().add(teamTableBox);
        
        MLBTeamsBorderPane.setCenter(MLBTeamsBox);
        
        initEventHandlers();
        
        return MLBTeamsBorderPane;
    }
    
    public ComboBox getTeamComboBox() {
        return teamComboBox;
    }
    
    // EVENT HANDLERS
    private void initEventHandlers() {
        // FOR CHOOSING FROM THE COMBOBOX
        draftController = new DraftEditController();
        
        teamComboBox.setOnAction(e -> {
            draftController.handleVisibleMLBTeamRequest(gui, this);
        });
    }
    
    // HELPER METHODS
    
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
    
}
