package wdk.gui;

import java.text.DecimalFormat;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import properties_manager.PropertiesManager;
import wdk.WDK_PropertyType;
import wdk.data.Team;
import wdk.WDK_StartupConstants.*;
import wdk.data.DoubleColumnComparator;

/**
 *This class is used to initialize the Fantasy Standings Screen of the 
 * Wolfieball Draft Kit application.
 * 
 * @author Brandon
 */
public class FantasyStandingsScreen {
    
    // THESE CONSTANTS ARE FOR TYING THE PRESENTATION STYLE OF
    // THIS GUI'S COMPONENTS TO A STYLE SHEET IT USES
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

    
    // THE FOLLOWING ARE FOR INITIALIZING THE PAGE WITH A LABEL
    BorderPane fantasyStandingsBorderPane;
    VBox fantasyStandingsBox;
    Label fantasyStandingsLabel;
    
    // THESE ARE FOR THE TABLEVIEW
    VBox standingsTableBox;
    TableView<Team> standingsTable;
    TableColumn teamNameColumn;
    TableColumn playersNeededColumn;
    TableColumn moneyLeftColumn;
    TableColumn ppColumn;
    TableColumn rColumn;
    TableColumn hrColumn;
    TableColumn rbiColumn;
    TableColumn sbColumn;
    TableColumn baColumn;
    TableColumn wColumn;
    TableColumn svColumn;
    TableColumn kColumn;
    TableColumn eraColumn;
    TableColumn whipColumn;
    TableColumn totalPointsColumn;
    DoubleColumnComparator dcc;
    
    
    public FantasyStandingsScreen(WDK_GUI wdkgui) {
        gui = wdkgui;
    }
    
    public BorderPane initWorkspace() {
        // INITIALIZE BORDER PANE WITH LABEL
        fantasyStandingsBorderPane = new BorderPane();
        fantasyStandingsBox = new VBox();
        fantasyStandingsBox.getStyleClass().add(CLASS_INNER_PANE);
        fantasyStandingsLabel = initChildLabel(fantasyStandingsBox, WDK_PropertyType.FANTASY_STANDINGS_LABEL, CLASS_HEADING_LABEL);
        
        // PUT THE TABLEVIEW SHOWING THE FANTASY TEAMS
        standingsTableBox = new VBox();
        standingsTable = new TableView();
        standingsTable.setEditable(true);
        standingsTableBox.getChildren().add(standingsTable);
        standingsTableBox.getStyleClass().add(CLASS_BORDERED_PANE);
        dcc = new DoubleColumnComparator();
        
        // SET UP THE TABLE COLUMNS
        teamNameColumn = new TableColumn(props.getProperty(WDK_PropertyType.TEAM_NAME_LABEL));
        playersNeededColumn = new TableColumn(props.getProperty(WDK_PropertyType.PLAYERS_NEEDED_LABEL));
        moneyLeftColumn = new TableColumn(props.getProperty(WDK_PropertyType.MONEY_LEFT_LABEL));
        ppColumn = new TableColumn(props.getProperty(WDK_PropertyType.PP_LABEL));
        rColumn = new TableColumn(props.getProperty(WDK_PropertyType.R_LABEL));
        hrColumn = new TableColumn(props.getProperty(WDK_PropertyType.HR_LABEL));
        rbiColumn = new TableColumn(props.getProperty(WDK_PropertyType.RBI_LABEL));
        sbColumn = new TableColumn(props.getProperty(WDK_PropertyType.SB_LABEL));
        baColumn = new TableColumn(props.getProperty(WDK_PropertyType.BA_LABEL));
        wColumn = new TableColumn(props.getProperty(WDK_PropertyType.W_LABEL));
        svColumn = new TableColumn(props.getProperty(WDK_PropertyType.SW_LABEL));
        kColumn = new TableColumn(props.getProperty(WDK_PropertyType.K_LABEL));
        eraColumn = new TableColumn(props.getProperty(WDK_PropertyType.ERA_LABEL));
        whipColumn = new TableColumn(props.getProperty(WDK_PropertyType.WHIP_LABEL));
        totalPointsColumn = new TableColumn(props.getProperty(WDK_PropertyType.TOTAL_POINTS_LABEL));
        
        DecimalFormat ba = new DecimalFormat("0.000");
        DecimalFormat df = new DecimalFormat("0.00");
        
        // AND LINK THE COLUMNS TO THE DATA
        teamNameColumn.setCellValueFactory(new PropertyValueFactory<String, String>("teamName"));
        playersNeededColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures, ObservableValue>() {
            @Override 
            public ObservableValue<String> call(TableColumn.CellDataFeatures c) {
                Team temp = (Team)c.getValue();
                return new ReadOnlyObjectWrapper((23-temp.getStartingPlayers().size())+ "");
            }
        });
        moneyLeftColumn.setCellValueFactory(new PropertyValueFactory<String, String>("moneyLeft"));
        ppColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures, ObservableValue>() {
            @Override 
            public ObservableValue<String> call(TableColumn.CellDataFeatures c) {
                Team temp = (Team)c.getValue();
                int playersNeeded = 23 - temp.getStartingPlayers().size();
                int pp;
                if (playersNeeded == 0)
                    pp = 0;
                else
                    pp = Integer.parseInt(temp.getMoneyLeft()) / playersNeeded;
                return new ReadOnlyObjectWrapper(pp + "");
            }
        });
        
        gui.getDataManager().getDraft().calcTotalPoints();
        
        rColumn.setCellValueFactory(new PropertyValueFactory<String, String>("r"));
        hrColumn.setCellValueFactory(new PropertyValueFactory<String, String>("hR"));
        rbiColumn.setCellValueFactory(new PropertyValueFactory<String, String>("rBI"));
        sbColumn.setCellValueFactory(new PropertyValueFactory<String, String>("sB"));
        baColumn.setCellValueFactory(new PropertyValueFactory<String, String>("bA"));
        wColumn.setCellValueFactory(new PropertyValueFactory<String, String>("w"));
        svColumn.setCellValueFactory(new PropertyValueFactory<String, String>("sV"));
        kColumn.setCellValueFactory(new PropertyValueFactory<String, String>("k"));
        eraColumn.setCellValueFactory(new PropertyValueFactory<String, String>("eRA"));
        whipColumn.setCellValueFactory(new PropertyValueFactory<String, String>("wHIP"));
        totalPointsColumn.setCellValueFactory(new PropertyValueFactory<String, String>("totalPoints"));
        
        playersNeededColumn.setComparator(dcc);
        moneyLeftColumn.setComparator(dcc);
        ppColumn.setComparator(dcc);
        rColumn.setComparator(dcc);
        hrColumn.setComparator(dcc);
        rbiColumn.setComparator(dcc);
        sbColumn.setComparator(dcc);
        baColumn.setComparator(dcc);
        wColumn.setComparator(dcc);
        svColumn.setComparator(dcc);
        kColumn.setComparator(dcc);
        eraColumn.setComparator(dcc);
        whipColumn.setComparator(dcc);
        totalPointsColumn.setComparator(dcc);
        
        standingsTable.getColumns().add(teamNameColumn);
        standingsTable.getColumns().add(playersNeededColumn);
        standingsTable.getColumns().add(moneyLeftColumn);
        standingsTable.getColumns().add(ppColumn);
        standingsTable.getColumns().add(rColumn);
        standingsTable.getColumns().add(hrColumn);
        standingsTable.getColumns().add(rbiColumn);
        standingsTable.getColumns().add(sbColumn);
        standingsTable.getColumns().add(baColumn);
        standingsTable.getColumns().add(wColumn);
        standingsTable.getColumns().add(svColumn);
        standingsTable.getColumns().add(kColumn);
        standingsTable.getColumns().add(eraColumn);
        standingsTable.getColumns().add(whipColumn);
        standingsTable.getColumns().add(totalPointsColumn);
        standingsTable.setItems(gui.getDataManager().getDraft().getFantasyTeams());
        standingsTable.setPrefHeight(500);
        
        // PUT THINGS INSIDE THE VBOX
        fantasyStandingsBox.getChildren().add(standingsTableBox);
        
        fantasyStandingsBorderPane.setCenter(fantasyStandingsBox);
        
        initEventHandlers();
        
        return fantasyStandingsBorderPane;
    }
    
    // EVENT HANDLERS
    private void initEventHandlers() {
        
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
