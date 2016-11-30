package wdk.gui;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import properties_manager.PropertiesManager;
import wdk.WDK_PropertyType;
import wdk.WDK_StartupConstants.*;
import static wdk.WDK_StartupConstants.PATH_IMAGES;
import wdk.controller.DraftEditController;
import wdk.data.Player;

/**
 *This class is used to initialize the Draft Summary Screen of the 
 * Wolfieball Draft Kit application.
 * @author Brandon
 */
public class DraftSummaryScreen {
    
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
    
    // FOR EVENT HANDLING
    DraftEditController draftController;
    
    BorderPane draftSummaryBorderPane;
    
    VBox draftSummaryBox;
    Label draftSummaryLabel;
    
    // THESE ARE FOR THE BUTTONS
    HBox draftBox;
    Button autoButton;
    Button playButton;
    Button pauseButton;
    
    // THESE ARE FOR THE TABLEVIEW
    VBox draftTableBox;
    TableView<Player> draftTable;
    TableColumn pickColumn;
    TableColumn firstNameColumn;
    TableColumn lastNameColumn;
    TableColumn teamColumn;
    TableColumn contractColumn;
    TableColumn salaryColumn;
    
    public DraftSummaryScreen(WDK_GUI wdkgui) {
        gui = wdkgui;
    }
    
    public BorderPane initWorkspace() {
        // INITIALIZE BORDER PANE WITH LABEL
        draftSummaryBorderPane = new BorderPane();
        draftSummaryBox = new VBox();
        draftSummaryBox.getStyleClass().add(CLASS_INNER_PANE);
        draftSummaryLabel = initChildLabel(draftSummaryBox, WDK_PropertyType.DRAFT_SUMMARY_LABEL, CLASS_HEADING_LABEL);
       
        // THIS SETS THE BUTTONS
        draftBox = new HBox();
        autoButton = initChildButton(draftBox, WDK_PropertyType.STAR_ICON, WDK_PropertyType.AUTO_TOOLTIP, false);
        playButton = initChildButton(draftBox, WDK_PropertyType.PLAY_ICON, WDK_PropertyType.PLAY_TOOLTIP, false);
        pauseButton = initChildButton(draftBox, WDK_PropertyType.PAUSE_ICON, WDK_PropertyType.PAUSE_TOOLTIP, false);
        draftBox.setSpacing(5);
        
        // PUT THE TABLEVIEW SHOWING DRAFTED PLAYERS
        draftTableBox = new VBox();
        draftTable = new TableView();
        draftTable.setEditable(true);
        draftTableBox.getChildren().add(draftTable);
        draftTableBox.getStyleClass().add(CLASS_BORDERED_PANE);
        
        // SET UP THE TABLE COLUMNS
        pickColumn = new TableColumn(props.getProperty(WDK_PropertyType.PICK_LABEL));
        firstNameColumn = new TableColumn(props.getProperty(WDK_PropertyType.FIRST_NAME_LABEL));
        lastNameColumn = new TableColumn(props.getProperty(WDK_PropertyType.LAST_NAME_LABEL));
        teamColumn = new TableColumn(props.getProperty(WDK_PropertyType.FANTASY_TEAM_LABEL));
        contractColumn = new TableColumn(props.getProperty(WDK_PropertyType.CONTRACT_LABEL));
        salaryColumn = new TableColumn(props.getProperty(WDK_PropertyType.SALARY_LABEL));
        
        // AND LINK THE COLUMNS TO THE DATA
        pickColumn.setCellValueFactory(new Callback<CellDataFeatures, ObservableValue>() {
            @Override 
            public ObservableValue call(CellDataFeatures p) {
                return new ReadOnlyObjectWrapper(draftTable.getItems().indexOf(p.getValue()) + 1 + "");
            }
        });  
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<String, String>("firstName"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<String, String>("lastName"));
        teamColumn.setCellValueFactory(new Callback<CellDataFeatures, ObservableValue>() {
            @Override 
            public ObservableValue<String> call(CellDataFeatures c) {
                Player temp = (Player)c.getValue();
                return new ReadOnlyObjectWrapper(temp.getFantasyTeam().getTeamName());
            }
        });
        contractColumn.setCellValueFactory(new PropertyValueFactory<String, String>("contract"));
        salaryColumn.setCellValueFactory(new PropertyValueFactory<String, String>("salary"));
        
        pickColumn.setSortable(false);
        firstNameColumn.setSortable(false);
        lastNameColumn.setSortable(false);
        teamColumn.setSortable(false);
        contractColumn.setSortable(false);
        salaryColumn.setSortable(false);
        
        draftTable.getColumns().add(pickColumn);
        draftTable.getColumns().add(firstNameColumn);
        draftTable.getColumns().add(lastNameColumn);
        draftTable.getColumns().add(teamColumn);
        draftTable.getColumns().add(contractColumn);
        draftTable.getColumns().add(salaryColumn);
        draftTable.setItems(gui.getDataManager().getDraft().getDraftedPlayers());
        draftTable.setPrefHeight(900);
        
        // PUT THINGS INSIDE THE VBOX
        draftSummaryBox.getChildren().add(draftBox);
        draftSummaryBox.getChildren().add(draftTableBox);
        
        draftSummaryBorderPane.setCenter(draftSummaryBox);
        
        initEventHandlers();
        
        return draftSummaryBorderPane;
    }
    
    // EVENT HANDLERS
    private void initEventHandlers() {
        // CONTROLS FOR AUTO ADD, PLAY, AND PAUSE
        draftController = new DraftEditController();
        autoButton.setOnAction(e -> {
            draftController.handleAutoAddRequest(gui);
        });
        playButton.setOnAction(e -> {
            draftController.handlePlayRequest(gui);
        });
        pauseButton.setOnAction(e -> {
            draftController.handlePauseRequest(gui);
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
