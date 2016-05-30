package tupa.kontrollerit;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.scene.control.TableView;
import tupa.Tupa;
import tupa.data.Yhteys;

/**
 *
 * @author Marianne
 */
public class AvausTaulukko {

    private Tupa ikkuna;
    private Connection con = null;
    private Statement st = null;
    private Yhteys yhteys = new Yhteys();
    private int turnaus_id;
    private String sql = "";
    private TableView taulukko = new TableView();

    public AvausTaulukko() {

    }

    public AvausTaulukko(Tupa ikkuna) {
        this.ikkuna = ikkuna;
    }

    public TableView annaTurnausTaulukko() {

        //avaa luettelon tietokannassa olevista turnauksista (NIMI + LUOMISPVM)
        //AUKEE UUTEEN IKKUNAAN
        try {
            con = yhteys.annaYhteys();
            st = con.createStatement();
        } catch (SQLException se) {

            se.printStackTrace();
        } catch (Exception e) {

            e.printStackTrace();
        } finally {

            try {
                if (st != null) {
                    con.close();
                }
            } catch (SQLException se) {
            }
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
        return taulukko;

    }
}
