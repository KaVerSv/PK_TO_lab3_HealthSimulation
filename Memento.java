import java.util.ArrayList;

class Memento {
    private final ArrayList<Person> population;
    private final ArrayList<InfectionProgress> infections;
    private final ArrayList<Person> infectedList;
    private final int step;

    public Memento(ArrayList<Person> population, int step, ArrayList<InfectionProgress> infections, ArrayList<Person> infectedList)  {
        this.step = step;
        this.population = new ArrayList<>(population);
        this.infections = new ArrayList<>(infections);
        this.infectedList = new ArrayList<>(infectedList);
    }

    public ArrayList<Person> getPopulation() {
        return new ArrayList<>(population);
    }

    public int getStep() {
        return step;
    }

    public ArrayList<InfectionProgress> getInfections() {
        return new ArrayList<>(infections);
    }

    public ArrayList<Person> getInfectedList() {
        return new ArrayList<>(infectedList);
    }
}