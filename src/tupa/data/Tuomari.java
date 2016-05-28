package tupa.data;

import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Marianne
 */
public class Tuomari extends Henkilo {

    //+henkilon ja kohteen attribuutit
    private int id_julkinen;
    private static int tuLaskuri;
    private Turnaus turnaus;

    private transient StringProperty taulukkonimi = new SimpleStringProperty();
    private transient StringProperty taulukkoturnaus = new SimpleStringProperty();
    private transient IntegerProperty taulukkojulkinen_id = new SimpleIntegerProperty();

    private List<TuomarinRooli> roolit = new ArrayList<>();

    public Tuomari() {

    }

    public Tuomari(String etunimi, String sukunimi) {
        super(etunimi, sukunimi, etunimi + " " + sukunimi);
    }

    public Tuomari(String nimi) {
        super(nimi);
    }

    public void kasvataLaskuria() {
        tuLaskuri++;
    }

    public void vahennaLaskuria() {
        tuLaskuri--;
    }

    public int annaLaskuri() {
        return tuLaskuri;
    }

    public void asetaLaskuri(int laskuri) {
        this.tuLaskuri = laskuri;
    }

    public int annaJulkinenId() {
        return id_julkinen;
    }

    public void asetaJulkinenId(int id) {
        this.id_julkinen = id;
    }

    public List<TuomarinRooli> annaTuomarinRoolit() {
        return roolit;
    }

    public void asetaTurnaus(Turnaus turnaus) {
        this.turnaus = turnaus;
    }

    public Turnaus annaTurnaus() {
        return turnaus;
    }

    public int annaTurnausID() {
        int id = this.annaTurnaus().annaID();
        return id;
    }

    public StringProperty taulukkonimiProperty() {
        return taulukkonimi;
    }

    public void asetaTaulukkonimi() {
        this.taulukkonimi = new SimpleStringProperty(this.toString());
    }

    public StringProperty taulukkoturnausProperty() {
        return taulukkoturnaus;
    }

    public void asetaTaulukkoturnaus() {
        this.taulukkoturnaus = new SimpleStringProperty(this.annaTurnaus().toString());
    }

    public void asetaTaulukkojulkinen_id() {
        this.taulukkojulkinen_id = new SimpleIntegerProperty(annaJulkinenId());
    }

    public IntegerProperty taulukkojulkinen_idProperty() {
        return taulukkojulkinen_id;
    }

}
