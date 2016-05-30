/*
Luokka, joka muodostaa päänäkymät
 */
package tupa.nakymat;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import tupa.Tupa;
import tupa.kontrollerit.Muuttaja;
import tupa.kontrollerit.Tiedottaja;
import tupa.kontrollerit.Varmistaja;
import tupa.kontrollerit.Tallennus;
import tupa.kontrollerit.Haku;
import tupa.data.Turnaus;
import tupa.data.Sarja;
import tupa.data.Kohde;
import tupa.data.Tuomari;

/**
 *
 * @author Marianne
 */
public class PaaNakyma {

    private Tupa ikkuna;
    private Muuttaja muuttaja;
    //uusien kohtien lisäystä varten
    private TextField nimi = new TextField();

    private Label pakollinen = new Label("*");
    private Label pakollinen2 = new Label("*");
    private Label pakollinen3 = new Label("*");
    private Label pakollinen4 = new Label("*");

    private Tiedottaja tiedottaja;
    private Varmistaja varmistaja;

    //muut näkymät
    private SarjaNakyma sarjanakyma;
    private OtteluNakyma ottelunakyma;
    private JoukkueNakyma joukkuenakyma;
    private PelaajaNakyma pelaajanakyma;
    private TuomariNakyma tuomarinakyma;
    private ToimariNakyma toimarinakyma;
    private VBox tulos = new VBox();

    public PaaNakyma() {

    }

    public PaaNakyma(Tupa ikkuna) {
        this.ikkuna = ikkuna;
        muuttaja = new Muuttaja(ikkuna, this);
        tiedottaja = new Tiedottaja(ikkuna);
        varmistaja = new Varmistaja(ikkuna, this);
        pakollinen.setId("label-pakko");
        pakollinen2.setId("label-pakko");
        pakollinen3.setId("label-pakko");
        pakollinen4.setId("label-pakko");
        sarjanakyma = new SarjaNakyma(ikkuna, this);
        ottelunakyma = new OtteluNakyma(ikkuna, this);
        joukkuenakyma = new JoukkueNakyma(ikkuna, this);
        pelaajanakyma = new PelaajaNakyma(ikkuna, this);
        tuomarinakyma = new TuomariNakyma(ikkuna, this);
        toimarinakyma = new ToimariNakyma(ikkuna, this);

    }

    public Tupa annaIkkuna() {
        return ikkuna;
    }

    public SarjaNakyma annaSarjanakyma() {
        return sarjanakyma;
    }

    public JoukkueNakyma annaJoukkuenakyma() {
        return joukkuenakyma;
    }

    public TuomariNakyma annaTuomarinakyma() {
        return tuomarinakyma;
    }

    public OtteluNakyma annaOttelunakyma() {
        return ottelunakyma;
    }

    public PelaajaNakyma annaPelaajanakyma() {
        return pelaajanakyma;
    }

    public ToimariNakyma annaToimarinakyma() {
        return toimarinakyma;
    }

    public void luoOhje(String uusiohje, TreeItem<Kohde> arvo) {
        ikkuna.asetaValittuTuomari(null);
        HBox ohjepalkki = new HBox();

        ohjepalkki.setPadding(new Insets(10, 30, 10, 30));
        Text ohje = new Text(uusiohje);
        ohje.setFont(Font.font("Papyrus", FontWeight.BOLD, 20));

        ohjepalkki.getChildren().add(ohje);

        VBox peitto = new VBox();
        peitto.setStyle("-fx-background-color: white;");
        ikkuna.annaNaytto().getChildren().add(peitto);

        GridPane grid = new GridPane();

        grid.setHgap(40);

        grid.add(ohjepalkki, 0, 3);

        Button uusi = new Button();

        if (arvo.getValue() instanceof Sarja) {
            uusi.setText("Lisää uusi sarja");

        } else if (arvo.getValue() instanceof Tuomari) {
            uusi.setText("Lisää uusi tuomari");

        }
        uusi.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {

                if (arvo.getValue() instanceof Sarja) {
                    sarjanakyma.luoSarjanLisaysSivu();
                } else if (arvo.getValue() instanceof Tuomari) {
                    tuomarinakyma.luoTuomarinLisaysSivu();
                }

            }
        });
        grid.setAlignment(Pos.CENTER);
        if (ikkuna.annaTaso() == 3 || ikkuna.annaTaso() == 2) {
            grid.add(uusi, 1, 1);
        }

        ikkuna.annaNaytto().getChildren().add(grid);
    }

    public void luoEtusivu() {
        ikkuna.asetaValittuTuomari(null);
        HBox nimipalkki = new HBox();

        nimipalkki.setPadding(new Insets(20));
        Label nimi = new Label(ikkuna.annaTurnaus().toString());
        nimi.setFont(Font.font("Papyrus", FontWeight.BOLD, 36));
        nimipalkki.setAlignment(Pos.CENTER);
        nimipalkki.getChildren().addAll(nimi);

        VBox peitto = new VBox();
        peitto.setStyle("-fx-background-color: white;");
        ikkuna.annaNaytto().getChildren().add(peitto);

        Button muokkausnappula = new Button();

        muokkausnappula.setText("\uD83D\uDD89");
        muokkausnappula.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {

                VBox peitto = new VBox();
                peitto.setStyle("-fx-background-color: white;");
                ikkuna.annaNaytto().getChildren().add(peitto);

                ikkuna.annaNaytto().getChildren().add(luoTurnauksenMuokkaus());

            }
        });

        VBox rivi1 = new VBox();
        rivi1.setPadding(new Insets(20));
        rivi1.getChildren().addAll(muokkausnappula);

        VBox hakupalkki = new VBox();
        hakupalkki.setAlignment(Pos.CENTER);

        hakupalkki.setSpacing(20);
        Label otsikko = new Label("Hae sarjaa/joukkuetta/pelaajaa/toimihenkilöä/tuomaria: ");
        otsikko.setFont(Font.font("Papyrus", FontWeight.BOLD, 18));
        //hakutoiminto HBox
        HBox hakuboxi = new HBox();
        Label hakuteksti = new Label("Hae: ");
        hakuteksti.setId("label-haku");
        hakuboxi.setAlignment(Pos.CENTER);
        TextField hakukentta = new TextField();

        //nappula
        ImageView hakukuva = new ImageView();
        Image hkuva = new Image("kuvat/haku.png");
        hakukuva.setImage(hkuva);
        hakukuva.setFitHeight(20);
        hakukuva.setFitWidth(20);

        Button hakunappula = new Button();
        hakunappula.setGraphic(hakukuva);
        hakunappula.setId("button-haku");

        hakuboxi.setPadding(new Insets(10));
        hakuboxi.setSpacing(10);
        hakuboxi.getChildren().addAll(hakuteksti, hakukentta, hakunappula);

        StackPane tulospalkki = new StackPane();
        tulospalkki.setPadding(new Insets(10));
        Label hotsikko = new Label();
        tulospalkki.setAlignment(Pos.CENTER);
        Haku haku = new Haku((Turnaus) ikkuna.annaTurnaus(), this);
        hakunappula.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    luoHakutulossivu(hakukentta.getText());
                } catch (SQLException ex) {
                    Logger.getLogger(PaaNakyma.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        });
        tulospalkki.setAlignment(Pos.CENTER);
        ScrollPane sb = new ScrollPane();
        sb.setStyle("-fx-background: #fff;");

        hakupalkki.getChildren().addAll(otsikko, hakuboxi, hotsikko, tulospalkki);

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(0, 0, 0, 300));

        if (ikkuna.annaTaso() == 3 || ikkuna.annaTaso() == 2) {
            grid.add(muokkausnappula, 2, 1);
        }
        grid.add(nimipalkki, 1, 1);

        grid.add(hakupalkki, 1, 2);
        grid.setAlignment(Pos.CENTER);
        grid.setVgap(40);

        sb.setContent(grid);

        ikkuna.annaNaytto().getChildren().add(sb);

    }

    public void luoEtusivuTyhja() {

        VBox peitto = new VBox();
        peitto.setStyle("-fx-background-color: white;");
        ikkuna.annaNaytto().getChildren().add(peitto);

        VBox palkki = new VBox();
        palkki.setAlignment(Pos.CENTER);

        palkki.setSpacing(20);
        Label otsikko = new Label("Avaa yläpalkin valikosta haluamasi toiminto.");
        otsikko.setFont(Font.font("Papyrus", FontWeight.BOLD, 18));

        palkki.getChildren().addAll(otsikko);

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(100, 0, 0, 100));

        grid.add(palkki, 1, 2);
        grid.setAlignment(Pos.CENTER);

        ikkuna.annaNaytto().getChildren().add(grid);

    }

    public void luoHakutulossivu(String hakusana) throws SQLException {

        HBox nimipalkki = new HBox();
        nimipalkki.setPadding(new Insets(20));
        nimipalkki.setPadding(new Insets(0, 20, 20, 400));
        Label nimi = new Label(ikkuna.annaTurnaus().toString());
        nimi.setFont(Font.font("Papyrus", FontWeight.BOLD, 36));
        nimipalkki.setAlignment(Pos.CENTER);
        nimipalkki.getChildren().addAll(nimi);

        VBox painike = new VBox();
        painike.setPadding(new Insets(20));

        Button paluunappula = new Button("<< Palaa takaisin");

        paluunappula.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                luoEtusivu();
            }
        });
        painike.getChildren().add(paluunappula);

        VBox peitto = new VBox();
        peitto.setStyle("-fx-background-color: white;");
        ikkuna.annaNaytto().getChildren().add(peitto);

        VBox hakupalkki = new VBox();
        hakupalkki.setAlignment(Pos.CENTER);
        hakupalkki.setPadding(new Insets(0, 20, 40, 400));
        hakupalkki.setSpacing(20);
        Label otsikko = new Label("Hakutulokset: ");
        otsikko.setFont(Font.font("Papyrus", FontWeight.BOLD, 18));

        Haku haku = new Haku((Turnaus) ikkuna.annaTurnaus(), this);

        if (ikkuna.muutettu()) {
            Tallennus tallennus = new Tallennus(ikkuna);

            try {
                tallennus.suoritaTallennus(true, false, false);
            } catch (InstantiationException ex) {
                Logger.getLogger(PaaNakyma.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SQLException ex) {
                Logger.getLogger(PaaNakyma.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalAccessException ex) {
                Logger.getLogger(PaaNakyma.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        tulos = haku.luoHakuTulos(hakusana);

        hakupalkki.getChildren().add(otsikko);
        hakupalkki.getChildren().add(tulos);

        hakupalkki.setAlignment(Pos.CENTER);
        ScrollPane sb = new ScrollPane();
        sb.setStyle("-fx-background: #fff;");

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20));
        grid.add(painike, 1, 0);
        grid.add(nimipalkki, 1, 1);

        grid.add(hakupalkki, 1, 2);
        grid.setAlignment(Pos.CENTER);

        sb.setContent(grid);

        ikkuna.annaNaytto().getChildren().add(sb);

    }

    public GridPane luoTurnauksenMuokkaus() {
        Button muokkausnappula = new Button("OK");
        muokkausnappula.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (nimi.getText().trim().isEmpty()) {

                    tiedottaja.annaVirhe("Et voi antaa tyhjää kenttää.");
                } else {

                    ikkuna.annaTurnaus().asetaNimi(nimi.getText());

                    tiedottaja.kirjoitaLoki("Turnauksen nimeä muokattu.");
                    nimi.clear();
                    ikkuna.asetaMuutos(true);
                    luoEtusivu();
                }

            }
        });

        Button peruuta = new Button("Peruuta");
        peruuta.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                luoEtusivu();

            }
        });

        nimi.setText(ikkuna.annaTurnaus().toString());

        HBox hbox1 = new HBox();
        Label label1 = new Label("Turnauksen nimi: ");
        HBox pakollinen_kentta1 = new HBox();

        pakollinen_kentta1.getChildren().addAll(label1, pakollinen);

        hbox1.setSpacing(10);
        hbox1.getChildren().addAll(pakollinen_kentta1, nimi, muokkausnappula, peruuta);

        HBox hbox2 = new HBox();
        hbox2.setSpacing(10);
        hbox2.getChildren().addAll(muokkausnappula, peruuta);
        VBox lisays = new VBox();
        lisays.setSpacing(20);
        lisays.getChildren().addAll(hbox1, hbox2);

        GridPane grid = new GridPane();

        grid.setAlignment(Pos.CENTER);

        Label otsikko = new Label("Muokkaa turnauksen nimeä");
        otsikko.setFont(Font.font("Papyrus", 28));
        grid.add(otsikko, 0, 0);
        grid.add(lisays, 0, 1);
        grid.setVgap(40);

        return grid;
    }

}
