/*
Luokka, joka rakentaa näkymien samanlaisena pysyvät osiot
 */
package tupa.nakymat;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import tupa.Tupa;
import tupa.kontrollerit.LinkkiLabel;
import tupa.data.Kohde;

/**
 * Luokka, joka muodostaa eri näkymissä olevia pysyviä osia.
 *
 * @author Marianne
 */
public class Pysyvat {

    private Tupa ikkuna;

    public Pysyvat() {

    }

    public Pysyvat(Tupa ikkuna) {
        this.ikkuna = ikkuna;

    }

    public HBox rakennaYlaosa() {
        HBox kuvaotsikko = new HBox();

        kuvaotsikko.setPrefWidth(400);
        ImageView selectedImage = new ImageView();
        Image image1 = new Image("kuvat/pallo.jpg");
        selectedImage.setImage(image1);
        selectedImage.setFitHeight(20);
        selectedImage.setFitWidth(20);

        ImageView selectedImage2 = new ImageView();
        Image image2 = new Image("kuvat/pallo.jpg");
        selectedImage2.setImage(image2);
        selectedImage2.setFitHeight(20);
        selectedImage2.setFitWidth(20);

        Label logo = new Label("TUPA \t - \t Tulospalvelu ");
        logo.setFont(Font.font("Papyrus", FontWeight.BOLD, 28));

        kuvaotsikko.setStyle("-fx-background-color:  linear-gradient(to bottom, #00b300, 	 #33ff33);");
        kuvaotsikko.setPadding(new Insets(10));
        kuvaotsikko.setSpacing(30);
        kuvaotsikko.setAlignment(Pos.CENTER);
        kuvaotsikko.getChildren().addAll(selectedImage, logo, selectedImage2);
        return kuvaotsikko;
    }

    public VBox rakennaAlaosa() {
        ListView loki = ikkuna.annaLoki();
        VBox alaosio = new VBox();
        alaosio.setStyle("-fx-background-color: linear-gradient(to bottom, #1a1a1a, #404040); -fx-border-color: BLACK; -fx-border-width: 1px; -fx-text-fill: #ff0099 ");
        alaosio.setPadding(new Insets(5, 10, 5, 10));
        alaosio.setSpacing(10);

        alaosio.setPrefWidth(600);
        alaosio.setPrefHeight(120);
        Label alaotsikko = new Label("Tapahtumatiedot:");
        alaotsikko.setStyle("-fx-text-fill: #fff ");
        alaosio.setVgrow(loki, Priority.ALWAYS);
        alaosio.getChildren().addAll(alaotsikko, loki);

        return alaosio;
    }

    public VBox rakennaVasensivu(TreeView<Kohde> spuu) {
        //sivulaitaan puurakenne, joka sisältää kohteet

        VBox sivu = new VBox();
        sivu.setStyle("-fx-background-color: linear-gradient(to right, #00b300, 	 #33ff33);");
        sivu.setPadding(new Insets(70, 5, 0, 5));
        sivu.setSpacing(10);

        //hakutoiminto HBox
        // Etusivu-linkki
        HBox etusivu = new HBox();

        LinkkiLabel etusivuteksti = new LinkkiLabel(ikkuna);
        etusivuteksti.setText("Etusivu");

        etusivuteksti.linkkiaKlikattu();
        etusivu.setPadding(new Insets(10));
        etusivu.setSpacing(10);
        etusivu.getChildren().addAll(etusivuteksti);

        HBox joukkuesivu = new HBox();

        LinkkiLabel joukkuesivuteksti = new LinkkiLabel(ikkuna);
        joukkuesivuteksti.setText("Oma joukkue");

        joukkuesivuteksti.linkkiaKlikattu();
        joukkuesivu.setPadding(new Insets(10));
        joukkuesivu.setSpacing(10);
        joukkuesivu.getChildren().addAll(joukkuesivuteksti);

        sivu.setPrefWidth(180);
        if (ikkuna.annaTaso() == 1) {
            sivu.getChildren().addAll(etusivu, joukkuesivu, spuu);
        } else {
            sivu.getChildren().addAll(etusivu, spuu);
        }

        sivu.setVgrow(spuu, Priority.ALWAYS);

        return sivu;
    }

    public VBox rakennaVasensivuAlku() {
        //sivulaitaan puurakenne, joka sisältää kohteet

        VBox sivu = new VBox();
        sivu.setStyle("-fx-background-color: linear-gradient(to right, #00b300, 	 #33ff33);");
        sivu.setPadding(new Insets(70, 5, 0, 5));
        sivu.setSpacing(10);
        sivu.setMinWidth(180);

        return sivu;
    }

}
