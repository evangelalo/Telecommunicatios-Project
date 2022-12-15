/**
 *Η εξιδείκευση του προγράμματος δορυφορικής γραμμής
 *η οποία κληρονομεί από την κλάση plan
 */
public class MobilePlan extends Plan {

    private int freeSms;

    private int freeGb;

    /**
     *
     * @param id
     * @param company
     * @param freeMin
     * @param cost
     * @param freeSms
     * @param freeGb
     */
    public MobilePlan(int id, TelecommunicationCompany company, int freeMin, double cost, int freeSms, int freeGb) {
        super(id, company, freeMin, cost);
        this.freeSms = freeSms;
        this.freeGb = freeGb;
    }

    /**
     *
     * @return
     */
    public int getFreeGb() {
           return freeGb;
       }

       /**
        *
        * @param freeGb
        */
    public void setFreeGb(int freeGb) {
        this.freeGb = freeGb;
    }


    /**
     *
     * @return
     */
    public int getFreeSms() {
        return freeSms;
    }

    /**
     *
     * @param freeSms
     */
    public void setFreeSms(int freeSms) {
        this.freeSms = freeSms;
    }
}
