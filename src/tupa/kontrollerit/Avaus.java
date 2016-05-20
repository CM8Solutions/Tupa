package tupa.kontrollerit;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import tupa.data.Ottelu;
import tupa.data.TuomarinRooli;
import tupa.data.Kokoonpano;
import tupa.data.Maali;
import tupa.data.Kohde;
import tupa.data.Turnaus;
import tupa.data.Sarja;
import tupa.data.Tuomari;
import tupa.data.Pelaaja;
import tupa.data.Toimihenkilo;
import tupa.data.Joukkue;

/**
 *
 * @author Marianne
 */
public class Avaus {

    private List<Kohde> kohdetk = new ArrayList<>();
    private Connection con = null;
    private Statement st = null;
    private Statement st2 = null;
    private Statement st3 = null;
    private Statement st4 = null;
    private Yhteys yhteys = new Yhteys();
    private String sql = "";
    private String sql2 = "";
    private String sql3 = "";
    private String sql4 = "";
    private int turnaus_id;
    private Turnaus turnaus;

    public Avaus() {

    }

    public List<Kohde> avaa() throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException {

        try {

            con = yhteys.annaYhteys();
            st = con.createStatement();
            st2 = con.createStatement();
             st3 = con.createStatement();
            st4 = con.createStatement();
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

                turnaus.annaTuomarit().add(tuomari);

                kohdetk.add((Kohde) tuomari);
            }

            //haetaan sarjat
            sql = "SELECT * FROM sarja WHERE turnaus_id='" + turnaus_id + "'";

            ResultSet sarjat = st.executeQuery(sql);

            while (sarjat.next()) {
                String snimi = sarjat.getString("nimi");

                int sid = sarjat.getInt("id");

                Sarja sarja = new Sarja(snimi, turnaus);
                sarja.asetaID(sid);

                turnaus.annaSarjat().add(sarja);

                kohdetk.add((Kohde) sarja);

                //haetaan ko sarjan joukkueet
                sql2 = "SELECT DISTINCT * FROM joukkue WHERE sarja_id='" + sid + "'";

                ResultSet joukkueet = st2.executeQuery(sql2);

                while (joukkueet.next()) {
                    String jnimi = joukkueet.getString("nimi");

                    int jid = joukkueet.getInt("id");

                    Joukkue joukkue = new Joukkue(jnimi);
                    joukkue.asetaID(jid);

                    joukkue.asetaSarja(sarja);

                    sarja.annaJoukkueet().add(joukkue);

                    kohdetk.add((Kohde) joukkue);

                    //haetaan ko joukkueen pelaajat
                    sql3 = "SELECT DISTINCT * FROM pelaaja WHERE joukkue_id='" + jid + "'";

                    ResultSet pelaajat = st3.executeQuery(sql3);

                    while (pelaajat.next()) {
                        String etunimi = pelaajat.getString("etunimi");
                        String sukunimi = pelaajat.getString("sukunimi");
                        String pelipaikka = pelaajat.getString("pelipaikka");

                        int pid = pelaajat.getInt("id");
                        int pelinumero = pelaajat.getInt("pelinumero");
                        int pelaaja_id = pelaajat.getInt("pelaaja_id");
                      

                        Pelaaja pelaaja = new Pelaaja(etunimi, sukunimi);
                        pelaaja.asetaID(pid);
                        pelaaja.asetaPelinumero(pelinumero);
                        pelaaja.asetaJulkinenID(pelaaja_id);
                        pelaaja.asetaPelipaikka(pelipaikka);
                        pelaaja.asetaJoukkue(joukkue);

                        joukkue.annaPelaajat().add(pelaaja);

                        kohdetk.add((Kohde) pelaaja);

                    }
                    
                         //haetaan ko joukkueen toimarit
                    sql4 = "SELECT DISTINCT * FROM toimari WHERE joukkue_id='" + jid + "'";

                    ResultSet toimarit = st4.executeQuery(sql4);

                    while (toimarit.next()) {
                        String etunimi = toimarit.getString("etunimi");
                        String sukunimi = toimarit.getString("sukunimi");
                        String rooli = toimarit.getString("rooli");
                        String sposti = toimarit.getString("sposti");
                        String puh = toimarit.getString("puh");
                        
                        int toid = toimarit.getInt("id");
                                     

                        Toimihenkilo toimari = new Toimihenkilo(etunimi, sukunimi);
                        toimari.asetaID(toid);
                        toimari.asetaSposti(sposti);
                        toimari.asetaPuh(puh);
                        toimari.asetaRooli(rooli);
                        

                        toimari.asetaJoukkue(joukkue);

                        joukkue.annaToimarit().add(toimari);

                        kohdetk.add((Kohde) toimari);

                    }

                }

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
