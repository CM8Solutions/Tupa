package tupa.kontrollerit;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import tupa.Tupa;

/**
 * Luokka, joka muodostaa eri näkymissä !-painikkeet takana olevat ohjeet
 * (ponnahdusikkuna).
 *
 * @author Marianne
 */
public class Opastus {

    private Tupa ikkuna;

    public Opastus() {

    }

    public Opastus(Tupa ikkuna) {
        this.ikkuna = ikkuna;
    }

    public void kirjoitaLoki(String msg) {

        ikkuna.annaLokilista().add(msg);
        ObservableList<String> viesti = FXCollections.observableArrayList(ikkuna.annaLokilista());
        ikkuna.annaLoki().setItems(viesti);

    }

    public void annaOhjeOtteluLuettelo() {

        String msg = "Klikkaamalla ottelua pääset tarkastelemaan otteluun liittyviä tietoja.";
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("TUPA - Tulospalvelu");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();

    }

    public void annaOhjeOtteluLuetteloMuokkaus() {

        String msg = "Klikkaa sitä taulukon solua, jonka sisältöä haluat muokata.";
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("TUPA - Tulospalvelu");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();

    }

    public void annaOhjeJoukkueLuettelo() {

        String msg = "Klikkaamalla joukkuetta pääset tarkastelemaan joukkueeseen liittyviä tietoja.";
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("TUPA - Tulospalvelu");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();

    }

    public void annaOhjeJoukkueLuetteloMuokkaus() {

        String msg = "Klikkaa taulukon solua, jos haluat muokata sen sisältöä.";
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("TUPA - Tulospalvelu");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();

    }

    public void annaOhjePelaajaLuettelo() {

        String msg = "Klikkaamalla pelaajaa pääset tarkastelemaan pelaajaan liittyviä tietoja.";
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("TUPA - Tulospalvelu");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();

    }

    public void annaOhjePelaajaLuetteloMuokkaus() {

        String msg = "Klikkaa sitä taulukon solua, jonka sisältöä haluat muokata.";
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("TUPA - Tulospalvelu");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();

    }

    public void annaOhjeToimariLuettelo() {

        String msg = "Klikkaamalla toimihenkilöä pääset tarkastelemaan toimihenkilöön liittyviä tietoja.";
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("TUPA - Tulospalvelu");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();

    }

    public void annaOhjeToimariLuetteloMuokkaus() {

        String msg = "Klikkaa sitä taulukon solua, jonka sisältöä haluat muokata.";
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("TUPA - Tulospalvelu");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();

    }

    public void annaOhjeMaalitilasto() {

        String msg = "Ottelun maalintekijäksi/syöttäjäksi voi merkitä vain sellaisen pelaajan, joka on merkitty ottelun kokoonpanoon.";
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("TUPA - Tulospalvelu");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();

    }

    public void annaOhjeKokoonpano() {

        String msg = "Ottelun kokoonpanoluettelossa on vain ne pelaajat, jotka on lisätty joukkueen pelaajaluetteloon.";
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("TUPA - Tulospalvelu");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();

    }

}
