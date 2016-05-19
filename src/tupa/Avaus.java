package tupa;

import java.io.EOFException;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Marianne
 */
public class Avaus {

    private List<Kohde> kohdetk = new ArrayList<>();
    private Connection con = null;
    private Statement st = null;
    private Yhteys yhteys = new Yhteys();
    private String sql = "";
    private int turnaus_id;
    private Turnaus turnaus;


    Avaus() {

    }

    public List<Kohde> avaa() throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException {

        try {

            con = yhteys.annaYhteys();
            st = con.createStatement();

            //haetaan turnaus
            sql = "SELECT * FROM turnaus WHERE id=1";

            ResultSet turnaukset = st.executeQuery(sql);

            while (turnaukset.next()) {
                String nimi = turnaukset.getString("nimi");
                turnaus_id = turnaukset.getInt("id");

                turnaus = new Turnaus();
                turnaus.asetaNimi(nimi);
                turnaus.asetaID(turnaus_id);
                kohdetk.add((Kohde) turnaus);
            }

            //haetaan tuomarit
            sql = "SELECT * FROM tuomari WHERE turnaus_id='" + turnaus_id + "'";

            ResultSet tuomarit = st.executeQuery(sql);

            while (tuomarit.next()) {
                String etunimi = tuomarit.getString("etunimi");
                String sukunimi = tuomarit.getString("sukunimi");
                int id = tuomarit.getInt("id");
                int julkinen_id = tuomarit.getInt("tuomari_id");
                Tuomari tuomari = new Tuomari(etunimi, sukunimi);
                tuomari.asetaID(id);
                tuomari.asetaTurnaus(turnaus);
                tuomari.asetaJulkinenId(julkinen_id);
                kohdetk.add((Kohde) tuomari);
            }

            //haetaan sarjat
            sql = "SELECT * FROM sarja WHERE turnaus_id='" + turnaus_id + "'";

            ResultSet sarjat = st.executeQuery(sql);

            while (sarjat.next()) {
                String nimi = sarjat.getString("nimi");

                int id = sarjat.getInt("id");

                Sarja sarja = new Sarja(nimi, turnaus);
                sarja.asetaID(id);

                kohdetk.add((Kohde) sarja);
            }

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

        return kohdetk;
    }

}
