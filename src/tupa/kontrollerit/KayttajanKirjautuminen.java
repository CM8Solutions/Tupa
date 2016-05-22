package tupa.kontrollerit;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
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
import javafx.stage.WindowEvent;
import tupa.Tupa;
import tupa.nakymat.PaaNakyma;

/**
 *
 * @author Marianne
 */
public class KayttajanKirjautuminen extends Stage {
    
    private Yhteys yhteys = new Yhteys();
    private Connection con = null;
    private Statement st = null;
    private String sql = "";
    private Tupa ikkuna;
    
    public KayttajanKirjautuminen(Stage owner, Tupa ikkuna){
      
     super();
       this.ikkuna = ikkuna;
        initOwner(owner);
           setTitle("TUPA \t - \t Tulospalvelu");
 

       this.getIcons().add(new Image("kuvat/icon.png"));
        BorderPane alue = new BorderPane();
        alue.setPadding(new Insets(10, 50, 50, 50));
      

        HBox hb = new HBox();
        hb.setPadding(new Insets(20, 20, 0, 30));

        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(20, 20, 0, 20));
        gridPane.setHgap(5);
        gridPane.setVgap(10);

        Label tunnus = new Label("Käyttäjätunnus");
        TextField ts = new TextField();
        Label salasana = new Label("Salasana");
        PasswordField ss = new PasswordField();
        
        Button nappula = new Button("Kirjaudu");

        Label viesti = new Label();

        gridPane.add(tunnus, 0, 0);
        gridPane.add(ts, 1, 0);
            gridPane.add(salasana, 0, 1);
        gridPane.add(ss, 1, 1);
        gridPane.add(nappula, 1, 2);
        gridPane.add(viesti, 1, 3);

        DropShadow dropShadow = new DropShadow();
        dropShadow.setOffsetX(5);
        dropShadow.setOffsetY(5);

        Text text = new Text("Kirjaudu TUPA-ohjelmaan antamalla käyttäjätunnus ja salasana.");
        text.setFont(Font.font("Papyrus", FontWeight.BOLD, 20));
        text.setEffect(dropShadow);

        hb.getChildren().add(text);

        alue.setStyle("-fx-background-color:  linear-gradient(to bottom, #00ff00, 	#ccffcc)");

        nappula.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                String syotetty_tunnus = ts.getText().toString();
             String syotetty_salasana = ss.getText().toString();
              
                
              
                
                try {

                    con = yhteys.annaYhteys();
                    st = con.createStatement();

                    sql = "SELECT DISTINCT * FROM kayttaja WHERE tunnus = '" + syotetty_tunnus + "' AND salasana = '" + syotetty_salasana + "'";

                    ResultSet haetut_rivit = st.executeQuery(sql);
                    int laskuri = 0;
                    int taso = 0;
                    int kayttaja_id = 0;
                    while (haetut_rivit.next()) {
                        laskuri++;
                        taso = haetut_rivit.getInt("taso");
                        kayttaja_id = haetut_rivit.getInt("id");
                    }
                    
                    if(laskuri == 1){
                   
                        ikkuna.asetaTaso(taso);
                         ikkuna.asetaKayttajaID(kayttaja_id);
                          Tiedottaja tiedottaja = new Tiedottaja(ikkuna);
                    tiedottaja.annaIlmoitus("Kirjautuminen onnistui!");
                    
                          close();
                    }
                  else {

                        viesti.setText("Väärä tunnus tai salasana.");
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

          VBox hb2 = new VBox();
        hb2.setPadding(new Insets(10, 20, 0, 30));
        hb2.setSpacing(20);
        
         Text text2 = new Text("Voit myös jatkaa ohjelmaan kirjautumatta rajoitetuimmilla oikeuksilla.");
        text2.setFont(Font.font("Papyrus", FontWeight.BOLD, 14));
        text2.setEffect(dropShadow);

        
        hb2.getChildren().add(text2);
         Button nappula2 = new Button("Jatka kirjautumatta");
         nappula2.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                PaaNakyma nakyma = ikkuna.annaPaaNakyma();
                nakyma.luoEtusivuTyhja();
                close();
            }
              });
        hb2.getChildren().add(nappula2);
        alue.setTop(hb);
        alue.setCenter(gridPane);
          alue.setBottom(hb2);
           Scene scene = new Scene(alue);
          scene.getStylesheets().add("css/tyylit.css");
           setScene(scene);
        
        this.setOnCloseRequest(new EventHandler<WindowEvent>() {

            public void handle(WindowEvent we) {

            
                    Platform.exit();


            }
        });
        
    }
    
}
