package tupa.kontrollerit;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
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
    private Statement st5 = null;
    private Statement st6 = null;
    private Statement st7 = null;
    private Statement st8 = null;
    private Statement st9 = null;
    private Statement st10 = null;
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
            st5 = con.createStatement();
            st6 = con.createStatement();
            st7 = con.createStatement();
          st8 = con.createStatement();
            st9 = con.createStatement();
            st10 = con.createStatement();
            

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

                //haetaan ottelut
                sql = "SELECT DISTINCT * FROM ottelu  WHERE sarja_id='" + sid + "'";
                ResultSet ottelut = st5.executeQuery(sql);

                while (ottelut.next()) {
                    String nimi = ottelut.getString("nimi");

                    String paikka = ottelut.getString("paikka");

                    String kellotunnit = ottelut.getString("kellotunnit");
                    String kellominuutit = ottelut.getString("kellominuutit");
                    String paiva = ottelut.getString("paiva");
                     String tulos = ottelut.getString("tulos");
                  
                    int id = ottelut.getInt("id");
                    int ottelu_id = ottelut.getInt("ottelu_id");
                    int kierros = ottelut.getInt("kierros");
                    int vierasmaalit = ottelut.getInt("vierasmaalit");
                    int kotimaalit = ottelut.getInt("kotimaalit");

                    int kotijoukkue_id = ottelut.getInt("kotijoukkue_id");
                    int vierasjoukkue_id = ottelut.getInt("vierasjoukkue_id");

                    Ottelu ottelu = new Ottelu(sarja);
                    ottelu.asetaNimi(nimi);

                    ottelu.asetaPaikka(paikka);
                    ottelu.asetaTunnit(kellotunnit);
                    ottelu.asetaMinuutit(kellominuutit);
                    DateFormat muoto = new SimpleDateFormat("dd.mm.yyyy", Locale.ENGLISH);
                    Date paiva_date = muoto.parse(paiva);

                    ottelu.asetaPaivaDate(paiva_date);
                    ottelu.asetaID(id);
                    ottelu.asetaOttelunumero(ottelu_id);
                    ottelu.asetaKierros(kierros);
                    ottelu.asetaVierasmaalit(vierasmaalit);
                    ottelu.asetaKotimaalit(kotimaalit);

                    Joukkue kotijoukkue = new Joukkue();
                    Joukkue vierasjoukkue = new Joukkue();

                    for (int k = 0; k < sarja.annaJoukkueet().size(); k++) {
                        if (sarja.annaJoukkueet().get(k).annaID() == vierasjoukkue_id) {
                            vierasjoukkue = sarja.annaJoukkueet().get(k);
                           
                        } else if (sarja.annaJoukkueet().get(k).annaID() == kotijoukkue_id) {
                            kotijoukkue = sarja.annaJoukkueet().get(k);
                          
                        }

                    }

                    ottelu.asetaJoukkueet(kotijoukkue, vierasjoukkue);
                     if(tulos == null){
                         ottelu.asetaTulosTyhja("-");
                     }
                     else{
                          ottelu.asetaTulos(kotimaalit, vierasmaalit);
                     }
                   
                    sarja.annaOttelut().add(ottelu);

                    //otteluun liittyvät kokoonpanot
                    //kotijoukkue
                    sql4 = "SELECT DISTINCT * FROM kokoonpano WHERE ottelu_id='" + id + "' AND joukkue_id = '" + kotijoukkue_id + "'";

                    ResultSet kotikokoonpano = st6.executeQuery(sql4);
                   
                    while (kotikokoonpano.next()) {

                       int kotikokoonpano_id = kotikokoonpano.getInt("id");
                       
                       Kokoonpano kokoonpano = ottelu.annaKotiKokoonpano();
                        kokoonpano.asetaID(kotikokoonpano_id);

                        //pelaajien kokoonpanotiedot
                        sql4 = "SELECT DISTINCT * FROM pelaajan_kokoonpano WHERE kokoonpano_id = '" + kotikokoonpano_id + "'";

                        ResultSet pelaajan_kokoonpanot = st8.executeQuery(sql4);

                        while (pelaajan_kokoonpanot.next()) {

                            int pelaaja_id = pelaajan_kokoonpanot.getInt("pelaaja_id");

                            for(int k=0; k<kotijoukkue.annaPelaajat().size(); k++){
                         
                                Pelaaja pelaaja = kotijoukkue.annaPelaajat().get(k);
                                if(pelaaja.annaID() == pelaaja_id){
                                        pelaaja.annaKokoonpanot().add(kokoonpano);
                                kokoonpano.annaPelaajat().add(pelaaja);
                                }
                            
                            }
                        }

                    }
                    //vierasjoukkue
                    sql4 = "SELECT DISTINCT * FROM kokoonpano WHERE ottelu_id='" + id + "' AND joukkue_id = '" + vierasjoukkue_id + "'";

                    ResultSet vieraskokoonpano = st7.executeQuery(sql4);
                 
                    while (vieraskokoonpano.next()) {

                         int vieraskokoonpano_id = vieraskokoonpano.getInt("id");
                            Kokoonpano kokoonpano = ottelu.annaVierasKokoonpano();
                        kokoonpano.asetaID(vieraskokoonpano_id);
 //pelaajien kokoonpanotiedot
                        sql4 = "SELECT DISTINCT * FROM pelaajan_kokoonpano WHERE kokoonpano_id = '" + vieraskokoonpano_id + "'";

                        ResultSet pelaajan_kokoonpanot = st9.executeQuery(sql4);

                        while (pelaajan_kokoonpanot.next()) {

                            int pelaaja_id = pelaajan_kokoonpanot.getInt("pelaaja_id");

                            for(int k=0; k<vierasjoukkue.annaPelaajat().size(); k++){
                         
                                Pelaaja pelaaja = vierasjoukkue.annaPelaajat().get(k);
                                if(pelaaja.annaID() == pelaaja_id){
                                        pelaaja.annaKokoonpanot().add(kokoonpano);
                                kokoonpano.annaPelaajat().add(pelaaja);
                                }
                            
                            }
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

        return kohdetk;
    }

}
