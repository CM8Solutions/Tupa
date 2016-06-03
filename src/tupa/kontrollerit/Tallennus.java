package tupa.kontrollerit;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.List;
import javafx.application.Platform;
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
import javafx.stage.WindowEvent;
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
import tupa.nakymat.PaaNakyma;
import tupa.data.Yhteys;

/**
 * Luokka, joka hoitaa tarvittavien tietojen tallentamisen tietokantaan.
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

    public void suoritaTallennus(boolean jatko, boolean avaus, boolean uusi) throws InstantiationException, SQLException, IllegalAccessException {

        DropShadow dropShadow = new DropShadow();
        dropShadow.setOffsetX(5);
        dropShadow.setOffsetY(5);

        Label otsikko = new Label("Tallennetaan turnausta..");
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
        tehtavastage.setOnCloseRequest(new EventHandler<WindowEvent>() {

            public void handle(WindowEvent we) {

                we.consume();

            }
        });

        tehtavastage.show();

        Task tehtava = new Task<Void>() {
            @Override
            protected Void call() throws Exception {

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

                    Turnaus turnaus = (Turnaus) ikkuna.annaTurnaus();
                    turnaus_id = turnaus.annaID();
                    String luomispvm = turnaus.annaLuomispvm();
                    String turnaus_nimi = turnaus.toString();

                    ResultSet turnaukset = st.executeQuery("SELECT * FROM  turnaus");
                    boolean loyty = false;
                    while (turnaukset.next()) {

                        int tid = turnaukset.getInt("tupaid");

                        if (tid == turnaus_id) {
                            loyty = true;
                            break;
                        }

                    }

                    //ei ollut kannassa ennestään
                    if (!loyty) {

                        st.executeUpdate("INSERT INTO turnaus (tupaid, nimi, luomispvm) VALUES('" + turnaus_id + "', '" + turnaus_nimi + "', '" + luomispvm + "')");

                    } //oli jo kannassa, jolloin kaikki siihen liittyvät tiedot tyhjennetään ennen tallentamista
                    else {
                        st.executeUpdate("UPDATE turnaus SET nimi='" + turnaus_nimi + "' WHERE tupaid='" + turnaus_id + "'");

                        if (poisto) {

                            //tyhjennetään pelaajat
                            sql = "SELECT DISTINCT pelaaja.tupaid as pid FROM sarja, joukkue, pelaaja WHERE sarja.turnaus_id='" + turnaus_id + "' AND joukkue.sarja_id = sarja.tupaid AND pelaaja.joukkue_id = joukkue.tupaid";
                            ResultSet pelaajat = st2.executeQuery(sql);

                            while (pelaajat.next()) {

                                int id = pelaajat.getInt("pid");
                                sql = "DELETE FROM pelaaja WHERE tupaid='" + id + "'";
                                st.executeUpdate(sql);
                                sql = "DELETE FROM pelaajan_kokoonpano WHERE pelaaja_id='" + id + "'";
                                st.executeUpdate(sql);
                            }

                            //tyhjennetään toimarit
                            sql = "SELECT DISTINCT toimari.tupaid as tid FROM sarja, joukkue, toimari WHERE sarja.turnaus_id='" + turnaus_id + "' AND joukkue.sarja_id = sarja.tupaid AND toimari.joukkue_id = joukkue.tupaid";
                            ResultSet toimarit = st3.executeQuery(sql);

                            while (toimarit.next()) {

                                int id = toimarit.getInt("tid");
                                sql = "DELETE FROM toimari WHERE tupaid='" + id + "'";
                                st.executeUpdate(sql);
                            }

                            //tyhjennetään ottelut
                            sql = "SELECT DISTINCT ottelu.tupaid as oid FROM sarja, joukkue, ottelu WHERE sarja.turnaus_id='" + turnaus_id + "' AND joukkue.sarja_id = sarja.tupaid AND (ottelu.kotijoukkue_id = joukkue.tupaid OR ottelu.vierasjoukkue_id = joukkue.tupaid)";
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

                                sql = "DELETE FROM ottelu WHERE tupaid='" + id + "'";
                                st.executeUpdate(sql);
                            }

                            //tyhjennetään joukkueet
                            sql = "SELECT DISTINCT joukkue.tupaid as jid FROM sarja, joukkue WHERE sarja.turnaus_id='" + turnaus_id + "' AND joukkue.sarja_id = sarja.tupaid";
                            ResultSet joukkueet = st5.executeQuery(sql);

                            while (joukkueet.next()) {

                                int id = joukkueet.getInt("jid");
                                sql = "DELETE FROM joukkue WHERE tupaid='" + id + "'";
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

                                st.executeUpdate("INSERT INTO sarja (tupaid, nimi, turnaus_id) VALUES('" + sid + "', '" + snimi + "', '" + turnaus_id + "')");

                            } else if (tiedot instanceof Tuomari) {

                                Tuomari tuomari = (Tuomari) tiedot;
                                int tuomari_id = tuomari.annaJulkinenId();
                                int tid = tuomari.annaID();
                                String etunimi = tuomari.annaEtuNimi();
                                String sukunimi = tuomari.annaSukuNimi();
                                int viety_tiedostoon = tuomari.annaVienti();

                                st.executeUpdate("INSERT INTO tuomari (tupaid, etunimi, sukunimi, tuomari_id, turnaus_id, viety_tiedostoon) VALUES('" + tid + "', '" + etunimi + "', '" + sukunimi + "', '" + tuomari_id + "', '" + turnaus_id + "', '" + viety_tiedostoon + "')");

                            } else if (tiedot instanceof Joukkue) {

                                Joukkue joukkue = (Joukkue) tiedot;
                                int jid = joukkue.annaID();
                                int sarja_id = joukkue.annaSarja().annaID();
                                String jnimi = joukkue.toString();

                                st.executeUpdate("INSERT INTO joukkue (tupaid, nimi, sarja_id) VALUES('" + jid + "', '" + jnimi + "', '" + sarja_id + "')");

                                //kyseisen joukkueen ottelut
                                for (int j = 0; j < joukkue.annaOttelut().size(); j++) {
                                    Ottelu ottelu = joukkue.annaOttelut().get(j);
                                    int oid = ottelu.annaId();
                                    //tarkistetaan ettei oo jo laitettu
                                    int ottelulaskuri = 0;
                                    sql = "SELECT DISTINCT * FROM ottelu WHERE sarja_id = '" + sarja_id + "' AND tupaid = '" + oid + "'";
                                    ResultSet ottelut = st5.executeQuery(sql);

                                    while (ottelut.next()) {
                                        ottelulaskuri++;

                                    }

                                    if (ottelulaskuri == 0) {

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

                                        if (tulos.equals("-")) {
                                            st.executeUpdate("INSERT INTO ottelu (tupaid, nimi, sarja_id, kotijoukkue_id, vierasjoukkue_id, kotimaalit, vierasmaalit, paikka, kello, paiva, kellotunnit, kellominuutit, ottelu_id, kierros) VALUES('"
                                                    + oid + "', '" + nimi + "', '" + sarja_id + "', '" + kotijoukkue_id + "', '" + vierasjoukkue_id + "', '" + kotimaalit + "', '" + vierasmaalit + "', '" + paikka + "', '" + kello + "', '" + paiva + "', '" + kellotunnit + "', '" + kellominuutit + "', '" + ottelu_id + "', '" + kierros + "')");

                                        } else {
                                            st.executeUpdate("INSERT INTO ottelu (tupaid, nimi, sarja_id, kotijoukkue_id, vierasjoukkue_id, kotimaalit, vierasmaalit, tulos, paikka, kello, paiva, kellotunnit, kellominuutit, ottelu_id, kierros) VALUES('"
                                                    + oid + "', '" + nimi + "', '" + sarja_id + "', '" + kotijoukkue_id + "', '" + vierasjoukkue_id + "', '" + kotimaalit + "', '" + vierasmaalit + "', '" + tulos + "', '" + paikka + "', '" + kello + "', '" + paiva + "', '" + kellotunnit + "', '" + kellominuutit + "', '" + ottelu_id + "', '" + kierros + "')");

                                        }

                                        //tallennetaan ko ottelun koti- ja vieraskokoonpanot
                                        st.executeUpdate("INSERT INTO kokoonpano (tupaid, ottelu_id, joukkue_id) VALUES ('" + kotikokoonpano_id + "', '" + oid + "', '" + kotijoukkue_id + "')");
                                        st.executeUpdate("INSERT INTO kokoonpano (tupaid, ottelu_id, joukkue_id) VALUES ('" + vieraskokoonpano_id + "', '" + oid + "', '" + vierasjoukkue_id + "')");

                                        //tallenetaan ko otteluun liittyvät maalitiedot
                                        for (int m = 0; m < ottelu.annaMaalit().size(); m++) {
                                            Maali maali = ottelu.annaMaalit().get(m);
                                            int maali_id = maali.annaID();
                                            Pelaaja maalintekija = maali.annaMaalinTekija();
                                            int maalintekija_id = maalintekija.annaID();
                                            Pelaaja syottaja = maali.annaSyottaja();
                                            int syottaja_id = syottaja.annaID();
                                            int aika = maali.annaAika();
                                            st.executeUpdate("INSERT INTO maali (tupaid, maalintekija_id, syottaja_id, ottelu_id, aika) VALUES ('" + maali_id + "', '" + maalintekija_id + "', '" + syottaja_id + "', '" + oid + "', '" + aika + "')");

                                        }

                                        //tallenetaan ko otteluun liittyvät tuomaritiedot
                                        for (int m = 0; m < ottelu.annaRoolit().size(); m++) {
                                            TuomarinRooli tuomarinrooli = ottelu.annaRoolit().get(m);
                                            int tuomarinrooli_id = tuomarinrooli.annaID();

                                            Tuomari tuomari = tuomarinrooli.annaTuomari();
                                            int tuomari_id = tuomari.annaID();
                                            String rooli = tuomarinrooli.annaRooli();

                                            st.executeUpdate("INSERT INTO tuomarinrooli (tupaid, tuomari_id, rooli, ottelu_id) VALUES ('" + tuomarinrooli_id + "', '" + tuomari_id + "', '" + rooli + "', '" + oid + "')");

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

                                st.executeUpdate("INSERT INTO pelaaja (tupaid, etunimi, sukunimi, pelipaikka, pelinumero, pelaaja_id, joukkue_id) VALUES('" + pid + "', '" + petunimi + "', '" + psukunimi + "', '" + pelipaikka + "', '" + nro + "', '" + pelaaja_id + "', '" + joukkue_id + "')");

                                //tallennetaan ko pelaajan kokoonpanotiedot
                                for (int p = 0; p < pelaaja.annaKokoonpanot().size(); p++) {

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

                                st.executeUpdate("INSERT INTO toimari (tupaid, etunimi, sukunimi, rooli, puh, sposti, joukkue_id, hallinta, hallinta_id) VALUES('" + toid + "', '" + tetunimi + "', '" + tsukunimi + "', '" + rooli + "', '" + puh + "', '" + sposti + "', '" + tjoukkue_id + "', '" + hallinta + "', '" + hallinta_id + "')");

                            }

                        }

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

                if (avaus) {

                    ikkuna.annaKohteet().clear();
                    ikkuna.annaTuomaritk().clear();
                    ikkuna.annaSarjatk().clear();

                    Turnaus turnaus = new Turnaus();
                    turnaus.asetaNimi("Uusi turnaus");
                    turnaus.kasvataLaskuria();
                    turnaus.asetaID(turnaus.annaLaskuri());
                    turnaus.asetaLuomispvm(LocalDate.now().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)));
                    Kohde uusiTurnaus = (Kohde) turnaus;
                    ikkuna.asetaTurnaus(uusiTurnaus);
                    ikkuna.annaKohteet().add(uusiTurnaus);

                    //vielä pitää tyhjentää puu
                    TreeItem<Kohde> parentSarjat = ikkuna.annaRootSarjat();
                    TreeItem<Kohde> parentTuomarit = ikkuna.annaRootTuomarit();
                    parentSarjat.getChildren().clear();
                    parentTuomarit.getChildren().clear();

                    PaaNakyma nakyma = ikkuna.annaPaaNakyma();
                    nakyma.luoEtusivuTyhja();
                    //sitten vasta avaukseen
                    TurnausValitsin valitsija = new TurnausValitsin(ikkuna);
                    try {
                        valitsija.annaTurnausLuettelo();
                    } catch (SQLException ex) {
                        Tiedottaja tiedottaja = new Tiedottaja();
                        tiedottaja.annaVirhe("" + ex);
                    }
                    ikkuna.asetaAloitus(false);
                } else if (uusi) {
                    ikkuna.annaKohteet().clear();
                    ikkuna.annaTuomaritk().clear();
                    ikkuna.annaSarjatk().clear();

                    Turnaus turnaus = new Turnaus();
                    turnaus.asetaNimi("Uusi turnaus");
                    turnaus.kasvataLaskuria();
                    turnaus.asetaID(turnaus.annaLaskuri());
                    turnaus.asetaLuomispvm(LocalDate.now().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)));
                    Kohde uusiTurnaus = (Kohde) turnaus;
                    ikkuna.asetaTurnaus(uusiTurnaus);
                    ikkuna.annaKohteet().add(uusiTurnaus);

                    //vielä pitää tyhjentää puu
                    TreeItem<Kohde> parentSarjat = ikkuna.annaRootSarjat();
                    TreeItem<Kohde> parentTuomarit = ikkuna.annaRootTuomarit();
                    parentSarjat.getChildren().clear();
                    parentTuomarit.getChildren().clear();

                    Turnaus turnausu = (Turnaus) ikkuna.annaTurnaus();
                    LaskuriPaivittaja paivittaja = new LaskuriPaivittaja(turnausu, ikkuna);
                    paivittaja.paivitaLaskurit();
                    PaaNakyma nakyma = ikkuna.annaPaaNakyma();
                    nakyma.luoEtusivu();
                    Tiedottaja tiedottaja = new Tiedottaja(ikkuna);
                    tiedottaja.kirjoitaLoki("Uusi turnaus avattu.");
                    ikkuna.asetaAloitus(false);
                }

                //päivitetään tilanne, että tallennus on suoritettu
                ikkuna.asetaMuutos(false);
                if (!jatko && !avaus && !uusi) {
                    Platform.exit();
                }

                tehtavastage.hide();
            }
        });
        edistyminen.progressProperty().bind(tehtava.progressProperty());

        tehtavastage.show();
        try {
            Thread th = new Thread(tehtava);
            th.start();
        } catch (IllegalStateException e) {
            Tiedottaja tiedottaja = new Tiedottaja();
            tiedottaja.annaIlmoitus("" + e);
        }

    }

}
