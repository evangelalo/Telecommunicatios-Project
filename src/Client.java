/**
 *Η κλάση του πελάτη
 */
public class Client {

    /**
     *
     */
    private int id;

    /**
     *
     */
    private int afm;

    /**
     *
     */
    private String name;

    /**
     *
     */
    private String occupation;

    /**
     *
     */
    private String address;

    /**
     *
     */
    private long phone;

    /**
     *
     */
    private String email;

    /**
     *
     * @param id
     * @param afm
     * @param name
     * @param occupation
     * @param address
     * @param phone
     * @param email
     */
    public Client(int id, int afm, String name, String occupation, String address, long phone, String email) {
        this.id = id;
        this.afm = afm;
        this.name = name;
        this.occupation = occupation;
        this.address = address;
        this.phone = phone;
        this.email = email;
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
        this.id = id;
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
    public String getName() {
        return name;
    }

    /**
     *
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     *
     * @return
     */
    public String getOccupation() {
        return occupation;
    }

    /**
     *
     * @param occupation
     */
    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    /**
     *
     * @return
     */
    public String getAddress() {
        return address;
    }

    /**
     *
     * @param address
     */
    public void setAddress(String address) {
        this.address = address;
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
}

