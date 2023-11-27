public class InfectionProgress {
    
    private int spreadTime;
    private Person infected;
    private Person healthy;

    InfectionProgress(Person infected, Person healthy) {
        this.spreadTime = 75;
        this.infected = infected;
        this.healthy = healthy;
    }

    InfectionProgress(InfectionProgress source) {
        this.spreadTime = source.spreadTime;
        this.infected = new Person(source.infected);
        this.healthy = new Person(source.healthy);
    }

    //on false remove
    public boolean updateProgress() {
        if (this.healthy == null || this.infected == null) {
            return false;
        }

        if (this.healthy.getLocation().spreadPossible(infected.getLocation()) && infected.getHealth().isInfected()) {
            spreadTime -= 1;
            if (spreadTime <= 0) {
                this.healthy.gainInfection(this.infected);
                return false;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    public Person getHealthy() {
        return healthy;
    }

    public Person getInfected() {
        return infected;
    }
}