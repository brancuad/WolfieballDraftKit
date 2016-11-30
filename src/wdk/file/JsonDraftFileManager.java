package wdk.file;

import static wdk.WDK_StartupConstants.PATH_DRAFTS;
import wdk.data.Draft;
import wdk.data.Hitter;
import wdk.data.Player;
import wdk.data.Pitcher;
import wdk.data.Team;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.ObservableList;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonWriter;
import javax.json.JsonValue;
import javafx.collections.FXCollections;

/**
 * This is a DraftFileManager that uses the JSON file format to implement
 * the necessary functions for loading and saving different data for our
 * drafts and players
 * @author Brandon
 */
public class JsonDraftFileManager implements DraftFileManager {
    // JSON FILE READING AND WRITING CONSTANTS
    String JSON_PITCHERS = "Pitchers";
    String JSON_HITTERS = "Hitters";
    String JSON_TEAM = "TEAM";
    String JSON_LAST_NAME = "LAST_NAME";
    String JSON_FIRST_NAME = "FIRST_NAME";
    String JSON_NOTES = "NOTES";
    String JSON_YEAR_OF_BIRTH = "YEAR_OF_BIRTH";
    String JSON_NATION_OF_BIRTH = "NATION_OF_BIRTH";
    String JSON_HITTER_QP = "QP";
    String JSON_HITTER_AB = "AB";
    String JSON_HITTER_R = "R";
    String JSON_HITTER_H = "H";
    String JSON_HITTER_HR = "HR";
    String JSON_HITTER_RBI = "RBI";
    String JSON_HITTER_SB = "SB";
    String JSON_PITCHER_IP = "IP";
    String JSON_PITCHER_ER = "ER";
    String JSON_PITCHER_W = "W";
    String JSON_PITCHER_SV = "SV";
    String JSON_PITCHER_H = "H";
    String JSON_PITCHER_BB = "BB";
    String JSON_PITCHER_K = "K";
    String JSON_TEAM_NAME = "TEAM_NAME";
    String JSON_OWNER_NAME = "OWNER_NAME";
    String JSON_STARTING_PITCHERS = "startingPitchers";
    String JSON_TAXI_PITCHERS = "taxiPitchers";
    String JSON_STARTING_HITTERS = "startingHitters";
    String JSON_TAXI_HITTERS = "taxiHitters";
    String JSON_POSITIONS = "positions";
    String JSON_NAME = "name";
    String JSON_VISIBLE_PITCHERS = "visiblePitchers";
    String JSON_VISIBLE_HITTERS = "visibleHitters";
    String JSON_FREE_PITCHERS = "freePitchers";
    String JSON_FREE_HITTERS = "freeHitters";
    String JSON_FANTASY_TEAMS = "fantasyTeams";
    String JSON_FANTASY_TEAM_NAMES = "fantasyTeamNames";
    String JSON_MLB = "teams";
    String JSON_VISIBLE_TEAM = "visibleTeam";
    String JSON_CONTRACT = "contract";
    String JSON_SALARY = "salary";
    String JSON_FANTASY_TEAM = "fantasyTeam";
    String JSON_CURRENT_POSITION = "currentPosition";
    String JSON_EXT = ".json";
    String SLASH = "/";
    
    /**
     * This method saves all the data associated with a draft to
     * a JSON file.
     * 
     * @param draftToSave The draft whose data we are saving.
     * 
     * @throws IOException Thrown when there are issues writing to the JSON
     * file.
     */
    @Override
    public void saveDraft(Draft draftToSave) throws IOException {
        // BUILD THE FILE PATH
        String draftListing = "" + draftToSave.getName();
        String jsonFilePath = PATH_DRAFTS + SLASH + draftListing + JSON_EXT;
        
        // INIT THE WRITER
        OutputStream os = new FileOutputStream(jsonFilePath);
        JsonWriter jsonWriter = Json.createWriter(os);
        
        // MAKE A JSON ARRAY FOR THE PITCHERS
        JsonArray pitchersJsonArray = makePitchersJsonArray(draftToSave.getPitchers(draftToSave.getPlayers()));
        
        // MAKE A JSON ARRAY FOR THE HITTERS
        JsonArray hittersJsonArray = makeHittersJsonArray(draftToSave.getHitters(draftToSave.getPlayers()));
        
        // MAKE A JSON ARRAY FOR THE PITCHING FREE AGENTS
        JsonArray freePitchersJsonArray = makePitchersJsonArray(draftToSave.getPitchers(draftToSave.getFreeAgents()));
        
        // MAKE A JSON ARRAY FOR THE HITTING FREE AGENTS
        JsonArray freeHittersJsonArray = makeHittersJsonArray(draftToSave.getHitters(draftToSave.getFreeAgents()));
        
        // MAKE A JSON ARRAY FOR THE VISIBLE PITCHERS
        JsonArray visiblePitchersJsonArray = makePitchersJsonArray(draftToSave.getPitchers(draftToSave.getVisiblePlayers()));
        
        // MAKE A JSON ARRAY FOR THE VISIBLE HITTERS
        JsonArray visibleHittersJsonArray = makeHittersJsonArray(draftToSave.getHitters(draftToSave.getVisiblePlayers()));
        
        // MAKE A JSON ARRAY FOR THE FANTASY TEAMS
        JsonArray fantasyTeamsJsonArray = makeTeamsJsonArray(draftToSave.getFantasyTeams());
        
        // MAKE A JSON ARRAY FOR THE FANTASY TEAM NAMES
        JsonArray fantasyTeamNamesJsonArray = makeStringsJsonArray(draftToSave.getFantasyTeamNames());
        
        // MAKE A JSON ARRAY FOR THE MLB TEAMS
        JsonArray MLBTeamsJsonArray = makeStringsJsonArray(draftToSave.getTeams());
        
        // MAKE A TEAM OBJECT FOR THE VISIBLE TEAM
        JsonObject visibleTeamObject = makeTeamJsonObject(draftToSave.getVisibleTeam());
        
        // NOW BUILD THE DRAFT USING EVERYTHING WE JUST MADE
        JsonObject draftJsonObject = Json.createObjectBuilder()
                                    .add(JSON_NAME, draftToSave.getName())
                                    .add(JSON_PITCHERS, pitchersJsonArray)
                                    .add(JSON_HITTERS, hittersJsonArray)
                                    .add(JSON_VISIBLE_PITCHERS, visiblePitchersJsonArray)
                                    .add(JSON_VISIBLE_HITTERS, visibleHittersJsonArray)
                                    .add(JSON_FREE_PITCHERS, freePitchersJsonArray)
                                    .add(JSON_FREE_HITTERS, freeHittersJsonArray)
                                    .add(JSON_FANTASY_TEAMS, fantasyTeamsJsonArray)
                                    .add(JSON_FANTASY_TEAM_NAMES, fantasyTeamNamesJsonArray)
                                    .add(JSON_MLB, MLBTeamsJsonArray)
                                    .add(JSON_VISIBLE_TEAM, visibleTeamObject)
                                    .build();
        
        // AND SAVE EVERYTHING AT ONCE
        jsonWriter.writeObject(draftJsonObject);
        
        
    }
    
    /**
     * Loads the draftToLoad argument using the data found in the json file.
     * 
     * @param draftToLoad Draft to load.
     * @param jsonFilePath File containing the data to load.
     * 
     * @throws IOException Thrown when IO fails.
     */
    @Override
    public void loadDraft(Draft draftToLoad, String jsonFilePath) throws IOException {
        // LOAD THE JSON FILE WITH ALL THE DATA
        JsonObject json = loadJSONFile(jsonFilePath);
        
        // NOW LOAD THE DRAFT
        draftToLoad.setName(json.getString(JSON_NAME));
        
        // GET THE PLAYERS
        draftToLoad.getPlayers().clear();
        JsonArray jsonPitchersArray = json.getJsonArray(JSON_PITCHERS);
       // jsonPlayersArray.addAll(json.getJsonArray(JSON_HITTERS));
        for (int i = 0 ; i < jsonPitchersArray.size(); i++) {
            Pitcher p = new Pitcher();
            JsonObject jso = jsonPitchersArray.getJsonObject(i);
            loadPitcherJsonObject(p, jso);
            
            // ADD IT TO THE DRAFT
            draftToLoad.addPlayer(p);
        }
        JsonArray jsonHittersArray = json.getJsonArray(JSON_HITTERS);
        for (int i = 0 ; i < jsonHittersArray.size(); i++) {
            Hitter h = new Hitter();
            JsonObject jso = jsonHittersArray.getJsonObject(i);
            loadHitterJsonObject(h, jso);
        }
        
        
        
        
    }
    
    // LOADS A PITCHER FROM A JSON OBJECT
    public void loadPitcherJsonObject(Pitcher p, JsonObject jso) {
        p.setBB(jso.getString(JSON_PITCHER_BB));
        p.setContract(jso.getString(JSON_CONTRACT));
        p.setCurrentPosition(jso.getString(JSON_CURRENT_POSITION));
        p.setER(jso.getString(JSON_PITCHER_ER));
        Team t = new Team();
        JsonObject teamObject = jso.getJsonObject(JSON_FANTASY_TEAM);
//        loadTeamJsonObject(t, teamObject);
        p.setFantasyTeam(t);
        p.setFirstName(jso.getString(JSON_FIRST_NAME));
        p.setH(jso.getString(JSON_PITCHER_H));
        p.setIP(jso.getString(JSON_PITCHER_IP));
        p.setK(jso.getString(JSON_PITCHER_K));
        p.setLastName(jso.getString(JSON_LAST_NAME));
        p.setNationOfBirth(jso.getString(JSON_NATION_OF_BIRTH));
        p.setNotes(jso.getString(JSON_NOTES));
        p.setSV(jso.getString(JSON_PITCHER_SV));
        p.setSalary(jso.getString(JSON_SALARY));
        p.setTeam(jso.getString(JSON_TEAM));
        p.setW(jso.getString(JSON_PITCHER_W));
        p.setYearOfBirth(jso.getString(JSON_YEAR_OF_BIRTH));
        
        
    }
    
    // LOADS A HITTER FROM A JSON OBJECT
    public void loadHitterJsonObject(Hitter h, JsonObject jso) {
        h.setAB(jso.getString(JSON_HITTER_AB));
        h.setContract(jso.getString(JSON_CONTRACT));
        h.setCurrentPosition(JSON_CURRENT_POSITION);
        Team t = new Team();
        JsonObject teamObject = jso.getJsonObject(JSON_FANTASY_TEAM);
//        loadTeamJsonObject(t, teamObject);
        h.setFantasyTeam(t);
        h.setFirstName(jso.getString(JSON_FIRST_NAME));
        h.setH(jso.getString(JSON_HITTER_H));
        h.setHR(jso.getString(JSON_HITTER_HR));
        h.setLastName(jso.getString(JSON_LAST_NAME));
        h.setNationOfBirth(jso.getString(JSON_NATION_OF_BIRTH));
        h.setNotes(jso.getString(JSON_NOTES));
        h.setQPSave(jso.getString(JSON_POSITIONS));
        h.setR(jso.getString(JSON_HITTER_R));
        h.setRBI(jso.getString(JSON_HITTER_RBI));
        h.setSB(jso.getString(JSON_HITTER_SB));
        h.setSalary(jso.getString(JSON_SALARY));
        h.setTeam(jso.getString(JSON_TEAM));
        h.setYearOfBirth(jso.getString(JSON_YEAR_OF_BIRTH));
    }
    
    /**
     * Saves the players list to a json file.
     * @param players List of players to save.
     * @param jsonFilePath Path of json file.
     * @throws IOException Thrown when I/O fails.
     */
    @Override
    public void savePlayers(List<Object> players, String jsonFilepath) throws IOException {
        
    }
    
    /**
     * Loads players from the json file.
     * @param jsonFilePath Json file containing the players.
     * @return List full of Players loaded from the file.
     * @throws IOException Thrown when I/O fails.
     */
    @Override
    public ObservableList<Player> loadPlayers(String pitcherFilePath, String hitterFilePath) throws IOException {
        ObservableList<Player> playersList = loadPitchers(pitcherFilePath, JSON_PITCHERS);
        playersList.addAll(loadHitters(hitterFilePath, JSON_HITTERS));
        return playersList;
    }
    
    // AND HERE ARE THE PRIVATE HELPER METHODS TO HELP THE PUBLIC ONES
    
    // LOADS A JSON FILE AS A SINGLE OBJECT AND RETURNS IT
    private JsonObject loadJSONFile(String jsonFilePath) throws IOException {
        InputStream is = new FileInputStream(jsonFilePath);
        JsonReader jsonReader = Json.createReader(is);
        JsonObject json = jsonReader.readObject();
        jsonReader.close();
        is.close();
        return json;   
    }
    
    // BUILDS AND RETURNS A JsonArray CONTAINING THE PROVIDED DATA
    public JsonArray buildJsonArray(List<Object> data) {
        JsonArrayBuilder jsb = Json.createArrayBuilder();
        for (Object d : data) {
            jsb.add(d.toString());
        }
        JsonArray jA = jsb.build();
        return jA;
    }
    
    // BUILDS AND RETURNS A JsonObject CONTAINING A JsonArray
    // THAT CONTAINS THE PROVIDED DATA
    private JsonObject buildJsonArrayObject(List<Object> data) {
        JsonArray jA = buildJsonArray(data);
        JsonObject arrayObject = Json.createObjectBuilder().add(JSON_PITCHERS, jA).build();
        return arrayObject;
    }
    
    // LOADS AN ObservableList OF PITCHERS 
    private ObservableList<Player> loadPitchers(String jsonFilePath, String arrayName) throws IOException {
        // LOAD THE JSON FILE WITH THE PITCHER DATA
        JsonObject json = loadJSONFile(jsonFilePath);
        // @todo json array to load pitchers
        
        // MAKE OBSERVABLE LIST OF PITCHERS
        ObservableList<Player> pitchers = FXCollections.observableArrayList();
        JsonArray jsonArray = json.getJsonArray(arrayName);
        
        // READ DATA FOR ALL ITEMS IN THE LIST OF PITCHERS
        for (int i = 0; i < jsonArray.size(); i++) {
            pitchers.add(loadPitcher((JsonObject)jsonArray.get(i)));
        }
        
        return pitchers;
        
    }
    
    // LOADS A SINGLE PLAYER FROM A JsonObject
    private Pitcher loadPitcher(JsonObject json) throws IOException {
        Pitcher pitcher = new Pitcher();

        //LOAD THE PITCHER
        pitcher.setTeam(json.getString(JSON_TEAM));
        pitcher.setLastName(json.getString(JSON_LAST_NAME));
        pitcher.setFirstName(json.getString(JSON_FIRST_NAME));
        pitcher.setIP(json.getString(JSON_PITCHER_IP));
        pitcher.setER(json.getString(JSON_PITCHER_ER));
        pitcher.setW(json.getString(JSON_PITCHER_W));
        pitcher.setSV(json.getString(JSON_PITCHER_SV));
        pitcher.setH(json.getString(JSON_PITCHER_H));
        pitcher.setBB(json.getString(JSON_PITCHER_BB));
        pitcher.setK(json.getString(JSON_PITCHER_K));
        pitcher.setERA();
        pitcher.setWHIP();
        pitcher.setNotes(json.getString(JSON_NOTES));
        pitcher.setYearOfBirth(json.getString(JSON_YEAR_OF_BIRTH));
        pitcher.setNationOfBirth(json.getString(JSON_NATION_OF_BIRTH));
        
        return pitcher;
        
    }
    
    // LOADS AN ObservableList OF HITTERS
    private ObservableList<Player> loadHitters(String jsonFilePath, String arrayName) throws IOException {
        // LOAD THE JSON FILE WITH THE HITTER DATA
        JsonObject json = loadJSONFile(jsonFilePath);
        
        // MAKE OBSERVABLE LIST OF HITTERS
        ObservableList<Player> hitters = FXCollections.observableArrayList();
        JsonArray jsonArray = json.getJsonArray(arrayName);
        
        // READ DATA FOR ALL ITEMS IN THE LIST OF PITCHERS
        for (int i = 0; i < jsonArray.size(); i++) {
            hitters.add(loadHitter((JsonObject)jsonArray.get(i)));
        }
        
        return hitters;
        
    }
    
    // LOADS A SINGLE PLAYER FROM A JsonObject
    private Hitter loadHitter(JsonObject json) throws IOException {
        Hitter hitter = new Hitter();

        //LOAD THE HITTER
        hitter.setTeam(json.getString(JSON_TEAM));
        hitter.setLastName(json.getString(JSON_LAST_NAME));
        hitter.setFirstName(json.getString(JSON_FIRST_NAME));
        hitter.setQP(json.getString(JSON_HITTER_QP));
        hitter.setAB(json.getString(JSON_HITTER_AB));
        hitter.setR(json.getString(JSON_HITTER_R));
        hitter.setH(json.getString(JSON_HITTER_H));
        hitter.setHR(json.getString(JSON_HITTER_HR));
        hitter.setRBI(json.getString(JSON_HITTER_RBI));
        hitter.setSB(json.getString(JSON_HITTER_SB));
        hitter.setBA();
        hitter.setNotes(json.getString(JSON_NOTES));
        hitter.setYearOfBirth(json.getString(JSON_YEAR_OF_BIRTH));
        hitter.setNationOfBirth(json.getString(JSON_NATION_OF_BIRTH));
        
        return hitter;
        
    }
    
    // BUILDS AND RETURNS A JsonArray CONTAINING THE PITCHERS FOR THIS DRAFT
    public JsonArray makePitchersJsonArray(List<Pitcher> data) {
        JsonArrayBuilder jsb = Json.createArrayBuilder();
        for (Pitcher p : data) {
            jsb.add(makePitcherJsonObject(p));
        }
        JsonArray jA = jsb.build();
        return jA;
    }
    
    // BUILDS AND RETURNS A JsonArray CONTAINING THE HITTERS FOR THIS DRAFT
    public JsonArray makeHittersJsonArray(List<Hitter> data) {
        JsonArrayBuilder jsb = Json.createArrayBuilder();
        for (Hitter h : data) {
            jsb.add(makeHitterJsonObject(h));
        }
        JsonArray jA = jsb.build();
        return jA;
    }
    
    // BUILDS AND RETURNS A JsonArray CONTAINING THE TEAM FOR THE DRAFT
    public JsonArray makeTeamsJsonArray(List<Team> data) {
        JsonArrayBuilder jsb = Json.createArrayBuilder();
        for (Team t : data) {
            jsb.add(makeTeamJsonObject(t));
        }
        JsonArray jA = jsb.build();
        return jA;
    }
    
    // BUILDS AND RETURNS A JsonArray CONTAINING STRINGS
    public JsonArray makeStringsJsonArray(List<String> data) {
        JsonArrayBuilder jsb = Json.createArrayBuilder();
        for (String s : data) {
            jsb.add(s);
        }
        JsonArray jA = jsb.build();
        return jA;
    }
    
    // MAKES AND RETURNS A JSON OBJECT FOR THE PROVIDED PITCHER
    private JsonObject makePitcherJsonObject(Pitcher p) {
        // MAKE FANTASY TEAM OBJECT
        JsonObject fantasyTeamObject = makeTeamJsonObject(p.getFantasyTeam());
        
        JsonObject jso = Json.createObjectBuilder().add(JSON_TEAM, p.getTeam())
                                                    .add(JSON_LAST_NAME, p.getLastName())
                                                    .add(JSON_FIRST_NAME, p.getFirstName())
                                                    .add(JSON_NOTES, p.getNotes())
                                                    .add(JSON_YEAR_OF_BIRTH, p.getYearOfBirth())
                                                    .add(JSON_NATION_OF_BIRTH, p.getNationOfBirth())
                                                    .add(JSON_PITCHER_IP, p.getIP())
                                                    .add(JSON_PITCHER_ER, p.getER())
                                                    .add(JSON_PITCHER_W, p.getW()) 
                                                    .add(JSON_PITCHER_SV, p.getSV())
                                                    .add(JSON_PITCHER_H, p.getH())
                                                    .add(JSON_PITCHER_BB, p.getBB())
                                                    .add(JSON_PITCHER_K, p.getK())
                                                    .add(JSON_CONTRACT, p.getContract())
                                                    .add(JSON_SALARY, p.getSalary())
                                                    .add(JSON_CURRENT_POSITION, p.getCurrentPosition())
                                                    .add(JSON_FANTASY_TEAM, fantasyTeamObject)
                                                    .build();
        return jso;
    }
    
    // MAKES AND RETURNS A JSON OBJECT FOR THE PROVIDED HITTER
    private JsonObject makeHitterJsonObject(Hitter h) {
                
        JsonObject fantasyTeamObject = makeTeamJsonObject(h.getFantasyTeam());
        
        JsonObject jso = Json.createObjectBuilder().add(JSON_TEAM, h.getTeam())
                                                    .add(JSON_LAST_NAME, h.getLastName())
                                                    .add(JSON_FIRST_NAME, h.getFirstName())
                                                    .add(JSON_NOTES, h.getNotes())
                                                    .add(JSON_YEAR_OF_BIRTH, h.getYearOfBirth())
                                                    .add(JSON_NATION_OF_BIRTH, h.getNationOfBirth())
                                                    .add(JSON_HITTER_QP, h.getQP())
                                                    .add(JSON_HITTER_AB, h.getAB())
                                                    .add(JSON_HITTER_R, h.getR())
                                                    .add(JSON_HITTER_H, h.getH())
                                                    .add(JSON_HITTER_HR, h.getHR())
                                                    .add(JSON_HITTER_RBI, h.getRBI())
                                                    .add(JSON_HITTER_SB, h.getSB())
                                                    .add(JSON_CONTRACT, h.getContract())
                                                    .add(JSON_SALARY, h.getSalary())
                                                    .add(JSON_CURRENT_POSITION, h.getCurrentPosition())
                                                    .add(JSON_FANTASY_TEAM, fantasyTeamObject)
                                                    .build();
        return jso;
    }
    
    // MAKES AND RETURNS A JSON OBJECT FOR THE PROVIDED TEAM
    private JsonObject makeTeamJsonObject(Team t) {
        
        // MAKE A JSON ARRAY FOR THE STARTING PITCHERS
        JsonArray startingPitchersJsonArray = makePitchersJsonArray(t.getPitchers(t.getStartingPlayers()));
        
        // MAKE A JSON ARRAY FOR THE STARTING HITTERS
        JsonArray startingHittersJsonArray = makeHittersJsonArray(t.getHitters(t.getStartingPlayers()));
        
        // MAKE A JSON ARRAY FOR THE TAXI PITCHERS
        JsonArray taxiPitchersJsonArray = makePitchersJsonArray(t.getPitchers(t.getStartingPlayers()));
        
        // MAKE A JSON ARRAY FOR THE TAXI HITTERS
        JsonArray taxiHittersJsonArray = makeHittersJsonArray(t.getHitters(t.getStartingPlayers()));
        
        // MAKE A JSON ARRAY FOR THE POSITIONS
        JsonArray positionsJsonArray = makeStringsJsonArray(t.getPositions());
        
        JsonObject jso = Json.createObjectBuilder().add(JSON_TEAM_NAME, t.getTeamName())
                                                    .add(JSON_OWNER_NAME, t.getOwnerName())
                                                    .add(JSON_STARTING_PITCHERS, startingPitchersJsonArray)
                                                    .add(JSON_STARTING_HITTERS, startingHittersJsonArray)
                                                    .add(JSON_TAXI_PITCHERS, taxiPitchersJsonArray)
                                                    .add(JSON_TAXI_HITTERS, taxiHittersJsonArray)
                                                    .add(JSON_POSITIONS, positionsJsonArray)
                                                    .build();
        return jso;
    }
}
