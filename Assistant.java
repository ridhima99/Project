package Mental_Health_Simulation_Assistant;

import java.util.*;

public class Assistant {
    private user user;
    private CalmActivity ca;
    private QuoteProvider qp;
    private  MoodTracker mt;

    Assistant (user user){
        this.user = user;
        ca = new CalmActivity();
        qp = new QuoteProvider();
        mt = new MoodTracker();
    }
    public void startSession(Scanner sc){
        String mood = mt.askMood(sc,user );
        System.out.println("Logging the mood history");
        user.addMoodHistory(mood);

        qp.getRandomQuote();

        System.out.println("Would you like to do some Exercise?(yes/no):");
        String ans = sc.nextLine().trim().toLowerCase();

        if(ans.equals("yes")){
            ca.breathingExercise();
        }
            System.out.println("Thank you for initiating this session!");
    }
    
    public void showMoodHistory(){
        for(String mood : user.getMoodHistory()){
            System.out.println(mood+",");
        }
        System.out.println();
    }
}
