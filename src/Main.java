import javax.swing.*;

/**
 * @author Ioanna
 */
public class Main {
    /**
     *Η main κλάση του προγράμματος όπου
     *δημιουργούμε ένα στιγμιότυπο της κλάσης console
     * @param args
     */
    public static void main(String[] args) {

        JFrame myFrame = new JFrame();
        myFrame.setSize(800, 800);
        myFrame.setTitle("Telecommunications Management");

        Console console = new Console(myFrame);
        console.ReadFile();
        console.mainMenu();

        myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        myFrame.setLocationRelativeTo(null);
        myFrame.setVisible(true);
    }
}
