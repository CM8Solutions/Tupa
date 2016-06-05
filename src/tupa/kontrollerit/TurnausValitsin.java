package tupa.kontrollerit;

import com.sun.prism.impl.Disposer;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;
import tupa.Tupa;
import tupa.data.Turnaus;
import tupa.data.Yhteys;

/**
 * Luokka, joka muodostaa näkymän, joissa voi valita sen turnauksen, jonka
 * haluaa avata.
 *
 * @author Marianne
 * @see Taulukko
 */
public class TurnausValitsin {

    private Tupa ikkuna;
    private Connection con = null;
    private Statement st = null;
    private Statement st2 = null;
    private Yhteys yhteys = new Yhteys();
    private String sql = "";
    private String sql2 = "";

    public TurnausValitsin() {

    }

    public TurnausValitsin(Tupa ikkuna) {
        this.ikkuna = ikkuna;
    }

    public void annaTurnausLuettelo() throws SQLException {

        try {
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

            TableView taulukko = new TableView();

            taulukko.setPlaceholder(new Label("Ei tallennettuja turnauksia"));
            taulukko.setId("my-table");
            List<Turnaus> turnauslista = new ArrayList<>();
            con = yhteys.annaYhteys();
            st = con.createStatement();

            //ylläpitäjille (ei yleinen) näytetään vain omat turnaukset
            if (ikkuna.annaTaso() == 1 || ikkuna.annaTaso() == 2) {
                sql = "SELECT * FROM turnaus, kayttajan_turnaus WHERE turnaus.tupaid = kayttajan_turnaus.turnaus_id AND kayttajan_turnaus.kayttaja_id = '" + ikkuna.annaKayttajaID() + "'";

                ResultSet turnaukset = st.executeQuery(sql);

                while (turnaukset.next()) {
                    String nimi = turnaukset.getString("nimi");
                    String luomispvm = turnaukset.getString("luomispvm");
                    int id = turnaukset.getInt("tupaid");

                    Turnaus turnaus = new Turnaus();
                    turnaus.asetaID(id);
                    turnaus.asetaNimi(nimi);

                    turnaus.asetaLuomispvm(luomispvm);
                    turnaus.asetaTaulukkonimi();
                    DateFormat muoto = new SimpleDateFormat("dd.MM.yyyy", Locale.ENGLISH);
                    Date paiva_date = muoto.parse(luomispvm);

                    turnaus.asetaLuomispvmDate(paiva_date);

                    turnauslista.add(turnaus);
                }

            } //yleiselle ylläpitäjälle ja kirjautumattomille käyttäjille näytetään kaikki järjestelmässä olevat turnaukset
            else {
                sql = "SELECT * FROM turnaus";

                ResultSet turnaukset = st.executeQuery(sql);

                while (turnaukset.next()) {
                    String nimi = turnaukset.getString("nimi");
                    String luomispvm = turnaukset.getString("luomispvm");
                    int id = turnaukset.getInt("tupaid");

                    Turnaus turnaus = new Turnaus();
                    turnaus.asetaID(id);

                    turnaus.asetaNimi(nimi);

                    turnaus.asetaLuomispvm(luomispvm);
                    turnaus.asetaTaulukkonimi();
                    DateFormat muoto = new SimpleDateFormat("dd.MM.yyyy", Locale.ENGLISH);
                    Date paiva_date = muoto.parse(luomispvm);

                    turnaus.asetaLuomispvmDate(paiva_date);

                    turnauslista.add(turnaus);
                }
            }

            ObservableList<Turnaus> data
                    = FXCollections.observableArrayList(turnauslista);

            TableColumn nimi = new TableColumn("Turnaus");
            TableColumn luomispvm = new TableColumn("Lisätty");
            TableColumn col_action = new TableColumn<>("Poista");
            nimi.setMinWidth(180);
            luomispvm.setMinWidth(150);
            nimi.setCellValueFactory(new PropertyValueFactory<Turnaus, String>("taulukkonimi"));
            luomispvm.setCellValueFactory(new PropertyValueFactory<Turnaus, String>("taulukkoluomispvmstring"));

            if (ikkuna.annaTaso() == 3 || ikkuna.annaTaso() == 2) {
                taulukko.getColumns().addAll(nimi, luomispvm, col_action);
            } else {
                taulukko.getColumns().addAll(nimi, luomispvm);
            }

            taulukko.setItems(data);
            luomispvm.setSortType(TableColumn.SortType.DESCENDING);
            taulukko.getSortOrder().add(luomispvm);

            taulukko.setFixedCellSize(25);

            if (taulukko.getItems().size() == 0) {
                taulukko.prefHeightProperty().bind(taulukko.fixedCellSizeProperty().multiply(Bindings.size(taulukko.getItems()).add(2)));
            } else {
                taulukko.prefHeightProperty().bind(taulukko.fixedCellSizeProperty().multiply(Bindings.size(taulukko.getItems()).add(1.1)));
            }

            col_action.setCellValueFactory(
                    new Callback<TableColumn.CellDataFeatures<Disposer.Record, Boolean>, ObservableValue<Boolean>>() {

                @Override
                public ObservableValue<Boolean> call(TableColumn.CellDataFeatures<Disposer.Record, Boolean> p) {
                    return new SimpleBooleanProperty(p.getValue() != null);
                }
            });

            col_action.setCellFactory(
                    new Callback<TableColumn<Disposer.Record, Boolean>, TableCell<Disposer.Record, Boolean>>() {

                @Override
                public TableCell<Disposer.Record, Boolean> call(TableColumn<Disposer.Record, Boolean> p) {

                    return new PoistoSoluTurnaus(data, ikkuna, stage);

                }

            });

            taulukko.minHeightProperty().bind(taulukko.prefHeightProperty());
            taulukko.maxHeightProperty().bind(taulukko.prefHeightProperty());

            taulukko.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

            taulukko.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
                Turnaus turnaus = (Turnaus) newSelection;

                Kirjautuminen kirjautuja = new Kirjautuminen(turnaus, ikkuna);
                kirjautuja.luoKirjautuminen();

                stage.close();
            });

            hbox2.setAlignment(Pos.CENTER);
            hbox2.getChildren().addAll(taulukko);
            vbox.getChildren().addAll(hbox1, hbox2);

            sb.setContent(vbox);
            alue.setCenter(sb);

            Scene sceneV = new Scene(alue);
            sceneV.getStylesheets().add("css/tyylit.css");
            stage.setTitle("TUPA - Tulospalvelu");
            stage.setScene(sceneV);
            stage.show();

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

}
