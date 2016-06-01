package tupa.data;

import java.util.ArrayList;
import java.util.List;
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
    //taulukkoattribuutit

    private transient StringProperty taulukkonimi = new SimpleStringProperty();
    private transient StringProperty taulukkoluomispvm = new SimpleStringProperty();

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

    public StringProperty taulukkoluomispvmProperty() {
        return taulukkoluomispvm;
    }

    public void asetaTaulukkoluomispvm() {

        this.taulukkoluomispvm = new SimpleStringProperty(this.annaLuomispvm());
    }

}
