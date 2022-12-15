/**
 * Η εξειδίκευση του προγράμματος καλωδιακής γραμμής
 * που κληρονομεί από την κλάση plan
 */
public class LandlinePlan extends Plan {

    private int speed;

    private String type;

    /**
     *
     * @param id
     * @param company
     * @param freeMin
     * @param cost
     * @param speed
     * @param type
     */
    public LandlinePlan(int id, TelecommunicationCompany company, int freeMin, double cost, int speed, String type) {
        super(id, company, freeMin, cost);
        this.speed = speed;
        this.type = type;
    }


    /**
     *
     * @return
     */
    public int getSpeed() {
        return speed;
    }

    /**
     *
     * @param speed
     */
    public void setSpeed(int speed) {
        this.speed = speed;
    }

    /**
     *
     * @return
     */
    public String getType() {
        return type;
    }

    /**
     *
     * @param type
     */
    public void setType(String type) {
        this.type = type;
    }
}
