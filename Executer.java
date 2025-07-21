package Mental_Health_Simulation_Assistant;

import java.util.*;

public class Executer {
    
    public static void main(String args[]){
        Scanner sc = new Scanner(System.in);
        System.out.println("Hi what's your name ?");
        String name = sc.nextLine();
        user user = new user(name);
        Assistant assistant = new Assistant(user);
        while(true){
            System.out.println("*** Checkin Menu ***");
            System.out.println("1. Start a session");
            System.out.println("2. Show mood history");
            System.out.println("3. Terminate the session");

            String ch = sc.nextLine();

            switch (ch) {
                case "1":
                    assistant.startSession(sc);
                    break;

                case "2":
                    assistant.showMoodHistory();
                    break;

                case "3":
                    System.out.println("I hope you had a great session...");
                    break;
            
                default:
                System.out.println("Wrong input try again..");
                    break;
            }
        }
    }
    
}
