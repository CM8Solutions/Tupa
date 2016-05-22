package tupa.kontrollerit;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Reflection;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import tupa.Tupa;
import tupa.data.Kohde;
import tupa.data.Turnaus;

/**
 *
 * @author Marianne
 */
public class Kirjautuminen {

    private Tupa ikkuna;
    private Turnaus turnaus;
    private Yhteys yhteys = new Yhteys();
    private Connection con = null;
    private Statement st = null;
    private String sql = "";

    public Kirjautuminen() {

    }

    public Kirjautuminen (Tupa ikkuna){
        this.ikkuna = ikkuna;
    }
    
    public Kirjautuminen(Turnaus turnaus, Tupa ikkuna) {
        this.turnaus = turnaus;
        this.ikkuna = ikkuna;
    }

    
    public void luoKirjautuminen() {

        Stage stage = new Stage();
        BorderPane alue = new BorderPane();
        alue.setPadding(new Insets(10, 50, 50, 50));
        stage.getIcons().add(new Image("kuvat/icon.png"));

        HBox hb = new HBox();
        hb.setPadding(new Insets(20, 20, 20, 30));

        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(20, 20, 20, 20));
        gridPane.setHgap(5);
        gridPane.setVgap(10);

        Label salasana = new Label("Salasana");
        PasswordField ss = new PasswordField();

        Button nappula = new Button("Kirjaudu");

        Label viesti = new Label();

        gridPane.add(salasana, 0, 0);
        gridPane.add(ss, 1, 0);
        gridPane.add(nappula, 1, 1);
        gridPane.add(viesti, 1, 2);

        DropShadow dropShadow = new DropShadow();
        dropShadow.setOffsetX(5);
        dropShadow.setOffsetY(5);

        Text text = new Text("Kirjaudu antamalla turnaukseen " + turnaus.toString() + " liittyvä salasana.");
        text.setFont(Font.font("Papyrus", FontWeight.BOLD, 20));
        text.setEffect(dropShadow);

        hb.getChildren().add(text);

        alue.setStyle("-fx-background-color:  linear-gradient(to bottom, #00ff00, 	#ccffcc)");

        nappula.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                String syotetty_salasana = ss.getText().toString();

                int turnaus_id = turnaus.annaID();

                try {

                    con = yhteys.annaYhteys();
                    st = con.createStatement();

                    sql = "SELECT DISTINCT * FROM turnauksen_salasana WHERE turnaus_id = '" + turnaus_id + "' AND salasana = '" + syotetty_salasana + "'";

                    ResultSet haetut_rivit = st.executeQuery(sql);
                    int laskuri = 0;
                    while (haetut_rivit.next()) {
                        laskuri++;

                    }

                    if (laskuri == 1) {

                        Avaus avaaja = new Avaus(turnaus, ikkuna);
                        try {
                            avaaja.avaa();
                        } catch (SQLException ex) {
                            Logger.getLogger(Taulukko.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (ClassNotFoundException ex) {
                            Logger.getLogger(Taulukko.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (InstantiationException ex) {
                            Logger.getLogger(Taulukko.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (IllegalAccessException ex) {
                            Logger.getLogger(Taulukko.class.getName()).log(Level.SEVERE, null, ex);
                        }

                        stage.close();

                    } else {

                        viesti.setText("Väärä salasana.");
                        viesti.setTextFill(Color.RED);

                    }

                    ss.setText("");
                } catch (SQLException se) {

                    se.printStackTrace();
                } catch (Exception e) {

                    e.printStackTrace();
                } finally {

                    try {
                        if (st != null) {
                            con.close();
                        }
                    } catch (SQLException se) {
                    }
                    try {
                        if (con != null) {
                            con.close();
                        }
                    } catch (SQLException se) {
                        se.printStackTrace();
                    }
                }

            }

        });

        alue.setTop(hb);
        alue.setCenter(gridPane);

        Scene sceneV = new Scene(alue);
        sceneV.getStylesheets().add("css/tyylit.css");
        stage.setTitle("TUPA - TULOSPALVELU");
        stage.setScene(sceneV);
        stage.show();

    }

   public void luoTurnauksenSalasananSyotto(){
       Stage stage = new Stage();
        BorderPane alue = new BorderPane();
        alue.setPadding(new Insets(10, 50, 50, 50));
        stage.getIcons().add(new Image("kuvat/icon.png"));

        HBox hb = new HBox();
        hb.setPadding(new Insets(20, 20, 20, 30));

        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(20, 20, 20, 20));
        gridPane.setHgap(5);
        gridPane.setVgap(10);

        Label salasana = new Label("Salasana");
        PasswordField ss = new PasswordField();
        Label salasana2 = new Label("Salasana uudelleen");
        PasswordField ss2 = new PasswordField();
        
        Button nappula = new Button("Tallenna");

        Label viesti = new Label();

        gridPane.add(salasana, 0, 0);
        gridPane.add(ss, 1, 0);
            gridPane.add(salasana2, 0, 1);
        gridPane.add(ss2, 1, 1);
        gridPane.add(nappula, 1, 2);
        gridPane.add(viesti, 1, 3);

        DropShadow dropShadow = new DropShadow();
        dropShadow.setOffsetX(5);
        dropShadow.setOffsetY(5);

        Text text = new Text("Syötä turnaukseen " + turnaus.toString() + " liittyvä salasana.");
        text.setFont(Font.font("Papyrus", FontWeight.BOLD, 20));
        text.setEffect(dropShadow);

        hb.getChildren().add(text);

        alue.setStyle("-fx-background-color:  linear-gradient(to bottom, #00ff00, 	#ccffcc)");

        nappula.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                String syotetty_salasana = ss.getText().toString();
             String syotetty_salasana2 = ss2.getText().toString();
                int turnaus_id = turnaus.annaID();
                
                if(syotetty_salasana.equals(syotetty_salasana2)){
                
                try {

                    con = yhteys.annaYhteys();
                    st = con.createStatement();

                 st.executeUpdate("INSERT INTO turnauksen_salasana (turnaus_id, salasana) VALUES('" + turnaus_id + "', '" + syotetty_salasana + "')");

                    Tiedottaja tiedottaja = new Tiedottaja();
                    tiedottaja.annaIlmoitus("Turnauksen "+turnaus.toString()+" salasana tallennettu!");

                        stage.close();


                
                } catch (SQLException se) {

                    se.printStackTrace();
                } catch (Exception e) {

                    e.printStackTrace();
                } finally {

                    try {
                        if (st != null) {
                            con.close();
                        }
                    } catch (SQLException se) {
                    }
                    try {
                        if (con != null) {
                            con.close();
                        }
                    } catch (SQLException se) {
                        se.printStackTrace();
                    }
                }

            }
                else{
                    
                    viesti.setText("Salasanat eivät vastaa toisiaan.");
                        viesti.setTextFill(Color.RED);
                        ss.setText("");
                            ss2.setText("");
                }
            }
        });

        alue.setTop(hb);
        alue.setCenter(gridPane);

        Scene sceneV = new Scene(alue);
        sceneV.getStylesheets().add("css/tyylit.css");
        stage.setTitle("TUPA - TULOSPALVELU");
        stage.setScene(sceneV);
        stage.show();
       
   }

}
