package tupa.nakymat;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import tupa.Tupa;
import tupa.kontrollerit.Muuttaja;
import tupa.kontrollerit.Tiedottaja;
import tupa.kontrollerit.Varmistaja;
import tupa.kontrollerit.Taulukko;
import tupa.data.Turnaus;
import tupa.data.Tuomari;
import tupa.data.Kohde;

/**
 *
 * @author Marianne
 */
public class TuomariNakyma {

    private Tupa ikkuna;
    private Muuttaja muuttaja;
    //uusien kohtien lisäystä varten
    private TextField nimi = new TextField();

    private TextField etunimi = new TextField();
    private TextField sukunimi = new TextField();

    private Label pakollinen = new Label("*");
    private Label pakollinen2 = new Label("*");
    private Label pakollinen3 = new Label("*");
    private Label pakollinen4 = new Label("*");

    private Tiedottaja tiedottaja;
    private Varmistaja varmistaja;

    //taulukot
    private TableView ottelut;

    private PaaNakyma nakyma;

    public TuomariNakyma() {

    }

    public TuomariNakyma(Tupa ikkuna, PaaNakyma nakyma) {
        this.ikkuna = ikkuna;
        this.nakyma = nakyma;
        muuttaja = new Muuttaja(ikkuna, nakyma);
        tiedottaja = new Tiedottaja(ikkuna);
        varmistaja = new Varmistaja(ikkuna, nakyma);
        pakollinen.setId("label-pakko");
        pakollinen2.setId("label-pakko");
        pakollinen3.setId("label-pakko");
        pakollinen4.setId("label-pakko");

    }

    public void luoTuomarinLisaysSivu() {
        if (!ikkuna.annaAloitus()) {
            Button lisaysnappula = new Button("Lisää");
            lisaysnappula.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    boolean ok = true;
                    //nimen syöttöjen tarkistus
                    if (etunimi.getText().trim().isEmpty() || sukunimi.getText().trim().isEmpty()) {

                        tiedottaja.annaVirhe("Et voi antaa tyhjää kenttää.");
                        ok = false;
                    } else if (etunimi.getText().length() > 64 || sukunimi.getText().length() > 64) {
                        tiedottaja.annaVirhe("Sekä etu- että sukunimi saa sisältää korkeintaan 64 merkkiä.");
                        ok = false;
                    }
                    for (char c : etunimi.getText().toCharArray()) {

                        if (!Character.isLetter(c)) {
                            if (!Character.toString(c).equals("-")) {
                                ok = false;
                                tiedottaja.annaVirhe("Sekä etu- että sukunimi saa sisältää vain kirjaimia ja tavuviivoja.");
                                break;
                            }

                        }
                    }
                    for (char c : sukunimi.getText().toCharArray()) {
                        if (!Character.isLetter(c)) {
                            if (!Character.toString(c).equals("-")) {
                                ok = false;
                                tiedottaja.annaVirhe("Sekä etu- että sukunimi saa sisältää vain kirjaimia ja tavuviivoja.");
                                break;
                            }
                        }
                    }

                    if (ok) {

                        Kohde uusi = new Tuomari(etunimi.getText(), sukunimi.getText());
                        Turnaus turnaus = (Turnaus) ikkuna.annaTurnaus();

                        turnaus.annaTuomarit().add((Tuomari) uusi);
                        muuttaja.lisaaKohde(uusi);
                        etunimi.clear();
                        sukunimi.clear();
                        ikkuna.asetaMuutos(true);
                        luoTuomarinLisaysSivu();

                    }

                }
            });

            Button peruuta = new Button("Peruuta");

            peruuta.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    nakyma.luoEtusivu();

                }
            });

            VBox vbox = new VBox();
            vbox.setSpacing(20);
            HBox hbox1 = new HBox();
            Label label1 = new Label("Etunimi: ");
            HBox pakollinen_kentta1 = new HBox();

            pakollinen_kentta1.getChildren().addAll(label1, pakollinen);
            hbox1.setSpacing(20);
            hbox1.getChildren().addAll(pakollinen_kentta1, etunimi);

            HBox hbox2 = new HBox();
            Label label2 = new Label("Sukunimi: ");
            HBox pakollinen_kentta2 = new HBox();

            pakollinen_kentta2.getChildren().addAll(label2, pakollinen2);
            hbox2.setSpacing(10);
            hbox2.getChildren().addAll(pakollinen_kentta2, sukunimi);

            HBox hbox3 = new HBox();
            hbox3.setSpacing(10);
            hbox3.getChildren().addAll(lisaysnappula, peruuta);

            vbox.getChildren().addAll(hbox1, hbox2, hbox3);

            GridPane grid = new GridPane();
            grid.setPadding(new Insets(40));
            grid.setAlignment(Pos.TOP_CENTER);
            grid.setVgap(20);

            VBox otsikkoboxi = new VBox();

            Label otsikko = new Label("Lisää uusi tuomari.");
            otsikko.setFont(Font.font("Papyrus", 28));
            Label ohjeT = new Label("(etu- ja sukunimi saa sisältää max. 64 merkkiä, vain kirjaimia ja tavuviivoja.)");
            ohjeT.setFont(Font.font("Papyrus", 12));

            otsikkoboxi.getChildren().addAll(otsikko, ohjeT);

            grid.add(otsikkoboxi, 1, 5);
            grid.add(vbox, 1, 7);

            VBox peitto = new VBox();
            peitto.setStyle("-fx-background-color: white;");
            ikkuna.annaNaytto().getChildren().add(peitto);
            ikkuna.annaNaytto().getChildren().add(grid);
        }

    }

    public void luoTuomariSivu(TreeItem<Kohde> arvo) {

        ScrollPane sb = new ScrollPane();
        Tuomari tuomari = (Tuomari) arvo.getValue();
        ikkuna.asetaValittuTuomari(tuomari);

        GridPane grid = new GridPane();

        //riville 1
        HBox painike = new HBox();
        painike.setSpacing(20);
        Button muokkausnappula = new Button();

        muokkausnappula.setText("\uD83D\uDD89");
        muokkausnappula.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {

                luoTuomariMuokkaus(tuomari);

            }
        });

        Button poistonappula = new Button("X");
        poistonappula.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                varmistaja.annaPoistoVarmistus(arvo.getValue());

            }
        });

        painike.setPadding(new Insets(20, 20, 40, 80));

        painike.getChildren().addAll(muokkausnappula, poistonappula);

        //riville 2
        HBox rivi2 = new HBox();
        rivi2.setAlignment(Pos.CENTER);
        rivi2.setPadding(new Insets(40, 40, 40, 240));
        VBox info = new VBox();

        Label nimi = new Label(tuomari.toString());
        nimi.setFont(Font.font("Papyrus", 28));
        Label id = new Label("TuomariID: \t" + tuomari.annaJulkinenId());
        id.setFont(Font.font("Papyrus", 14));

        Label turnaukset = new Label("Saman käyttäjän hallinnoimat turnaukset, jossa tuomari on mukana:\t " + tuomari.annaTurnausLuetteloKaikki());
        turnaukset.setFont(Font.font("Papyrus", 14));
        info.setAlignment(Pos.CENTER);
        info.getChildren().addAll(nimi, id, turnaukset);

        if (ikkuna.annaTaso() == 3 || ikkuna.annaTaso() == 2) {
            rivi2.getChildren().addAll(info, painike);
        } else {
            rivi2.getChildren().addAll(info);
        }

        grid.add(rivi2, 0, 0);

        Taulukko taulukontekija1 = new Taulukko(nakyma, varmistaja);

        ottelut = taulukontekija1.luoTuomarinOtteluTaulukko(tuomari);
        ottelut.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        //riville 3
        VBox osio1 = new VBox();
        osio1.setSpacing(20);
        osio1.setPadding(new Insets(10, 20, 200, 10));
        osio1.setAlignment(Pos.CENTER);
        Label otsikko1 = new Label("Ottelut:");
        otsikko1.setFont(Font.font("Papyrus", 18));
        osio1.getChildren().addAll(otsikko1, ottelut);
        grid.setPadding(new Insets(20));
        grid.add(osio1, 0, 1);
        sb.setContent(grid);

        VBox peitto = new VBox();
        peitto.setStyle("-fx-background-color: white;");
        ikkuna.annaNaytto().getChildren().add(peitto);
        ikkuna.annaNaytto().getChildren().add(sb);

    }

    public void luoTuomariMuokkaus(Tuomari tuomari) {
        Button muokkausnappula = new Button("OK");
        muokkausnappula.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                boolean ok = true;
                //nimen syöttöjen tarkistus
                if (etunimi.getText().trim().isEmpty() || sukunimi.getText().trim().isEmpty()) {

                    tiedottaja.annaVirhe("Et voi antaa tyhjää kenttää.");
                    ok = false;
                } else if (etunimi.getText().length() > 64 || sukunimi.getText().length() > 64) {
                    tiedottaja.annaVirhe("Sekä etu- että sukunimi saa sisältää korkeintaan 64 merkkiä.");
                    ok = false;
                }
                for (char c : etunimi.getText().toCharArray()) {

                    if (!Character.isLetter(c)) {
                        if (!Character.toString(c).equals("-")) {
                            ok = false;
                            tiedottaja.annaVirhe("Sekä etu- että sukunimi saa sisältää vain kirjaimia ja tavuviivoja.");
                            break;
                        }

                    }
                }
                for (char c : sukunimi.getText().toCharArray()) {
                    if (!Character.isLetter(c)) {
                        if (!Character.toString(c).equals("-")) {
                            ok = false;
                            tiedottaja.annaVirhe("Sekä etu- että sukunimi saa sisältää vain kirjaimia ja tavuviivoja.");
                            break;
                        }
                    }
                }

                if (ok) {

                    //poisto puusta
                    TreeItem<Kohde> parent = new TreeItem<>();
                    parent = ikkuna.annaRootTuomarit();

                    for (int i = 0; i < parent.getChildren().size(); i++) {

                        if (parent.getChildren().get(i).getValue().equals(tuomari)) {
                            parent.getChildren().remove(i);
                        }

                    }

                    tuomari.asetaEtuNimi(etunimi.getText());
                    tuomari.asetaSukuNimi(sukunimi.getText());
                    tuomari.asetaNimi(etunimi.getText() + " " + sukunimi.getText());

                    //muutettu takaisin puuhun
                    TreeItem<Kohde> muutettu = new TreeItem<Kohde>(tuomari);
                    parent.getChildren().add(muutettu);

                    tiedottaja.kirjoitaLoki("Tuomarin nimeä muokattu.");
                    nimi.setText("");
                    ikkuna.asetaMuutos(true);

                    luoTuomariSivu(muutettu);
                }

            }
        });

        Button peruuta = new Button("Peruuta");
        peruuta.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                TreeItem<Kohde> kuka = new TreeItem<>(tuomari);
                luoTuomariSivu(kuka);

            }
        });

        etunimi.setText(tuomari.annaEtuNimi());
        sukunimi.setText(tuomari.annaSukuNimi());

        VBox vbox = new VBox();
        vbox.setSpacing(20);

        VBox otsikkoboxi = new VBox();

        Label otsikko = new Label("Muokkaa tuomarin " + tuomari.toString() + " tietoja: ");
        otsikko.setFont(Font.font("Papyrus", 22));
        Label ohjeT = new Label("(etu- ja sukunimi saa sisältää max. 64 merkkiä, vain kirjaimia ja tavuviivoja.)");
        ohjeT.setFont(Font.font("Papyrus", 12));
        otsikkoboxi.getChildren().addAll(otsikko, ohjeT);

        HBox hbox1 = new HBox();
        Label label1 = new Label("Etunimi: ");
        HBox pakollinen_kentta1 = new HBox();

        pakollinen_kentta1.getChildren().addAll(label1, pakollinen);
        hbox1.setSpacing(20);
        hbox1.getChildren().addAll(pakollinen_kentta1, etunimi);
        HBox hbox2 = new HBox();
        Label label2 = new Label("Sukunimi: ");
        HBox pakollinen_kentta2 = new HBox();

        pakollinen_kentta2.getChildren().addAll(label2, pakollinen2);
        hbox2.setSpacing(10);
        hbox2.getChildren().addAll(pakollinen_kentta2, sukunimi);

        HBox painikkeet = new HBox();
        painikkeet.setSpacing(20);
        painikkeet.getChildren().addAll(muokkausnappula, peruuta);
        vbox.getChildren().addAll(otsikkoboxi, hbox1, hbox2, painikkeet);

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(40));
        grid.setAlignment(Pos.CENTER);

        grid.add(vbox, 1, 7);

        VBox peitto = new VBox();
        peitto.setStyle("-fx-background-color: white;");
        ikkuna.annaNaytto().getChildren().add(peitto);
        ikkuna.annaNaytto().getChildren().add(grid);
    }
}
