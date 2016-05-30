package tupa.kontrollerit;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import tupa.data.Turnaus;
import tupa.data.Yhteys;

/**
 *
 * @author Marianne
 */
public class Aloitus {

    private Turnaus turnaus = new Turnaus();
    private Connection con = null;
    private Statement st = null;
    private Yhteys yhteys = new Yhteys();
    private String sql = "";

    public Aloitus() {

    }

    public Turnaus luoAlkuTurnaus() {

        try {

            con = yhteys.annaYhteys();
            st = con.createStatement();

            //haetaan turnaus
            int laskuri = 0;
            ResultSet turnaukset = st.executeQuery("SELECT DISTINCT * FROM turnaus");
            while (turnaukset.next()) {
                turnaus.kasvataLaskuria();
            }
            laskuri = turnaus.annaLaskuri();
            turnaus.asetaID(laskuri + 1);
            turnaus.kasvataLaskuria();
            turnaus.asetaNimi("Uusi turnaus");

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

        return turnaus;
    }

}
