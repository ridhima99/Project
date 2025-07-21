package Mental_Health_Simulation_Assistant;
import java.util.*;
public class user {
    private String name;
    private ArrayList<String>moodHistory;
    public user(String name){
        this.name = name;
        this.moodHistory = new ArrayList<>();

    }
    public String getName(){
        return this.name;
    }
    public ArrayList<String>getMoodHistory(){
        return this.moodHistory;
    }
    public void addMoodHistory(String currentMood){
        this.moodHistory.add(currentMood);        
    }
}
