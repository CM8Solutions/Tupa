package tupa.kontrollerit;

import java.sql.SQLException;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableView;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import tupa.Tupa;
import tupa.data.Tuomari;

/**
 * Luokka, joka muodostaa näkymät, joissa voi valita sen tuomarin, jonka haluaa viedä tiedostoon/tuoda tiedostosta.
 * 
 * @author Marianne
 * @see Taulukko
 */
public class TuomariValitsin {

    private Tupa ikkuna;

    public TuomariValitsin() {

    }

    public TuomariValitsin(Tupa ikkuna) {
        this.ikkuna = ikkuna;
    }

    public void annaTuomariLuetteloVietavat() throws SQLException {
        int kayttaja_id = ikkuna.annaKayttajaID();
        Stage stage = new Stage();
        BorderPane alue = new BorderPane();

        stage.getIcons().add(new Image("kuvat/icon.png"));

        ScrollPane sb = new ScrollPane();
        VBox vbox = new VBox();
        vbox.setPadding(new Insets(30, 100, 30, 100));
        vbox.setSpacing(10);
        sb.setId("my-scrollpane");

        DropShadow dropShadow = new DropShadow();
        dropShadow.setOffsetX(5);
        dropShadow.setOffsetY(5);

        VBox box1 = new VBox();
        Text otsikko = new Text("Valitse tuomari, jonka tiedot haluat viedä tiedostoon.");
        otsikko.setFont(Font.font("Papyrus", FontWeight.BOLD, 20));
        otsikko.setEffect(dropShadow);

        Text opastus = new Text("(Huom! Valittavana on vain ne turnauksen tuomarit, joiden tietoja ei ole vielä viety tiedostoon.)");
        opastus.setFont(Font.font("Papyrus", FontWeight.BOLD, 14));

        box1.setAlignment(Pos.CENTER);
        box1.getChildren().addAll(otsikko, opastus);

        HBox hbox2 = new HBox();
        hbox2.setPadding(new Insets(30, 30, 10, 30));

        Taulukko taulukontekija = new Taulukko(ikkuna);

        TableView turnaukset = taulukontekija.luoVietavienTuomarienTaulukko(ikkuna.annaKayttajaID());
        turnaukset.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        turnaukset.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            Tuomari tuomari = (Tuomari) newSelection;
            int id = tuomari.annaID();
            Vie vieja = new Vie();
            try {
                vieja.vieTiedostoon(id, kayttaja_id);
            } catch (ParserConfigurationException ex) {
                Tiedottaja tiedottaja = new Tiedottaja();
                tiedottaja.annaVirhe("" + ex);
            } catch (SQLException ex) {
                Tiedottaja tiedottaja = new Tiedottaja();
                tiedottaja.annaVirhe("" + ex);
            } catch (TransformerException ex) {
                Tiedottaja tiedottaja = new Tiedottaja();
                tiedottaja.annaVirhe("" + ex);
            }
            try {

                annaTuomariLuetteloVietavat();
                stage.close();
            } catch (SQLException ex) {
                Tiedottaja tiedottaja = new Tiedottaja();
                tiedottaja.annaVirhe("" + ex);
            }

        });

        hbox2.setAlignment(Pos.CENTER);
        hbox2.getChildren().addAll(turnaukset);
        vbox.getChildren().addAll(box1, hbox2);

        sb.setContent(vbox);
        alue.setCenter(sb);

        Scene sceneV = new Scene(alue);
        sceneV.getStylesheets().add("css/tyylit.css");
        stage.setTitle("TUPA - Tulospalvelu");
        stage.setScene(sceneV);
        stage.show();

    }

    public void annaTuomariLuetteloTuotavat() throws SQLException {

        int kayttaja_id = ikkuna.annaKayttajaID();

        Stage stage = new Stage();
        BorderPane alue = new BorderPane();

        stage.getIcons().add(new Image("kuvat/icon.png"));

        ScrollPane sb = new ScrollPane();
        VBox vbox = new VBox();
        vbox.setPadding(new Insets(30, 100, 30, 100));
        vbox.setSpacing(10);
        sb.setId("my-scrollpane");

        DropShadow dropShadow = new DropShadow();
        dropShadow.setOffsetX(5);
        dropShadow.setOffsetY(5);

        VBox box1 = new VBox();

        Text otsikko = new Text("Valitse tuomari, jonka tiedot haluat tuoda turnaukseen.");
        otsikko.setFont(Font.font("Papyrus", FontWeight.BOLD, 20));
        otsikko.setEffect(dropShadow);
        Text opastus = new Text("(Huom! Valittavana on vain ne tuomarit, joiden tiedot on viety tiedostoon siitä turnauksesta, johon tuomari on alunperin lisätty.)");
        opastus.setFont(Font.font("Papyrus", FontWeight.BOLD, 14));

        box1.setAlignment(Pos.CENTER);
        box1.getChildren().addAll(otsikko, opastus);

        HBox hbox2 = new HBox();
        hbox2.setPadding(new Insets(30, 30, 10, 30));

        Taulukko taulukontekija = new Taulukko(ikkuna);

        TableView turnaukset = taulukontekija.luoTuotavienTuomarienTaulukko(ikkuna.annaKayttajaID());
        turnaukset.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        turnaukset.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            Tuomari tuomari = (Tuomari) newSelection;
            int id = tuomari.annaID();
            Tuo tuoja = new Tuo(ikkuna);
            try {
                tuoja.tuoTiedostosta(id, kayttaja_id);
            } catch (ParserConfigurationException ex) {
                Tiedottaja tiedottaja = new Tiedottaja();
                tiedottaja.annaVirhe("" + ex);
            } catch (SQLException ex) {
                Tiedottaja tiedottaja = new Tiedottaja();
                tiedottaja.annaVirhe("" + ex);
            } catch (TransformerException ex) {
                Tiedottaja tiedottaja = new Tiedottaja();
                tiedottaja.annaVirhe("" + ex);
            }
            try {
                annaTuomariLuetteloTuotavat();
            } catch (SQLException ex) {
                Tiedottaja tiedottaja = new Tiedottaja();
                tiedottaja.annaVirhe("" + ex);
            }
            stage.close();
        });

        hbox2.setAlignment(Pos.CENTER);
        hbox2.getChildren().addAll(turnaukset);
        vbox.getChildren().addAll(box1, hbox2);

        sb.setContent(vbox);
        alue.setCenter(sb);

        Scene sceneV = new Scene(alue);
        sceneV.getStylesheets().add("css/tyylit.css");
        stage.setTitle("TUPA - Tulospalvelu");
        stage.setScene(sceneV);
        stage.show();

    }

}
