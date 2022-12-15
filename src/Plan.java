/**
 *Η γενική κλάση των προγραμμάτων
 *που θα προσφέρονται
 */
public class Plan {

    /**
     *
     */
    private int id;

    /**
     *
     */
    private TelecommunicationCompany company;

    /**
     *
     */
    private int freeMin;

    /**
     *
     */
    private double cost;

    /**
     *Αύξων μοναδικός κωδικός
     * που καταχωρείται αυτόματα
     * με την δημιουργία ενός νέου
     * προγράμματος
     */
    static int CODE = 0;

    /**
     * Κατασκευαστής
     * @param id
     * @param company
     * @param freeMin
     * @param cost
     */
    public Plan(int id, TelecommunicationCompany company, int freeMin, double cost) {
        this.id = id;
        this.company = company;
        this.freeMin = freeMin;
        this.cost = cost;
        incrementMaxCode();
    }


    /**
     *
     * @return
     */
    public int getId() {
        return id;
    }

    /**
     *
     * @param id
     */
    public void setId(int id) {
        this.id = CODE++;
    }

    /**
     *
     * @return
     */
    public double getCost() {
        return cost;
    }

    /**
     *
     * @param cost
     */
    public void setCost(double cost) {
        this.cost = cost;
    }

    /**
     *
     * @return
     */
    public int getFreeMin() {
        return freeMin;
    }

    /**
     *
     * @param freeMin
     */
    public void setFreeMin(int freeMin) {
        this.freeMin = freeMin;
    }

    /**
     *
     * @return
     */
    public TelecommunicationCompany getCompany() {
        return company;
    }

    /**
     *
     * @param company
     */
    public void setCompany(TelecommunicationCompany company) {
        this.company = company;
    }

    /**
     *
     */
    public void incrementMaxCode() {
        CODE++;
    }
}
