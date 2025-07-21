package Mental_Health_Simulation_Assistant;
import java.util.Random;
public class QuoteProvider {
    private static String quotes[] = {
        "You are stringer than you think.",
        "Take it one step at a time.",
        "This too shall pass.",
        "Breathe. You're doing okay.",
        "It's okay to ask for help.",
        "You matter. Your feelings are valid.",
        "Progress, not perfection."};

    public String getRandomQuote(){
        Random rand = new Random();
        return quotes[rand.nextInt(quotes.length)];
    }
}
