package tupa.kontrollerit;

import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import tupa.Tupa;
import tupa.data.Joukkue;
import tupa.data.Toimihenkilo;
import tupa.data.Turnaus;
import tupa.nakymat.PaaNakyma;
import tupa.nakymat.ToimariNakyma;

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
    private Statement st2 = null;
    private Statement st3 = null;
    private Statement st4 = null;
    private String sql = "";

    public Kirjautuminen() {

    }

    public Kirjautuminen(Tupa ikkuna) {
        this.ikkuna = ikkuna;
        this.turnaus = (Turnaus) ikkuna.annaTurnaus();
    }

    public Kirjautuminen(Turnaus turnaus, Tupa ikkuna) {
        this.turnaus = turnaus;
        this.ikkuna = ikkuna;
    }

    public void luoKirjautuminen() {

        int turnaus_id = turnaus.annaID();

        try {
            int kayttaja_id = ikkuna.annaKayttajaID();

            con = yhteys.annaYhteys();
            st2 = con.createStatement();

            if(ikkuna.annaTaso() !=0){
                
                //joukkueen ylläpitäjälle joukkueen tiedot
                if(ikkuna.annaTaso() == 1){
                      sql = "SELECT DISTINCT * FROM kayttajan_turnaus WHERE turnaus_id = '" + turnaus_id + "' AND kayttaja_id = '" + kayttaja_id + "'";

                    ResultSet haetut_rivit = st2.executeQuery(sql);
                    int laskuri = 0;
                    int joukkue_id = 0;
                    while (haetut_rivit.next()) {
                        laskuri++;
                        joukkue_id = haetut_rivit.getInt("joukkue_id");
                    }

                ikkuna.asetaJoukkueID(joukkue_id);
                }

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

            } 
            
            else {
                Stage stage = new Stage();
                BorderPane alue = new BorderPane();
                alue.setPadding(new Insets(10, 50, 50, 50));
                stage.getIcons().add(new Image("kuvat/icon.png"));

                HBox hb = new HBox();
                hb.setPadding(new Insets(20, 20, 0, 30));

                GridPane gridPane = new GridPane();
                gridPane.setPadding(new Insets(20, 20, 0, 20));
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

                Text text = new Text("Anna turnaukseen " + turnaus.toString() + " liittyvä salasana.");
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
                            st3 = con.createStatement();

                            //haetaan ensin salt
                            sql = "SELECT DISTINCT * FROM turnauksen_salasana WHERE turnaus_id = '" + turnaus_id + "'";

                            ResultSet haetut_saltit = st.executeQuery(sql);
                            String salt = "";
                            while (haetut_saltit.next()) {
                                salt = haetut_saltit.getString("salt");

                            }
                            if (salt != null) {
                        //muutetaan tavuiksi
                                  byte[] salt_byte = salt.getBytes();

                                //hashataan tällä syötetty salasana, jota verrataan tietokantaan tallennetuun häshii..
                                KeySpec spec = new PBEKeySpec(syotetty_salasana.toCharArray(), salt_byte, 65536, 128);
                                SecretKeyFactory f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
                                byte[] hash = f.generateSecret(spec).getEncoded();
                                Base64.Encoder enc = Base64.getEncoder();
                       
                                 sql = "SELECT DISTINCT * FROM turnauksen_salasana WHERE turnaus_id = ? AND salasana = ?";

                        PreparedStatement stmt2 = con.prepareStatement(sql);
                        stmt2.setInt(1, turnaus_id);
                        stmt2.setString(2, enc.encodeToString(hash));

                        ResultSet haetut_rivit = stmt2.executeQuery();
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


                    }
    
                            
                            
                            
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

    public void luoTurnauksenSalasananSyotto() {
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

                if (syotetty_salasana.equals(syotetty_salasana2)) {

                    try {

                        con = yhteys.annaYhteys();
                        st = con.createStatement();

                        //ennen vientiä kantaan kryptataan salasana
                        byte[] salt = new byte[16];
                        SecureRandom random = new SecureRandom();
                        random.nextBytes(salt);
                        Base64.Encoder enc = Base64.getEncoder();

                        //tallennetaan ensin salt
                        sql = "INSERT INTO turnauksen_salasana (turnaus_id, salt) VALUES(?, ?)";

                        PreparedStatement stmt = con.prepareStatement(sql);
                        stmt.setInt(1, turnaus_id);
                        stmt.setString(2, enc.encodeToString(salt));
                        stmt.executeUpdate();

                        //sitten haetaan ko salt tk:sta 
                        sql = "SELECT salt FROM turnauksen_salasana WHERE turnaus_id = ?";
                        PreparedStatement stmt2 = con.prepareStatement(sql);
                        stmt2.setInt(1, turnaus_id);

                        ResultSet haetut_saltit = stmt2.executeQuery();
                        String haettu_salt = "";

                        while (haetut_saltit.next()) {

                            haettu_salt = haetut_saltit.getString("salt");

                        }
                        //muutetaan string tavuiksi
                        byte[] salt_byte = haettu_salt.getBytes();

                        //tällä nyt häshätään syötetty salasana
                        KeySpec spec = new PBEKeySpec(syotetty_salasana.toCharArray(), salt_byte, 65536, 128);
                        SecretKeyFactory f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
                        byte[] hash = f.generateSecret(spec).getEncoded();

                        sql = "UPDATE turnauksen_salasana SET salasana = ? WHERE turnaus_id = ?";

                        PreparedStatement stmt3 = con.prepareStatement(sql);

                        stmt3.setString(1, enc.encodeToString(hash));
                        stmt3.setInt(2, turnaus_id);
                        stmt3.executeUpdate();

                        int kayttaja_id = ikkuna.annaKayttajaID();

                        //turnauksen luoja saa kaikki oikeudet
                        st.executeUpdate("INSERT INTO kayttajan_turnaus  (turnaus_id, kayttaja_id)  VALUES ('" + turnaus_id + "', '" + kayttaja_id + "')");

                     
                        
                        Tiedottaja tiedottaja = new Tiedottaja(ikkuna);
                        tiedottaja.annaIlmoitus("Turnauksen " + turnaus.toString() + " salasana tallennettu!");
                        Tallennus tallenna = new Tallennus(ikkuna);
                        tallenna.suoritaTallennus();
                        tiedottaja.kirjoitaLoki("Turnaus tallennettu.");
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

                } else {

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

    public void luoHallintaLisaysToimari(Toimihenkilo toimari) {
        int toimari_id = toimari.annaID();
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

        Label tunnus = new Label("Käyttäjätunnus");
        TextField ts = new TextField();
        Label salasana = new Label("Salasana");
        PasswordField ss = new PasswordField();
        Label salasana2 = new Label("Salasana uudelleen");
        PasswordField ss2 = new PasswordField();

        Button nappula = new Button("Tallenna");
       Button peruuta = new Button("Peruuta");
        
               nappula.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
              
                PaaNakyma nakyma = ikkuna.annaPaaNakyma();
                nakyma.luoEtusivuTyhja();
                
            }
              
        });
        
        Label viesti = new Label();

        gridPane.add(tunnus, 0, 0);
        gridPane.add(ts, 1, 0);
        gridPane.add(salasana, 0, 1);
        gridPane.add(ss, 1, 1);
        gridPane.add(salasana2, 0, 2);
        gridPane.add(ss2, 1, 2);
        gridPane.add(nappula, 1, 3);
          gridPane.add(peruuta, 2, 3);
        gridPane.add(viesti, 1, 4);

        DropShadow dropShadow = new DropShadow();
        dropShadow.setOffsetX(5);
        dropShadow.setOffsetY(5);

        Text text = new Text("Valitse toimihenkilölle " + toimari.toString() + " käyttäjätunnus ja salasana.");
        text.setFont(Font.font("Papyrus", FontWeight.BOLD, 20));
        text.setEffect(dropShadow);

        hb.getChildren().add(text);

        alue.setStyle("-fx-background-color:  linear-gradient(to bottom, #00ff00, 	#ccffcc)");

        nappula.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String syotetty_tunnus = ts.getText().toString();
                String syotetty_salasana = ss.getText().toString();
                String syotetty_salasana2 = ss2.getText().toString();

                int turnaus_id = turnaus.annaID();
                int joukkue_id = toimari.annaJoukkue().annaID();
                Joukkue joukkue = toimari.annaJoukkue();

                if (syotetty_salasana.equals(syotetty_salasana2)) {

                    try {

                        con = yhteys.annaYhteys();
                        st = con.createStatement();
                        st4 = con.createStatement();

                        //tarkistetaan, onko tunnus jo tietokannassa
                        sql = "SELECT * FROM kayttaja WHERE tunnus = ?";

                        PreparedStatement stmt8 = con.prepareStatement(sql);
                        stmt8.setString(1, syotetty_tunnus);

                        ResultSet haetut_tunnukset = stmt8.executeQuery();
                        int laskuri = 0;

                        while (haetut_tunnukset.next()) {
                            laskuri++;
                        }

                        if (laskuri == 0) {
                            // tunnus on vapaa

                            //ennen vientiä kantaan kryptataan salasana ja siivotaan syötteet
                            byte[] salt = new byte[16];
                            SecureRandom random = new SecureRandom();
                            random.nextBytes(salt);
                            Base64.Encoder enc = Base64.getEncoder();

                            //tallennetaan ensin salt
                            sql = "INSERT INTO kayttaja (tunnus, salt, taso) VALUES(?, ?, 1)";

                            PreparedStatement stmt = con.prepareStatement(sql);
                            stmt.setString(1, syotetty_tunnus);
                            stmt.setString(2, enc.encodeToString(salt));
                            stmt.executeUpdate();

                            //sitten haetaan ko salt tk:sta 
                            sql = "SELECT salt FROM kayttaja WHERE tunnus = ?";
                            PreparedStatement stmt2 = con.prepareStatement(sql);
                            stmt2.setString(1, syotetty_tunnus);

                            ResultSet haetut_saltit = stmt2.executeQuery();
                            String haettu_salt = "";

                            while (haetut_saltit.next()) {

                                haettu_salt = haetut_saltit.getString("salt");

                            }
                            //muutetaan string tavuiksi
                            byte[] salt_byte = haettu_salt.getBytes();

                            //tällä nyt häshätään syötetty salasana
                            KeySpec spec = new PBEKeySpec(syotetty_salasana.toCharArray(), salt_byte, 65536, 128);
                            SecretKeyFactory f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
                            byte[] hash = f.generateSecret(spec).getEncoded();

                            sql = "UPDATE kayttaja SET salasana = ? WHERE tunnus = ?";

                            PreparedStatement stmt3 = con.prepareStatement(sql);

                            stmt3.setString(1, enc.encodeToString(hash));
                            stmt3.setString(2, syotetty_tunnus);
                            stmt3.executeUpdate();

                            //haetaan tallennettu id
                            ResultSet kayttajat = st.executeQuery("SELECT * FROM  kayttaja");
                            int kayttaja_id = 0;
                            while (kayttajat.next()) {
                                kayttaja_id = kayttajat.getInt("id");
                            }

                            //asetetaan toimihenkilölle oikeudet turnaukseen liittyen
                            st.executeUpdate("INSERT INTO kayttajan_turnaus (kayttaja_id, joukkue_id, turnaus_id) VALUES('" + kayttaja_id + "', '" + joukkue_id + "', '" + turnaus_id + "')");
                            toimari.asetaHallinta(1);
                            toimari.asetaHallintaID(kayttaja_id);

                            PaaNakyma paanakyma = ikkuna.annaPaaNakyma();
                            ToimariNakyma toimarinakyma = paanakyma.annaToimarinakyma();
                            toimarinakyma.luoToimariSivu(toimari);
                            ikkuna.asetaMuutos(true);

                            Tiedottaja tiedottaja = new Tiedottaja(ikkuna);
                            tiedottaja.annaIlmoitus("Toimihenkilö " + toimari.toString() + " lisätty joukkueen " + joukkue.toString() + " ylläpitäjäksi.");

                            stage.close();

                        } else {
                            //tunnus on jo käytössä
                            viesti.setText("Käyttäjätunnus on varattu!");
                            viesti.setTextFill(Color.RED);
                            ss.setText("");
                            ss2.setText("");
                        }

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

                } else {

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

    public void luoYleinenHallintaLisays() {

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

        Label tunnus = new Label("Käyttäjätunnus");
        TextField ts = new TextField();
        Label salasana = new Label("Salasana");
        PasswordField ss = new PasswordField();
        Label salasana2 = new Label("Salasana uudelleen");
        PasswordField ss2 = new PasswordField();

        Button nappula = new Button("Tallenna");
        Button peruuta = new Button("Peruuta");
        
               nappula.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
              
                PaaNakyma nakyma = ikkuna.annaPaaNakyma();
                nakyma.luoEtusivuTyhja();
                
            }
              
        });
        
        
        Label viesti = new Label();

        gridPane.add(tunnus, 0, 0);
        gridPane.add(ts, 1, 0);
        gridPane.add(salasana, 0, 1);
        gridPane.add(ss, 1, 1);
        gridPane.add(salasana2, 0, 2);
        gridPane.add(ss2, 1, 2);
        gridPane.add(nappula, 1, 3);
         gridPane.add(peruuta, 2, 3);
        gridPane.add(viesti, 1, 4);

        DropShadow dropShadow = new DropShadow();
        dropShadow.setOffsetX(5);
        dropShadow.setOffsetY(5);

        Text text = new Text("Valitse käyttäjälle käyttäjätunnus ja salasana.");
        text.setFont(Font.font("Papyrus", FontWeight.BOLD, 20));
        text.setEffect(dropShadow);

        hb.getChildren().add(text);

        alue.setStyle("-fx-background-color:  linear-gradient(to bottom, #00ff00, 	#ccffcc)");

        nappula.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String syotetty_tunnus = ts.getText().toString();
                String syotetty_salasana = ss.getText().toString();
                String syotetty_salasana2 = ss2.getText().toString();

                int turnaus_id = turnaus.annaID();

                if (syotetty_salasana.equals(syotetty_salasana2)) {

                    try {

                        con = yhteys.annaYhteys();
                        st = con.createStatement();
                        st4 = con.createStatement();

                        //tarkistetaan, onko tunnus jo tietokannassa
                        sql = "SELECT * FROM kayttaja WHERE tunnus = ?";

                        PreparedStatement stmt8 = con.prepareStatement(sql);
                        stmt8.setString(1, syotetty_tunnus);

                        ResultSet haetut_tunnukset = stmt8.executeQuery();
                        int laskuri = 0;

                        while (haetut_tunnukset.next()) {
                            laskuri++;
                        }

                        if (laskuri == 0) {
                            // tunnus on vapaa

                            //ennen vientiä kantaan kryptataan salasana ja siivotaan syötteet
                            byte[] salt = new byte[16];
                            SecureRandom random = new SecureRandom();
                            random.nextBytes(salt);
                            Base64.Encoder enc = Base64.getEncoder();

                            //tallennetaan ensin salt
                            sql = "INSERT INTO kayttaja (tunnus, salt, taso) VALUES(?, ?, 2)";

                            PreparedStatement stmt = con.prepareStatement(sql);
                            stmt.setString(1, syotetty_tunnus);
                            stmt.setString(2, enc.encodeToString(salt));
                            stmt.executeUpdate();

                            //sitten haetaan ko salt tk:sta 
                            sql = "SELECT salt FROM kayttaja WHERE tunnus = ?";
                            PreparedStatement stmt2 = con.prepareStatement(sql);
                            stmt2.setString(1, syotetty_tunnus);

                            ResultSet haetut_saltit = stmt2.executeQuery();
                            String haettu_salt = "";

                            while (haetut_saltit.next()) {

                                haettu_salt = haetut_saltit.getString("salt");

                            }
                            //muutetaan string tavuiksi
                            byte[] salt_byte = haettu_salt.getBytes();

                            //tällä nyt häshätään syötetty salasana
                            KeySpec spec = new PBEKeySpec(syotetty_salasana.toCharArray(), salt_byte, 65536, 128);
                            SecretKeyFactory f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
                            byte[] hash = f.generateSecret(spec).getEncoded();

                            sql = "UPDATE kayttaja SET salasana = ? WHERE tunnus = ?";

                            PreparedStatement stmt3 = con.prepareStatement(sql);

                            stmt3.setString(1, enc.encodeToString(hash));
                            stmt3.setString(2, syotetty_tunnus);
                            stmt3.executeUpdate();

                          

                            Tiedottaja tiedottaja = new Tiedottaja(ikkuna);
                            tiedottaja.annaIlmoitus("Käyttäjä lisätty järjestelmään onnistuneesti.");

                            stage.close();

                        } else {
                            //tunnus on jo käytössä
                            viesti.setText("Käyttäjätunnus on varattu!");
                            viesti.setTextFill(Color.RED);
                            ss.setText("");
                            ss2.setText("");
                        }

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

                } else {

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
