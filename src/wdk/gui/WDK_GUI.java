package wdk.gui;

import static wdk.WDK_StartupConstants.*;
import wdk.WDK_PropertyType;
import wdk.data.Draft;
import wdk.data.DraftDataManager;
import wdk.controller.FileController;
import wdk.controller.PlayerEditController;
import wdk.controller.TeamEditController;
import wdk.data.Hitter;
import wdk.data.Pitcher;
import wdk.data.Player;
import wdk.data.Team;
import wdk.file.DraftFileManager;
import wdk.file.DraftSiteExporter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;
import properties_manager.PropertiesManager;
import wdk.data.DraftDataView;
import wdk.controller.DraftEditController;
import javafx.geometry.Rectangle2D;

/**
 *This class provides the Graphical User Interface for this application,
 * managing all the UI components for editing a Draft, switching screens,
 * and exporting it to its summary page.
 * 
 * @author Brandon
 */
public class WDK_GUI implements DraftDataView {
    
    //THESE CONSTANTS ARE FOR TYING THE PRESENTATION STYLE OF
    // THIS GUI'S COMPONENTS TO A STYLE SHEET THAT IT USES
    
    static final String PRIMARY_STYLE_SHEET = PATH_CSS + "wdk_style.css";
    static final String CLASS_BORDERED_PANE = "bordered_pane";
    static final String CLASS_SCREEN_PANE = "screen_pane";
    static final String CLASS_HEADING_LABEL = "heading_label";
    static final String CLASS_SUBHEADING_LABEL = "subheading_label";
    static final String CLASS_PROMPT_LABEL = "prompt_label";
    static final String EMPTY_TEXT = "";
    static final int LARGE_TEXT_FIELD_LENGTH = 20;
    static final int SMALL_TEXT_FIELD_LENGTH = 5;
    
    // THIS MANAGES ALL OF THE APPLICATION'S DATA
    DraftDataManager dataManager;
    
    // THIS MANAGES COURSE FILE I/0
    DraftFileManager draftFileManager;
    
    // THIS MANAGES EXPORTING OUR SITE PAGE
    DraftSiteExporter siteExporter;
    
    // THIS HANDLES INTERACTIONS WITH FILE-RELATED CONTROLS
    FileController fileController;
    
    // THIS HANDLES INTERACTIONS WITH DRAFT INFO CONTROLS
    DraftEditController draftController = new DraftEditController();
    
    // THIS HANDLES REQUESTS TO ADD OR EDIT PLAYER STUFF
    PlayerEditController playerController;
    
    // THIS HANDLES REQUESTS TO ADD OR EDIT TEAM STUFF
    TeamEditController teamController;
    
    // THIS IS THE APPLICATION WINDOW
    Stage primaryStage;
    
    // THIS IS THE STAGE'S SCENE GRAPH
    Scene primaryScene;
    
    // THIS PANE ORGANIZES THE BIG PICTURE CONTAINERS FOR THE
    // APPLICATION GUI
    BorderPane wdkPane;
    
    // THIS IS THE TOP TOOLBAR AND ITS CONTROLS
    FlowPane fileToolbarPane;
    Button newDraftButton;
    Button loadDraftButton;
    Button saveDraftButton;
    Button exportSiteButton;
    Button exitButton;
    
    // THIS IS THE BOTTOM TOOLBAR AND ITS CONTROLS
    FlowPane screenToolbarPane;
    Button fantasyTeamsButton;
    Button playersButton;
    Button fantasyStandingsButton;
    Button draftSummaryButton;
    Button MLBTeamsButton;
    
    // WE'LL ORGANIZE OUR WORKSPACE COMPONENTS USING A BORDER PANE
    BorderPane workspacePane;
    boolean workspaceActivated;
    
    // WE'LL PUT THE WORKSPACE INSIDE A SCROLL PANE
    VBox workspaceScrollPane;
    
    // WE'LL PUT THIS IN THE TOP OF THE WORKSPACE, IT WILL
    // HOLD TWO OTHER PANES FULL OF CONTROLS AS WELL AS A LABEL
    VBox topWorkspacepane;
    Label draftHeadingLabel;
    SplitPane topWorkspaceSplitPane;
    
    // HERE ARE THE DIALOGS
    MessageDialog messageDialog;
    YesNoCancelDialog yesNoCancelDialog;
    
    // HERE ARE THE SCREENS
    private FantasyTeamsScreen fantasyTeamsScreen = new FantasyTeamsScreen(this);
    private PlayersScreen playersScreen = new PlayersScreen(this);
    private FantasyStandingsScreen fantasyStandingsScreen = new FantasyStandingsScreen(this);
    private DraftSummaryScreen draftSummaryScreen = new DraftSummaryScreen(this);
    private MLBTeamsScreen MLBTeamsScreen = new MLBTeamsScreen(this);
    
    /**
     * Constructor for making this GUI, note that it does not initialize the UI
     * controls.  To do that, call initGUI.
     * 
     * @param initPrimaryStage Window inside which the GUI will be displayed.
     */
    public WDK_GUI(Stage initPrimaryStage) {
        primaryStage = initPrimaryStage;
    }
    
    /**
     * Accessor method for the data manager.
     * 
     * @return The CourseDataManager used by this UI.
     */
    public DraftDataManager getDataManager() {
        return dataManager;
    }
    
    /**
     * Accessor method for the file controller.
     * 
     * @return The FileController used by this UI.
     */
    public FileController getFileController() {
        return fileController;
    }
    
    /**
     * Accessor method for the draft file manager.
     * 
     * @return the DraftFileManager used by this UI.
     */
    public DraftFileManager getDraftFileManager() {
        return draftFileManager;
    }
    
    /**
     * Accessor method for the site exporter.
     * 
     * @return The DraftSiteExporter used by this UI.
     */
    public DraftSiteExporter getSiteExporter() {
        return siteExporter;
    }
    
    /**
     * Accessor method for the window (i.e. stage).
     * 
     * @return The Window (i.e. Stage) used by this UI.
     */
    public Stage getWindow() {
        return primaryStage;
    }
    
    public MessageDialog getMessageDialog() {
        return messageDialog;
    }
    
    public YesNoCancelDialog getYesNoCancelDialog() {
        return yesNoCancelDialog;
    }
    
    /**
     * Mutator method for the data manager.
     * 
     * @param initDataMangaer The DraftDataManager to be used by this UI.
     */
    public void setDataManager(DraftDataManager initDataManager) {
        dataManager = initDataManager;
    }
    
    /**
     * Mutator method for the draft file manager.
     * 
     * @param initDraftFileManager The DraftFileManager to be used by this UI.
     */
    public void setDraftFileManager(DraftFileManager initDraftFileManager) {
        draftFileManager = initDraftFileManager;
    }
    
    /**
     * Mutator method for the site exporter.
     * 
     * @param initSiteExporter The DraftSiteExporter to be used by this UI.
     */
    public void setSiteExporter(DraftSiteExporter initSiteExporter) {
        siteExporter = initSiteExporter;
    }
    
    /**
     * This method fully initializes the user interface for use.
     * 
     * @param windowTitle The text to appear in the UI window's title bar.
     * @param players The list of players involved in the draft.
     * @param IOException Thrown if any initialization files fail to load.
     */
    public void initGUI(String windowTitle, ObservableList<Player> players) throws IOException {
        // INIT THE DIALOGS
        initDialogs();
        
        // INIT THE FILE TOOLBAR
        initFileToolbar();
        
        // INIT THE SCREEN TOOLBAR
        initScreenToolbar();
        
        // INIT THE CENTER WORKSPACE CONTROLS BUT DON'T ADD THEM
        // TO THE WINDOW YET
        initWorkspace(getFantasyTeamsScreen().initWorkspace());
        
        // NOW SETUP THE EVENT HANDLERS
        initEventHandlers();
        
        // AND FINALLY START UP THE WINDOW (WITHOUT THE WORKSPACE)
        initWindow(windowTitle);
    }
    
    /**
     * When called this function puts the workspace into the window,
     * revealing the screen for Fantasy Teams and the ability to 
     * switch between screens.
     * 
     * @param initScreen The to set the center of wdkPane to
     */
    public void activateWorkspace() {
        if(!workspaceActivated) {
            // PUT WORKSPACE IN THE GUI
            wdkPane.setCenter(workspaceScrollPane);
            wdkPane.setBottom(screenToolbarPane);
            workspaceActivated = true;
        }
    }
    
    /**
     * This function takes all of the data out of the draftToReload
     * argument and loads its values into the user interface controls.
     * 
     * @param draftToReload The draft whose data we'll load into the GUI.
     */
    @Override
    public void reloadDraft(Draft draftToReload) {
        // FIRST ACTIVATE THE WORKSPACE IF NECESSARY
        if (!workspaceActivated) { 
            activateWorkspace();
        }
        else{
           draftController.handleFantasyTeamsRequest(this); 
        }
        
        // WE DON'T WANT TO RESPOND TO EVNETS FORCED BY
        // OUR INITIALIZATION SELECTIONS
        draftController.enable(false);
        
        // FIRST LOAD ALL OF THE SCREENS
        // @todo load screens
        
        // NOW WE DO WANT TO RESPOND WHEN THE USER INTERACTS WITH OUR CONTROLS
        draftController.enable(true);
    }
    
    /**
     * This method is used to activate/deactivate toolbar buttons when
     * they can and cannot be used as to provide foolproof design.
     * 
     * @param saved Describes whether the loaded Draft has been saved or not.
     */
    public void updateToolbarControls(boolean saved) {
        // THIS TOGGLES WITH WHETHER THE CURRENT DRAFT
        // HAS BEEN SAVED OR NOT
        saveDraftButton.setDisable(saved);
        
        // ALL THE OTHER BUTTONS ARE ALWAYS ENABLED
        // ONCE EDITING THAT FIRST DRAFT BEGINS
        loadDraftButton.setDisable(false);
        exportSiteButton.setDisable(false);
        
        // NOTE THAT THE NEW, LOAD AND EXIT BUTTONS
        // ARE NEVER DISABLED SO WE ENVER HAVE TO TOUCH THEM
    }
    
    // INCASE IT'S NEEDED
    public void updateDraftInfo(Draft draft) {
        
    }
    
    /*****************************************************************************/
    /*  BELOW AR EALL THE PRIVATE/PUBLIC HELPER METHODS WE USE FOR INITIALIZING OUR GUI */
    /*****************************************************************************/
    
    private void initDialogs() {
        messageDialog = new MessageDialog(primaryStage, CLOSE_BUTTON_LABEL);
        yesNoCancelDialog = new YesNoCancelDialog(primaryStage);
    }
    
    /**
     * This function initializes all the buttons in the toolbar at the top of
     * the application window. These are related to file management.
     */
    private void initFileToolbar() {
        fileToolbarPane = new FlowPane();
        
        // HERE ARE OUR FILE TOOLBAR BUTTONS, NOT THAT SOME WILL
        // START AS ENABLED (false), WHILE OTHERS DISABLED (true)
        newDraftButton = initChildButton(fileToolbarPane, WDK_PropertyType.NEW_DRAFT_ICON, WDK_PropertyType.NEW_DRAFT_TOOLTIP, false);
        loadDraftButton = initChildButton(fileToolbarPane, WDK_PropertyType.LOAD_DRAFT_ICON, WDK_PropertyType.LOAD_DRAFT_TOOLTIP, false);
        saveDraftButton = initChildButton(fileToolbarPane, WDK_PropertyType.SAVE_DRAFT_ICON, WDK_PropertyType.SAVE_DRAFT_TOOLTIP, true);
        exportSiteButton = initChildButton(fileToolbarPane, WDK_PropertyType.EXPORT_PAGE_ICON, WDK_PropertyType.EXPORT_PAGE_TOOLTIP, true);
        exitButton = initChildButton(fileToolbarPane, WDK_PropertyType.EXIT_ICON, WDK_PropertyType.EXIT_TOOLTIP, false);
    }
    
    /**
     * This function initializes all the buttons in the toolbar at the bottom of
     * the application window.  These are related to screen selection.
     */
    private void initScreenToolbar() {
        screenToolbarPane = new FlowPane();
        screenToolbarPane.getStyleClass().add(CLASS_SCREEN_PANE);
        
        // HERE ARE OUR SCREEN TOOLBAR BUTTONS
        fantasyTeamsButton = initChildButton(screenToolbarPane, WDK_PropertyType.FANTASY_TEAMS_ICON, WDK_PropertyType.FANTASY_TEAMS_TOOLTIP, false);
        playersButton = initChildButton(screenToolbarPane, WDK_PropertyType.PLAYERS_ICON, WDK_PropertyType.PLAYERS_TOOLTIP, false);
        fantasyStandingsButton = initChildButton(screenToolbarPane, WDK_PropertyType.FANTASY_STANDINGS_ICON, WDK_PropertyType.FANTASY_STANDINGS_TOOLTIP, false);
        draftSummaryButton = initChildButton(screenToolbarPane, WDK_PropertyType.DRAFT_SUMMARY_ICON, WDK_PropertyType.DRAFT_SUMMARY_TOOLTIP, false);
        MLBTeamsButton = initChildButton(screenToolbarPane, WDK_PropertyType.MLB_TEAMS_ICON, WDK_PropertyType.MLB_TEAMS_TOOLTIP, false);
    }
    
    // CREATES AND SETS UP ALL THE SCREENS TO GO IN THE APP WORKSPACE
    public void initWorkspace(BorderPane screen) {

        
        workspaceScrollPane = new VBox();
        workspaceScrollPane.getChildren().add(screen);
        workspaceScrollPane.getStyleClass().add(CLASS_BORDERED_PANE);
    }
    
    public void updateWorkspace(BorderPane screen) {
        workspaceScrollPane = new VBox();
        workspaceScrollPane.getChildren().add(screen);
        //workspaceScrollPane.setFitToWidth(true);
        workspaceScrollPane.getStyleClass().add(CLASS_BORDERED_PANE);
        wdkPane.setCenter(workspaceScrollPane);
        
    }
    
    // INITIALIZE THE IWNDOW (i.e STAGE) PUTTING ALL THE CONTROLS
    // THERE EXCEPT THE WORKSPACE, WHICH WILL BE ADDED THE FIRST
    // TIME A NEW Draft IS CREATED OR LOADED
    private void initWindow(String windowTitle) {
        // SET THE WINDOW TITLE
        primaryStage.setTitle(windowTitle);
        
        // GET THE SIZE OF THE SCREEN
        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();
        
        // AND USE IT TO SIZE THE WINDOW
        primaryStage.setX(bounds.getMinX());
        primaryStage.setY(bounds.getMinY());
        primaryStage.setWidth(bounds.getWidth());
        primaryStage.setHeight(bounds.getHeight());
        
        // ADD THE TOOLBAR ONLY, NOTE THAT THE WORKSPACE
        // HAS BEEN CONSTRUCTED, BUT WON'T BE ADDED UNTIL
        // THE USER STARTS EDITING A DRAFT
        wdkPane = new BorderPane();
        wdkPane.setTop(fileToolbarPane);
        primaryScene = new Scene(wdkPane);
        
        // NOW TIE THE SCENE TO THE WINDOW, SELECT THE STYLESHEET
        // WE'LL USE THE STYLIZE OUR GUI CONTROLS AND OPEN THE WINDOW
        primaryScene.getStylesheets().add(PRIMARY_STYLE_SHEET);
        primaryStage.setScene(primaryScene);
        primaryStage.show();
    }
    
    // INIT ALL THE EVENT HANDLERS NOT SPECIFIC TO A SCREEN
    private void initEventHandlers() {
        // FIRST THE FILE CONTROLS
        fileController = new FileController(messageDialog, yesNoCancelDialog, draftFileManager, siteExporter);
        newDraftButton.setOnAction(e -> {
            fileController.handleNewDraftRequest(this);
        });
        loadDraftButton.setOnAction(e -> {
          //  fileController.handleLoadDraftRequest(this);
        });
        saveDraftButton.setOnAction(e -> {
           // fileController.handleSaveDraftRequest(this, dataManager.getDraft());
        });
        exportSiteButton.setOnAction(e -> {
          //  fileController.handleExportDraftRequest(this);
        });
        exitButton.setOnAction(e -> {
            fileController.handleExitRequest(this);
        });
        
        // NOW THE SCREEN SWITCHING CONTROLS
        //@todo allow switching of screens
        fantasyTeamsButton.setOnAction(e -> {
            draftController.handleFantasyTeamsRequest(this);
        });
        playersButton.setOnAction(e -> {
            draftController.handlePlayersRequest(this);
        });
        fantasyStandingsButton.setOnAction(e -> {
            draftController.handleFantasyStandingsRequest(this);
        });
        draftSummaryButton.setOnAction(e -> {
            draftController.handleDraftSummaryRequest(this);
        });
        MLBTeamsButton.setOnAction(e -> {
            draftController.handleMLBTeamsRequest(this);
        });
        
    }
        // INIT A BUTTON AND ADD IT TO A CONTAINER IN A TOOLBAR
        private Button initChildButton(Pane toolbar, WDK_PropertyType icon, WDK_PropertyType tooltip, boolean disabled){
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

    /**
     * @return the fantasyTeamsScreen
     */
    public FantasyTeamsScreen getFantasyTeamsScreen() {
        return fantasyTeamsScreen;
    }

    /**
     * @return the playersScreen
     */
    public PlayersScreen getPlayersScreen() {
        return playersScreen;
    }

    /**
     * @return the fantasyStandingsScreen
     */
    public FantasyStandingsScreen getFantasyStandingsScreen() {
        return fantasyStandingsScreen;
    }

    /**
     * @return the draftSummaryScreen
     */
    public DraftSummaryScreen getDraftSummaryScreen() {
        return draftSummaryScreen;
    }

    /**
     * @return the MLBTeamsScreen
     */
    public MLBTeamsScreen getMLBTeamsScreen() {
        return MLBTeamsScreen;
    }
}