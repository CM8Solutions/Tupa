package tupa.kontrollerit;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import tupa.Tupa;
import tupa.data.Kohde;
import tupa.data.Turnaus;
import tupa.data.Sarja;
import tupa.data.Tuomari;
import tupa.data.Pelaaja;
import tupa.data.Toimihenkilo;
import tupa.data.Joukkue;
import tupa.data.Ottelu;
import tupa.data.Maali;
import tupa.data.Kokoonpano;
import tupa.data.TuomarinRooli;

/**
 *
 * @author Marianne
 */
public class Tallennus {

    private Tupa ikkuna;
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
             st2 = con.createStatement();
              st3 = con.createStatement();
               st4 = con.createStatement();
                 st5 = con.createStatement();
                         st6 = con.createStatement();
               st7 = con.createStatement();
                 st8 = con.createStatement();

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
                        
                        
                        //tyhjennetään pelaajat
                        sql = "SELECT DISTINCT pelaaja.id as pid FROM sarja, joukkue, pelaaja WHERE sarja.turnaus_id='" + turnaus_id + "' AND joukkue.sarja_id = sarja.id AND pelaaja.joukkue_id = joukkue.id";
                        ResultSet pelaajat = st2.executeQuery(sql);

                        while (pelaajat.next()) {

                            int id = pelaajat.getInt("pid");
                            sql = "DELETE FROM pelaaja WHERE id='" + id + "'";
                            st.executeUpdate(sql);
                                  sql = "DELETE FROM pelaajan_kokoonpano WHERE pelaaja_id='" + id + "'";
                            st.executeUpdate(sql);
                        }

                        //tyhjennetään toimarit
                        sql = "SELECT DISTINCT toimari.id as tid FROM sarja, joukkue, toimari WHERE sarja.turnaus_id='" + turnaus_id + "' AND joukkue.sarja_id = sarja.id AND toimari.joukkue_id = joukkue.id";
                        ResultSet toimarit = st3.executeQuery(sql);

                        while (toimarit.next()) {

                            int id = toimarit.getInt("tid");
                            sql = "DELETE FROM toimari WHERE id='" + id + "'";
                            st.executeUpdate(sql);
                        }
                    
                        //tyhjennetään ottelut
                        sql = "SELECT DISTINCT ottelu.id as oid FROM sarja, joukkue, ottelu WHERE sarja.turnaus_id='" + turnaus_id + "' AND joukkue.sarja_id = sarja.id AND (ottelu.kotijoukkue_id = joukkue.id OR ottelu.vierasjoukkue_id = joukkue.id)";
                        ResultSet ottelut = st4.executeQuery(sql);

                        while (ottelut.next()) {
                            int id = ottelut.getInt("oid");
                            
                            //tyhjennetään kokoonpanot
                             sql = "DELETE FROM kokoonpano WHERE ottelu_id='" + id + "'";
                            st.executeUpdate(sql);
                            
                           //tyhjennetään ensin maalit
                           sql = "DELETE FROM maali WHERE ottelu_id='" + id + "'";
                            st.executeUpdate(sql);
                           //ja tuomarinroolit
                           
                           sql = "DELETE FROM tuomarinrooli WHERE ottelu_id='" + id + "'";
                            st.executeUpdate(sql);
                           
                            sql = "DELETE FROM ottelu WHERE id='" + id + "'";
                            st.executeUpdate(sql);
                        }

                             //tyhjennetään joukkueet
                        sql = "SELECT DISTINCT joukkue.id as jid FROM sarja, joukkue WHERE sarja.turnaus_id='" + turnaus_id + "' AND joukkue.sarja_id = sarja.id";
                        ResultSet joukkueet = st5.executeQuery(sql);

                        while (joukkueet.next()) {

                            int id = joukkueet.getInt("jid");
                            sql = "DELETE FROM joukkue WHERE id='" + id + "'";
                            st.executeUpdate(sql);
                        }
                       
                          //tyhjennetään sarjat
                        sql = "DELETE FROM sarja WHERE turnaus_id='" + turnaus_id + "'";
                        st.executeUpdate(sql);
                                              
                        //tyhjennetään tuomarit
                        sql = "DELETE FROM tuomari WHERE turnaus_id='" + turnaus_id + "'";
                        st.executeUpdate(sql);      

                    }

                }

                //ja täytetään se kohdetk:n mukaiseksi
                if (!kohdetk.isEmpty()) {

                    for (int i = 0; i < kohdetk.size(); i++) {

                        Kohde tiedot = kohdetk.get(i);

                        if (tiedot instanceof Sarja) {

                            Sarja sarja = (Sarja) tiedot;
                            int sid = sarja.annaID();
                            String snimi = sarja.toString();

                            st.executeUpdate("INSERT INTO sarja (id, nimi, turnaus_id) VALUES('" + sid + "', '" + snimi + "', '" + turnaus_id + "')");

                        } else if (tiedot instanceof Tuomari) {

                            Tuomari tuomari = (Tuomari) tiedot;
                            int tuomari_id = tuomari.annaJulkinenId();
                            int tid = tuomari.annaID();
                            String etunimi = tuomari.annaEtuNimi();
                            String sukunimi = tuomari.annaSukuNimi();
 
                            st.executeUpdate("INSERT INTO tuomari (id, etunimi, sukunimi, tuomari_id, turnaus_id) VALUES('" + tid + "', '" + etunimi + "', '" + sukunimi + "', '" + tuomari_id + "', '" + turnaus_id + "')");

                        } else if (tiedot instanceof Joukkue) {

                          Joukkue joukkue = (Joukkue) tiedot;
                           int jid = joukkue.annaID();
                         int sarja_id = joukkue.annaSarja().annaID();
                          String jnimi = joukkue.toString();

                           st.executeUpdate("INSERT INTO joukkue (id, nimi, sarja_id) VALUES('" + jid + "', '" + jnimi + "', '" + sarja_id + "')");
                          
                           //kyseisen joukkueen ottelut
                           
                           for(int j=0; j<joukkue.annaOttelut().size(); j++){
                               Ottelu ottelu = joukkue.annaOttelut().get(j);
                               int oid = ottelu.annaId();
                                   //tarkistetaan ettei oo jo laitettu
                                int ottelulaskuri = 0;
                                 sql = "SELECT DISTINCT * FROM ottelu WHERE sarja_id = '" + sarja_id + "' AND id = '"+oid+"'";
                                 ResultSet ottelut = st5.executeQuery(sql);

                                while (ottelut.next()) {
                                    ottelulaskuri++;

                                }

                                if(ottelulaskuri == 0){
                                    
                                    int ottelu_id = ottelu.annaOtteluNumero();
                                    int kotijoukkue_id = ottelu.annaKotijoukkue().annaID();
                                    int vierasjoukkue_id = ottelu.annaVierasjoukkue().annaID();
                                    int kotimaalit = ottelu.annaKotimaalit();
                                    int vierasmaalit = ottelu.annaVierasmaalit();
                                    String tulos = ottelu.annaTulos();
                                
                                    String nimi = ottelu.toString();
                                    String paiva = ottelu.getDate().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM));
                                    String kello = ottelu.annaKello();
                                    String kellotunnit = ottelu.annaKellotunnit();
                                    String kellominuutit = ottelu.annaKellominuutit();
                                    int kierros = ottelu.annaKierros();
                                    String paikka = ottelu.annaPaikka();
                                    int kotikokoonpano_id = ottelu.annaKotiKokoonpano().annaID();
                                     int vieraskokoonpano_id = ottelu.annaVierasKokoonpano().annaID();      
                              
                                       if(tulos.equals("-")){
                                      st.executeUpdate("INSERT INTO ottelu (id, nimi, sarja_id, kotijoukkue_id, vierasjoukkue_id, kotimaalit, vierasmaalit, paikka, kello, paiva, kellotunnit, kellominuutit, ottelu_id, kierros) VALUES('" + 
        
        oid + "', '" + nimi + "', '" + sarja_id + "', '" + kotijoukkue_id + "', '" + vierasjoukkue_id + "', '" + kotimaalit + "', '" + vierasmaalit + "', '" + paikka + "', '" +  kello + "', '" + paiva + "', '" + kellotunnit + "', '"+  kellominuutit + "', '" + ottelu_id + "', '" + kierros + "')");
           
                                    }
                                       else{
                                           st.executeUpdate("INSERT INTO ottelu (id, nimi, sarja_id, kotijoukkue_id, vierasjoukkue_id, kotimaalit, vierasmaalit, tulos, paikka, kello, paiva, kellotunnit, kellominuutit, ottelu_id, kierros) VALUES('" + 
        
        oid + "', '" + nimi + "', '" + sarja_id + "', '" + kotijoukkue_id + "', '" + vierasjoukkue_id + "', '" + kotimaalit + "', '" + vierasmaalit + "', '" + tulos + "', '" + paikka + "', '" +  kello + "', '" + paiva + "', '" + kellotunnit + "', '"+  kellominuutit + "', '" + ottelu_id + "', '" + kierros + "')");
           
                                       }
                                       
                
                                    //tallennetaan ko ottelun koti- ja vieraskokoonpanot
                                                                        
                                    st.executeUpdate("INSERT INTO kokoonpano (id, ottelu_id, joukkue_id) VALUES ('"+kotikokoonpano_id + "', '" + oid + "', '" + kotijoukkue_id +"')"); 
                                    st.executeUpdate("INSERT INTO kokoonpano (id, ottelu_id, joukkue_id) VALUES ('"+vieraskokoonpano_id + "', '" + oid + "', '" + vierasjoukkue_id +"')"); 

                                    
                                    //tallenetaan ko otteluun liittyvät maalitiedot
                                    
                                    for(int m=0; m<ottelu.annaMaalit().size(); m++){
                                        Maali maali = ottelu.annaMaalit().get(m);
                                        int maali_id = maali.annaID();
                                        Pelaaja maalintekija = maali.annaMaalinTekija();
                                        int maalintekija_id = maalintekija.annaID();
                                        Pelaaja syottaja = maali.annaSyottaja();
                                        int syottaja_id = syottaja.annaID();
                                        int aika = maali.annaAika();
                                        st.executeUpdate("INSERT INTO maali (id, maalintekija_id, syottaja_id, ottelu_id, aika) VALUES ('" + maali_id + "', '" + maalintekija_id + "', '" + syottaja_id + "', '" + oid + "', '" + aika+"')");
                                     
                                    }
                                    
                                     //tallenetaan ko otteluun liittyvät tuomaritiedot
                                          for(int m=0; m<ottelu.annaRoolit().size(); m++){
                                        TuomarinRooli tuomarinrooli = ottelu.annaRoolit().get(m);
                                        int tuomarinrooli_id = tuomarinrooli.annaID();
                                       
                                        Tuomari tuomari = tuomarinrooli.annaTuomari();
                                        int tuomari_id = tuomari.annaID();
                                        String rooli = tuomarinrooli.annaRooli();
                                        
                                        st.executeUpdate("INSERT INTO tuomarinrooli (id, tuomari_id, rooli, ottelu_id) VALUES ('" + tuomarinrooli_id + "', '" + tuomari_id + "', '" + rooli + "', '" + oid + "')");
                                     
                                    }
                                }
                           }
                           
                  
                           
                        } else if (tiedot instanceof Pelaaja) {

                            Pelaaja pelaaja = (Pelaaja) tiedot;
                            int pelaaja_id = pelaaja.annaJulkinenID();
                            int pid = pelaaja.annaID();
                            String petunimi = pelaaja.annaEtuNimi();
                            String psukunimi = pelaaja.annaSukuNimi();
                            String pelipaikka = pelaaja.annaPelipaikka();
                            int nro = pelaaja.annaPelinumero();
                            int joukkue_id = pelaaja.annaJoukkue().annaID();
                       
                            st.executeUpdate("INSERT INTO pelaaja (id, etunimi, sukunimi, pelipaikka, pelinumero, pelaaja_id, joukkue_id) VALUES('" + pid + "', '" + petunimi + "', '" + psukunimi + "', '" + pelipaikka + "', '" + nro + "', '" + pelaaja_id + "', '" + joukkue_id + "')");

                            //tallennetaan ko pelaajan kokoonpanotiedot
                            
                            for(int p=0; p<pelaaja.annaKokoonpanot().size(); p++){
                                
                                Kokoonpano kokoonpano = pelaaja.annaKokoonpanot().get(p);
                                
                                int kokoonpano_id = kokoonpano.annaID();
                                
                                st.executeUpdate("INSERT INTO pelaajan_kokoonpano (pelaaja_id, kokoonpano_id) VALUES ('" + pid + "', '" + kokoonpano_id + "')");
                                
                                
                            }
                            
                            
                        } else if (tiedot instanceof Toimihenkilo) {

                            Toimihenkilo toimari = (Toimihenkilo) tiedot;
                            String puh = toimari.annaPuh();
                            int toid = toimari.annaID();
                            String tetunimi = toimari.annaEtuNimi();
                            String tsukunimi = toimari.annaSukuNimi();

                            String rooli = toimari.annaRooli();
                            String sposti = toimari.annaSposti();
                            int tjoukkue_id = toimari.annaJoukkue().annaID();
                            int hallinta = toimari.annaHallinta();
                            int hallinta_id = toimari.annaHallintaID();
 
                            st.executeUpdate("INSERT INTO toimari (id, etunimi, sukunimi, rooli, puh, sposti, joukkue_id, hallinta, hallinta_id) VALUES('" + toid + "', '" + tetunimi + "', '" + tsukunimi + "', '" + rooli + "', '" + puh + "', '" + sposti + "', '" + tjoukkue_id + "', '" + hallinta +"', '" + hallinta_id +"')");

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
                            int id = joukkue.annaID();
                            int sarja_id = joukkue.annaSarja().annaID();
                            String nimi = joukkue.toString();

                            st.executeUpdate("UPDATE joukkue SET nimi='" + nimi + "' WHERE id='" + id + "' AND sarja_id='" + sarja_id + "'");

                        } else if (tiedot instanceof Pelaaja) {

                            Pelaaja pelaaja = (Pelaaja) tiedot;
                            int pelaaja_id = pelaaja.annaJulkinenID();
                            int pid = pelaaja.annaID();
                            String petunimi = pelaaja.annaEtuNimi();
                            String psukunimi = pelaaja.annaSukuNimi();
                            String pelipaikka = pelaaja.annaPelipaikka();
                            int nro = pelaaja.annaPelinumero();
                            int joukkue_id = pelaaja.annaJoukkue().annaID();

                            st.executeUpdate("UPDATE pelaaja SET etunimi='" + petunimi + "' WHERE id='" + pid + "' AND joukkue_id='" + joukkue_id + "'");
                            st.executeUpdate("UPDATE pelaaja SET sukunimi='" + psukunimi + "'  WHERE id='" + pid + "' AND joukkue_id='" + joukkue_id + "'");
                            st.executeUpdate("UPDATE pelaaja SET pelipaikka='" + pelipaikka + "'  WHERE id='" + pid + "' AND joukkue_id='" + joukkue_id + "'");
                            st.executeUpdate("UPDATE pelaaja SET pelinumero='" + nro + "'  WHERE id='" + pid + "' AND joukkue_id='" + joukkue_id + "'");

                        } else if (tiedot instanceof Toimihenkilo) {

                            Toimihenkilo toimari = (Toimihenkilo) tiedot;
                            String puh = toimari.annaPuh();
                            int toid = toimari.annaID();
                            String tetunimi = toimari.annaEtuNimi();
                            String tsukunimi = toimari.annaSukuNimi();

                            String rooli = toimari.annaRooli();
                            String sposti = toimari.annaSposti();
                            int tjoukkue_id = toimari.annaJoukkue().annaID();

                            st.executeUpdate("UPDATE toimari SET etunimi='" + tetunimi + "' WHERE id='" + toid + "' AND joukkue_id='" + tjoukkue_id + "'");
                            st.executeUpdate("UPDATE toimari SET sukunimi='" + tsukunimi + "'  WHERE id='" + toid + "' AND joukkue_id='" + tjoukkue_id + "'");
                            st.executeUpdate("UPDATE toimari SET rooli='" + rooli + "'  WHERE id='" + toid + "' AND joukkue_id='" + tjoukkue_id + "'");
                            st.executeUpdate("UPDATE toimari SET sposti='" + sposti + "'  WHERE id='" + toid + "' AND joukkue_id='" + tjoukkue_id + "'");
                            st.executeUpdate("UPDATE toimari SET puh='" + puh + "'  WHERE id='" + toid + "' AND joukkue_id='" + tjoukkue_id + "'");

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
