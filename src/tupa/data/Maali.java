package tupa.data;

import java.io.Serializable;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Marianne
 */
public class Maali implements Serializable {

    private static int laskuri;
    private int id;
    private Pelaaja maalintekija;
    private Pelaaja syottaja;
    private Ottelu ottelu;
    private int aika;

    //taulukkoattribuutut
    private transient IntegerProperty taulukkoaika = new SimpleIntegerProperty();
    private transient StringProperty taulukkomaalintekija = new SimpleStringProperty();
    private transient StringProperty taulukkosyottaja = new SimpleStringProperty();
    private transient StringProperty taulukkojoukkue = new SimpleStringProperty();

    public Maali() {
       
        id = laskuri+1;
    }

    public Maali(Ottelu ottelu) {
        this.ottelu = ottelu;
        
        id = laskuri+1;
    }

    public void asetaID(int id) {
        this.id = id;
    }

    public int annaID() {
        return id;
    }
  public void vahennaLaskuria(){
        laskuri--;
    }
    public void asetaTiedot(int aika, Pelaaja maalintekija, Pelaaja syottaja) {
        this.aika = aika;

        this.maalintekija = maalintekija;
        this.maalintekija.annaKaikkiMaalit().add(this);

        this.syottaja = syottaja;
        this.syottaja.annaKaikkiMaalit().add(this);

        this.ottelu.annaMaalit().add(this);
    }

    public void asetaAika(int aika) {
        this.aika = aika;
    }

    public int annaAika() {
        return aika;
    }

    public Pelaaja annaMaalinTekija() {

        return maalintekija;
    }

    public Pelaaja annaSyottaja() {

        return syottaja;
    }

    public void asetaMaalinTekija(Pelaaja maalintekija) {

        this.maalintekija = maalintekija;
        this.maalintekija.annaKaikkiMaalit().add(this);
    }

    public void asetaSyottaja(Pelaaja syottaja) {

        this.syottaja = syottaja;
        this.syottaja.annaKaikkiMaalit().add(this);
    }

    public Ottelu annaOttelu() {
        return ottelu;
    }

    public StringProperty taulukkomaalintekijaProperty() {
        return taulukkomaalintekija;
    }

    public void asetaTaulukkomaalintekija() {
        if(annaMaalinTekija() != null)
        this.taulukkomaalintekija = new SimpleStringProperty(this.annaMaalinTekija().toString());
    }

    public StringProperty taulukkosyottajaProperty() {
        return taulukkosyottaja;
    }

    public void asetaTaulukkosyottaja() {
        if(annaSyottaja() != null){
                    this.taulukkosyottaja = new SimpleStringProperty(this.annaSyottaja().toString());
        }

    }

    public StringProperty taulukkojoukkueProperty() {
        return taulukkojoukkue;
    }

    public void asetaTaulukkojoukkue() {

    if(annaMaalinTekija() != null)
                this.taulukkojoukkue = new SimpleStringProperty(this.annaMaalinTekija().annaJoukkue().toString());
            
        

    }

    public IntegerProperty taulukkoaikaProperty() {
        return taulukkoaika;
    }

    public void asetaTaulukkoaika() {
        this.taulukkoaika = new SimpleIntegerProperty(this.annaAika());
    }

    public int annaLaskuri() {
        return laskuri;
    }

    public void asetaLaskuri(int laskuri) {
        this.laskuri = laskuri;
    }
       public void kasvataLaskuria(){
        laskuri++;
    }
    
}
