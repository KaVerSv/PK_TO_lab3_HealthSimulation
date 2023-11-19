interface Health {
    public boolean haveSymptoms();
    public boolean isImmune();
    public boolean isInfected();

    public void updateRecoveryTime();
}
