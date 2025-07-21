package Mental_Health_Simulation_Assistant;
import java.util. * ;
public class MoodTracker {
    public String askMood(Scanner sc, user user){
        System.out.println("Hi," + user.getName()+" how are you feeling today ?");
        System.out.println("Options: Happy, Sad, Amxious, Angry, Tired, Excited, Okay");
        System.out.println("Your Mood: ");
        String mood = sc.nextLine();
        return mood;

    }
}
