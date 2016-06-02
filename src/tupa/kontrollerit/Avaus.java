package tupa.kontrollerit;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Vector;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TreeItem;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import tupa.Tupa;
import tupa.data.Henkilo;
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
import tupa.data.Yhteys;

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
    private Statement st12 = null;
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
        DropShadow dropShadow = new DropShadow();
        dropShadow.setOffsetX(5);
        dropShadow.setOffsetY(5);

        Label otsikko = new Label("Avataan turnausta..");
        otsikko.setFont(Font.font("Papyrus", FontWeight.BOLD, 16));
        otsikko.setEffect(dropShadow);

        ProgressBar edistyminen = new ProgressBar();
        edistyminen.setPrefWidth(200);
        edistyminen.setPrefHeight(30);

        VBox palkki = new VBox();
        palkki.setPadding(new Insets(10));
        palkki.setSpacing(10);
        palkki.setStyle("-fx-background-color:  linear-gradient(to bottom, #00ff00, 	#ccffcc)");
        palkki.getChildren().addAll(otsikko, edistyminen);

        Stage tehtavastage = new Stage(StageStyle.UTILITY);
        Scene scene = new Scene(palkki);

        scene.getStylesheets().add("css/tyylit.css");

        tehtavastage.setScene(scene);
        tehtavastage.show();

        Task tehtava = new Task<Void>() {
            @Override
            protected Void call() throws Exception {

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
                    st12 = con.createStatement();

                    turnaus_id = turnaus.annaID();

                    //päivitetään ensin laskurit
                    LaskuriPaivittaja paivittaja = new LaskuriPaivittaja(turnaus, ikkuna);
                    paivittaja.paivitaLaskurit();

                    ikkuna.asetaTurnaus((Kohde) turnaus);

                    //haetaan tuomarit
                    sql = "SELECT * FROM tuomari WHERE turnaus_id='" + turnaus_id + "'";

                    ResultSet tuomarit = st.executeQuery(sql);

                    while (tuomarit.next()) {
                        String etunimi = tuomarit.getString("etunimi");
                        String sukunimi = tuomarit.getString("sukunimi");
                        int id = tuomarit.getInt("tupaid");

                        int julkinen_id = tuomarit.getInt("tuomari_id");
                        int viety = tuomarit.getInt("viety_tiedostoon");

                        Tuomari tuomari = new Tuomari(etunimi, sukunimi);
                        tuomari.asetaID(id);
                        tuomari.asetaTurnaus(turnaus);
                        tuomari.asetaVienti(viety);
                        tuomari.asetaJulkinenId(julkinen_id);

                        sql2 = "SELECT DISTINCT turnaus.tupaid as turid, turnaus.nimi as turnimi FROM tuomari, turnaus WHERE tuomari.turnaus_id = turnaus.tupaid AND tuomari.tupaid='" + id + "'";
                        ResultSet tuomarinturnaukset = st12.executeQuery(sql2);
                        while (tuomarinturnaukset.next()) {
                            int turnaus_id2 = tuomarinturnaukset.getInt("turid");
                            String turnimi = tuomarinturnaukset.getString("turnimi");
                            Turnaus turnaus2 = new Turnaus();
                            turnaus2.asetaNimi(turnimi);

                            tuomari.annaKaikkiTurnaukset().add(turnaus2);
                        }

                        turnaus.annaTuomarit().add(tuomari);

                        kohdetk.add((Kohde) tuomari);
                    }

                    //haetaan sarjat
                    sql = "SELECT * FROM sarja WHERE turnaus_id='" + turnaus_id + "'";

                    ResultSet sarjat = st.executeQuery(sql);

                    while (sarjat.next()) {
                        String snimi = sarjat.getString("nimi");

                        int sid = sarjat.getInt("tupaid");

                        Sarja sarja = new Sarja(snimi, turnaus);
                        sarja.asetaID(sid);

                        turnaus.annaSarjat().add(sarja);

                        kohdetk.add((Kohde) sarja);

                        //haetaan ko sarjan joukkueet
                        sql2 = "SELECT DISTINCT * FROM joukkue WHERE sarja_id='" + sid + "'";

                        ResultSet joukkueet = st2.executeQuery(sql2);

                        while (joukkueet.next()) {
                            String jnimi = joukkueet.getString("nimi");

                            int jid = joukkueet.getInt("tupaid");

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

                                int pid = pelaajat.getInt("tupaid");
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
                                int hallinta = toimarit.getInt("hallinta");
                                int hallinta_id = toimarit.getInt("hallinta_id");
                                int toid = toimarit.getInt("tupaid");

                                Toimihenkilo toimari = new Toimihenkilo(etunimi, sukunimi);
                                toimari.asetaID(toid);
                                toimari.asetaSposti(sposti);
                                toimari.asetaPuh(puh);
                                toimari.asetaRooli(rooli);
                                toimari.asetaHallinta(hallinta);
                                toimari.asetaHallintaID(hallinta_id);
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

                            int id = ottelut.getInt("tupaid");
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
                            DateFormat muoto = new SimpleDateFormat("dd.M.yyyy", Locale.ENGLISH);
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
                            if (tulos == null) {
                                ottelu.asetaTulosTyhja("-");
                            } else {
                                ottelu.asetaTulos(kotimaalit, vierasmaalit);
                            }

                            sarja.annaOttelut().add(ottelu);

                            //otteluun liittyvät kokoonpanot
                            //kotijoukkue
                            sql4 = "SELECT DISTINCT * FROM kokoonpano WHERE ottelu_id='" + id + "' AND joukkue_id = '" + kotijoukkue_id + "'";

                            ResultSet kotikokoonpano = st6.executeQuery(sql4);

                            while (kotikokoonpano.next()) {

                                int kotikokoonpano_id = kotikokoonpano.getInt("tupaid");

                                Kokoonpano kokoonpano = new Kokoonpano(ottelu, kotijoukkue);

                                kokoonpano.asetaID(kotikokoonpano_id);

                                ottelu.asetaKotiKokoonpano(kokoonpano);
                                //pelaajien kokoonpanotiedot
                                sql4 = "SELECT DISTINCT * FROM pelaajan_kokoonpano WHERE kokoonpano_id = '" + kotikokoonpano_id + "'";

                                ResultSet pelaajan_kokoonpanot = st8.executeQuery(sql4);

                                while (pelaajan_kokoonpanot.next()) {

                                    int pelaaja_id = pelaajan_kokoonpanot.getInt("pelaaja_id");

                                    for (int k = 0; k < kotijoukkue.annaPelaajat().size(); k++) {

                                        Pelaaja pelaaja = kotijoukkue.annaPelaajat().get(k);
                                        if (pelaaja.annaID() == pelaaja_id) {
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

                                int vieraskokoonpano_id = vieraskokoonpano.getInt("tupaid");
                                Kokoonpano kokoonpano = new Kokoonpano(ottelu, vierasjoukkue);
                                kokoonpano.asetaID(vieraskokoonpano_id);

                                ottelu.asetaVierasKokoonpano(kokoonpano);
                                //pelaajien kokoonpanotiedot
                                sql4 = "SELECT DISTINCT * FROM pelaajan_kokoonpano WHERE kokoonpano_id = '" + vieraskokoonpano_id + "'";

                                ResultSet pelaajan_kokoonpanot = st9.executeQuery(sql4);

                                while (pelaajan_kokoonpanot.next()) {

                                    int pelaaja_id = pelaajan_kokoonpanot.getInt("pelaaja_id");

                                    for (int k = 0; k < vierasjoukkue.annaPelaajat().size(); k++) {

                                        Pelaaja pelaaja = vierasjoukkue.annaPelaajat().get(k);
                                        if (pelaaja.annaID() == pelaaja_id) {
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

                                int maali_id = maalit.getInt("tupaid");
                                int maalintekija_id = maalit.getInt("maalintekija_id");
                                int syottaja_id = maalit.getInt("syottaja_id");
                                int aika = maalit.getInt("aika");

                                Maali maali = new Maali(ottelu);
                                maali.asetaID(maali_id);
                                maali.asetaAika(aika);
                                //kotijoukkueen pelaajat
                                for (int p = 0; p < ottelu.annaKotijoukkue().annaPelaajat().size(); p++) {
                                    Pelaaja pelaaja = ottelu.annaKotijoukkue().annaPelaajat().get(p);
                                    if (maalintekija_id == pelaaja.annaID()) {
                                        maali.asetaMaalinTekija(pelaaja);
                                    } else if (syottaja_id == pelaaja.annaID()) {
                                        maali.asetaSyottaja(pelaaja);
                                    }
                                }

                                //vierasjoukkueen pelaajat
                                for (int p = 0; p < ottelu.annaVierasjoukkue().annaPelaajat().size(); p++) {
                                    Pelaaja pelaaja = ottelu.annaVierasjoukkue().annaPelaajat().get(p);
                                    if (maalintekija_id == pelaaja.annaID()) {
                                        maali.asetaMaalinTekija(pelaaja);
                                    } else if (syottaja_id == pelaaja.annaID()) {
                                        maali.asetaSyottaja(pelaaja);
                                    }
                                }

                                ottelu.annaMaalit().add(maali);

                            }

                            //otteluun liittyvät tuomaritiedot
                            sql4 = "SELECT DISTINCT * FROM tuomarinrooli WHERE ottelu_id='" + id + "'";

                            ResultSet tuomarinroolit = st11.executeQuery(sql4);

                            while (tuomarinroolit.next()) {

                                int tuomarinrooli_id = tuomarinroolit.getInt("tupaid");
                                int tuomari_id = tuomarinroolit.getInt("tuomari_id");
                                String rooli = tuomarinroolit.getString("rooli");

                                TuomarinRooli tuomarinrooli = new TuomarinRooli(rooli, ottelu);
                                Tuomari tuomari = new Tuomari();
                                for (int t = 0; t < turnaus.annaTuomarit().size(); t++) {
                                    tuomari = turnaus.annaTuomarit().get(t);

                                    if (tuomari.annaID() == tuomari_id) {
                                        tuomarinrooli.asetaTuomari(tuomari);
                                        tuomari.annaTuomarinRoolit().add(tuomarinrooli);
                                        break;
                                    }
                                }

                                tuomarinrooli.asetaID(tuomarinrooli_id);

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
                            TreeItem<Kohde> uusi = new TreeItem<Kohde>(kohdetk.get(i));
                            parent.getChildren().add(uusi);

                        } else if (kohdetk.get(i) instanceof Tuomari) {
                            Tuomari tuomari = (Tuomari) kohdetk.get(i);
                            tuomaritk.add(tuomari);
                            ikkuna.annaTuomaritk().add(tuomari);

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

                    //tuomarit puuhun aakkosjärjestykseen
                    Vector<Henkilo> tuomaritV = new Vector<>();

                    for (int i = 0; i < ikkuna.annaTuomaritk().size(); i++) {
                        tuomaritV.add((Henkilo) ikkuna.annaTuomaritk().get(i));

                    }

                    Collections.sort(tuomaritV);

                    TreeItem<Kohde> parentT = new TreeItem<>();
                    parentT = ikkuna.annaRootTuomarit();

                    for (Henkilo tuomari : tuomaritV) {

                        TreeItem<Kohde> uusiKohde = new TreeItem<Kohde>((Kohde) tuomari);
                        parentT.getChildren().add(uusiKohde);

                    }

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
                return null;
            }
        };
        tehtava.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent t) {
                Tiedottaja tiedottaja = new Tiedottaja(ikkuna);
                tiedottaja.kirjoitaLoki("Turnaus " + turnaus.toString() + " avattu.");
                ikkuna.asetaAloitus(false);
                PaaNakyma nakyma = ikkuna.annaPaaNakyma();
                nakyma.luoEtusivu();
                tehtavastage.hide();
            }
        });
        edistyminen.progressProperty().bind(tehtava.progressProperty());

        tehtavastage.show();
        Thread th = new Thread(tehtava);
        th.start();

    }

}
