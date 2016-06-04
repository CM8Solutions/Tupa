package tupa.kontrollerit;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import tupa.Tupa;
import tupa.data.Joukkue;
import tupa.data.Kokoonpano;
import tupa.data.Maali;
import tupa.data.Ottelu;
import tupa.data.Pelaaja;
import tupa.data.Sarja;
import tupa.data.Toimihenkilo;
import tupa.data.Tuomari;
import tupa.data.TuomarinRooli;
import tupa.data.Turnaus;
import tupa.data.Yhteys;

/**
 * Luokka, joka päivittää tietoja tietokannassa olevista tupaid-attribuuteista
 * (olion ID-attribuutti).
 *
 * @author Marianne
 */
public class LaskuriPaivittaja {

    private Connection con = null;
    private Statement st0 = null;
    private Statement st = null;
    private Statement st2 = null;
    private Statement st3 = null;
    private Statement st4 = null;
    private Statement st5 = null;
    private Statement st6 = null;
    private Statement st7 = null;
    private Statement st8 = null;
    private Statement st9 = null;
    private Statement st10 = null;
    private Statement st11 = null;
    private Yhteys yhteys = new Yhteys();
    private String sql = "";
    private Tupa ikkuna;
    private Turnaus turnaus;

    public LaskuriPaivittaja() {

    }

    public LaskuriPaivittaja(Turnaus turnaus, Tupa ikkuna) {
        this.ikkuna = ikkuna;
        this.turnaus = turnaus;
    }

    public void paivitaLaskurit() {

        try {

            con = yhteys.annaYhteys();
            st0 = con.createStatement();
            st = con.createStatement();
            st2 = con.createStatement();
            st3 = con.createStatement();
            st4 = con.createStatement();
            st5 = con.createStatement();
            st6 = con.createStatement();
            st7 = con.createStatement();
            st8 = con.createStatement();
            st9 = con.createStatement();
            st10 = con.createStatement();
            st11 = con.createStatement();

            sql = "SELECT MAX(tupaid) AS arvo FROM turnaus";

            ResultSet t = st0.executeQuery(sql);

            int maara0 = 0;
            while (t.next()) {
                maara0 = t.getInt("arvo");
            }

            turnaus.asetaLaskuri(maara0);
            int turnaus_id = turnaus.annaID();

            sql = "SELECT MAX(tupaid) AS arvo FROM sarja";

            ResultSet s = st.executeQuery(sql);

            int maara = 0;
            while (s.next()) {
                maara = s.getInt("arvo");

            }

            Sarja sarja = new Sarja();
            sarja.asetaLaskuri(maara);

            sql = "SELECT MAX(tupaid) AS arvo FROM tuomari";

            ResultSet tu = st2.executeQuery(sql);

            int maara2 = 0;
            while (tu.next()) {
                maara2 = tu.getInt("arvo");
            }

            Tuomari tuomari = new Tuomari();
            tuomari.asetaLaskuri(maara2);

            sql = "SELECT MAX(tupaid) AS arvo FROM joukkue";

            ResultSet j = st3.executeQuery(sql);

            int maara3 = 0;
            while (j.next()) {
                maara3 = j.getInt("arvo");
            }

            Joukkue joukkue = new Joukkue();
            joukkue.asetaLaskuri(maara3);

            sql = "SELECT MAX(tupaid) AS arvo FROM pelaaja";

            ResultSet p = st4.executeQuery(sql);

            int maara4 = 0;
            while (p.next()) {
                maara4 = p.getInt("arvo");
            }

            Pelaaja pelaaja = new Pelaaja();
            pelaaja.asetaLaskuri(maara4);

            sql = "SELECT MAX(tupaid) AS arvo FROM toimari";

            ResultSet to = st5.executeQuery(sql);

            int maara5 = 0;
            while (to.next()) {
                maara5 = to.getInt("arvo");
            }

            Toimihenkilo toimari = new Toimihenkilo();
            toimari.asetaLaskuri(maara5);

            sql = "SELECT MAX(tupaid) AS arvo FROM ottelu";

            ResultSet o = st6.executeQuery(sql);

            int maara6 = 0;
            while (o.next()) {
                maara6 = o.getInt("arvo");
            }

            Ottelu ottelu = new Ottelu();
            ottelu.asetaLaskuri(maara6);

            sql = "SELECT MAX(tupaid) AS arvo FROM maali";

            ResultSet m = st7.executeQuery(sql);

            int maara7 = 0;
            while (m.next()) {
                maara7 = m.getInt("arvo");
            }

            Maali maali = new Maali();
            maali.asetaLaskuri(maara7);

            sql = "SELECT MAX(tupaid) AS arvo FROM kokoonpano";

            ResultSet k = st8.executeQuery(sql);

            int maara8 = 0;
            while (k.next()) {
                maara8 = k.getInt("arvo");
            }
            Kokoonpano kokoonpano = new Kokoonpano();
            kokoonpano.asetaLaskuri(maara8);

            sql = "SELECT MAX(tupaid) AS arvo FROM tuomarinrooli";

            ResultSet tr = st9.executeQuery(sql);

            int maara9 = 0;
            while (tr.next()) {
                maara9 = tr.getInt("arvo");
            }

            TuomarinRooli rooli = new TuomarinRooli();
            rooli.asetaLaskuri(maara9);

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

    }

}
