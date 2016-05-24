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
import javafx.scene.control.TreeItem;
import tupa.Tupa;
import tupa.data.Ottelu;
import tupa.data.Kokoonpano;
import tupa.data.Maali;
import tupa.data.Kohde;
import tupa.data.Turnaus;
import tupa.data.Sarja;
import tupa.data.Tuomari;
import tupa.data.Pelaaja;
import tupa.data.Toimihenkilo;
import tupa.data.Joukkue;
import tupa.data.TuomarinRooli;
import tupa.nakymat.PaaNakyma;
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
        private Statement st11 = null;
    private Yhteys yhteys = new Yhteys();
    private String sql = "";
    private String sql2 = "";
    private String sql3 = "";
    private String sql4 = "";
    private int turnaus_id;
    private Turnaus turnaus;
    private Tupa ikkuna;
    private int laskuri;

    public Avaus() {

    }
    public Avaus(Turnaus turnaus, Tupa ikkuna) {
        this.turnaus = turnaus;
        this.ikkuna = ikkuna;
    }

    public void avaa() throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException {

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
             st11 = con.createStatement();
            
             turnaus_id = turnaus.annaID();
            //haetaan turnaus
            sql = "SELECT * FROM turnaus WHERE id = '" + turnaus_id +"'";

            ResultSet turnaukset = st.executeQuery(sql);
            
            while (turnaukset.next()) {
             
                String nimi = turnaukset.getString("nimi");
                turnaus_id = turnaukset.getInt("id");

                turnaus = new Turnaus();
                turnaus.asetaNimi(nimi);
                turnaus.asetaID(turnaus_id);
                turnaus.kasvataLaskuria();
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
             tuomari.kasvataLaskuria();
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
sarja.kasvataLaskuria();
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
joukkue.kasvataLaskuria();
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
pelaaja.kasvataLaskuria();
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
                        int hallinta = toimarit.getInt("hallinta");
 int hallinta_id = toimarit.getInt("hallinta_id");
                        int toid = toimarit.getInt("id");

                        Toimihenkilo toimari = new Toimihenkilo(etunimi, sukunimi);
                        toimari.asetaID(toid);
                        toimari.asetaSposti(sposti);
                        toimari.asetaPuh(puh);
                        toimari.asetaRooli(rooli);
                        toimari.asetaHallinta(hallinta);
                        toimari.asetaHallintaID(hallinta_id);
                        toimari.asetaJoukkue(joukkue);
toimari.kasvataLaskuria();
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
ottelu.kasvataLaskuria();
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
kokoonpano.kasvataLaskuria();
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
kokoonpano.kasvataLaskuria();
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
                    
                    
                    //otteluun liittyvät maalit
                   sql4 = "SELECT DISTINCT * FROM maali WHERE ottelu_id='" + id + "'";

                    ResultSet maalit = st10.executeQuery(sql4);
                 
                    while (maalit.next()) {

                        int maali_id = maalit.getInt("id");
                        int maalintekija_id = maalit.getInt("maalintekija_id");
                        int syottaja_id = maalit.getInt("syottaja_id");
                        int aika = maalit.getInt("aika");
                        
                        
                        Maali maali = new Maali(ottelu);
                        maali.asetaID(maali_id);
                        maali.asetaAika(aika);
                        //kotijoukkueen pelaajat
                        for(int p=0; p<ottelu.annaKotijoukkue().annaPelaajat().size(); p++){
                            Pelaaja pelaaja = ottelu.annaKotijoukkue().annaPelaajat().get(p);
                            if(maalintekija_id == pelaaja.annaID()){
                                maali.asetaMaalinTekija(pelaaja);
                            }
                            else if(syottaja_id == pelaaja.annaID()){
                                maali.asetaSyottaja(pelaaja);
                            }
                        }
                        
                          //vierasjoukkueen pelaajat
                        for(int p=0; p<ottelu.annaVierasjoukkue().annaPelaajat().size(); p++){
                            Pelaaja pelaaja = ottelu.annaVierasjoukkue().annaPelaajat().get(p);
                            if(maalintekija_id == pelaaja.annaID()){
                                maali.asetaMaalinTekija(pelaaja);
                            }
                            else if(syottaja_id == pelaaja.annaID()){
                                maali.asetaSyottaja(pelaaja);
                            }
                        }
                        maali.kasvataLaskuria();
                        ottelu.annaMaalit().add(maali);
               
                    }
        
                    
                     //otteluun liittyvät tuomaritiedot
                   sql4 = "SELECT DISTINCT * FROM tuomarinrooli WHERE ottelu_id='" + id + "'";

                    ResultSet tuomarinroolit = st11.executeQuery(sql4);
                 
                    while (tuomarinroolit.next()) {

                        int tuomarinrooli_id = tuomarinroolit.getInt("id");
                       int tuomari_id = tuomarinroolit.getInt("tuomari_id");
                       String rooli = tuomarinroolit.getString("rooli"); 
                   
                       TuomarinRooli tuomarinrooli = new TuomarinRooli();
                       Tuomari tuomari = new Tuomari();
                       for(int t=0; t<turnaus.annaTuomarit().size(); t++){
                           tuomari = turnaus.annaTuomarit().get(t);
                      
                           if(tuomari.annaID() == tuomari_id){
                               tuomarinrooli = new TuomarinRooli(tuomari, ottelu); 
                                 tuomari.annaTuomarinRoolit().add(tuomarinrooli);
                              break;
                           }
                       }
                       
                   tuomarinrooli.kasvataLaskuria();
                       tuomarinrooli.asetaID(tuomarinrooli_id);
                       tuomarinrooli.asetaRooli(rooli);
                        
                        ottelu.annaRoolit().add(tuomarinrooli);
               
                    }
                    

                }

            }
            
                    ikkuna.asetaKohteet(kohdetk);

                    List<Sarja> sarjatk = new ArrayList<>();

                    List<Tuomari> tuomaritk = new ArrayList<>();
                    List<Joukkue> joukkuetk = new ArrayList<>();
                    List<Pelaaja> pelaajatk = new ArrayList<>();
                    List<Toimihenkilo> toimaritk = new ArrayList<>();
                    //viedään kohteet omiin listoihin
                    TreeItem<Kohde> parent = new TreeItem<>();
                    for (int i = 0; i < kohdetk.size(); i++) {

                        if (kohdetk.get(i) instanceof Sarja) {
                            Sarja sarja = (Sarja) kohdetk.get(i);

                            sarjatk.add(sarja);
                            ikkuna.annaSarjatk().add(sarja);

                            parent = ikkuna.annaRootSarjat();
                            TreeItem<Kohde> newItem = new TreeItem<Kohde>(kohdetk.get(i));
                            parent.getChildren().add(newItem);

                        } else if (kohdetk.get(i) instanceof Tuomari) {
                            Tuomari tuomari = (Tuomari) kohdetk.get(i);
                            tuomaritk.add(tuomari);
                            ikkuna.annaTuomaritk().add(tuomari);

                            parent = ikkuna.annaRootTuomarit();
                            TreeItem<Kohde> uusiKohde = new TreeItem<Kohde>(kohdetk.get(i));
                            parent.getChildren().add(uusiKohde);
                        } else if (kohdetk.get(i) instanceof Joukkue) {
                            Joukkue joukkue = (Joukkue) kohdetk.get(i);
                            joukkuetk.add(joukkue);
                            ikkuna.annaJoukkuetk().add(joukkue);

                            joukkue.asetaTaulukkonimi();
                        } else if (kohdetk.get(i) instanceof Pelaaja) {
                            Pelaaja pelaaja = (Pelaaja) kohdetk.get(i);
                            pelaajatk.add(pelaaja);
                            ikkuna.annaPelaajatk().add(pelaaja);

                        } else if (kohdetk.get(i) instanceof Toimihenkilo) {
                            Toimihenkilo toimari = (Toimihenkilo) kohdetk.get(i);
                            toimaritk.add(toimari);
                            ikkuna.annaToimaritk().add(toimari);

                            toimari.asetaTaulukkonimi();
                            toimari.asetaTaulukkosposti();
                            toimari.asetaTaulukkopuh();
                            toimari.asetaTaulukkorooli();
                        } else if (kohdetk.get(i) instanceof Turnaus) {

                            ikkuna.asetaTurnaus(kohdetk.get(i));

                        }
                    }
                
                    Tiedottaja tiedottaja = new Tiedottaja(ikkuna);
                tiedottaja.kirjoitaLoki("Turnaus "+turnaus.toString()+" avattu.");
                
                PaaNakyma nakyma = ikkuna.annaPaaNakyma();
                nakyma.luoEtusivu(); 
        
        }
                
          
     
        catch (SQLException se) {

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

    }

}
