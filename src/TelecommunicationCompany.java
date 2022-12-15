/**
 *Η κλάση της εταιρείας τηλεπικοινωνιών
 */
public class TelecommunicationCompany {

    private int id;

    private String name;

    private String email;

    private long phone;

    /**
     *
     *Αύξων μοναδικός κωδικός
     * που καταχωρείται αυτόματα
     * με την δημιουργία ενός νέου
     * προγράμματος
     */
    static int CODE = 0;

    /**
     *
     * @param id
     * @param name
     * @param phone
     * @param email
     */
    public TelecommunicationCompany(int id, String name, long phone, String email) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.email = email;
        incrementMaxCode();
    }


    /**
     *
     * @param name
     */
    public void setName(String name) {this.name = name;}

    /**
     *
     * @return
     */
    public int getId() {return id;}

    /**
     *
     * @return
     */
    public String getName() {return name;}

    /**
     *
     */
    public void incrementMaxCode() {
        CODE++;}

    /**
     *
     * @param id
     */
    public void setId(int id) {this.id = CODE++;}

    /**
     *
     * @return
     */
    public String getEmail() {
        return email;
    }

    /**
     *
     * @param email
     */
    public void setEmail(String email) {
        this.email = email;
    }

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
}
