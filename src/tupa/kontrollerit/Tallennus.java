package tupa.kontrollerit;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import tupa.Tupa;
import tupa.data.Kohde;
import tupa.data.Turnaus;
import tupa.data.Sarja;
import tupa.data.Tuomari;
import tupa.data.Pelaaja;
import tupa.data.Toimihenkilo;
import tupa.data.Joukkue;

/**
 *
 * @author Marianne & Victor
 */
public class Tallennus {

    private Tupa ikkuna;
    private List<Kohde> kohdetk = new ArrayList<>();
    private Connection con = null;
    private Statement st = null;
    private Yhteys yhteys = new Yhteys();
    private int turnaus_id;
    private String sql = "";
    private boolean poisto = true;

    public Tallennus() {

    }

    public Tallennus(Tupa ikkuna) {

        this.ikkuna = ikkuna;
    }

    public void suoritaTallennus() throws InstantiationException, SQLException, IllegalAccessException {

        kohdetk = ikkuna.annaKohteet();

        try {
            con = yhteys.annaYhteys();
            st = con.createStatement();

            if (poisto) {

                Turnaus turnaus = (Turnaus) ikkuna.annaTurnaus();
                turnaus_id = turnaus.annaID();

                String turnaus_nimi = turnaus.toString();

                ResultSet turnaukset = st.executeQuery("SELECT * FROM  turnaus");
                boolean loyty = false;
                while (turnaukset.next()) {

                    int tid = turnaukset.getInt("id");

                    if (tid == turnaus_id) {
                        loyty = true;
                        break;
                    }

                }

                //ei ollut kannassa ennestään
                if (!loyty) {

                    st.executeUpdate("INSERT INTO turnaus (id, nimi) VALUES('" + turnaus_id + "', '" + turnaus_nimi + "')");

                } //oli jo kannassa, jolloin kaikki siihen liittyvät tiedot tyhjennetään ennen tallentamista
                else {
                    st.executeUpdate("UPDATE turnaus SET nimi='" + turnaus_nimi + "' WHERE id='" + turnaus_id + "'");

                    if (poisto) {
                        //tyhjennetään tuomarit
                        sql = "DELETE FROM tuomari WHERE turnaus_id='" + turnaus_id + "'";
                        st.executeUpdate(sql);
                        //tyhjennetään sarjat
                        sql = "DELETE FROM sarja WHERE turnaus_id='" + turnaus_id + "'";
                        st.executeUpdate(sql);
                        //MUIDEN TYHJENNYS!!
                    }

                }

                //ja täytetään se kohdetk:n mukaiseksi
                if (!kohdetk.isEmpty()) {

                    for (int i = 0; i < kohdetk.size(); i++) {

                        Kohde tiedot = kohdetk.get(i);

                        if (tiedot instanceof Sarja) {

                            Sarja sarja = (Sarja) tiedot;

                            int id = sarja.annaID();
                            String nimi = sarja.toString();

                            st.executeUpdate("INSERT INTO sarja (id, nimi, turnaus_id) VALUES('" + id + "', '" + nimi + "', '" + turnaus_id + "')");

                        } else if (tiedot instanceof Tuomari) {

                            Tuomari tuomari = (Tuomari) tiedot;

                            int tuomari_id = tuomari.annaJulkinenId();
                            int id = tuomari.annaID();
                            String etunimi = tuomari.annaEtuNimi();
                            String sukunimi = tuomari.annaSukuNimi();

                            st.executeUpdate("INSERT INTO tuomari (id, etunimi, sukunimi, tuomari_id, turnaus_id) VALUES('" + id + "', '" + etunimi + "', '" + sukunimi + "', '" + tuomari_id + "', '" + turnaus_id + "')");

                        } else if (tiedot instanceof Joukkue) {

                            Joukkue joukkue = (Joukkue) tiedot;

                        } else if (tiedot instanceof Pelaaja) {

                            Pelaaja pelaaja = (Pelaaja) tiedot;

                        } else if (tiedot instanceof Toimihenkilo) {

                            Toimihenkilo toimari = (Toimihenkilo) tiedot;

                        }

                    }

                }
            } //JOS POISTOA EI OLLA TEHTY, päivitetään ainostaan tiedot (tätä käytetään hakutoiminnon yhteydessä), tän voi tehdä vain niille, joita haetaan!
            else {

                Turnaus turnaus = (Turnaus) ikkuna.annaTurnaus();
                turnaus_id = turnaus.annaID();

                String turnaus_nimi = turnaus.toString();

                st.executeUpdate("UPDATE turnaus SET nimi='" + turnaus_nimi + "' WHERE id='" + turnaus_id + "'");

                //ja täytetään se kohdetk:n mukaiseksi
                if (!kohdetk.isEmpty()) {

                    for (int i = 0; i < kohdetk.size(); i++) {

                        Kohde tiedot = kohdetk.get(i);

                        if (tiedot instanceof Sarja) {

                            Sarja sarja = (Sarja) tiedot;

                            int id = sarja.annaID();
                            String nimi = sarja.toString();

                            st.executeUpdate("UPDATE sarja SET nimi='" + nimi + "' WHERE id='" + id + "' AND turnaus_id='" + turnaus_id + "'");

                        } else if (tiedot instanceof Tuomari) {

                            Tuomari tuomari = (Tuomari) tiedot;

                            int tuomari_id = tuomari.annaJulkinenId();
                            int id = tuomari.annaID();
                            String etunimi = tuomari.annaEtuNimi();
                            String sukunimi = tuomari.annaSukuNimi();

                            st.executeUpdate("UPDATE tuomari SET etunimi='" + etunimi + "' WHERE id='" + id + "' AND turnaus_id='" + turnaus_id + "'");
                            st.executeUpdate("UPDATE tuomari SET sukunimi='" + sukunimi + "' WHERE id='" + id + "' AND turnaus_id='" + turnaus_id + "'");

                        } else if (tiedot instanceof Joukkue) {

                            Joukkue joukkue = (Joukkue) tiedot;

                        } else if (tiedot instanceof Pelaaja) {

                            Pelaaja pelaaja = (Pelaaja) tiedot;

                        } else if (tiedot instanceof Toimihenkilo) {

                            Toimihenkilo toimari = (Toimihenkilo) tiedot;

                        }

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
        //päivitetään tilanne, että tallennus on suoritettu
        ikkuna.asetaMuutos(false);

    }

    public void asetaPoisto(boolean poisto) {
        this.poisto = poisto;
    }

}
