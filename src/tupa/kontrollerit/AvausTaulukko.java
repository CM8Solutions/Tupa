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

            Tiedottaja tiedottaja = new Tiedottaja();
            tiedottaja.annaVirhe("" + se);
        } catch (Exception e) {

            Tiedottaja tiedottaja = new Tiedottaja();
            tiedottaja.annaVirhe("" + e);
        } finally {

            try {
                if (st != null) {
                    con.close();
                }
            } catch (SQLException se) {
                Tiedottaja tiedottaja = new Tiedottaja();
                tiedottaja.annaVirhe("" + se);
            }
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException se) {
                Tiedottaja tiedottaja = new Tiedottaja();
                tiedottaja.annaVirhe("" + se);
            }
        }
        return taulukko;

    }
}
