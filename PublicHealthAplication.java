

public class PublicHealthAplication {
    public static void main(String[] args) {
        
        

        int population = 100;
        boolean immune= false;
        InfectedList.getInstance();
        Simulation symulacja = new Simulation(population, immune);
        symulacja.startSimulation();
    }
}
