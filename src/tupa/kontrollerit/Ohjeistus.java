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

import javafx.stage.Stage;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;

import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

/**
 *
 * @author Marianne
 */
public class Ohjeistus {

    InputStream fs = null;

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
        vbox.setPadding(new Insets(30, 700, 30, 30));
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

        Scene sceneV = new Scene(sp, 800, 500);
        stage.setTitle("TUPA - Tulospalvelu");
        stage.setScene(sceneV);
        stage.show();

    }

    public void annaTietoa() throws IOException {
        Stage stage = new Stage();
        BorderPane alue = new BorderPane();
        alue.setPadding(new Insets(10, 20, 0, 10));
        alue.setStyle("-fx-background-color:  linear-gradient(to bottom, #00ff00, 	#ccffcc)");
        stage.getIcons().add(new Image("kuvat/icon.png"));

        VBox vbox = new VBox();
        vbox.setPadding(new Insets(40));
        vbox.setSpacing(30);
        vbox.setAlignment(Pos.CENTER);
        VBox vbox1 = annaPitkaOhje2();

        Label viesti1 = new Label("TUPA - tulospalvelu v1.0");

        Label viesti5 = new Label("\u00A9 \t Marianne Sjöberg & Victor Slätis 2016");

        viesti1.setFont(Font.font("Papyrus", FontWeight.BOLD, 16));

        viesti5.setFont(Font.font("Papyrus", FontWeight.BOLD, 16));

        vbox1.setAlignment(Pos.CENTER);

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
        vbox.getChildren().addAll(viesti1, vbox1, viesti5);
        alue.setCenter(vbox);

        Scene sceneV = new Scene(alue);
        stage.setTitle("TUPA - Tulospalvelu");
        stage.setScene(sceneV);
        stage.show();

    }

    public void annaUnohtunutOhje() throws UnsupportedEncodingException, IOException {

        Stage stage = new Stage();
        stage.getIcons().add(new Image("kuvat/icon.png"));
        HBox alue = new HBox();

        ScrollPane sp = new ScrollPane();
        sp.setId("my-scrollpane");

        VBox vbox = new VBox();

        vbox.setSpacing(10);
        vbox.setPadding(new Insets(30, 1200, 800, 30));
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

        Label ekaotsikko = new Label("Unohtunut tunnus/salasana:");
        ekaotsikko.setFont(Font.font("Papyrus", FontWeight.BOLD, 20));

        tekstiboxi.getChildren().addAll(ekaotsikko);
        kuvaboxi.getChildren().addAll(selectedImage);
        otsikkorivi.getChildren().addAll(tekstiboxi, kuvaboxi);
        box0.getChildren().addAll(otsikkorivi, annaPitkaOhje3());

        vbox.getChildren().addAll(box0);
        sp.setContent(vbox);

        vbox.setStyle("-fx-background-color:  linear-gradient(to bottom, #00ff00, 	#ccffcc)");

        Scene sceneV = new Scene(sp, 800, 400);
        stage.setTitle("TUPA - Tulospalvelu");
        stage.setScene(sceneV);
        stage.show();

    }

    public VBox annaPitkaOhje() throws IOException {

        VBox boxi = new VBox();
        fs = getClass().getClassLoader().getResourceAsStream("tiedostot/tupa_ohje.txt");

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

    public VBox annaPitkaOhje2() throws IOException {

        VBox boxi = new VBox();
        fs = getClass().getClassLoader().getResourceAsStream("tiedostot/tupa_tietoa.txt");

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

    public VBox annaPitkaOhje3() throws IOException {

        VBox boxi = new VBox();
        fs = getClass().getClassLoader().getResourceAsStream("tiedostot/tupa_unohtunutohje.txt");

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
