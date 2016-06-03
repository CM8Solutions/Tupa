package tupa.kontrollerit;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TreeItem;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import tupa.data.Turnaus;
import tupa.data.Kohde;
import tupa.data.Sarja;
import tupa.data.Joukkue;
import tupa.data.Pelaaja;
import tupa.data.Tuomari;
import tupa.data.Toimihenkilo;
import tupa.nakymat.PaaNakyma;
import tupa.nakymat.SarjaNakyma;
import tupa.nakymat.JoukkueNakyma;
import tupa.nakymat.PelaajaNakyma;
import tupa.nakymat.ToimariNakyma;
import tupa.nakymat.TuomariNakyma;
import tupa.data.Yhteys;

/**
 * Luokka, joka hoitaa etusivulla olevan hakutoiminnon.
 * 
 * @author Marianne
 * @see PaaNakyma
 */
public class Haku {

    //alaosan loki
    private VBox hakutulos = new VBox();
    private boolean loyty = false;
    private Connection con = null;
    private Statement st = null;
    private Yhteys yhteys = new Yhteys();
    private int turnaus_id;
    private Turnaus turnaus;
    private String sql = "";
    private PreparedStatement pst;
    private PaaNakyma paanakyma;
    private int ROW_HEIGHT = 60;

    public Haku() {

    }

    public Haku(Turnaus turnaus, PaaNakyma paanakyma) {
        this.turnaus = turnaus;
        this.paanakyma = paanakyma;
    }

    public VBox luoHakuTulos(String hakusana) throws SQLException {
        hakutulos.setSpacing(10);
        turnaus_id = turnaus.annaID();

        try {
            con = yhteys.annaYhteys();
            st = con.createStatement();

            if (!hakusana.trim().isEmpty()) {

                //sarjat
                sql = "SELECT DISTINCT * FROM sarja WHERE (turnaus_id = ?) AND (nimi LIKE ?)";
                pst = con.prepareStatement(sql);
                pst.setInt(1, turnaus_id);
                pst.setString(2, "%" + hakusana + "%");

                ResultSet sarjat = pst.executeQuery();

                ListView sarjatulokset = new ListView();
                List<Kohde> sarjalista = new ArrayList<>();
                Label sarja_otsikko = new Label("Sarjat: ");
                sarja_otsikko.setFont(Font.font("Papyrus", FontWeight.BOLD, 16));

                ObservableList<Kohde> sarjatL;
                boolean sarjaloyty = false;
                while (sarjat.next()) {
                    loyty = true;
                    sarjaloyty = true;
                    int sid = sarjat.getInt("tupaid");

                    for (int i = 0; i < turnaus.annaSarjat().size(); i++) {

                        if (turnaus.annaSarjat().get(i).annaID() == sid) {
                            Kohde sarja = turnaus.annaSarjat().get(i);
                            sarjalista.add(sarja);

                        }

                    }

                }

                sarjatL = FXCollections.observableArrayList(sarjalista);
                sarjatulokset.setItems(sarjatL);
                sarjatulokset.setId("my-list");

                sarjatulokset.setPrefHeight(sarjatL.size() * ROW_HEIGHT + 2);
                sarjatulokset.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {

                    TreeItem<Kohde> mihin = new TreeItem<>((Sarja) newSelection);

                    SarjaNakyma sarjanakyma = paanakyma.annaSarjanakyma();
                    sarjanakyma.luoSarjaSivu(mihin);

                });

                if (sarjaloyty) {
                    hakutulos.getChildren().add(sarja_otsikko);
                    hakutulos.getChildren().add(sarjatulokset);
                }

                //joukkueet
                sql = "SELECT DISTINCT joukkue.tupaid as jid FROM sarja, joukkue WHERE ((sarja.turnaus_id = ? AND joukkue.sarja_id = sarja.tupaid) AND (joukkue.nimi LIKE ?))";
                pst = con.prepareStatement(sql);
                pst.setInt(1, turnaus_id);
                pst.setString(2, "%" + hakusana + "%");

                ResultSet joukkueet = pst.executeQuery();

                ListView joukkuetulokset = new ListView();
                List<Kohde> joukkuelista = new ArrayList<>();
                Label joukkue_otsikko = new Label("Joukkueet: ");
                joukkue_otsikko.setFont(Font.font("Papyrus", FontWeight.BOLD, 16));

                ObservableList<Kohde> joukkueetL;
                boolean joukkueloyty = false;
                while (joukkueet.next()) {
                    loyty = true;
                    joukkueloyty = true;
                    int jid = joukkueet.getInt("jid");
                    for (int i = 0; i < turnaus.annaSarjat().size(); i++) {
                        Sarja sarja = turnaus.annaSarjat().get(i);
                        for (int j = 0; j < sarja.annaJoukkueet().size(); j++) {
                            if (sarja.annaJoukkueet().get(j).annaID() == jid) {
                                Kohde joukkue = sarja.annaJoukkueet().get(j);
                                joukkuelista.add(joukkue);

                            }

                        }

                    }

                }

                joukkueetL = FXCollections.observableArrayList(joukkuelista);
                joukkuetulokset.setItems(joukkueetL);
                joukkuetulokset.setId("my-list");
                joukkuetulokset.setPrefHeight(joukkueetL.size() * ROW_HEIGHT + 2);
                joukkuetulokset.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {

                    JoukkueNakyma joukkuenakyma = paanakyma.annaJoukkuenakyma();
                    joukkuenakyma.luoJoukkueSivu((Joukkue) newSelection);

                });
                if (joukkueloyty) {
                    hakutulos.getChildren().add(joukkue_otsikko);
                    hakutulos.getChildren().add(joukkuetulokset);
                }

                //pelaajat
                sql = "SELECT DISTINCT pelaaja.tupaid as pid FROM pelaaja, sarja, joukkue WHERE ((sarja.turnaus_id = ? AND joukkue.sarja_id = sarja.tupaid AND pelaaja.joukkue_id = joukkue.tupaid) AND (pelaaja.etunimi LIKE ? OR pelaaja.sukunimi LIKE ?))";
                pst = con.prepareStatement(sql);
                pst.setInt(1, turnaus_id);
                pst.setString(2, "%" + hakusana + "%");
                pst.setString(3, "%" + hakusana + "%");

                ResultSet pelaajat = pst.executeQuery();

                ListView pelaajatulokset = new ListView();
                List<Kohde> pelaajalista = new ArrayList<>();
                Label pelaaja_otsikko = new Label("Pelaajat: ");
                pelaaja_otsikko.setFont(Font.font("Papyrus", FontWeight.BOLD, 16));

                ObservableList<Kohde> pelaajatL;
                boolean pelaajaloyty = false;
                while (pelaajat.next()) {
                    loyty = true;
                    pelaajaloyty = true;
                    int pid = pelaajat.getInt("pid");
                    for (int i = 0; i < turnaus.annaSarjat().size(); i++) {
                        Sarja sarja = turnaus.annaSarjat().get(i);
                        for (int j = 0; j < sarja.annaJoukkueet().size(); j++) {
                            Joukkue joukkue = sarja.annaJoukkueet().get(j);

                            for (int k = 0; k < joukkue.annaPelaajat().size(); k++) {
                                if (joukkue.annaPelaajat().get(k).annaID() == pid) {
                                    Kohde pelaaja = joukkue.annaPelaajat().get(k);
                                    pelaajalista.add(pelaaja);

                                }
                            }

                        }

                    }
                }

                pelaajatL = FXCollections.observableArrayList(pelaajalista);
                pelaajatulokset.setItems(pelaajatL);
                pelaajatulokset.setId("my-list");
                pelaajatulokset.setPrefHeight(pelaajatL.size() * ROW_HEIGHT + 2);
                pelaajatulokset.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
                    PelaajaNakyma pelaajanakyma = paanakyma.annaPelaajanakyma();
                    pelaajanakyma.luoPelaajaSivu((Pelaaja) newSelection);

                });

                if (pelaajaloyty) {
                    hakutulos.getChildren().add(pelaaja_otsikko);
                    hakutulos.getChildren().add(pelaajatulokset);
                }

                //toimihenkilöt
                sql = "SELECT DISTINCT toimari.tupaid as toid FROM toimari, sarja, joukkue WHERE ((sarja.turnaus_id = ? AND joukkue.sarja_id = sarja.tupaid AND toimari.joukkue_id = joukkue.tupaid) AND (toimari.etunimi LIKE ? OR toimari.sukunimi LIKE ?))";
                pst = con.prepareStatement(sql);
                pst.setInt(1, turnaus_id);
                pst.setString(2, "%" + hakusana + "%");
                pst.setString(3, "%" + hakusana + "%");

                ResultSet toimarit = pst.executeQuery();

                ListView toimaritulokset = new ListView();
                List<Kohde> toimarilista = new ArrayList<>();
                Label toimari_otsikko = new Label("Toimihenkilöt: ");
                toimari_otsikko.setFont(Font.font("Papyrus", FontWeight.BOLD, 16));

                ObservableList<Kohde> toimaritL;
                boolean toimariloyty = false;
                while (toimarit.next()) {
                    loyty = true;
                    toimariloyty = true;
                    int toid = toimarit.getInt("toid");
                    for (int i = 0; i < turnaus.annaSarjat().size(); i++) {
                        Sarja sarja = turnaus.annaSarjat().get(i);
                        for (int j = 0; j < sarja.annaJoukkueet().size(); j++) {
                            Joukkue joukkue = sarja.annaJoukkueet().get(j);

                            for (int k = 0; k < joukkue.annaToimarit().size(); k++) {
                                if (joukkue.annaToimarit().get(k).annaID() == toid) {
                                    Kohde toimari = joukkue.annaToimarit().get(k);
                                    toimarilista.add(toimari);

                                }
                            }

                        }

                    }

                }

                toimaritL = FXCollections.observableArrayList(toimarilista);
                toimaritulokset.setItems(toimaritL);
                toimaritulokset.setId("my-list");
                toimaritulokset.setPrefHeight(toimaritL.size() * ROW_HEIGHT + 2);
                toimaritulokset.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {

                    ToimariNakyma toimarinakyma = paanakyma.annaToimarinakyma();
                    toimarinakyma.luoToimariSivu((Toimihenkilo) newSelection);

                });

                if (toimariloyty) {
                    hakutulos.getChildren().add(toimari_otsikko);
                    hakutulos.getChildren().add(toimaritulokset);
                }

                //tuomarit
                sql = "SELECT DISTINCT * FROM tuomari WHERE ((turnaus_id = ?) AND (tuomari.etunimi LIKE ? OR tuomari.sukunimi LIKE ?))";

                pst = con.prepareStatement(sql);
                pst.setInt(1, turnaus_id);
                pst.setString(2, "%" + hakusana + "%");
                pst.setString(3, "%" + hakusana + "%");

                ResultSet tuomarit = pst.executeQuery();

                ListView tuomaritulokset = new ListView();
                List<Kohde> tuomarilista = new ArrayList<>();
                Label tuomari_otsikko = new Label("Tuomarit: ");
                tuomari_otsikko.setFont(Font.font("Papyrus", FontWeight.BOLD, 16));

                ObservableList<Kohde> tuomaritL;

                boolean tuomariloyty = false;
                while (tuomarit.next()) {
                    loyty = true;
                    tuomariloyty = true;

                    int tid = tuomarit.getInt("tupaid");

                    for (int i = 0; i < turnaus.annaTuomarit().size(); i++) {
                        if (turnaus.annaTuomarit().get(i).annaID() == tid) {
                            Kohde tuomari = turnaus.annaTuomarit().get(i);
                            tuomarilista.add(tuomari);
                        }

                    }
                }
                tuomaritL = FXCollections.observableArrayList(tuomarilista);
                tuomaritulokset.setItems(tuomaritL);
                tuomaritulokset.setId("my-list");
                tuomaritulokset.setPrefHeight(tuomaritL.size() * ROW_HEIGHT + 2);
                tuomaritulokset.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {

                    TuomariNakyma tuomarinakyma = paanakyma.annaTuomarinakyma();
                    TreeItem<Kohde> mihin = new TreeItem<>((Tuomari) newSelection);
                    tuomarinakyma.luoTuomariSivu(mihin);

                });
                if (tuomariloyty) {
                    hakutulos.getChildren().add(tuomari_otsikko);
                    hakutulos.getChildren().add(tuomaritulokset);
                }

            }

            if (!loyty || hakusana.trim().isEmpty()) {

                Label eitulokset = new Label("Ei tuloksia.");
                eitulokset.setFont(Font.font("Papyrus", FontWeight.BOLD, 16));
                hakutulos.getChildren().add(eitulokset);
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

        return hakutulos;
    }
}
