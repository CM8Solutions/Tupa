package tupa.kontrollerit;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import tupa.data.Ottelu;
import tupa.data.Kohde;
import tupa.data.Turnaus;
import tupa.data.Sarja;
import tupa.data.Tuomari;
import tupa.data.Pelaaja;
import tupa.data.Toimihenkilo;
import tupa.data.Joukkue;
import tupa.Tupa;
import tupa.nakymat.PaaNakyma;
import tupa.nakymat.SarjaNakyma;
import tupa.nakymat.JoukkueNakyma;
import tupa.nakymat.PelaajaNakyma;
import tupa.nakymat.ToimariNakyma;
import tupa.nakymat.TuomariNakyma;

/**
 *
 * @author Marianne
 */
public class Varmistaja {

    private List<Kohde> kohdetk = new ArrayList<>();
    private Tupa ikkuna;
    private Muuttaja muuttaja;
    private PaaNakyma nakyma;
    private SarjaNakyma sarjanakyma;
    private JoukkueNakyma joukkuenakyma;
    private PelaajaNakyma pelaajanakyma;
    private ToimariNakyma toimarinakyma;
    private TuomariNakyma tuomarinakyma;

    public Varmistaja() {

    }

    public Varmistaja(List<Kohde> kohteet, Tupa ikkuna) {
        kohdetk = kohteet;
        this.ikkuna = ikkuna;
        sarjanakyma = ikkuna.annaPaaNakyma().annaSarjanakyma();
        joukkuenakyma = ikkuna.annaPaaNakyma().annaJoukkuenakyma();
    }

    public Varmistaja(Tupa ikkuna, PaaNakyma nakyma) {
        muuttaja = new Muuttaja(ikkuna, nakyma);
        this.ikkuna = ikkuna;
        this.nakyma = nakyma;
        sarjanakyma = nakyma.annaSarjanakyma();
        joukkuenakyma = nakyma.annaJoukkuenakyma();

    }

    public void annaVarmistus() {
        Stage stageV = new Stage();
        BorderPane alue = new BorderPane();

        VBox vbox = new VBox();
        vbox.setPadding(new Insets(10));
        vbox.setSpacing(10);

        HBox hbox1 = new HBox();
        Label viesti = new Label("Haluatko tallentaa ennen ohjelman lopettamista?");

        hbox1.setAlignment(Pos.CENTER);
        hbox1.getChildren().add(viesti);

        HBox hbox2 = new HBox();
        hbox2.setPadding(new Insets(10));
        hbox2.setSpacing(10);
        Button jooTallennus = new Button("Tallenna");
        Button eiTallennus = new Button("Älä tallenna");
        Button peruuta = new Button("Peruuta");
        eiTallennus.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Platform.exit();
            }
        });

        jooTallennus.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                Tarkistaja tarkistaja = new Tarkistaja(ikkuna, (Turnaus) ikkuna.annaTurnaus());
             tarkistaja.tarkistaTurnaustiedot();
                Platform.exit();
            }
        });
        peruuta.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                stageV.close();
            }
        });
        hbox2.setAlignment(Pos.CENTER);
        hbox2.getChildren().addAll(jooTallennus, eiTallennus, peruuta);
        vbox.getChildren().addAll(hbox1, hbox2);
        alue.setCenter(vbox);

        Scene sceneV = new Scene(alue, 400, 100);
        stageV.setTitle("TUPA - TULOSPALVELU");
        stageV.setScene(sceneV);
        stageV.show();

    }

    public void annaPoistoVarmistus(Kohde arvo) {
        Stage stageV = new Stage();
        BorderPane alue = new BorderPane();

        VBox vbox = new VBox();
        vbox.setPadding(new Insets(10));
        vbox.setSpacing(10);
        String nimi = arvo.toString();
        HBox hbox1 = new HBox();
        Label viesti = new Label("Haluatko todella poistaa kohteen " + nimi + " ?");

        hbox1.setAlignment(Pos.CENTER);
        hbox1.getChildren().add(viesti);

        HBox hbox2 = new HBox();
        hbox2.setPadding(new Insets(10));
        hbox2.setSpacing(10);
        Button joo = new Button("Kyllä");

        Button peruuta = new Button("Peruuta");

        joo.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                muuttaja.poistaKohde(arvo);
                stageV.close();

            }
        });
        peruuta.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                stageV.close();
            }
        });
        hbox2.setAlignment(Pos.CENTER);
        hbox2.getChildren().addAll(joo, peruuta);
        vbox.getChildren().addAll(hbox1, hbox2);
        alue.setCenter(vbox);

        Scene sceneV = new Scene(alue, 400, 100);
        stageV.setTitle("TUPA - TULOSPALVELU");
        stageV.setScene(sceneV);
        stageV.show();

    }

    public void annaUudenVarmistus() {
        Stage stageV = new Stage();
        BorderPane alue = new BorderPane();

        VBox vbox = new VBox();
        vbox.setPadding(new Insets(10));
        vbox.setSpacing(10);

        HBox hbox1 = new HBox();
        Label viesti = new Label("Haluatko tallentaa ennen uuden avaamista?");

        hbox1.setAlignment(Pos.CENTER);
        hbox1.getChildren().add(viesti);

        HBox hbox2 = new HBox();
        hbox2.setPadding(new Insets(10));
        hbox2.setSpacing(10);
        Button jooTallennus = new Button("Tallenna");
        Button eiTallennus = new Button("Älä tallenna");
        Button peruuta = new Button("Peruuta");
        eiTallennus.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                //tyhjennetään kaikki tiedot 
                ikkuna.annaKohteet().clear();

                Kohde uusiTurnaus = new Turnaus();
                ikkuna.asetaTurnaus(uusiTurnaus);

                //vielä pitää tyhjentää puu
                TreeItem<Kohde> parentSarjat = ikkuna.annaRootSarjat();
                TreeItem<Kohde> parentTuomarit = ikkuna.annaRootTuomarit();
                parentSarjat.getChildren().clear();
                parentTuomarit.getChildren().clear();
                ikkuna.annaPaaNakyma().luoEtusivu();
                stageV.close();
            }
        });

        jooTallennus.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                Tarkistaja tarkistaja = new Tarkistaja(ikkuna, (Turnaus) ikkuna.annaTurnaus());
             tarkistaja.tarkistaTurnaustiedot();
                //tyhjennetään kaikki tiedot 
                ikkuna.annaKohteet().clear();

                Kohde uusiTurnaus = new Turnaus();
                ikkuna.asetaTurnaus(uusiTurnaus);

                //vielä pitää tyhjentää puu
                TreeItem<Kohde> parentSarjat = ikkuna.annaRootSarjat();
                TreeItem<Kohde> parentTuomarit = ikkuna.annaRootTuomarit();
                parentSarjat.getChildren().clear();
                parentTuomarit.getChildren().clear();
                ikkuna.annaPaaNakyma().luoEtusivu();
                stageV.close();
            }
        });
        peruuta.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                stageV.close();
            }
        });
        hbox2.setAlignment(Pos.CENTER);
        hbox2.getChildren().addAll(jooTallennus, eiTallennus, peruuta);
        vbox.getChildren().addAll(hbox1, hbox2);
        alue.setCenter(vbox);

        Scene sceneV = new Scene(alue, 400, 100);
        stageV.setTitle("TUPA - TULOSPALVELU");
        stageV.setScene(sceneV);
        stageV.show();

    }

    public void annaAvausVarmistus() {
        Stage stageV = new Stage();
        BorderPane alue = new BorderPane();

        VBox vbox = new VBox();
        vbox.setPadding(new Insets(10));
        vbox.setSpacing(10);

        HBox hbox1 = new HBox();
        Label viesti = new Label("Haluatko tallentaa ennen uuden avaamista?");

        hbox1.setAlignment(Pos.CENTER);
        hbox1.getChildren().add(viesti);

        HBox hbox2 = new HBox();
        hbox2.setPadding(new Insets(10));
        hbox2.setSpacing(10);
        Button jooTallennus = new Button("Tallenna");
        Button eiTallennus = new Button("Älä tallenna");
        Button peruuta = new Button("Peruuta");
        eiTallennus.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                //tyhjennetään kaikki tiedot 
                ikkuna.annaKohteet().clear();

                Kohde uusiTurnaus = new Turnaus();
                ikkuna.asetaTurnaus(uusiTurnaus);

                //vielä pitää tyhjentää puu
                TreeItem<Kohde> parentSarjat = ikkuna.annaRootSarjat();
                TreeItem<Kohde> parentTuomarit = ikkuna.annaRootTuomarit();
                parentSarjat.getChildren().clear();
                parentTuomarit.getChildren().clear();

                //sitten vasta avaukseen
                  TurnausValitsin valitsija = new TurnausValitsin(ikkuna);
                try {
                    valitsija.annaTurnausLuettelo();
                } catch (SQLException ex) {
                    Logger.getLogger(Varmistaja.class.getName()).log(Level.SEVERE, null, ex);
                }
                stageV.close();
            }
        });

        jooTallennus.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                Tarkistaja tarkistaja = new Tarkistaja(ikkuna, (Turnaus) ikkuna.annaTurnaus());
             tarkistaja.tarkistaTurnaustiedot();
                //tyhjennetään kaikki tiedot 
                ikkuna.annaKohteet().clear();

                Kohde uusiTurnaus = new Turnaus();
                ikkuna.asetaTurnaus(uusiTurnaus);

                //vielä pitää tyhjentää puu
                TreeItem<Kohde> parentSarjat = ikkuna.annaRootSarjat();
                TreeItem<Kohde> parentTuomarit = ikkuna.annaRootTuomarit();
                parentSarjat.getChildren().clear();
                parentTuomarit.getChildren().clear();
                //sitten vasta avaukseen
                   //sitten vasta avaukseen
                  TurnausValitsin valitsija = new TurnausValitsin(ikkuna);
                try {
                    valitsija.annaTurnausLuettelo();
                } catch (SQLException ex) {
                    Logger.getLogger(Varmistaja.class.getName()).log(Level.SEVERE, null, ex);
                }
                stageV.close();
            }
        });
        peruuta.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                stageV.close();
            }
        });
        hbox2.setAlignment(Pos.CENTER);
        hbox2.getChildren().addAll(jooTallennus, eiTallennus, peruuta);
        vbox.getChildren().addAll(hbox1, hbox2);
        alue.setCenter(vbox);

        Scene sceneV = new Scene(alue, 400, 100);
        stageV.setTitle("TUPA - TULOSPALVELU");
        stageV.setScene(sceneV);
        stageV.show();

    }

    public void annaOtteluPoistoVarmistus(Ottelu ottelu) {

        Sarja sarja = ottelu.annaSarja();
        Stage stageV = new Stage();
        BorderPane alue = new BorderPane();

        VBox vbox = new VBox();
        vbox.setPadding(new Insets(10));
        vbox.setSpacing(10);

        HBox hbox1 = new HBox();
        Label viesti = new Label("Haluatko todella poistaa ottelun " + ottelu.toString() + " ?");

        hbox1.setAlignment(Pos.CENTER);
        hbox1.getChildren().add(viesti);

        HBox hbox2 = new HBox();
        hbox2.setPadding(new Insets(10));
        hbox2.setSpacing(10);
        Button joo = new Button("Kyllä");

        Button peruuta = new Button("Peruuta");

        joo.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                muuttaja.poistaOttelu(ottelu);
                sarjanakyma = nakyma.annaSarjanakyma();
                sarjanakyma.luoOtteluLuetteloMuokkaus(sarja);
                stageV.close();

            }
        });
        peruuta.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                stageV.close();
            }
        });
        hbox2.setAlignment(Pos.CENTER);
        hbox2.getChildren().addAll(joo, peruuta);
        vbox.getChildren().addAll(hbox1, hbox2);
        alue.setCenter(vbox);

        Scene sceneV = new Scene(alue, 400, 100);
        stageV.setTitle("TUPA - TULOSPALVELU");
        stageV.setScene(sceneV);
        stageV.show();

    }

    public void annaKaikkienOtteluidenPoistoVarmistus(Sarja sarja) {
        Stage stageV = new Stage();
        BorderPane alue = new BorderPane();

        VBox vbox = new VBox();
        vbox.setPadding(new Insets(10));
        vbox.setSpacing(10);

        HBox hbox1 = new HBox();
        Label viesti = new Label("Haluatko todella poistaa kaikki sarjan " + sarja.toString() + " ottelut?");

        hbox1.setAlignment(Pos.CENTER);
        hbox1.getChildren().add(viesti);

        HBox hbox2 = new HBox();
        hbox2.setPadding(new Insets(10));
        hbox2.setSpacing(10);
        Button joo = new Button("Kyllä");

        Button peruuta = new Button("Peruuta");

        joo.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                List<Ottelu> poistettavat = (sarja.annaOttelut());

                muuttaja.poistaKaikkiOttelut(poistettavat, sarja);
                sarjanakyma = nakyma.annaSarjanakyma();
                TreeItem<Kohde> mihin = new TreeItem<>(sarja);

                sarjanakyma.luoSarjaSivu(mihin);
                stageV.close();

            }
        });
        peruuta.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                stageV.close();
            }
        });
        hbox2.setAlignment(Pos.CENTER);
        hbox2.getChildren().addAll(joo, peruuta);
        vbox.getChildren().addAll(hbox1, hbox2);
        alue.setCenter(vbox);

        Scene sceneV = new Scene(alue, 400, 100);
        stageV.setTitle("TUPA - TULOSPALVELU");
        stageV.setScene(sceneV);
        stageV.show();

    }

    public void annaAutoVarmistus(Sarja sarja) {
        Stage stageV = new Stage();
        BorderPane alue = new BorderPane();

        VBox vbox = new VBox();
        vbox.setPadding(new Insets(10));
        vbox.setSpacing(10);

        HBox hbox1 = new HBox();
        Label viesti = new Label("Automaattinen otteluluettelon laadinta poistaa ensin kaikki sarjaan lisätyt ottelut. Haluatko jatkaa?");

        hbox1.setAlignment(Pos.CENTER);
        hbox1.getChildren().add(viesti);

        HBox hbox2 = new HBox();
        hbox2.setPadding(new Insets(10));
        hbox2.setSpacing(10);
        Button joo = new Button("Kyllä");

        Button peruuta = new Button("Peruuta");

        joo.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                muuttaja.suoritaAutoOtteluLista(sarja);
                sarjanakyma = nakyma.annaSarjanakyma();

                sarjanakyma.luoOtteluLuetteloMuokkaus(sarja);
                stageV.close();

            }
        });
        peruuta.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                stageV.close();
            }
        });
        hbox2.setAlignment(Pos.CENTER);
        hbox2.getChildren().addAll(joo, peruuta);
        vbox.getChildren().addAll(hbox1, hbox2);
        alue.setCenter(vbox);

        Scene sceneV = new Scene(alue, 600, 100);
        stageV.setTitle("TUPA - TULOSPALVELU");
        stageV.setScene(sceneV);
        stageV.show();

    }

    public void annaKaikkienJoukkueidenPoistoVarmistus(Sarja sarja) {
        Stage stageV = new Stage();
        BorderPane alue = new BorderPane();

        VBox vbox = new VBox();
        vbox.setPadding(new Insets(10));
        vbox.setSpacing(10);

        HBox hbox1 = new HBox();
        Label viesti = new Label("Joukkueiden poisto poistaa samalla kaikki joukkueiden pelaajat järjestelmästä. Haluatko jatkaa?");

        hbox1.setAlignment(Pos.CENTER);
        hbox1.getChildren().add(viesti);

        HBox hbox2 = new HBox();
        hbox2.setPadding(new Insets(10));
        hbox2.setSpacing(10);
        Button joo = new Button("Kyllä");

        Button peruuta = new Button("Peruuta");

        joo.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                List<Joukkue> poistettavat = (sarja.annaJoukkueet());

                muuttaja.poistaKaikkiJoukkueet(poistettavat, sarja);
                sarjanakyma = nakyma.annaSarjanakyma();
                TreeItem<Kohde> mihin = new TreeItem<>(sarja);

                sarjanakyma.luoSarjaSivu(mihin);
                stageV.close();

            }
        });
        peruuta.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                stageV.close();
            }
        });
        hbox2.setAlignment(Pos.CENTER);
        hbox2.getChildren().addAll(joo, peruuta);
        vbox.getChildren().addAll(hbox1, hbox2);
        alue.setCenter(vbox);

        Scene sceneV = new Scene(alue, 600, 100);
        stageV.setTitle("TUPA - TULOSPALVELU");
        stageV.setScene(sceneV);
        stageV.show();

    }

    public void annaJoukkueenPoistoVarmistus(Joukkue joukkue) {
        Stage stageV = new Stage();
        BorderPane alue = new BorderPane();

        VBox vbox = new VBox();
        vbox.setPadding(new Insets(10));
        vbox.setSpacing(10);

        HBox hbox1 = new HBox();
        Label viesti = new Label("Haluatko todella poistaa joukkueen " + joukkue.toString() + "?");

        hbox1.setAlignment(Pos.CENTER);
        hbox1.getChildren().add(viesti);

        HBox hbox2 = new HBox();
        hbox2.setPadding(new Insets(10));
        hbox2.setSpacing(10);
        Button joo = new Button("Kyllä");

        Button peruuta = new Button("Peruuta");

        joo.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                muuttaja.poistaJoukkue(joukkue, joukkue.annaSarja());

                sarjanakyma = nakyma.annaSarjanakyma();

                sarjanakyma.luoJoukkueenLisaysSivu(joukkue.annaSarja());
                stageV.close();

            }
        });
        peruuta.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                stageV.close();
            }
        });
        hbox2.setAlignment(Pos.CENTER);
        hbox2.getChildren().addAll(joo, peruuta);
        vbox.getChildren().addAll(hbox1, hbox2);
        alue.setCenter(vbox);

        Scene sceneV = new Scene(alue, 300, 100);
        stageV.setTitle("TUPA - TULOSPALVELU");
        stageV.setScene(sceneV);
        stageV.show();

    }

    public void annaPelaajanPoistoVarmistus(Pelaaja pelaaja) {
        Stage stageV = new Stage();
        BorderPane alue = new BorderPane();

        VBox vbox = new VBox();
        vbox.setPadding(new Insets(10));
        vbox.setSpacing(10);

        HBox hbox1 = new HBox();
        Label viesti = new Label("Haluatko todella poistaa pelaajan " + pelaaja.toString() + "?");

        hbox1.setAlignment(Pos.CENTER);
        hbox1.getChildren().add(viesti);

        HBox hbox2 = new HBox();
        hbox2.setPadding(new Insets(10));
        hbox2.setSpacing(10);
        Button joo = new Button("Kyllä");

        Button peruuta = new Button("Peruuta");

        joo.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                muuttaja.poistaPelaaja(pelaaja, pelaaja.annaJoukkue());

                joukkuenakyma = nakyma.annaJoukkuenakyma();

                joukkuenakyma.luoJoukkueenPelaajaLisays(pelaaja.annaJoukkue());
                stageV.close();

            }
        });
        peruuta.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                stageV.close();
            }
        });
        hbox2.setAlignment(Pos.CENTER);
        hbox2.getChildren().addAll(joo, peruuta);
        vbox.getChildren().addAll(hbox1, hbox2);
        alue.setCenter(vbox);

        Scene sceneV = new Scene(alue, 300, 100);
        stageV.setTitle("TUPA - TULOSPALVELU");
        stageV.setScene(sceneV);
        stageV.show();

    }

    public void annaKaikkienPelaajienPoistoVarmitus(Joukkue joukkue) {
        Stage stageV = new Stage();
        BorderPane alue = new BorderPane();

        VBox vbox = new VBox();
        vbox.setPadding(new Insets(10));
        vbox.setSpacing(10);

        HBox hbox1 = new HBox();
        Label viesti = new Label("Haluatko todella poistaa kaikki pelaajat joukkueesta " + joukkue.toString() + "?");

        hbox1.setAlignment(Pos.CENTER);
        hbox1.getChildren().add(viesti);

        HBox hbox2 = new HBox();
        hbox2.setPadding(new Insets(10));
        hbox2.setSpacing(10);
        Button joo = new Button("Kyllä");

        Button peruuta = new Button("Peruuta");

        joo.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                List<Pelaaja> poistettavat = (joukkue.annaPelaajat());

                muuttaja.poistaKaikkiPelaajat(poistettavat, joukkue);
                joukkuenakyma = nakyma.annaJoukkuenakyma();
                joukkuenakyma.luoJoukkueSivu(joukkue);
                stageV.close();

            }
        });
        peruuta.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                stageV.close();
            }
        });
        hbox2.setAlignment(Pos.CENTER);
        hbox2.getChildren().addAll(joo, peruuta);
        vbox.getChildren().addAll(hbox1, hbox2);
        alue.setCenter(vbox);

        Scene sceneV = new Scene(alue, 300, 100);
        stageV.setTitle("TUPA - TULOSPALVELU");
        stageV.setScene(sceneV);
        stageV.show();

    }

    void annaKaikkienToimarienPoistoVarmitus(Joukkue joukkue) {
        Stage stageV = new Stage();
        BorderPane alue = new BorderPane();

        VBox vbox = new VBox();
        vbox.setPadding(new Insets(10));
        vbox.setSpacing(10);

        HBox hbox1 = new HBox();
        Label viesti = new Label("Haluatko todella poistaa kaikki toimihenkilöt joukkueesta " + joukkue.toString() + "?");

        hbox1.setAlignment(Pos.CENTER);
        hbox1.getChildren().add(viesti);

        HBox hbox2 = new HBox();
        hbox2.setPadding(new Insets(10));
        hbox2.setSpacing(10);
        Button joo = new Button("Kyllä");

        Button peruuta = new Button("Peruuta");

        joo.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                List<Toimihenkilo> poistettavat = (joukkue.annaToimarit());

                muuttaja.poistaKaikkiToimarit(poistettavat, joukkue);
                joukkuenakyma = nakyma.annaJoukkuenakyma();
                joukkuenakyma.luoJoukkueSivu(joukkue);
                stageV.close();

            }
        });
        peruuta.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                stageV.close();
            }
        });
        hbox2.setAlignment(Pos.CENTER);
        hbox2.getChildren().addAll(joo, peruuta);
        vbox.getChildren().addAll(hbox1, hbox2);
        alue.setCenter(vbox);

        Scene sceneV = new Scene(alue, 300, 100);
        stageV.setTitle("TUPA - TULOSPALVELU");
        stageV.setScene(sceneV);
        stageV.show();
    }

    public void annaToimarinPoistoVarmistus(Toimihenkilo toimari) {
        Stage stageV = new Stage();
        BorderPane alue = new BorderPane();

        VBox vbox = new VBox();
        vbox.setPadding(new Insets(10));
        vbox.setSpacing(10);

        HBox hbox1 = new HBox();
        Label viesti = new Label("Haluatko todella poistaa toimihenkilön " + toimari.toString() + "?");

        hbox1.setAlignment(Pos.CENTER);
        hbox1.getChildren().add(viesti);

        HBox hbox2 = new HBox();
        hbox2.setPadding(new Insets(10));
        hbox2.setSpacing(10);
        Button joo = new Button("Kyllä");

        Button peruuta = new Button("Peruuta");

        joo.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                muuttaja.poistaToimari(toimari, toimari.annaJoukkue());

                joukkuenakyma = nakyma.annaJoukkuenakyma();

                joukkuenakyma.luoJoukkueenToimariLisays(toimari.annaJoukkue());
                stageV.close();

            }
        });
        peruuta.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                stageV.close();
            }
        });
        hbox2.setAlignment(Pos.CENTER);
        hbox2.getChildren().addAll(joo, peruuta);
        vbox.getChildren().addAll(hbox1, hbox2);
        alue.setCenter(vbox);

        Scene sceneV = new Scene(alue, 300, 100);
        stageV.setTitle("TUPA - TULOSPALVELU");
        stageV.setScene(sceneV);
        stageV.show();

    }

}
