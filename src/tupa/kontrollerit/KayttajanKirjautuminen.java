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
import java.security.spec.KeySpec;
import java.sql.PreparedStatement;
import java.util.Base64;
import javafx.geometry.Pos;
import javafx.scene.image.ImageView;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import tupa.Tupa;
import tupa.nakymat.PaaNakyma;
import tupa.data.Yhteys;

/**
 * Luokka, joka muodostaa käynnistyksen yhteydessä avautuvan kirjautumisnäkymän
 * ja siihen liittyvän toiminnallisuuden.
 *
 * @author Marianne
 */
public class KayttajanKirjautuminen extends Stage {

    private Yhteys yhteys = new Yhteys();
    private Connection con = null;
    private Statement st = null;
    private Statement st2 = null;
    private String sql = "";
    private Tupa ikkuna;

    public KayttajanKirjautuminen(Stage owner, Tupa ikkuna) {

        super();
        this.ikkuna = ikkuna;
        initOwner(owner);
        setTitle("TUPA \t - \t Tulospalvelu");

        this.getIcons().add(new Image("kuvat/icon.png"));
        BorderPane alue = new BorderPane();
        alue.setPadding(new Insets(10, 30, 50, 30));

        HBox hb = new HBox();
        hb.setPadding(new Insets(0, 20, 0, 10));

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

        HBox otsikkorivi = new HBox();
        otsikkorivi.setSpacing(60);

        HBox tekstiboxi = new HBox();
        tekstiboxi.setAlignment(Pos.CENTER);

        HBox kuvaboxi = new HBox();
        kuvaboxi.setAlignment(Pos.TOP_LEFT);

        ImageView selectedImage = new ImageView();
        Image image1 = new Image("kuvat/login.png");
        selectedImage.setImage(image1);
        selectedImage.setFitHeight(120);
        selectedImage.setFitWidth(120);

        Label text = new Label("Kirjaudu TUPA-tulospalveluun antamalla käyttäjätunnus ja salasana.");
        text.setFont(Font.font("Papyrus", FontWeight.BOLD, 20));
        text.setEffect(dropShadow);

        tekstiboxi.getChildren().addAll(text);
        kuvaboxi.getChildren().addAll(selectedImage);
        otsikkorivi.getChildren().addAll(tekstiboxi, kuvaboxi);
        hb.getChildren().add(otsikkorivi);

        alue.setStyle("-fx-background-color:  linear-gradient(to bottom, #00ff00, 	#ccffcc)");

        VBox hb3 = new VBox();
        hb3.setPadding(new Insets(0, 20, 30, 0));

        LinkkiLabel unohdus = new LinkkiLabel();
        unohdus.setText("Unohtunut tunnus/salasana?");
        unohdus.setFont(Font.font("Papyrus", FontWeight.BOLD, 14));
        unohdus.setEffect(dropShadow);
        unohdus.setStyle("-fx-underline: true");
        unohdus.linkkiaKlikattu();
        hb3.getChildren().add(unohdus);

        gridPane.add(hb3, 0, 4);

        nappula.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                String syotetty_tunnus = ts.getText().toString();
                String syotetty_salasana = ss.getText().toString();

                try {

                    con = yhteys.annaYhteys();
                    st = con.createStatement();
                    st2 = con.createStatement();

                    sql = "SELECT DISTINCT * FROM kayttaja WHERE tunnus = ?";

                    PreparedStatement stmt = con.prepareStatement(sql);
                    stmt.setString(1, syotetty_tunnus);

                    ResultSet tunnukset = stmt.executeQuery();

                    //haetaan ensin ko tunnuksen salt
                    String salt = "";

                    while (tunnukset.next()) {

                        salt = tunnukset.getString("salt");

                    }

                    if (salt != null) {
                        //muutetaan tavuiksi
                        byte[] salt_byte = salt.getBytes();

                        //hashataan tällä syötetty salasana, jota verrataan tietokantaan tallennetuun häshii..
                        KeySpec spec = new PBEKeySpec(syotetty_salasana.toCharArray(), salt_byte, 65536, 128);
                        SecretKeyFactory f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
                        byte[] hash = f.generateSecret(spec).getEncoded();
                        Base64.Encoder enc = Base64.getEncoder();

                        sql = "SELECT DISTINCT * FROM kayttaja WHERE tunnus = ? AND salasana = ?";

                        PreparedStatement stmt2 = con.prepareStatement(sql);
                        stmt2.setString(1, syotetty_tunnus);
                        stmt2.setString(2, enc.encodeToString(hash));

                        ResultSet haetut_rivit = stmt2.executeQuery();
                        int laskuri = 0;
                        int taso = 0;
                        int kayttaja_id = 0;
                        String tunnus = "";
                        while (haetut_rivit.next()) {
                            laskuri++;
                            taso = haetut_rivit.getInt("taso");
                            kayttaja_id = haetut_rivit.getInt("id");
                            tunnus = haetut_rivit.getString("tunnus");
                        }

                        if (laskuri == 1) {

                            ikkuna.asetaTaso(taso);
                            ikkuna.asetaKayttajaID(kayttaja_id);
                            Tiedottaja tiedottaja = new Tiedottaja(ikkuna);

                            tiedottaja.kirjoitaLoki("Käyttäjä " + tunnus + " kirjautui sisään.");

                            close();
                        } else {

                            viesti.setText("Väärä tunnus tai salasana.");
                            viesti.setTextFill(Color.RED);

                        }

                        ss.setText("");

                    } else {

                        //Tällä hetkellä yleisen ylläpitäjän salasanaa ei ole häshätty
                        sql = "SELECT DISTINCT * FROM kayttaja WHERE tunnus = '" + syotetty_tunnus + "' AND salasana = '" + syotetty_salasana + "'";
                        ResultSet haetut_rivit = st.executeQuery(sql);
                        int laskuri = 0;
                        int taso = 0;
                        int kayttaja_id = 0;
                        String tunnus = "";
                        while (haetut_rivit.next()) {
                            laskuri++;
                            taso = haetut_rivit.getInt("taso");
                            kayttaja_id = haetut_rivit.getInt("id");
                            tunnus = haetut_rivit.getString("tunnus");
                        }

                        if (laskuri == 1) {

                            ikkuna.asetaTaso(taso);
                            ikkuna.asetaKayttajaID(kayttaja_id);
                            Tiedottaja tiedottaja = new Tiedottaja(ikkuna);
                            tiedottaja.kirjoitaLoki("Käyttäjä " + tunnus + " kirjautui sisään.");

                            close();
                        } else {

                            viesti.setText("Väärä tunnus tai salasana.");
                            viesti.setTextFill(Color.RED);

                        }

                        ss.setText("");

                    }

                } catch (SQLException se) {

                    Tiedottaja tiedottaja = new Tiedottaja();
                    tiedottaja.annaVirhe("" + se);
                } catch (Exception e) {

                    Tiedottaja tiedottaja = new Tiedottaja();
                    tiedottaja.annaVirhe("" + e);
                } finally {

                    try {
                        if (st != null) {
                            con.close();
                        }
                    } catch (SQLException se) {
                        Tiedottaja tiedottaja = new Tiedottaja();
                        tiedottaja.annaVirhe("" + se);
                    }
                    try {
                        if (con != null) {
                            con.close();
                        }
                    } catch (SQLException se) {
                        Tiedottaja tiedottaja = new Tiedottaja();
                        tiedottaja.annaVirhe("" + se);
                    }
                }

            }
        });

        VBox hb2 = new VBox();
        hb2.setPadding(new Insets(10, 20, 0, 20));
        hb2.setSpacing(20);

        Text text2 = new Text("Voit myös jatkaa ohjelmaan kirjautumatta, jolloin käytössäsi ovat rajoitetuimmat oikeudet.");
        text2.setFont(Font.font("Papyrus", FontWeight.BOLD, 16));
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
