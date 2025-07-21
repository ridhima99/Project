package Mental_Health_Simulation_Assistant;
public class CalmActivity {
    public void breathingExercise(){
        System.out.println("We will start with a breathing exercise");
         try {
            for(int i = 0;i<3;i++){
                System.out.println("Breath in....");
                Thread.sleep(3000);
                System.out.println("Hold The Breath....");
                Thread.sleep(2000);
                System.out.println("Breath out....");
                Thread.sleep(3000);
            } 
        } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                System.out.println("Exercise interrupted");
        }
        System.out.println("I hope it is making you feel better....");
    } 
}
