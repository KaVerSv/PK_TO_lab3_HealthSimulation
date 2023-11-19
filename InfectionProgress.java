public class InfectionProgress {
    
    private int spreadTime;
    private Person infected;
    private Person healthy;

    InfectionProgress(Person infected, Person healthy) {
        this.spreadTime = 75;
        this.infected = infected;
        this.healthy = healthy;
    }

    //on false remove
    public boolean updateProgress() {

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
}