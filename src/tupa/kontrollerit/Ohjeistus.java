package tupa.kontrollerit;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 *
 * @author Marianne
 */
public class Ohjeistus {

    public Ohjeistus() {

    }

    public void annaYleisOhje() {
        Stage stage = new Stage();
        BorderPane alue = new BorderPane();
        alue.setStyle("-fx-background-color:  linear-gradient(to bottom, #00ff00, 	#ccffcc)");
        alue.setPadding(new Insets(10, 30, 0, 30));
        stage.getIcons().add(new Image("kuvat/icon.png"));

        VBox vbox = new VBox();
        vbox.setPadding(new Insets(30, 30 , 0, 30));
        vbox.setSpacing(10);
          

        VBox box0 = new VBox();
  box0.setSpacing(10);
  
   HBox otsikkorivi = new HBox();
        otsikkorivi.setSpacing(400);

        HBox tekstiboxi = new HBox();
   
        HBox kuvaboxi = new HBox();
        kuvaboxi.setAlignment(Pos. TOP_LEFT);
        
        ImageView selectedImage = new ImageView();
        Image image1 = new Image("kuvat/ohje.png");
        selectedImage.setImage(image1);
        selectedImage.setFitHeight(60);
        selectedImage.setFitWidth(60);
  
        Label ekaotsikko = new Label("TUPA - tulospalvelussa on 4 eri käyttäjätasoa:");
         ekaotsikko.setFont(Font.font("Papyrus", FontWeight.BOLD, 16));
        Label ohje = new Label(annaPitkaOhje());

        ohje.setFont(Font.font("Papyrus", FontWeight.BOLD, 14));
    
        tekstiboxi.getChildren().addAll(ekaotsikko);
      kuvaboxi.getChildren().addAll(selectedImage);
       otsikkorivi.getChildren().addAll(tekstiboxi, kuvaboxi);
        box0.getChildren().addAll(otsikkorivi, ohje);

        HBox hbox1 = new HBox();

        Label guiviesti = new Label("\nTUPA - tulospalvelun käyttöliittymä on pyritty toteuttamaan siten, ettei käyttäjän tarvitse lukea erillistä käyttöohjetta.\nTarpeen mukaan sellainen voidaan kuitenkin myöhemmin laatia tähän.");

        guiviesti.setFont(Font.font("Papyrus", FontWeight.BOLD, 16));

        hbox1.setAlignment(Pos.CENTER);
        hbox1.getChildren().add(guiviesti);

        HBox hbox2 = new HBox();
        hbox2.setPadding(new Insets(20,40,40,40));

        Button ok = new Button("Sulje");

        ok.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                stage.close();
            }
        });
        hbox2.setAlignment(Pos.CENTER);
        hbox2.getChildren().addAll(ok);
        vbox.getChildren().addAll(box0, hbox1, hbox2);
        alue.setCenter(vbox);

        Scene sceneV = new Scene(alue);
        stage.setTitle("TUPA - TULOSPALVELU");
        stage.setScene(sceneV);
        stage.showAndWait();

    }

    public void annaTietoa() {
        Stage stage = new Stage();
        BorderPane alue = new BorderPane();
        alue.setPadding(new Insets(10, 50, 0, 50));
        alue.setStyle("-fx-background-color:  linear-gradient(to bottom, #00ff00, 	#ccffcc)");
        stage.getIcons().add(new Image("kuvat/icon.png"));

        VBox vbox = new VBox();
        vbox.setPadding(new Insets(40));
        vbox.setSpacing(30);
        vbox.setAlignment(Pos.CENTER);
        VBox vbox1 = new VBox();

        Label viesti1 = new Label("TUPA - tulospalvelu v1.0");
        Label viesti2 = new Label("TUPA-ohjelma soveltuu erilaisten sarjamuotoisina järjestettävien turnausten hallintaan.");
        Label viesti3 = new Label("Yhdellä lisenssillä voi muodostaa 5 kpl turnauksia.");
        Label viesti4 = new Label(" Lisenssejä voi tiedustella lähettämällä sähköpostia osoitteeseen u97506@student.uwasa.fi");

        Label viesti5 = new Label("\u00A9 \t Marianne Sjöberg & Victor Slätis 2016");

        viesti1.setFont(Font.font("Papyrus", FontWeight.BOLD, 16));
        viesti2.setFont(Font.font("Papyrus", FontWeight.BOLD, 16));
        viesti3.setFont(Font.font("Papyrus", FontWeight.BOLD, 16));
        viesti4.setFont(Font.font("Papyrus", FontWeight.BOLD, 16));
        viesti5.setFont(Font.font("Papyrus", FontWeight.BOLD, 16));

        vbox1.setAlignment(Pos.CENTER);
        vbox1.getChildren().addAll(viesti2, viesti3, viesti4);

        HBox hbox2 = new HBox();

        Button ok = new Button("Sulje");

        ok.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                stage.close();
            }
        });
        hbox2.setAlignment(Pos.CENTER);
        hbox2.getChildren().addAll(ok);
        vbox.getChildren().addAll(viesti1, vbox1, viesti5, hbox2);
        alue.setCenter(vbox);

        Scene sceneV = new Scene(alue);
        stage.setTitle("TUPA - TULOSPALVELU");
        stage.setScene(sceneV);
        stage.showAndWait();

    }

    public String annaPitkaOhje() {
        String ohje = "-Taso 3: Yleinen ylläpitäjä (kaikki oikeudet)\n-Taso 2: Lisenssin ostanut käyttäjä, joka voi luoda korkeintaan 5 turnausta ja hallinnoida niitä sekä luoda tason 1 käyttäjätunnuksia\n-Taso 1: Joukkueen tietoja hallinnoiva käyttäjä, joka voi\n\t* hallinnoida turnaukseen osallistuvia joukkueensa pelaajia ja toimihenkilöitä\n\t* ilmoittaa kokoonpanoja otteluihin\n\t* ilmoittaa joukkueensa otteluiden tuloksia ja maalintekijöitä\n- Taso 0: Käyttäjä, joka voi tarkastella turnaukseen liittyvien\n\t* sarjojen otteluohjelmia, otteluiden tuloksia, sarjatilannetta ja pistepörssiä\n\t* joukkueiden, pelaajien ja toimihenkilöiden sekä tuomareiden tietoja.\n\nKaikkien paitsi tason 0 käyttäjien on kirjauduttava TUPA-tulospalveluun käyttäjätunnuksella ja sitä vastaavalla salasanalla.\nLisäksi kirjautumattomien käyttäjien tulee syöttää turnaukseen liittyvä salasana päästäkseen tarkastelemaan kyseisen turnauksen tietoja.\nMikäli olet unohtanut kirjautumisen yhteydessä käyttäjätunnuksen/salasanan tai turnauksen salasanan, ota yhteyttä turnauksen ylläpitäjään.\n";

        return ohje;
    }

}
