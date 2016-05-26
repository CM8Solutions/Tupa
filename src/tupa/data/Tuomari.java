package tupa.data;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Marianne
 */
public class Tuomari extends Henkilo {

    //+henkilon ja kohteen attribuutit
    private static int id_julkinen;
    private static int tuLaskuri;
    private Turnaus turnaus;

    private List<TuomarinRooli> roolit = new ArrayList<>();

    public Tuomari() {

    }

    public Tuomari(String etunimi, String sukunimi) {
        super(etunimi, sukunimi, etunimi + " " + sukunimi);
    }

    public Tuomari(String nimi) {
        super(nimi);
    }
   public void kasvataLaskuria(){
        tuLaskuri++;
    }
      public void vahennaLaskuria(){
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
}
