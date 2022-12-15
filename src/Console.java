import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

/**
 * Η κλάση που δημιουργεί την διεπαφή του προγράμματος
 * και που διαβάζει και αποθηκεύει τα δεδομένα στο αρχείο
 * entries.txt
 */
public class Console {

    /**
     * Οι λίστες με τις συλλογές των αντικειμένων της καθε κλάσης
     */

    private static final ArrayList<TelecommunicationCompany> companies = new ArrayList<>();

    private static final ArrayList<Contract> contracts = new ArrayList<>();

    private static final ArrayList<Plan> plans = new ArrayList<>();

    private static final ArrayList<Client> clients = new ArrayList<>();

    private final JFrame myFrame;

    /**
     *Ο κατασκευαστής
     * @param myFrame
     */
    public Console(JFrame myFrame) {
        this.myFrame =myFrame;
    }

    /**
     *Η μέθοδος που δημιουργεί το αρχείο .txt και αποθηκεύει τις εγγραφές των
     * αντικειμένων
     */
    public void CreateFile() {
        try {
            File file = new File("src/entries.txt");
            FileWriter writer = new FileWriter(file);
            for (TelecommunicationCompany company : companies) {
                writer.write("company"+","+company.getId()+","+company.getName()+","+company.getPhone()+","+company.getEmail()+"\n");
            }
            for (Plan plan : plans) {
                if (plan instanceof MobilePlan) {
                    writer.write("plan"+","+"mobile"+","+plan.getId()+","+plan.getCompany().getId()+","+plan.getFreeMin()+","+plan.getCost()+","+((MobilePlan) plan).getFreeSms()+","+((MobilePlan) plan).getFreeGb()+"\n");
                } else if(plan instanceof LandlinePlan){
                    writer.write("plan"+","+"landline"+","+plan.getId()+","+plan.getCompany().getId()+","+plan.getFreeMin()+","+plan.getCost()+","+((LandlinePlan) plan).getSpeed() + "," +((LandlinePlan) plan).getType()+"\n");
                }
            }
            for (Client client : clients) {
                writer.write("client"+","+client.getId()+","+client.getAfm()+","+client.getName()+","+client.getOccupation()+","+client.getAddress()+","+client.getPhone()+","+client.getEmail()+"\n");
            }
            for (Contract contract : contracts) {
                writer.write("contract"+","+contract.getId()+","+contract.getPhone()+","+contract.getAfm()+","+contract.getPlan().getId()+","+contract.getDate()+","+contract.getDuration()+","+contract.getDiscount()+","+contract.getCost()+","+contract.getBillType()+","+contract.getPaymentMethod()+","+contract.getCancelCost()+","+contract.isActive()+"\n");
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *Η μέθοδος που διαβάζει από το αρχείο .txt τα αποθηκευμένα δεδομένα
     */
    public void ReadFile() {
        try {
            FileReader file = new FileReader("src/entries.txt");
            BufferedReader reader = new BufferedReader(file);
            String line = "";
            while ((line = reader.readLine()) != null) {
                String[] tokens = line.split(",");
                if (tokens[0].equals("company")){
                    TelecommunicationCompany company = new TelecommunicationCompany(Integer.parseInt(tokens[1]), tokens[2],Long.parseLong(tokens[3]), tokens[4]);
                    companies.add(company);
                } else if (tokens[0].equals("plan")) {
                    if (tokens[1].equals("mobile")) {
                        Plan plan = new MobilePlan(Integer.parseInt(tokens[2]), findTelecommunicationCompanyById(tokens[3]), Integer.parseInt(tokens[4]), Double.parseDouble(tokens[5]), Integer.parseInt(tokens[6]), Integer.parseInt(tokens[7]));
                        plans.add(plan);
                    } else {
                        Plan plan = new LandlinePlan(Integer.parseInt(tokens[2]), findTelecommunicationCompanyById(tokens[3]), Integer.parseInt(tokens[4]), Double.parseDouble(tokens[5]), Integer.parseInt(tokens[6]), tokens[7]);
                        plans.add(plan);
                    }
                } else if (tokens[0].equals("client")) {
                    Client client = new Client(Integer.parseInt(tokens[1]), Integer.parseInt(tokens[2]), tokens[3], tokens[4], tokens[5], Long.parseLong(tokens[6]), tokens[7]);
                    clients.add(client);
                } else if (tokens[0].equals("contract")) {
                    Contract contract = new Contract(tokens[1], Long.parseLong(tokens[2]), Integer.parseInt(tokens[3]), findPlanById(Integer.parseInt(tokens[4])), LocalDate.parse(tokens[5]), Integer.parseInt(tokens[6]), Double.parseDouble(tokens[7]), Double.parseDouble(tokens[8]), tokens[9], tokens[10], Double.parseDouble(tokens[11]), Boolean.parseBoolean(tokens[12]));
                    contracts.add(contract);
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //////////////////Βοηθητικές Μέθοδοι εύρεσης, διαγραφής, και ενημέρωσης των στοιχείων της κάθε λίστας/////////////

    /**
     *
     * @param name
     * @param phone
     * @param email
     */
    public static void addTelecommunicationCompany(String name, long phone, String email) {
        companies.add(new TelecommunicationCompany(TelecommunicationCompany.CODE++, name, phone, email));
    }

    /**
     *
     * @param name
     */
    public static void removeTelecommunicationCompany(String name) {
        companies.remove(findTelecommunicationCompanyByName(name));
    }

    /**
     *
     * @param id
     */
    public static void removePlan(int id) {
        plans.remove(findPlanById(id));}

    /**
     *
     * @param id
     */
    public static void removeClient(int id) {clients.remove(findClientById(id));}

    /**
     *
     * @param id
     * @return
     */
    public static Contract findContractById(String id) {
        for (Contract contract : contracts) {
            if (contract.getId().equals(id)) {
                return contract;
            }
        }
        return null;
    }

    /**
     *
     * @param id
     * @return
     */
    public static Client findClientById(int id) {
        for (Client client : clients) {
            if (client.getId() == id) {
                return client;
            }
        }
        return null;
    }

    /**
     *
     * @param afm
     * @return
     */
    public static Client findClientByAfm(int afm) {
        for (Client client : clients) {
            if (client.getAfm() == afm) {
                return client;
            }
        }
        return null;
    }

    /**
     *
     * @param name
     * @return
     */
    public static TelecommunicationCompany findTelecommunicationCompanyByName(String name) {
        for (TelecommunicationCompany telecommunicationCompany : companies) {
            if(telecommunicationCompany.getName().equals(name)) {
                return telecommunicationCompany;
            }
        }
        return null;
    }

    /**
     *
     * @param id
     * @return
     */
    public static TelecommunicationCompany findTelecommunicationCompanyById(String id) {
        for (TelecommunicationCompany telecommunicationCompany : companies) {
            if(telecommunicationCompany.getId() == Integer.parseInt(id)) {
                return telecommunicationCompany;
            }
        }
        return null;
    }

    /**
     *
     * @param id
     * @return
     */
    public static Plan findPlanById(int id) {
        for (Plan plan : plans) {
            if (plan.getId() == id) {
                return plan;
            }
        }
        return null;
    }

    /**
     *
     * @return
     */
    public JPanel mainMenu() {
        Font myFont = new Font(Font.SANS_SERIF, Font.BOLD, 20);
        JButton companiesBtn = new JButton("Companies", new ImageIcon("src/images/company.png"));
        companiesBtn.setFont(myFont);
        companiesBtn.setVerticalTextPosition(SwingConstants.BOTTOM);
        companiesBtn.setHorizontalTextPosition(SwingConstants.CENTER);
        JButton clientBtn = new JButton("Clients", new ImageIcon("src/images/client.png"));
        clientBtn.setFont(myFont);
        clientBtn.setVerticalTextPosition(SwingConstants.BOTTOM);
        clientBtn.setHorizontalTextPosition(SwingConstants.CENTER);
        JButton plansBtn = new JButton("Plans", new ImageIcon("src/images/plan.png"));
        plansBtn.setFont(myFont);
        plansBtn.setVerticalTextPosition(SwingConstants.BOTTOM);
        plansBtn.setHorizontalTextPosition(SwingConstants.CENTER);
        JButton contractBtn = new JButton("Contracts", new ImageIcon("src/images/contract.png"));
        contractBtn.setFont(myFont);
        contractBtn.setVerticalTextPosition(SwingConstants.BOTTOM);
        contractBtn.setHorizontalTextPosition(SwingConstants.CENTER);

        JPanel myPanel = new JPanel(new GridLayout(2, 1));
        JPanel panel1 = new JPanel(new GridLayout(1, 4));
        panel1.add(companiesBtn);
        panel1.add(clientBtn);
        panel1.add(plansBtn);
        myPanel.add(panel1);
        JPanel panel2 = new JPanel(new GridLayout(1, 1));
        panel2.add(contractBtn);
        myPanel.add(panel2);
        myFrame.add(myPanel);

        companiesBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                myFrame.getContentPane().removeAll();
                myFrame.invalidate();
                myFrame.setContentPane(panelTelecommunicationCompany());
                myFrame.repaint();
                myFrame.revalidate();
            }
        });

        clientBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                myFrame.getContentPane().removeAll();
                myFrame.invalidate();
                myFrame.setContentPane(panelClient());
                myFrame.repaint();
                myFrame.revalidate();
            }
        });

        plansBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                myFrame.getContentPane().removeAll();
                myFrame.invalidate();
                myFrame.setContentPane(panelPlan());
                myFrame.repaint();
                myFrame.revalidate();
            }
        });

        contractBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                myFrame.getContentPane().removeAll();
                myFrame.invalidate();
                myFrame.setContentPane(panelContract());
                myFrame.repaint();
                myFrame.revalidate();
            }
        });
        CreateFile();
        return myPanel;
    }

    /**
     *
     * @return
     */
    public String[][] updateTelecommunicationCompanyTable() {
        String[][] table = new String[companies.size()][4];
        int i = 0;
        for (TelecommunicationCompany telecommunicationCompany : companies) {
            table[i][0] = String.valueOf(telecommunicationCompany.getId());
            table[i][1] = telecommunicationCompany.getName();
            table[i][2] = String.valueOf(telecommunicationCompany.getPhone());
            table[i][3] = telecommunicationCompany.getEmail();
            i++;
        }
        return table;
    }

    /**
     *
     * @return
     */
    public String[][] updateClientTable() {
        String[][] table = new String[clients.size()][7];
        int i = 0;
        for (Client client : clients) {
            table[i][0] = String.valueOf(client.getId());
            table[i][1] = String.valueOf(client.getAfm());
            table[i][2] = client.getName();
            table[i][3] = client.getOccupation();
            table[i][4] = client.getAddress();
            table[i][5] = String.valueOf(client.getPhone());
            table[i][6] = client.getEmail();
            i++;
        }
        return table;
    }

    /**
     *
     * @return
     */
    public String[][] updateContractTable() {
        String[][] table = new String[contracts.size()][12];
        int i = 0;
        for (Contract contract : contracts) {
            table[i][0] = contract.getId();
            table[i][1] = String.valueOf(contract.getPhone());
            table[i][2] = String.valueOf(contract.getAfm());
            table[i][3] = String.valueOf(contract.getPlan().getId());
            table[i][4] = String.valueOf(contract.getDate());
            table[i][5] = String.valueOf(contract.getDuration());
            table[i][6] = String.valueOf(contract.getDiscount());
            table[i][7] = String.valueOf(contract.getCost());
            table[i][8] = contract.getBillType();
            table[i][9] = contract.getPaymentMethod();
            table[i][10] = String.valueOf(contract.getCancelCost());
            table[i][11] = String.valueOf(contract.isActive());
            i++;
        }
        return table;
    }

    /**
     *
     * @param table
     * @param type
     * @return
     */
    public String[][] updateContractTableFiltered(String[][] table, String type) {
        int[] counters = countContracts();
        if (type.equals("MobilePlan")) {
            table = new String[counters[0]][12];
        } else if (type.equals("LandlinePlan")){
            table = new String[counters[1]][12];
        }
        int i = 0;
        for (Contract contract : contracts) {
            if (contract.getPlan() instanceof MobilePlan && type.equals("MobilePlan")) {
                table[i][0] = contract.getId();
                table[i][1] = String.valueOf(contract.getPhone());
                table[i][2] = String.valueOf(contract.getAfm());
                table[i][3] = String.valueOf(contract.getPlan().getId());
                table[i][4] = String.valueOf(contract.getDate());
                table[i][5] = String.valueOf(contract.getDuration());
                table[i][6] = String.valueOf(contract.getDiscount());
                table[i][7] = String.valueOf(contract.getCost());
                table[i][8] = contract.getBillType();
                table[i][9] = contract.getPaymentMethod();
                table[i][10] = String.valueOf(contract.getCancelCost());
                table[i][11] = String.valueOf(contract.isActive());
                i++;
            } else if (contract.getPlan() instanceof LandlinePlan && type.equals("LandlinePlan")) {
                table[i][0] = contract.getId();
                table[i][1] = String.valueOf(contract.getPhone());
                table[i][2] = String.valueOf(contract.getAfm());
                table[i][3] = String.valueOf(contract.getPlan().getId());
                table[i][4] = String.valueOf(contract.getDate());
                table[i][5] = String.valueOf(contract.getDuration());
                table[i][6] = String.valueOf(contract.getDiscount());
                table[i][7] = String.valueOf(contract.getCost());
                table[i][8] = contract.getBillType();
                table[i][9] = contract.getPaymentMethod();
                table[i][10] = String.valueOf(contract.getCancelCost());
                table[i][11] = String.valueOf(contract.isActive());
                i++;
            }
        }
        return table;
    }

    /**
     *
     * @return
     */
    public String[][] updatePlanTable() {
        String[][] table = new String[plans.size()][8];
        int i = 0;
        for (Plan plan : plans) {
            table[i][0] = String.valueOf(plan.getId());
            table[i][1] = plan.getCompany().getName();
            table[i][2] = String.valueOf(plan.getFreeMin());
            table[i][3] = String.valueOf(plan.getCost());
            if (plan instanceof MobilePlan) {
                table[i][4] = String.valueOf(((MobilePlan) plan).getFreeSms());
                table[i][5] = String.valueOf(((MobilePlan) plan).getFreeGb());
                table[i][6] = "";
                table[i][7] = "";
            } else {
                table[i][4] = "";
                table[i][5] = "";
                table[i][6] = String.valueOf(((LandlinePlan) plan).getSpeed());
                table[i][7] = ((LandlinePlan) plan).getType();
            }
            i++;
        }
        return table;
    }

    /**
     *
     * @return
     */
    public int[] countPlans() {
        int[] counters = new int[2];
        for (Plan plan : plans) {
            if (plan instanceof MobilePlan) {
                counters[0]++;
            } else {
                counters[1]++;
            }
        }
        return counters;
    }

    /**
     *
     * @return
     */
    public int[] countContracts() {
        int[] counters = new int[2];
        for (Contract contract : contracts) {
            if (contract.getPlan() instanceof MobilePlan) {
                counters[0]++;
            } else {
                counters[1]++;
            }
        }
        return counters;
    }

    /**
     *
     * @param table
     * @param type
     * @return
     */
    public String[][] updatePlanTableFiltered(String[][] table, String type) {
        int[] counters = countPlans();
        if (type.equals("MobilePlan")) {
            table = new String[counters[0]][8];
        } else if (type.equals("LandlinePlan")){
            table = new String[counters[1]][8];
        }
        int i = 0;
        for (Plan plan : plans) {
            if (plan instanceof MobilePlan && type.equals("MobilePlan")) {
                table[i][0] = String.valueOf(plan.getId());
                table[i][1] = plan.getCompany().getName();
                table[i][2] = String.valueOf(plan.getFreeMin());
                table[i][3] = String.valueOf(plan.getCost());
                table[i][4] = String.valueOf(((MobilePlan) plan).getFreeSms());
                table[i][5] = String.valueOf(((MobilePlan) plan).getFreeGb());
                table[i][6] = "";
                table[i][7] = "";
                i++;
            } else if (plan instanceof LandlinePlan && type.equals("LandlinePlan")) {
                table[i][0] = String.valueOf(plan.getId());
                table[i][1] = plan.getCompany().getName();
                table[i][2] = String.valueOf(plan.getFreeMin());
                table[i][3] = String.valueOf(plan.getCost());
                table[i][4] = "";
                table[i][5] = "";
                table[i][6] = String.valueOf(((LandlinePlan) plan).getSpeed());
                table[i][7] = ((LandlinePlan) plan).getType();
                i++;
            }
        }
        return table;
    }

    /////////////////////Οι μέθοδοι που δημιουργούν τα JPanels για κάθε view της διεπαφής////////////////

    /**
     *
     * @return
     */
    public JPanel panelTelecommunicationCompany() {
        JPanel myPanel = new JPanel(new GridBagLayout());
        myPanel.setSize(myFrame.getSize());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridy = 0;
        constraints.gridx = 0;
        constraints.weightx = 1;
        constraints.weighty = 0.33;
        constraints.anchor = GridBagConstraints.WEST;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.insets = new Insets(4, 4, 4, 4);

        JPanel panel1 = new JPanel(new GridLayout(2, 1));
        panel1.setBorder(new TitledBorder(null, "TelecommunicationCompany Section", TitledBorder.LEADING, TitledBorder.TOP, null, null));

        JPanel panel1in = new JPanel(new BorderLayout());
        String[] header = {"ID", "NAME", "PHONE", "EMAIL"};
        final String[][][] rec = {new String[][]{}};
        final JTable[] table = {new JTable(rec[0], header)};
        final JScrollPane[] newScroll = {new JScrollPane(table[0])};
        panel1in.add(newScroll[0]);
        panel1.add(panel1in);

        JPanel panel3 = new JPanel(new BorderLayout());
        panel3.setBorder(new TitledBorder(null, "Actions", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        panel1.add(panel3);

        myPanel.add(panel1, constraints);

        JButton addTelecommunicationCompanyBtn = new JButton("Add Company");
        JButton listCompaniesBtn = new JButton("Show Companies");
        final boolean[] hitOnce = {false};
        JButton editTelecommunicationCompanyBtn = new JButton("Edit Company");
        JButton deleteTelecommunicationCompanyBtn = new JButton("Delete Company");
        JButton returnBtn = new JButton("RETURN");

        JTextField textField = new JTextField(5);
        JButton searchBtn = new JButton(new ImageIcon("src/images/search.png"));
        JPanel searchPanel = new JPanel(new FlowLayout());
        JLabel searchLbl = new JLabel("Company name");
        searchPanel.add(searchLbl);
        searchPanel.add(searchBtn);

        JPanel panel2 = new JPanel(new BorderLayout(0, 0));
        panel2.setBorder(new TitledBorder(null, "Options", TitledBorder.CENTER, TitledBorder.TOP, null, null));
        JPanel panel2inside = new JPanel(new GridBagLayout());
        GridBagConstraints c2 = new GridBagConstraints();
        c2.gridx = 0;
        c2.gridy = 0;
        c2.gridwidth = GridBagConstraints.REMAINDER;
        c2.fill = GridBagConstraints.HORIZONTAL;
        c2.weightx = 1;
        c2.insets = new Insets(4, 4, 4, 4);
        panel2inside.add(searchPanel, c2);
        c2.gridy++;
        panel2inside.add(textField, c2);
        c2.gridy++;
        panel2inside.add(addTelecommunicationCompanyBtn, c2);
        c2.gridy++;
        panel2inside.add(listCompaniesBtn, c2);
        c2.gridy++;
        panel2inside.add(editTelecommunicationCompanyBtn, c2);
        c2.gridy++;
        panel2inside.add(deleteTelecommunicationCompanyBtn, c2);
        c2.gridy++;
        c2.weighty = 1;
        c2.anchor = GridBagConstraints.SOUTH;
        panel2inside.add(returnBtn, c2);
        constraints.gridy = 0;
        constraints.gridx++;
        constraints.gridheight = GridBagConstraints.REMAINDER;
        constraints.fill = GridBagConstraints.VERTICAL;
        constraints.weighty = 1;
        constraints.weightx = 0;
        panel2.add(panel2inside);
        myPanel.add(panel2, constraints);

        listCompaniesBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                hitOnce[0] = true;
                panel1in.removeAll();
                rec[0] = updateTelecommunicationCompanyTable();
                table[0] = new JTable(rec[0], header);
                newScroll[0] = new JScrollPane(table[0]);
                panel1in.add(newScroll[0]);
                panel1in.repaint();
                panel1in.revalidate();
            }
        });

        deleteTelecommunicationCompanyBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (table[0].getSelectedRow() != -1) {
                    String nameSelected = table[0].getModel().getValueAt(table[0].getSelectedRow(), 1).toString();
                    int input = JOptionPane.showConfirmDialog(null, "Remove company: " + nameSelected + "?");
                    //0=yes,1=no,2=cancel
                    if (input == 0 ) {
                        boolean cannotDelete = false;
                        for (Plan plan : plans) {
                            if (plan.getCompany() == findTelecommunicationCompanyByName(nameSelected)) {
                                cannotDelete = true;
                            }
                        }
                        if (!cannotDelete) {
                            removeTelecommunicationCompany(nameSelected);
                            panel1in.removeAll();
                            rec[0] = updateTelecommunicationCompanyTable();
                            table[0] = new JTable(rec[0], header);
                            newScroll[0] = new JScrollPane(table[0]);
                            panel1in.add(newScroll[0]);
                            panel1in.repaint();
                            panel1in.revalidate();
                        } else {
                            String message = "Cannot delete!" + "\n" + "There are plans associated with this company!";
                            JOptionPane.showMessageDialog(new JFrame(),message , "Warning", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                } else {
                    String message = "Select a company first!";
                    JOptionPane.showMessageDialog(new JFrame(),message , "Warning", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        returnBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                myFrame.getContentPane().removeAll();
                myFrame.invalidate();
                myFrame.setContentPane(mainMenu());
                myFrame.repaint();
                myFrame.revalidate();
            }
        });

        editTelecommunicationCompanyBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (table[0].getSelectedRow() != -1) {
                    String selected = table[0].getModel().getValueAt(table[0].getSelectedRow(), 1).toString();
                    TelecommunicationCompany company = findTelecommunicationCompanyByName(selected);
                    panel3.removeAll();
                    JPanel panel3in = new JPanel(new GridBagLayout());
                    GridBagConstraints constraints1 = new GridBagConstraints();
                    constraints1.gridx = 0;
                    constraints1.gridy = 0;
                    constraints1.insets = new Insets(4, 4, 4, 4);
                    assert company != null;
                    JLabel title = new JLabel("Edit company's " + company.getName() + " info");
                    panel3in.add(title, constraints1);
                    constraints1.gridy++;

                    JPanel panel = new JPanel(new GridLayout(5, 0));

                    String[] headers = {"ID", "NAME ", "PHONE", "EMAIL"};

                    JLabel[] labels = new JLabel[4];
                    JTextField[] fields = new JTextField[4];

                    for (int i = 0; i < 4; i++) {
                        labels[i] = new JLabel(headers[i], JLabel.TRAILING);
                        fields[i] = new JTextField(10);
                        panel.add(labels[i]);
                        labels[i].setLabelFor(fields[i]);
                        panel.add(fields[i]);
                    }

                    fields[0].setText(String.valueOf(company.getId()));
                    fields[0].setEnabled(false);
                    fields[1].setText(company.getName());
                    fields[1].setEnabled(false);


                    JButton submit = new JButton("SUBMIT");
                    panel.add(submit);
                    JLabel warning = new JLabel();
                    warning.setVisible(false);
                    panel.add(warning);
                    panel3in.add(panel, constraints1);

                    submit.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            boolean error = false;
                            String[] entries = new String[2];
                            entries[0] = fields[2].getText();
                            entries[1] = fields[3].getText();
                            if (!entries[0].equals("")) {
                                try {
                                    long id = Long.parseLong(entries[0]);
                                } catch (NumberFormatException ex) {
                                    warning.setText(labels[2].getText() + " must be a number!");
                                    warning.setForeground(Color.RED);
                                    warning.setVisible(true);
                                    error = true;
                                }
                            }
                            if (!error) {
                                if (!entries[0].equals("")) {
                                    company.setPhone(Long.parseLong(entries[0]));
                                }
                                if (!entries[1].equals("")) {
                                    company.setEmail(entries[1]);
                                }
                                warning.setText("Info edited successfully!");
                                warning.setForeground(Color.GREEN);
                                warning.setVisible(true);

                                panel1in.removeAll();
                                rec[0] = updateTelecommunicationCompanyTable();
                                table[0] = new JTable(rec[0], header);
                                table[0].clearSelection();
                                if (hitOnce[0]) {
                                    newScroll[0] = new JScrollPane(table[0]);
                                    panel1in.add(newScroll[0]);
                                    panel1in.repaint();
                                    panel1in.revalidate();
                                }
                            }
                        }
                    });
                    panel3.add(panel3in);
                    panel3.repaint();
                    panel3.revalidate();
                } else {

                    String message = "Select a company first!";
                    JOptionPane.showMessageDialog(new JFrame(), message, "Warning", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        addTelecommunicationCompanyBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panel3.removeAll();
                JPanel panel3in = new JPanel(new GridBagLayout());
                GridBagConstraints constraints1 = new GridBagConstraints();
                constraints1.gridx = 0;
                constraints1.gridy = 0;
                constraints1.insets = new Insets(4, 4, 4, 4);
                JLabel title = new JLabel("Insert new company");
                panel3in.add(title, constraints1);
                constraints1.gridy++;

                JPanel panel = new JPanel(new GridLayout(5, 0));

                String[] headers = {"NAME ", "PHONE", "EMAIL"};

                JLabel[] labels = new JLabel[3];
                JTextField[] fields = new JTextField[3];

                for (int i = 0; i < 3; i++) {
                    labels[i] = new JLabel(headers[i], JLabel.TRAILING);
                    fields[i] = new JTextField(10);
                    panel.add(labels[i]); labels[i].setLabelFor(fields[i]); panel.add(fields[i]);
                }

                JButton submit = new JButton("SUBMIT");
                panel.add(submit);
                JLabel warning = new JLabel();
                warning.setVisible(false);
                panel.add(warning);
                panel3in.add(panel, constraints1);

                submit.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        boolean error = false;
                        String[] entries = new String[3];
                        for (int i = 0; i < 3; i++) {
                            entries[i] = fields[i].getText();
                        }
                        for (int i = 0; i < 3; i++) {
                            if (entries[i].equals("")) {
                                warning.setText(labels[i].getText() + " is not valid!");
                                warning.setForeground(Color.RED);
                                warning.setVisible(true);
                                error = true;
                                break;
                            }
                        }
                        if (findTelecommunicationCompanyByName(fields[0].getText().trim()) != null) {
                            warning.setText("This company is already inserted!");
                            warning.setForeground(Color.RED);
                            warning.setVisible(true);
                            error = true;
                        }
                        try {
                            int id = Integer.parseInt(entries[1]);
                        } catch (NumberFormatException ex) {
                            warning.setText(labels[1].getText() + " must be a number!");
                            warning.setForeground(Color.RED);
                            warning.setVisible(true);
                            error = true;
                        }
                        if (!error) {
                            warning.setText("Company inserted successfully!");
                            warning.setForeground(Color.GREEN);
                            warning.setVisible(true);
                            addTelecommunicationCompany(fields[0].getText(), Long.parseLong(fields[1].getText()), fields[2].getText());

                            panel1in.removeAll();
                            rec[0] = updateTelecommunicationCompanyTable();
                            table[0] = new JTable(rec[0], header);
                            table[0].clearSelection();
                            if (hitOnce[0]) {
                                newScroll[0] = new JScrollPane(table[0]);
                                panel1in.add(newScroll[0]);
                                panel1in.repaint();
                                panel1in.revalidate();
                            }
                        }
                    }
                });
                panel3.add(panel3in);
                panel3.repaint();
                panel3.revalidate();
            }
        });


        searchBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean flag = true;
                for (int i = 0; i < table[0].getRowCount(); i++) {
                    if (textField.getText().trim().equals(table[0].getModel().getValueAt(i, 1).toString())) {
                        table[0].setRowSelectionInterval(i, i);
                        flag = false;
                    }
                }
                if (flag) {
                    table[0].clearSelection();
                }
            }
        });

        return myPanel;
    }

    /**
     *
     * @return
     */
    public JPanel panelClient() {
        JPanel myPanel = new JPanel(new GridBagLayout());
        myPanel.setSize(myFrame.getSize());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridy = 0;
        constraints.gridx = 0;
        constraints.weightx = 1;
        constraints.weighty = 0.33;
        constraints.anchor = GridBagConstraints.WEST;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.insets = new Insets(4, 4, 4, 4);

        JPanel panel1 = new JPanel(new GridLayout(2, 1));
        panel1.setBorder(new TitledBorder(null, "Client Section", TitledBorder.LEADING, TitledBorder.TOP, null, null));

        JPanel panel1in = new JPanel(new BorderLayout());
        String[] header = {"ID", "AFM", "NAME", "OCCUPATION", "ADDRESS", "PHONE", "EMAIL"};
        final String[][][] rec = {new String[][]{}};
        final JTable[] table = {new JTable(rec[0], header)};
        final JScrollPane[] newScroll = {new JScrollPane(table[0])};
        panel1in.add(newScroll[0]);
        panel1.add(panel1in);

        JPanel panel3 = new JPanel(new BorderLayout());
        panel3.setBorder(new TitledBorder(null, "Actions", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        panel1.add(panel3);

        myPanel.add(panel1, constraints);

        JButton addClientBtn = new JButton("Add Client");
        JButton listClientsBtn = new JButton("Show Clients");
        final boolean[] hitOnce = {false};
        JButton editClientBtn = new JButton("Edit Client");
        JButton deleteClientBtn = new JButton("Delete Client");
        JButton returnBtn = new JButton("RETURN");

        JTextField textField = new JTextField(5);
        JButton searchBtn = new JButton(new ImageIcon("src/images/search.png"));
        JPanel searchPanel = new JPanel(new FlowLayout());
        JLabel searchLbl = new JLabel("Client ID/AFM");
        searchPanel.add(searchLbl);
        searchPanel.add(searchBtn);

        JPanel panel2 = new JPanel(new BorderLayout(0, 0));
        panel2.setBorder(new TitledBorder(null, "Options", TitledBorder.CENTER, TitledBorder.TOP, null, null));
        JPanel panel2inside = new JPanel(new GridBagLayout());
        GridBagConstraints c2 = new GridBagConstraints();
        c2.gridx = 0;
        c2.gridy = 0;
        c2.gridwidth = GridBagConstraints.REMAINDER;
        c2.fill = GridBagConstraints.HORIZONTAL;
        c2.weightx = 1;
        c2.insets = new Insets(4, 4, 4, 4);
        panel2inside.add(searchPanel, c2);
        c2.gridy++;
        panel2inside.add(textField, c2);
        c2.gridy++;
        panel2inside.add(addClientBtn, c2);
        c2.gridy++;
        panel2inside.add(listClientsBtn, c2);
        c2.gridy++;
        panel2inside.add(editClientBtn, c2);
        c2.gridy++;
        panel2inside.add(deleteClientBtn, c2);
        c2.gridy++;
        c2.weighty = 1;
        c2.anchor = GridBagConstraints.SOUTH;
        panel2inside.add(returnBtn, c2);
        constraints.gridy = 0;
        constraints.gridx++;
        constraints.gridheight = GridBagConstraints.REMAINDER;
        constraints.fill = GridBagConstraints.VERTICAL;
        constraints.weighty = 1;
        constraints.weightx = 0;
        panel2.add(panel2inside);
        myPanel.add(panel2, constraints);

        listClientsBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                hitOnce[0] = true;
                panel1in.removeAll();
                rec[0] = updateClientTable();
                table[0] = new JTable(rec[0], header);
                newScroll[0] = new JScrollPane(table[0]);
                panel1in.add(newScroll[0]);
                panel1in.repaint();
                panel1in.revalidate();
            }
        });

        deleteClientBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (table[0].getSelectedRow() != -1) {
                    String idSelected = table[0].getModel().getValueAt(table[0].getSelectedRow(), 0).toString();
                    Client client = findClientById(Integer.parseInt(idSelected));
                    int input = JOptionPane.showConfirmDialog(null, "Remove client with ID: " + idSelected + "?");
                    //0=yes,1=no,2=cancel
                    if (input == 0) {
                        boolean cannotDelete = false;
                        for (Contract contract : contracts) {
                            assert client != null;
                            if (contract.getAfm() == client.getAfm()) {
                                cannotDelete = true;
                            }
                        }
                        if (!cannotDelete) {
                            assert client != null;
                            removeClient(client.getId());
                            panel1in.removeAll();
                            rec[0] = updateClientTable();
                            table[0] = new JTable(rec[0], header);
                            newScroll[0] = new JScrollPane(table[0]);
                            panel1in.add(newScroll[0]);
                            panel1in.repaint();
                            panel1in.revalidate();
                        } else {
                            String message = "Cannot delete!" + "\n" + "There are contracts associated with this client!";
                            JOptionPane.showMessageDialog(new JFrame(), message, "Warning", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                } else {
                    String message = "Select a client first!";
                    JOptionPane.showMessageDialog(new JFrame(), message, "Warning", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        returnBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                myFrame.getContentPane().removeAll();
                myFrame.invalidate();
                myFrame.setContentPane(mainMenu());
                myFrame.repaint();
                myFrame.revalidate();
            }
        });

        addClientBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panel3.removeAll();
                JPanel panel3in = new JPanel(new GridBagLayout());
                GridBagConstraints constraints1 = new GridBagConstraints();
                constraints1.gridx = 0;
                constraints1.gridy = 0;
                constraints1.insets = new Insets(4, 4, 4, 4);
                JLabel title = new JLabel("Insert new client");
                panel3in.add(title, constraints1);
                constraints1.gridy++;

                JPanel panel = new JPanel(new GridLayout(9, 0));

                String[] header = {"ID       ", "AFM      ", "NAME     ", "OCCUPATION", "ADDRESS  ", "PHONE    ", "EMAIL    "};

                JLabel[] labels = new JLabel[7];
                JTextField[] fields = new JTextField[7];
                JComboBox<String> occupationBox = new JComboBox<>();
                occupationBox.addItem("<empty>");
                occupationBox.addItem("professional");
                occupationBox.addItem("student");
                occupationBox.addItem("private citizen");
                for (int i = 0; i < 7; i++) {
                    labels[i] = new JLabel(header[i], JLabel.TRAILING);
                    if ( i == 3) {
                        panel.add(labels[i]); labels[i].setLabelFor(occupationBox); panel.add(occupationBox);
                    } else {
                        fields[i] = new JTextField(10);
                        panel.add(labels[i]);
                        labels[i].setLabelFor(fields[i]);
                        panel.add(fields[i]);
                    }
                }

                JButton submit = new JButton("Submit");
                JLabel warning = new JLabel("");
                warning.setVisible(false);
                panel.add(submit);
                panel.add(warning);
                panel3in.add(panel, constraints1);

                submit.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        boolean error = false;
                        String[] entries = new String[7];
                        for (int i = 0; i < 7; i++) {
                            if (i == 3) {
                                entries[i] = occupationBox.getItemAt(occupationBox.getSelectedIndex());
                            } else {
                                entries[i] = fields[i].getText();
                            }
                        }
                        try {
                            long x = Long.parseLong(entries[5]);
                        } catch (NumberFormatException ex) {
                            fields[5].setText("");
                            warning.setText("The PHONE must be a number!");
                            warning.setForeground(Color.RED);
                            warning.setVisible(true);
                            error = true;
                        }
                        try {
                            int x = Integer.parseInt(entries[1]);
                            if (findClientByAfm(Integer.parseInt(entries[1])) != null) {
                                warning.setText("This AFM number is already in use!");
                                warning.setForeground(Color.RED);
                                warning.setVisible(true);
                                error = true;
                            }
                        } catch (NumberFormatException ex) {
                            fields[1].setText("");
                            warning.setText("The AFM must be a number!");
                            warning.setForeground(Color.RED);
                            warning.setVisible(true);
                            error = true;
                        }
                        try {
                            int x = Integer.parseInt(entries[0]);
                            if (findClientById(Integer.parseInt(entries[0])) != null) {
                                warning.setText("This ID number is already in use!");
                                warning.setForeground(Color.RED);
                                warning.setVisible(true);
                                error = true;
                            }
                        } catch (NumberFormatException ex) {
                            fields[0].setText("");
                            warning.setText("The ID must be a number!");
                            warning.setForeground(Color.RED);
                            warning.setVisible(true);
                            error = true;
                        }
                        for (int i = 0; i < 7; i++) {
                            if (entries[i].equals("") || entries[i].equals("<empty>")) {
                                warning.setText(labels[i].getText() + " is not valid!");
                                warning.setForeground(Color.RED);
                                warning.setVisible(true);
                                error = true;
                                break;
                            }
                        }
                        if (!error) {
                            clients.add(new Client(Integer.parseInt(entries[0]), Integer.parseInt(entries[1]), entries[2], entries[3], entries[4], Long.parseLong(entries[5]), entries[6]));
                            warning.setText("New client created successfully!");
                            warning.setForeground(Color.GREEN);
                            warning.setVisible(true);
                            if (hitOnce[0]) {
                                panel1in.removeAll();
                                rec[0] = updateClientTable();
                                table[0] = new JTable(rec[0], header);
                                newScroll[0] = new JScrollPane(table[0]);
                                panel1in.add(newScroll[0]);
                                panel1in.repaint();
                                panel1in.revalidate();
                            }
                        }
                    }
                });
                panel3.add(panel3in);
                panel3.repaint();
                panel3.revalidate();
            }
        });

        editClientBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (table[0].getSelectedRow() != -1) {
                    panel3.removeAll();
                    String idSelected = table[0].getModel().getValueAt(table[0].getSelectedRow(), 0).toString();
                    Client client = findClientById(Integer.parseInt(idSelected));
                    JPanel panel3in = new JPanel(new GridBagLayout());
                    GridBagConstraints constraints1 = new GridBagConstraints();
                    constraints1.gridx = 0;
                    constraints1.gridy = 0;
                    constraints1.insets = new Insets(4, 4, 4, 4);
                    assert client != null;
                    JLabel title = new JLabel("Edit " + client.getName() + "'s info");
                    panel3in.add(title, constraints1);
                    constraints1.gridy++;

                    JPanel panel = new JPanel(new GridLayout(9, 0));

                    String[] header = {"ID       ", "AFM      ", "NAME     ", "OCCUPATION", "ADDRESS  ", "PHONE    ", "EMAIL    "};

                    JLabel[] labels = new JLabel[7];
                    JTextField[] fields = new JTextField[7];
                    JComboBox<String> occupationBox = new JComboBox<>();
                    occupationBox.addItem("<empty>");
                    occupationBox.addItem("professional");
                    occupationBox.addItem("student");
                    occupationBox.addItem("private citizen");
                    for (int i = 0; i < 7; i++) {
                        labels[i] = new JLabel(header[i], JLabel.TRAILING);
                        if ( i == 3) {
                            panel.add(labels[i]); labels[i].setLabelFor(occupationBox); panel.add(occupationBox);
                        } else {
                            fields[i] = new JTextField(10);
                            panel.add(labels[i]);
                            labels[i].setLabelFor(fields[i]);
                            panel.add(fields[i]);
                            if (i == 0) {
                                fields[0].setText(String.valueOf(client.getId()));
                                fields[0].setEnabled(false);
                            }
                            if (i == 1) {
                                fields[1].setText(String.valueOf(client.getAfm()));
                                fields[1].setEnabled(false);
                            }
                        }
                    }

                    JButton submit = new JButton("Submit");
                    JLabel warning = new JLabel("");
                    warning.setVisible(false);
                    panel.add(submit);
                    panel.add(warning);
                    panel3in.add(panel, constraints1);

                    submit.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            String[] entries = new String[7];
                            boolean flag = false;
                            for (int i = 2; i < 7; i++) {
                                if (i == 3) {
                                    entries[i] = occupationBox.getItemAt(occupationBox.getSelectedIndex());
                                } else {
                                    entries[i] = fields[i].getText();
                                }
                            }
                            for (int i = 2; i < 7; i++) {
                                if (!entries[3].equals("<empty>")) {
                                    client.setOccupation(entries[3]);
                                }
                                if (!entries[i].equals("") && i !=3 ) {
                                    flag =true;
                                    if (i == 2) {
                                        client.setName(entries[2]);
                                    } else if (i == 4) {
                                        client.setAddress(entries[4]);
                                    } else if (i == 5) {
                                        boolean error = false;
                                        try {
                                            long x = Long.parseLong(entries[5]);
                                        } catch (NumberFormatException ex) {
                                            fields[5].setText("");
                                            warning.setText("The PHONE must be a number!");
                                            warning.setForeground(Color.RED);
                                            warning.setVisible(true);
                                            error = true;
                                        }
                                        if (!error) {
                                            client.setPhone(Long.parseLong(entries[5]));
                                        }
                                    } else {
                                        client.setEmail(entries[6]);
                                    }
                                }
                                if (flag) {
                                    warning.setText("Info changed successfully!");
                                    warning.setForeground(Color.GREEN);
                                } else {
                                    warning.setText("No info changed!");
                                    warning.setForeground(Color.GRAY);
                                }
                                warning.setVisible(true);
                            }
                            panel1in.removeAll();
                            rec[0] = updateClientTable();
                            table[0] = new JTable(rec[0], header);
                            newScroll[0] = new JScrollPane(table[0]);
                            panel1in.add(newScroll[0]);
                            panel1in.repaint();
                            panel1in.revalidate();
                        }
                    });
                    panel3.add(panel3in);
                    panel3.repaint();
                    panel3.revalidate();
                } else {
                    String message = "Select a client first!";
                    JOptionPane.showMessageDialog(new JFrame(), message, "Warning", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        searchBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean flag = true;
                for (int i = 0; i < table[0].getRowCount(); i++) {
                    if (textField.getText().trim().equals(table[0].getModel().getValueAt(i, 0).toString())) {
                        table[0].setRowSelectionInterval(i, i);
                        flag = false;
                    }
                    if (textField.getText().trim().equals(table[0].getModel().getValueAt(i, 1).toString())) {
                        table[0].setRowSelectionInterval(i, i);
                        flag = false;
                    }
                }
                if (flag) {
                    table[0].clearSelection();
                }
            }
        });

        return myPanel;
    }

    /**
     *
     * @return
     */
    public JPanel panelContract() {
        JPanel myPanel = new JPanel(new GridBagLayout());
        myPanel.setSize(myFrame.getSize());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridy = 0;
        constraints.gridx = 0;
        constraints.weightx = 1;
        constraints.weighty = 0.33;
        constraints.anchor = GridBagConstraints.WEST;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.insets = new Insets(4, 4, 4, 4);

        JPanel panel1 = new JPanel(new GridLayout(2, 1));
        panel1.setBorder(new TitledBorder(null, "Contract Section", TitledBorder.LEADING, TitledBorder.TOP, null, null));

        JPanel panel1in = new JPanel(new BorderLayout());
        String[] header = {"ID", "PHONE", "AFM", "PLAN", "DATE", "DURATION", "DISCOUNT", "COST", "BILL_TYPE", "PAYMENT", "CANCEL_COST", "ACTIVE"};
        final String[][][] rec = {new String[][]{}};
        final JTable[] table = {new JTable(rec[0], header)};
        final JScrollPane[] newScroll = {new JScrollPane(table[0])};
        panel1in.add(newScroll[0]);
        panel1.add(panel1in);

        JPanel panel3 = new JPanel(new BorderLayout());
        panel3.setBorder(new TitledBorder(null, "Actions", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        panel1.add(panel3);

        myPanel.add(panel1, constraints);

        JButton addContractBtn = new JButton("Add Contract");
        JButton listContractsBtn = new JButton("Show Contracts");
        final boolean[] hitOnce = {false};
        JButton deleteContractBtn = new JButton("Disable Contract");
        JButton returnBtn = new JButton("RETURN");

        JTextField textField = new JTextField(5);
        JButton searchBtn = new JButton(new ImageIcon("src/images/search.png"));
        JPanel searchPanel = new JPanel(new FlowLayout());
        JLabel searchLbl = new JLabel("Contract Phone");
        searchPanel.add(searchLbl);
        searchPanel.add(searchBtn);

        JLabel search1Lbl = new JLabel(new ImageIcon("src/images/search.png"));
        JPanel search1Panel = new JPanel(new FlowLayout());
        JComboBox<String> boxType = new JComboBox<>();
        boxType.addItem("<empty>");
        boxType.addItem("MobilePlan");
        boxType.addItem("LandlinePlan");
        search1Panel.add(search1Lbl);
        search1Panel.add(boxType);

        JPanel panel2 = new JPanel(new BorderLayout(0, 0));
        panel2.setBorder(new TitledBorder(null, "Options", TitledBorder.CENTER, TitledBorder.TOP, null, null));
        JPanel panel2inside = new JPanel(new GridBagLayout());
        GridBagConstraints c2 = new GridBagConstraints();
        c2.gridx = 0;
        c2.gridy = 0;
        c2.gridwidth = GridBagConstraints.REMAINDER;
        c2.fill = GridBagConstraints.HORIZONTAL;
        c2.weightx = 1;
        c2.insets = new Insets(4, 4, 4, 4);
        panel2inside.add(searchPanel, c2);
        c2.gridy++;
        panel2inside.add(textField, c2);
        c2.gridy++;
        panel2inside.add(search1Panel, c2);
        c2.gridy++;
        panel2inside.add(addContractBtn, c2);
        c2.gridy++;
        panel2inside.add(listContractsBtn, c2);
        c2.gridy++;
        panel2inside.add(deleteContractBtn, c2);
        c2.gridy++;
        c2.weighty = 1;
        c2.anchor = GridBagConstraints.SOUTH;
        panel2inside.add(returnBtn, c2);
        constraints.gridy = 0;
        constraints.gridx++;
        constraints.gridheight = GridBagConstraints.REMAINDER;
        constraints.fill = GridBagConstraints.VERTICAL;
        constraints.weighty = 1;
        constraints.weightx = 0;
        panel2.add(panel2inside);
        myPanel.add(panel2, constraints);

        listContractsBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                hitOnce[0] = true;
                panel1in.removeAll();
                rec[0] = updateContractTable();
                table[0] = new JTable(rec[0], header);
                newScroll[0] = new JScrollPane(table[0]);
                panel1in.add(newScroll[0]);
                panel1in.repaint();
                panel1in.revalidate();
            }
        });

        deleteContractBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panel3.removeAll();
                JPanel panel3in = new JPanel(new GridBagLayout());
                GridBagConstraints constraints1 = new GridBagConstraints();
                constraints1.gridx = 0;
                constraints1.gridy = 0;
                constraints1.insets = new Insets(4, 4, 4, 4);
                if (table[0].getSelectedRow() != -1) {
                    String idSelected = table[0].getModel().getValueAt(table[0].getSelectedRow(), 0).toString();
                    Contract contract = findContractById(idSelected);
                    assert contract != null;
                    if (!contract.isActive()) {
                        String message = "Choose an active contract to disable!";
                        JOptionPane.showMessageDialog(new JFrame(), message, "Contract Status", JOptionPane.ERROR_MESSAGE);
                    } else {
                        JLabel title = new JLabel("Contract Status " + contract.getId() + ":");
                        panel3in.add(title, constraints1);
                        constraints1.gridy++;
                        JComboBox<String> statusBox = new JComboBox<>();
                        statusBox.addItem("disable");
                        statusBox.addItem("enable");
                        panel3in.add(statusBox, constraints1);
                        constraints1.gridy++;
                        JButton submit = new JButton("Submit");
                        panel3in.add(submit, constraints1);
                        constraints1.gridy++;
                        JLabel warning = new JLabel("");
                        warning.setVisible(false);
                        panel3in.add(warning, constraints1);
                        submit.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                String selection = statusBox.getItemAt(statusBox.getSelectedIndex());
                                System.out.println(selection);
                                if (selection.equals("enable")) {
                                    warning.setText("No changes made!");
                                    warning.setForeground(Color.GRAY);
                                    warning.setVisible(true);
                                } else if (selection.equals("disable")) {
                                    int input = JOptionPane.showConfirmDialog(null, "Disable contract with ID: " + idSelected + "?" + "\n" + "Cost will be: " + contract.getCancelCost());
                                    if (input == 0) {
                                        contract.setActive(false);
                                        warning.setForeground(Color.GREEN);
                                        warning.setText("Contract Status changed successfully!");
                                        warning.setVisible(true);
                                        if (hitOnce[0]) {
                                            panel1in.removeAll();
                                            rec[0] = updateContractTable();
                                            table[0] = new JTable(rec[0], header);
                                            newScroll[0] = new JScrollPane(table[0]);
                                            panel1in.add(newScroll[0]);
                                            panel1in.repaint();
                                            panel1in.revalidate();
                                        }
                                    }
                                }
                            }
                        });
                    }
                } else {
                    String message = "Select a contract first!";
                    JOptionPane.showMessageDialog(new JFrame(),message , "Warning", JOptionPane.ERROR_MESSAGE);
                }
                panel3.add(panel3in);
                panel3in.repaint();
                panel3in.revalidate();
            }
        });

        returnBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                myFrame.getContentPane().removeAll();
                myFrame.invalidate();
                myFrame.setContentPane(mainMenu());
                myFrame.repaint();
                myFrame.revalidate();
            }
        });

        addContractBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panel3.removeAll();
                JPanel panel3in = new JPanel(new GridBagLayout());
                GridBagConstraints constraints1 = new GridBagConstraints();
                constraints1.gridx = 0;
                constraints1.gridy = 0;
                constraints1.insets = new Insets(4, 4, 4, 4);
                JLabel title = new JLabel("Insert new contract");
                panel3in.add(title, constraints1);
                constraints1.gridy++;

                JPanel panel = new JPanel(new GridLayout(9, 0));

                String[] headers = {"PHONE     ", "AFM       ", "PLAN      ", "DATE      ", "DURATION  ", "BILL_TYPE ", "PAYMENT   "};

                JLabel[] labels = new JLabel[7];

                for (int i = 0; i < 7; i++) {
                    labels[i] = new JLabel(headers[i], JLabel.TRAILING);
                }

                JTextField phoneField = new JTextField(16);
                panel.add(labels[0]);
                labels[0].setLabelFor(phoneField);
                panel.add(phoneField);

                JComboBox<String>[] boxes = new JComboBox[6];

                boxes[0] = new JComboBox<>();
                boxes[0].addItem("<empty>");
                for (Client client : clients) {
                    boxes[0].addItem(String.valueOf(client.getAfm()));
                }
                panel.add(labels[1]);
                labels[1].setLabelFor(boxes[0]);
                panel.add(boxes[0]);

                boxes[1] = new JComboBox<>();
                boxes[1].addItem("<empty>");
                for (Plan plan : plans) {
                    boxes[1].addItem(String.valueOf(plan.getId()));
                }
                panel.add(labels[2]);
                labels[2].setLabelFor(boxes[1]);
                panel.add(boxes[1]);

                boxes[1].addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String selectedPlan = boxes[1].getItemAt(boxes[1].getSelectedIndex());
                        if (!selectedPlan.equals("<empty>")) {
                            Plan plan = findPlanById(Integer.parseInt(selectedPlan));
                            String planType = "";
                            if (plan instanceof MobilePlan) {
                                planType = "(Mobile)";
                            } else {
                                planType = "(Landline)";
                            }
                            labels[2].setText("PLAN " + planType);
                        }
                    }
                });

                boxes[2] = new JComboBox<>();
                boxes[2].addItem("<empty>");
                LocalDate startTime = LocalDate.now();
                LocalDate stopTime = LocalDate.of(2021, 11, 30);
                while (startTime.isBefore(stopTime)) {
                    boxes[2].addItem(startTime.toString());
                    startTime = startTime.plusDays(1);
                }
                panel.add(labels[3]);
                labels[3].setLabelFor(boxes[2]);
                panel.add(boxes[2]);

                boxes[3] = new JComboBox<>();
                boxes[3].addItem("<empty>");
                boxes[3].addItem("12");
                boxes[3].addItem("24");
                panel.add(labels[4]);
                labels[4].setLabelFor(boxes[3]);
                panel.add(boxes[3]);

                boxes[4] = new JComboBox<>();
                boxes[4].addItem("<empty>");
                boxes[4].addItem("paper");
                boxes[4].addItem("electronic");
                panel.add(labels[5]);
                labels[5].setLabelFor(boxes[4]);
                panel.add(boxes[4]);

                boxes[5] = new JComboBox<>();
                boxes[5].addItem("<empty>");
                boxes[5].addItem("cash");
                boxes[5].addItem("card");
                panel.add(labels[6]);
                labels[6].setLabelFor(boxes[5]);
                panel.add(boxes[5]);

                JButton submit = new JButton("Submit");
                JLabel warning = new JLabel("");
                warning.setVisible(false);
                panel.add(submit);
                panel.add(warning);
                panel3in.add(panel, constraints1);

                submit.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        boolean error = false;
                        String[] entries = new String[7];
                        entries[0] = phoneField.getText().trim();
                        for (int i = 1; i < 7; i++) {
                            entries[i] = boxes[i - 1].getItemAt(boxes[i - 1].getSelectedIndex());
                        }
                        for (int i = 0; i < 7; i++) {
                            if (i > 0 && entries[i].equals("<empty>")) {
                                warning.setText(labels[i].getText() + " is not valid!");
                                warning.setForeground(Color.RED);
                                warning.setVisible(true);
                                error = true;
                                break;
                            } else if (i == 0 && entries[0].equals("")) {
                                warning.setText(labels[i].getText() + " is not valid!");
                                warning.setForeground(Color.RED);
                                warning.setVisible(true);
                                error = true;
                                break;
                            } else if (i == 0 && !entries[0].equals("")) {
                                if (entries[0].matches("[0-9]+")) {
                                    for(Contract contract : contracts) {
                                        if (contract.getPhone() == Long.parseLong(entries[0]) && contract.isActive()) {
                                            warning.setText("This phone number is already in use");
                                            warning.setForeground(Color.RED);
                                            warning.setVisible(true);
                                            error = true;
                                        }
                                    }
                                    String selectedPlan = boxes[1].getItemAt(boxes[1].getSelectedIndex());
                                    if (!selectedPlan.equals("<empty>")) {
                                        Plan plan = findPlanById(Integer.parseInt(selectedPlan));
                                        int count = 0;
                                        char firstDigit = entries[0].charAt(0);
                                        for (int j = 0; j < entries[0].length(); j++) {
                                            if (entries[0].charAt(j) != ' ') {
                                                count++;
                                            }
                                        }
                                        if (count == 10) {
                                            if (plan instanceof MobilePlan && firstDigit != '6') {
                                                warning.setText("Mobile Phone must begin with the digit '6'!");
                                                warning.setForeground(Color.RED);
                                                warning.setVisible(true);
                                                error = true;
                                            } else if (plan instanceof LandlinePlan && firstDigit != '2') {
                                                warning.setText("Landline Phone must begin with the digit '2'!");
                                                warning.setForeground(Color.RED);
                                                warning.setVisible(true);
                                                error = true;
                                            }
                                        } else {
                                            warning.setText("Phone must consist of 10-digits!");
                                            warning.setForeground(Color.RED);
                                            warning.setVisible(true);
                                            error = true;
                                        }
                                    }
                                } else {
                                    phoneField.setText("");
                                    warning.setText("Phone must consist of numbers!");
                                    warning.setForeground(Color.RED);
                                    warning.setVisible(true);
                                    error = true;
                                    break;
                                }
                            } else if (i == 3) {
                                LocalDate selectedDate = LocalDate.parse(boxes[2].getItemAt(boxes[2].getSelectedIndex()));
                                for (Contract contract : contracts) {
                                    long phone = contract.getPhone();
                                    if (phone == Long.parseLong(entries[0])) {
                                        LocalDate date = contract.getDate();
                                        if (date.isBefore(selectedDate) && contract.isActive()) {
                                            warning.setText("There is another active contract " + "\n" + "associated with the same phone number");
                                            warning.setForeground(Color.RED);
                                            warning.setVisible(true);
                                            error = true;
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                        if (!error) {
                            long phone = Long.parseLong(entries[0]);
                            int afm = Integer.parseInt(entries[1]);
                            Plan plan = findPlanById(Integer.parseInt(entries[2]));
                            LocalDate date = LocalDate.parse(entries[3]);
                            String dateForId = date.format(DateTimeFormatter.ofPattern("ddMMyyyy"));
                            String type = null;
                            if (plan instanceof MobilePlan) {
                                type = "Mobile";
                            } else {
                                type = "Landline";
                            }
                            String id = dateForId + "-" + afm + "-" + type;
                            int duration = Integer.parseInt(entries[4]);
                            String billType = entries[5];
                            System.out.println(billType);
                            System.out.println(entries[6]);
                            String payment = entries[6];
                            boolean active = true;
                            Client client = findClientByAfm(afm);
                            assert client != null;
                            double discount = 0.0;
                            if (client.getOccupation().equals("professional")) {
                                discount += 0.10;
                            } else if (client.getOccupation().equals("student")) {
                                discount += 0.15;
                            }
                            if (plan.getFreeMin() > 1000) {
                                if (plan instanceof MobilePlan) {
                                    discount += 0.11;
                                } else {
                                    discount += 0.08;
                                }
                            }
                            if (billType.equals("electronic")) {
                                discount += 0.05;
                            }
                            if (payment.equals("card")) {
                                discount += 0.02;
                            }
                            double cost = plan.getCost();
                            if (discount != 0.0) {
                                cost = cost - cost*discount;
                            }
                            double cancelCost = 0.1 * cost;

                            contracts.add(new Contract(id, phone, afm, plan, date, duration, discount, cost, billType, payment, cancelCost, active));
                            warning.setText("New contract created successfully!");
                            warning.setForeground(Color.GREEN);
                            warning.setVisible(true);
                            if (hitOnce[0]) {
                                panel1in.removeAll();
                                rec[0] = updateContractTable();
                                table[0] = new JTable(rec[0], header);
                                newScroll[0] = new JScrollPane(table[0]);
                                panel1in.add(newScroll[0]);
                                panel1in.repaint();
                                panel1in.revalidate();
                            }
                        }
                    }
                });
                panel3.add(panel3in);
                panel3.repaint();
                panel3.revalidate();
            }
        });

        searchBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean flag = true;
                for (int i = 0; i < table[0].getRowCount(); i++) {
                    if (textField.getText().trim().equals(table[0].getModel().getValueAt(i, 1).toString())) {
                        table[0].setRowSelectionInterval(i, i);
                        flag = false;
                    }
                }
                if (flag) {
                    table[0].clearSelection();
                }
            }
        });

        boxType.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selection = boxType.getItemAt(boxType.getSelectedIndex());
                if (!selection.equals("<empty>")) {
                    panel1in.removeAll();
                    rec[0] = updateContractTableFiltered(rec[0], selection);
                    table[0] = new JTable(rec[0], header);
                    newScroll[0] = new JScrollPane(table[0]);
                    panel1in.add(newScroll[0]);
                    panel1in.repaint();
                    panel1in.revalidate();
                } else {
                    panel1in.removeAll();
                    rec[0] = new String[][]{};
                    table[0] = new JTable(rec[0], header);
                    newScroll[0] = new JScrollPane(table[0]);
                    panel1in.add(newScroll[0]);
                    panel1in.repaint();
                    panel1in.revalidate();
                }
            }

        });

        return myPanel;
    }

    /**
     *
     * @return
     */
    public JPanel panelPlan() {
        JPanel myPanel = new JPanel(new GridBagLayout());
        myPanel.setSize(myFrame.getSize());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridy = 0;
        constraints.gridx = 0;
        constraints.weightx = 1;
        constraints.weighty = 0.33;
        constraints.anchor = GridBagConstraints.WEST;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.insets = new Insets(4, 4, 4, 4);

        JPanel panel1 = new JPanel(new GridLayout(2, 1));
        panel1.setBorder(new TitledBorder(null, "Plan Section", TitledBorder.LEADING, TitledBorder.TOP, null, null));

        JPanel panel1in = new JPanel(new BorderLayout());
        String[] header = {"ID", "COMPANY", "FREE_MIN", "COST", "FREE_SMS", "FREE_Gb", "SPEED", "TYPE"};
        final String[][][] rec = {new String[][]{}};
        final JTable[] table = {new JTable(rec[0], header)};
        final JScrollPane[] newScroll = {new JScrollPane(table[0])};
        panel1in.add(newScroll[0]);
        panel1.add(panel1in);

        JPanel panel3 = new JPanel(new BorderLayout());
        panel3.setBorder(new TitledBorder(null, "Actions", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        panel1.add(panel3);

        myPanel.add(panel1, constraints);

        JButton addPlanBtn = new JButton("Add Plan");
        JButton listPlansBtn = new JButton("Show Plans");
        final boolean[] hitOnce = {false};
        JButton editPlanBtn = new JButton("Edit Plan");
        JButton deletePlanBtn = new JButton("Delete Plan");
        JButton returnBtn = new JButton("RETURN");

        JTextField textField = new JTextField(5);
        JButton searchBtn = new JButton(new ImageIcon("src/images/search.png"));
        JPanel search1Panel = new JPanel();
        JLabel search1Lbl = new JLabel("Company Name");
        search1Panel.add(search1Lbl);
        search1Panel.add(searchBtn);

        JLabel searchLbl = new JLabel(new ImageIcon("src/images/search.png"));
        JPanel searchPanel = new JPanel(new FlowLayout());
        JComboBox<String> boxType = new JComboBox<>();
        boxType.addItem("<empty>");
        boxType.addItem("MobilePlan");
        boxType.addItem("LandlinePlan");
        searchPanel.add(searchLbl);
        searchPanel.add(boxType);

        JPanel panel2 = new JPanel(new BorderLayout(0, 0));
        panel2.setBorder(new TitledBorder(null, "Options", TitledBorder.CENTER, TitledBorder.TOP, null, null));
        JPanel panel2inside = new JPanel(new GridBagLayout());
        GridBagConstraints c2 = new GridBagConstraints();
        c2.gridx = 0;
        c2.gridy = 0;
        c2.gridwidth = GridBagConstraints.REMAINDER;
        c2.fill = GridBagConstraints.HORIZONTAL;
        c2.weightx = 1;
        c2.insets = new Insets(4, 4, 4, 4);
        panel2inside.add(searchPanel, c2);
        c2.gridy++;
        panel2inside.add(search1Panel, c2);
        c2.gridy++;
        panel2inside.add(textField, c2);
        c2.gridy++;
        panel2inside.add(addPlanBtn, c2);
        c2.gridy++;
        panel2inside.add(listPlansBtn, c2);
        c2.gridy++;
        panel2inside.add(editPlanBtn, c2);
        c2.gridy++;
        panel2inside.add(deletePlanBtn, c2);
        c2.gridy++;
        c2.weighty = 1;
        c2.anchor = GridBagConstraints.SOUTH;
        panel2inside.add(returnBtn, c2);
        constraints.gridy = 0;
        constraints.gridx++;
        constraints.gridheight = GridBagConstraints.REMAINDER;
        constraints.fill = GridBagConstraints.VERTICAL;
        constraints.weighty = 1;
        constraints.weightx = 0;
        panel2.add(panel2inside);
        myPanel.add(panel2, constraints);

        listPlansBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                hitOnce[0] = true;
                panel1in.removeAll();
                rec[0] = updatePlanTable();
                table[0] = new JTable(rec[0], header);
                newScroll[0] = new JScrollPane(table[0]);
                panel1in.add(newScroll[0]);
                panel1in.repaint();
                panel1in.revalidate();
            }
        });

        deletePlanBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (table[0].getSelectedRow() != -1) {
                    String selected = table[0].getModel().getValueAt(table[0].getSelectedRow(), 0).toString();
                    int id = Integer.parseInt(selected);
                    int input = JOptionPane.showConfirmDialog(null, "Remove plan: " + selected + " ?");
                    //0=yes,1=no,2=cancel
                    if (input == 0 ) {
                        boolean cannotDelete = false;
                        for (Contract contract : contracts) {
                            if (contract.getPlan() == findPlanById(id)) {
                                cannotDelete = true;
                            }
                        }
                        if (!cannotDelete) {
                            removePlan(id);
                            panel1in.removeAll();
                            rec[0] = updatePlanTable();
                            table[0] = new JTable(rec[0], header);
                            newScroll[0] = new JScrollPane(table[0]);
                            panel1in.add(newScroll[0]);
                            panel1in.repaint();
                            panel1in.revalidate();
                        } else {
                            String message = "Cannot delete!" + "\n" + "There are contracts associated with this plan!";
                            JOptionPane.showMessageDialog(new JFrame(),message , "Warning", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                } else {
                    String message = "Select a plan first!";
                    JOptionPane.showMessageDialog(new JFrame(),message , "Warning", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        returnBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                myFrame.getContentPane().removeAll();
                myFrame.invalidate();
                myFrame.setContentPane(mainMenu());
                myFrame.repaint();
                myFrame.revalidate();
            }
        });

        addPlanBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panel3.removeAll();
                JPanel panel3in = new JPanel(new GridBagLayout());
                GridBagConstraints constraints1 = new GridBagConstraints();
                constraints1.gridx = 0;
                constraints1.gridy = 0;
                constraints1.insets = new Insets(4, 4, 4, 4);
                JLabel title = new JLabel("Insert new vehicle");
                panel3in.add(title, constraints1);
                constraints1.gridy++;
                JPanel panelLineType = new JPanel(new FlowLayout());
                JLabel typeLbl = new JLabel("Choose type: ");
                panelLineType.add(typeLbl);
                JComboBox<String> boxType = new JComboBox<>();
                boxType.addItem("<empty>");
                boxType.addItem("MobilePlan");
                boxType.addItem("LandlinePlan");
                panelLineType.add(boxType);
                panel3in.add(panelLineType, constraints1);
                constraints1.gridy++;

                boxType.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        boxType.setEnabled(false);
                        String selection = boxType.getItemAt(boxType.getSelectedIndex());
                        if (!selection.equals("<empty>")) {

                            JPanel panel = new JPanel(new GridLayout(7, 0));

                            String[] header = {"COMPANY ", "FREE_MIN", "COST    ", "FREE_SMS", "FREE_Gb ", "SPEED   ", "TYPE    "};

                            JLabel[] labels = new JLabel[5];
                            JTextField[] fields = new JTextField[4];

                            for (int i = 0; i < 3; i++) {
                                labels[i] = new JLabel(header[i], JLabel.TRAILING);
                            }

                            JComboBox<String> boxCompanies = new JComboBox<>();
                            boxCompanies.addItem("<empty>");
                            for (TelecommunicationCompany company : companies) {
                                boxCompanies.addItem(company.getName());
                            }
                            panel.add(labels[0]); labels[0].setLabelFor(boxCompanies); panel.add(boxCompanies);

                            fields[0] = new JTextField(10);
                            fields[1] = new JTextField(10);
                            panel.add(labels[1]); labels[1].setLabelFor(fields[0]); panel.add(fields[0]);
                            panel.add(labels[2]); labels[2].setLabelFor(fields[1]); panel.add(fields[1]);

                            if (selection.equals("MobilePlan")) {
                                labels[3] = new JLabel(header[3], JLabel.TRAILING);
                                labels[4] = new JLabel(header[4], JLabel.TRAILING);
                            } else {
                                labels[3] = new JLabel(header[5], JLabel.TRAILING);
                                labels[4] = new JLabel(header[6], JLabel.TRAILING);
                            }

                            fields[2] = new JTextField(10);
                            fields[3] = new JTextField(10);
                            panel.add(labels[3]); labels[3].setLabelFor(fields[2]); panel.add(fields[2]);
                            panel.add(labels[4]); labels[4].setLabelFor(fields[3]); panel.add(fields[3]);

                            JButton submit = new JButton("Submit");
                            JLabel warning = new JLabel("");
                            warning.setVisible(false);
                            panel.add(submit);
                            panel.add(warning);

                            panel3in.add(panel, constraints1);
                            panel3in.repaint();
                            panel3in.revalidate();

                            submit.addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    boolean error = false;
                                    String[] entries = new String[5];
                                    entries[0] = boxCompanies.getItemAt(boxCompanies.getSelectedIndex());
                                    for (int i = 1; i < 5; i++) {
                                        entries[i] = fields[i - 1].getText();
                                    }
                                    for (int i = 0; i < 5; i++) {
                                        if (entries[i].equals("") || entries[i].equals("<empty>")) {
                                            warning.setText(labels[i].getText() + " is not valid!");
                                            warning.setForeground(Color.RED);
                                            warning.setVisible(true);
                                            error = true;
                                            break;
                                        }
                                    }
                                    if (labels[4].getText().equals("FREE_Gb ")) {
                                        try {
                                            int x = Integer.parseInt(entries[4]);
                                        } catch (NumberFormatException ex) {
                                            fields[3].setText("");
                                            warning.setText("FREE_Gb must be a number!");
                                            warning.setForeground(Color.RED);
                                            warning.setVisible(true);
                                            error = true;
                                        }
                                    }
                                    try {
                                        int x = Integer.parseInt(entries[3]);
                                    } catch (NumberFormatException ex) {
                                        if (labels[3].getText().equals("FREE_SMS")) {
                                            warning.setText("FREE_SMS must be a number!");
                                        } else {
                                            warning.setText("SPEED must be a number!");
                                        }
                                        warning.setForeground(Color.RED);
                                        warning.setVisible(true);
                                        fields[2].setText("");
                                        error = true;
                                    }
                                    try {
                                        Double x = Double.parseDouble(entries[2]);
                                    } catch (NumberFormatException ex) {
                                        warning.setText("COST must be a number!");
                                        warning.setForeground(Color.RED);
                                        warning.setVisible(true);
                                        fields[1].setText("");
                                        error = true;
                                    }
                                    try {
                                        int x = Integer.parseInt(entries[1]);
                                    } catch (NumberFormatException ex) {
                                        warning.setText("FREE_MIN must be a number!");
                                        warning.setForeground(Color.RED);
                                        warning.setVisible(true);
                                        fields[0].setText("");
                                        error = true;
                                    }
                                    if (!error) {
                                        if (selection.equals("MobilePlan")) {
                                            plans.add(new MobilePlan(Plan.CODE + 1 , findTelecommunicationCompanyByName(entries[0]), Integer.parseInt(entries[1]), Double.parseDouble(entries[2]), Integer.parseInt(entries[3]), Integer.parseInt(entries[4])));
                                        } else {
                                            plans.add(new LandlinePlan(Plan.CODE + 1 , findTelecommunicationCompanyByName(entries[0]), Integer.parseInt(entries[1]), Double.parseDouble(entries[2]), Integer.parseInt(entries[3]), entries[4]));
                                        }
                                        warning.setText("New Plan created successfully!");
                                        warning.setForeground(Color.GREEN);
                                        warning.setVisible(true);

                                        if (hitOnce[0]) {
                                            panel1in.removeAll();
                                            rec[0] = updatePlanTable();
                                            table[0] = new JTable(rec[0], header);
                                            newScroll[0] = new JScrollPane(table[0]);
                                            panel1in.add(newScroll[0]);
                                            panel1in.repaint();
                                            panel1in.revalidate();
                                        }
                                    }
                                }
                            });

                        }
                    }
                });
                panel3.add(panel3in);
                panel3.repaint();
                panel3.revalidate();
            }
        });

        editPlanBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (table[0].getSelectedRow() != -1) {
                    String selected = table[0].getModel().getValueAt(table[0].getSelectedRow(), 0).toString();
                    Plan plan = findPlanById(Integer.parseInt(selected));
                    panel3.removeAll();
                    JPanel panel3in = new JPanel(new GridBagLayout());
                    GridBagConstraints constraints1 = new GridBagConstraints();
                    constraints1.gridx = 0;
                    constraints1.gridy = 0;
                    constraints1.insets = new Insets(4, 4, 4, 4);
                    assert plan != null;
                    JLabel title = new JLabel("Edit plan's " + plan.getId() + " info");
                    panel3in.add(title, constraints1);
                    constraints1.gridy++;

                    JPanel panel = new JPanel(new GridLayout(7, 0));

                    String[] header = {"ID", "COMPANY ", "FREE_MIN", "COST    ", "FREE_SMS", "FREE_Gb ", "SPEED   ", "TYPE    "};

                    JLabel[] labels = new JLabel[6];
                    JTextField[] fields = new JTextField[6];

                    for (int i = 0; i < 4; i++) {
                        labels[i] = new JLabel(header[i], JLabel.TRAILING);
                        fields[i] = new JTextField(10);
                        panel.add(labels[i]); labels[i].setLabelFor(fields[i]); panel.add(fields[i]);
                    }
                    fields[0].setText(String.valueOf(plan.getId())); fields[0].setEnabled(false);
                    fields[1].setText(plan.getCompany().getName()); fields[1].setEnabled(false);
                    if (plan instanceof MobilePlan) {
                        labels[4] = new JLabel(header[4], JLabel.TRAILING);
                        labels[5] = new JLabel(header[5], JLabel.TRAILING);
                    } else {
                        labels[4] = new JLabel(header[6], JLabel.TRAILING);
                        labels[5] = new JLabel(header[7], JLabel.TRAILING);
                    }
                    fields[4] = new JTextField(10);
                    fields[5] = new JTextField(10);
                    panel.add(labels[4]); labels[4].setLabelFor(fields[4]); panel.add(fields[4]);
                    panel.add(labels[5]); labels[5].setLabelFor(fields[5]); panel.add(fields[5]);

                    JButton submit = new JButton("Submit");
                    JLabel warning = new JLabel("");
                    warning.setVisible(false);
                    panel.add(submit);
                    panel.add(warning);

                    panel3in.add(panel, constraints1);
                    panel3in.repaint();
                    panel3in.revalidate();

                    submit.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            boolean error = false;
                            String[] entries = new String[4];
                            for (int i = 0; i < 4; i++) {
                                entries[i] = fields[i + 2].getText();
                            }
                            if (!entries[3].equals("")) {
                                if (labels[5].getText().equals("FREE_Gb ")) {
                                    try {
                                        int x = Integer.parseInt(entries[3]);
                                    } catch (NumberFormatException ex) {
                                        error = true;
                                        fields[5].setText("");
                                        warning.setText("FREE_Gb must be a number!");
                                        warning.setForeground(Color.RED);
                                        warning.setVisible(true);
                                    }
                                }
                            }
                            if (!entries[2].equals("")) {
                                try {
                                    int x = Integer.parseInt(entries[2]);
                                } catch (NumberFormatException ex) {
                                    error = true;
                                    if (labels[3].getText().equals("FREE_SMS")) {
                                        warning.setText("FREE_SMS must be a number!");
                                    } else {
                                        warning.setText("SPEED must be a number!");
                                    }
                                    fields[4].setText("");
                                    warning.setForeground(Color.RED);
                                    warning.setVisible(true);
                                }
                            }
                            if (!entries[1].equals("")) {
                                try {
                                    Double x = Double.parseDouble(entries[1]);
                                } catch (NumberFormatException ex) {
                                    warning.setText("COST must be a number!");
                                    warning.setForeground(Color.RED);
                                    warning.setVisible(true);
                                    fields[3].setText("");
                                    error = true;
                                }
                            }
                            if (!entries[0].equals("")) {
                                try {
                                    int x = Integer.parseInt(entries[0]);
                                } catch (NumberFormatException ex) {
                                    warning.setText("FREE_MIN must be a number!");
                                    warning.setForeground(Color.RED);
                                    warning.setVisible(true);
                                    fields[2].setText("");
                                    error = true;
                                }
                            }
                            if (!error) {
                                for (int i = 0; i < 4; i ++) {
                                    if (!entries[i].equals("")) {
                                        if (i == 0) {
                                            plan.setFreeMin(Integer.parseInt(entries[0]));
                                        }
                                        if (i == 1) {
                                            plan.setCost(Double.parseDouble(entries[1]));
                                        }
                                        if (i == 2) {
                                            if (plan instanceof MobilePlan) {
                                                ((MobilePlan) plan).setFreeSms(Integer.parseInt(entries[2]));
                                            } else {
                                                ((LandlinePlan) plan).setSpeed(Integer.parseInt(entries[2]));
                                            }
                                        }
                                        if (i == 3) {
                                            if (plan instanceof MobilePlan) {
                                                ((MobilePlan) plan).setFreeGb(Integer.parseInt(entries[3]));
                                            } else {
                                                ((LandlinePlan) plan).setType(entries[3]);
                                            }
                                        }
                                    }
                                }

                                warning.setText("Info edited successfully!");
                                warning.setForeground(Color.GREEN);
                                warning.setVisible(true);

                                if (hitOnce[0]) {
                                    panel1in.removeAll();
                                    rec[0] = updatePlanTable();
                                    table[0] = new JTable(rec[0], header);
                                    newScroll[0] = new JScrollPane(table[0]);
                                    panel1in.add(newScroll[0]);
                                    panel1in.repaint();
                                    panel1in.revalidate();
                                }
                            }
                        }
                    });
                    panel3.add(panel3in);
                    panel3.repaint();
                    panel3.revalidate();
                } else {
                    String message = "Select a plan first!";
                    JOptionPane.showMessageDialog(new JFrame(), message, "Warning", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        boxType.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selection = boxType.getItemAt(boxType.getSelectedIndex());
                if (!selection.equals("<empty>")) {
                    panel1in.removeAll();
                    rec[0] = updatePlanTableFiltered(rec[0], selection);
                    table[0] = new JTable(rec[0], header);
                    newScroll[0] = new JScrollPane(table[0]);
                    panel1in.add(newScroll[0]);
                    panel1in.repaint();
                    panel1in.revalidate();
                } else {
                    panel1in.removeAll();
                    rec[0] = new String[][]{};
                    table[0] = new JTable(rec[0], header);
                    newScroll[0] = new JScrollPane(table[0]);
                    panel1in.add(newScroll[0]);
                    panel1in.repaint();
                    panel1in.revalidate();
                }
            }
        });

        searchBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean flag = true;
                for (int i = 0; i < table[0].getRowCount(); i++) {
                    if (textField.getText().trim().equals(table[0].getModel().getValueAt(i, 1).toString())) {
                        table[0].setRowSelectionInterval(i, i);
                        flag = false;
                    }
                }
                if (flag) {
                    table[0].clearSelection();
                }
            }
        });

        return myPanel;
    }
}