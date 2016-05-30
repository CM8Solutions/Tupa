package tupa.kontrollerit;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
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
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;

import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

/**
 *
 * @author Marianne
 */
public class Ohjeistus {

    FileInputStream fs = null;

    String tiedosto = "tupa.txt";

    public Ohjeistus() {

    }

    public void annaYleisOhje() throws UnsupportedEncodingException, IOException {

        Stage stage = new Stage();
        stage.getIcons().add(new Image("kuvat/icon.png"));
        HBox alue = new HBox();

        ScrollPane sp = new ScrollPane();
        sp.setId("my-scrollpane");

        VBox vbox = new VBox();

        vbox.setSpacing(10);
        vbox.setPadding(new Insets(30, 700, 20, 30));
        vbox.setAlignment(Pos.CENTER);

        VBox box0 = new VBox();

        HBox otsikkorivi = new HBox();
        otsikkorivi.setSpacing(300);

        HBox tekstiboxi = new HBox();

        HBox kuvaboxi = new HBox();
        kuvaboxi.setAlignment(Pos.TOP_LEFT);

        ImageView selectedImage = new ImageView();
        Image image1 = new Image("kuvat/ohje.png");
        selectedImage.setImage(image1);
        selectedImage.setFitHeight(60);
        selectedImage.setFitWidth(60);

        Label ekaotsikko = new Label("TUPA - tulospalvelu:");
        ekaotsikko.setFont(Font.font("Papyrus", FontWeight.BOLD, 20));

        tekstiboxi.getChildren().addAll(ekaotsikko);
        kuvaboxi.getChildren().addAll(selectedImage);
        otsikkorivi.getChildren().addAll(tekstiboxi, kuvaboxi);
        box0.getChildren().addAll(otsikkorivi, annaPitkaOhje());

        vbox.getChildren().addAll(box0);
        sp.setContent(vbox);

        vbox.setStyle("-fx-background-color:  linear-gradient(to bottom, #00ff00, 	#ccffcc)");

        Scene sceneV = new Scene(sp, 700, 400);
        stage.setTitle("TUPA - TULOSPALVELU");
        stage.setScene(sceneV);
        stage.show();

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
        stage.show();

    }

    public VBox annaPitkaOhje() throws IOException {

        VBox boxi = new VBox();
        try {
            fs = new FileInputStream(tiedosto);
        } catch (FileNotFoundException e) {
        
            return boxi;
        }

        InputStreamReader is = new InputStreamReader(fs, "ISO-8859-1");
        BufferedReader input = new BufferedReader(is);

        String rivi = input.readLine();
        while (rivi != null) {
            Label uusi = new Label();
            uusi.setText(rivi);
            uusi.setFont(Font.font("Papyrus", FontWeight.BOLD, 14));
            boxi.getChildren().add(uusi);
            rivi = input.readLine();
        }

        fs.close();

        return boxi;
    }

}
