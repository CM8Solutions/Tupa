package tupa.data;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Marianne
 */
public class Turnaus extends Kohde {

    private static int turLaskuri;
    private List<Sarja> sarjat = new ArrayList<>();
    private List<Tuomari> tuomarit = new ArrayList<>();
    private String luomispvm = "";
    private Date luomispvmdate;

    //taulukkoattribuutit
    private transient StringProperty taulukkonimi = new SimpleStringProperty();
    private transient StringProperty taulukkoluomispvmstring = new SimpleStringProperty();
    private transient ObjectProperty<Date> taulukkoluomispvmdate = new SimpleObjectProperty();

    public Turnaus() {

    }

    public List<Sarja> annaSarjat() {
        return sarjat;
    }

    public List<Tuomari> annaTuomarit() {
        return tuomarit;
    }

    public void kasvataLaskuria() {
        turLaskuri++;
    }

    public void vahennaLaskuria() {
        turLaskuri--;
    }

    public int annaLaskuri() {
        return turLaskuri;
    }

    public void asetaLaskuri(int laskuri) {
        this.turLaskuri = laskuri;
    }

    public void asetaLuomispvm(String luomispvm) {
        this.luomispvm = luomispvm;
    }

    public String annaLuomispvm() {
        return luomispvm;
    }

    public StringProperty taulukkonimiProperty() {
        return taulukkonimi;
    }

    public void asetaTaulukkonimi() {

        this.taulukkonimi = new SimpleStringProperty(this.toString());
    }

    public StringProperty taulukkoluomispvmstringProperty() {
        return taulukkoluomispvmstring;
    }

    public void asetaTaulukkoluomispvmstring() {

        this.taulukkoluomispvmstring = new SimpleStringProperty(getDate().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)));
    }

    public ObjectProperty taulukkoluomispvmadateProperty() {
        return taulukkoluomispvmdate;
    }

    public void asetaTaulukkoluomispvmdate() {

        this.taulukkoluomispvmdate = new SimpleObjectProperty(this.annaLuomispvmDate());
    }

    public LocalDate getDate() {

        return luomispvmdate == null ? LocalDate.now() : luomispvmdate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

    }

    public Date annaLuomispvmDate() {
        return luomispvmdate;
    }

    public void asetaLuomispvmDate(Date luomispvm) {
        this.luomispvmdate = luomispvm;
        asetaTaulukkoluomispvmstring();
    }
}
