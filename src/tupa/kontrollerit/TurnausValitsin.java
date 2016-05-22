package tupa.kontrollerit;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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
import tupa.Tupa;
import tupa.data.Turnaus;

/**
 *
 * @author Marianne
 */
public class TurnausValitsin {
    
    private Tupa ikkuna;
    
    public TurnausValitsin(){
        
    }
    
    public TurnausValitsin(Tupa ikkuna){
        this.ikkuna = ikkuna;
    }
    
    public void annaTurnausLuettelo() throws SQLException{
        
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
 
        HBox hbox1 = new HBox();
        Text otsikko = new Text("Valitse haluamasi turnaus.");
   otsikko.setFont(Font.font("Papyrus", FontWeight.BOLD, 20));
   otsikko.setEffect(dropShadow);
   
        hbox1.setAlignment(Pos.CENTER);
        hbox1.getChildren().add(otsikko);

        HBox hbox2 = new HBox();
        hbox2.setPadding(new Insets(30, 30, 10, 30));
     
         Taulukko taulukontekija = new Taulukko(ikkuna);

        TableView turnaukset = taulukontekija.luoTurnausTaulukko();
        turnaukset.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        
        
        turnaukset.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            Kirjautuminen kirjautuja = new Kirjautuminen((Turnaus) newSelection, ikkuna);
           kirjautuja.luoKirjautuminen();
            
            stage.close();
        });
        
        hbox2.setAlignment(Pos.CENTER);
        hbox2.getChildren().addAll(turnaukset);
        vbox.getChildren().addAll(hbox1, hbox2);
        
        sb.setContent(vbox);
        alue.setCenter(sb);

        Scene sceneV = new Scene(alue);
         sceneV.getStylesheets().add("css/tyylit.css");
        stage.setTitle("TUPA - TULOSPALVELU");
        stage.setScene(sceneV);
        stage.show();
        
    }
    
}
