public class NotInfected implements Health{
    private boolean immune;

    NotInfected(boolean immune) {
        this.immune = immune;
    }

    @Override
    public boolean haveSymptoms() {
        return false;
    }

    @Override
    public boolean isImmune() {
        return this.immune;
    }

    @Override
    public boolean isInfected() {
        return false;
    }

    @Override
    public void updateRecoveryTime() {}
}
