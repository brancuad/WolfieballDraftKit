package wdk.data;

import java.util.ArrayList;
import java.util.Arrays;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * This class serves as the super class to both pitcher and hitter.  It contains
 * values for the first and last names, team as well as the contracts and 
 * salaries
 * 
 * @author Brandon
 */
public class Player implements Comparable{
    final StringProperty LAST_NAME;
    final StringProperty FIRST_NAME;
    final StringProperty TEAM;
    final StringProperty NOTES;
    final StringProperty YEAR_OF_BIRTH;
    final StringProperty NATION_OF_BIRTH;
    final StringProperty QP;
    final StringProperty CURRENT_POSITION;
    final StringProperty CONTRACT;
    final StringProperty SALARY;
    Team FANTASY_TEAM;
    public static final String DEFAULT = "";
    
    public Player() {
        LAST_NAME = new SimpleStringProperty(DEFAULT);
        FIRST_NAME = new SimpleStringProperty(DEFAULT);
        TEAM = new SimpleStringProperty(DEFAULT);
        NOTES = new SimpleStringProperty(DEFAULT);
        QP = new SimpleStringProperty(DEFAULT);
        YEAR_OF_BIRTH = new SimpleStringProperty(DEFAULT);
        NATION_OF_BIRTH = new SimpleStringProperty(DEFAULT);
        CURRENT_POSITION = new SimpleStringProperty(DEFAULT);
        CONTRACT = new SimpleStringProperty(DEFAULT);
        SALARY = new SimpleStringProperty(DEFAULT);
        FANTASY_TEAM = new Team();
    }
    
    public void reset() {
        setLastName(DEFAULT);
        setFirstName(DEFAULT);
        setTeam(DEFAULT);
        setNotes(DEFAULT);
        setYearOfBirth(DEFAULT);
        setNationOfBirth(DEFAULT);
        setCurrentPosition(DEFAULT);
    }
    
    public String getLastName() {
        return LAST_NAME.get();
    }
    
    public void setLastName(String initName) {
        LAST_NAME.set(initName);
    }
    
    public StringProperty lastNameProperty() {
        return LAST_NAME;
    }
    
    public String getFirstName() {
        return FIRST_NAME.get();
    }
    
    public void setFirstName(String initName) {
        FIRST_NAME.set(initName);
    }
    
    public StringProperty firstNameProperty() {
        return FIRST_NAME;
    }
    
    public String getTeam() {
        return TEAM.get();
    }
    
    public void setTeam(String initTeam) {
        TEAM.set(initTeam);
    }
    
    public StringProperty teamProperty() {
        return TEAM;
    }
    
    public String getNotes() {
        return NOTES.get();
    }
    
    public void setNotes(String initNotes) {
        NOTES.set(initNotes);
    }
    
    public StringProperty notesProperty() {
        return NOTES;
    }
    
    public String getYearOfBirth() {
        return YEAR_OF_BIRTH.get();
    }
    
    public void setYearOfBirth(String initYearOfBirth) {
        YEAR_OF_BIRTH.set(initYearOfBirth);
    }
    
    public StringProperty yearOfBirthProperty() {
        return YEAR_OF_BIRTH;
    }
    
    public String getNationOfBirth() {
        return NATION_OF_BIRTH.get();
    }
    
    public void setNationOfBirth(String initNationOfBirth) {
        NATION_OF_BIRTH.set(initNationOfBirth);
    }
    
    public StringProperty nationOfBirthProperty() {
        return NATION_OF_BIRTH;
    }
    
    public String getQP() {
        return QP.get();
    }
    
    public void setQP(String initQP) {
        QP.set(initQP);
    }
    
    public StringProperty QPProperty() {
        return QP;
    }
    
    public String getCurrentPosition() {
        return CURRENT_POSITION.get();
    }
    
    public void setCurrentPosition(String initCurrentPos) {
        CURRENT_POSITION.set(initCurrentPos);
    }
    
    public StringProperty currentPositionProperty() {
        return CURRENT_POSITION;
    }
    
    public String getContract() {
        return CONTRACT.get();
    }
    
    public void setContract(String initContract) {
        CONTRACT.set(initContract);
    }
    
    public StringProperty contractProperty() {
        return CONTRACT;
    }
    
    public String getSalary() {
        return SALARY.get();
    }
    
    public void setSalary(String initSalary) {
        SALARY.set(initSalary);
    }
    
    public StringProperty salaryProperty() {
        return SALARY;
    }
    
    public Team getFantasyTeam() {
        return FANTASY_TEAM;
    }
    
    public void setFantasyTeam(Team t) {
        FANTASY_TEAM = t;
    }
    
    
    
    public ArrayList<String> getPositions() {
        // MAKE AN ARRAY LIST OF QP
        ArrayList<String> qpArray = new ArrayList(Arrays.asList(getQP().split("_")));
        
        return qpArray;
    }
    
    @Override
    public int compareTo(Object t) {
        Player otherPlayer = (Player) t;
        return getLastName().compareTo(otherPlayer.getLastName());
    }
}
