import java.util.ArrayList;

public class InfectedList {
    // Prywatne pole przechowujące jedyną instancję klasy
    private static InfectedList instance;

    private ArrayList<Person> infected;

    private InfectedList() {
        this.infected = new ArrayList<Person>();
    }

    // Metoda do uzyskania instancji singletona
    public static InfectedList getInstance() {
        if (instance == null) {
            // Jeżeli instancja nie istnieje, utwórz ją
            instance = new InfectedList();
        }
        return instance;
    }

    public void addInfected(Person infected) {
        this.infected.add(infected);
    }

    public void removeInfected(Person recovered) {
        this.infected.remove(recovered);
    }

    public ArrayList<Person> getInfectedList() {
        return this.infected;
    }
}