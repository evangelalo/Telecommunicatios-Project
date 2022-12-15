import java.time.LocalDate;

/**
 * Η κλάση του συμβολαίου
 */
public class Contract {

    /**
     *
     */
    private String id;

    /**
     *
     */
    private long phone;

    /**
     *
     */
    private int afm;

    /**
     *
     */
    private Plan plan;

    /**
     *
     */
    private LocalDate date;

    /**
     *
     */
    private int duration;

    /**
     *
     */
    private double discount;

    /**
     *
     */
    private double cost;

    /**
     *
     */
    private String billType;

    /**
     *
     */
    private String paymentMethod;

    /**
     *
     */
    private double cancelCost;

    /**
     *
     */
    private boolean active;

    /**
     * Ο κατασκευαστής της κλάσης
     * @param id
     * @param phone
     * @param afm
     * @param plan
     * @param date
     * @param duration
     * @param discount
     * @param cost
     * @param billType
     * @param paymentMethod
     * @param cancelCost
     * @param active
     */
    public Contract(String id, long phone, int afm, Plan plan, LocalDate date, int duration, double discount, double cost, String billType, String paymentMethod, double cancelCost, boolean active) {
        this.id = id;
        this.phone = phone;
        this.afm = afm;
        this.plan = plan;
        this.date = date;
        this.duration = duration;
        this.discount = discount;
        this.cost = cost;
        this.billType = billType;
        this.paymentMethod = paymentMethod;
        this.cancelCost = cancelCost;
        this.active = active;
        /**
         * υπολογισμός του κόστους ακύρωσης αυτόματα
         */
        calculateCancelCost();
    }

    /**
     *
     * @return
     */
    public String getId() {
        return id;
    }

    /**
     *
     * @param id
     */
    public void setId(String id) { this.id = id;}

    /**
     *
     * @return
     */
    public long getPhone() {
        return phone;
    }

    /**
     *
     * @param phone
     */
    public void setPhone(long phone) {
        this.phone = phone;
    }

    /**
     *
     * @return
     */
    public int getAfm() {
        return afm;
    }

    /**
     *
     * @param afm
     */
    public void setAfm(int afm) {
        this.afm = afm;
    }

    /**
     *
     * @return
     */
    public Plan getPlan() {
        return plan;
    }

    /**
     *
     * @param plan
     */
    public void setPlan(Plan plan) {
        this.plan = plan;
    }

    /**
     *
     * @return
     */
    public LocalDate getDate() {
        return date;
    }

    /**
     *
     * @param date
     */
    public void setDate(LocalDate date) {
        this.date = date;
    }

    /**
     *
     * @return
     */
    public int getDuration() {
        return duration;
    }

    /**
     *
     * @param duration
     */
    public void setDuration(int duration) {
        this.duration = duration;
    }

    /**
     *
     * @return
     */
    public double getDiscount() {
        return discount;
    }

    /**
     *
     * @param discount
     */
    public void setDiscount(double discount) {
        this.discount = discount;
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
    public String getBillType() {
        return billType;
    }

    /**
     *
     * @param billType
     */
    public void setBillType(String billType) {
        this.billType = billType;
    }

    /**
     *
     * @return
     */
    public String getPaymentMethod() {
        return paymentMethod;
    }

    /**
     *
     * @param paymentMethod
     */
    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    /**
     *
     * @return
     */
    public double getCancelCost() {
        return cancelCost;
    }

    /**
     *
     * @param cancelCost
     */
    public void setCancelCost(double cancelCost) {
        this.cancelCost = cancelCost;
    }

    /**
     *
     * @return
     */
    public boolean isActive() {
        return active;
    }

    /**
     *
     * @param active
     */
    public void setActive(boolean active) {
        this.active = active;
    }

    /**
     *
     */
    public void calculateCancelCost() {
        if (isActive()) {
            if (LocalDate.now().isAfter(this.date.plusMonths(3))) {
                this.cancelCost = this.cost * 0.1;
            }
        } else {
            this.cancelCost = 0.0;
        }
    }

}
