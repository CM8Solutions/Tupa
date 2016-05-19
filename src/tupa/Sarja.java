package tupa;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Marianne
 */
public class Sarja extends Kohde {

    //+kohteen attribuutit
    private static int saLaskuri;
    private Turnaus turnaus;

    private List<Ottelu> ottelut = new ArrayList<Ottelu>();
    private List<Joukkue> joukkueet = new ArrayList<Joukkue>();

    Sarja() {
        saLaskuri++;
        asetaID(saLaskuri);
    }

    Sarja(String nimi, Turnaus turnaus) {

        super(nimi);
        saLaskuri++;
        asetaID(saLaskuri);
        this.turnaus = turnaus;
    }

    public int annaLaskuri() {
        return saLaskuri;
    }

    public void asetaLaskuri(int laskuri) {
        this.saLaskuri = laskuri;
    }

    public Turnaus annaTurnaus() {
        return turnaus;
    }

    public void asetaTurnaus(Turnaus turnaus) {
        this.turnaus = turnaus;
    }

    public List<Ottelu> annaOttelut() {
        return ottelut;
    }

    public List<Joukkue> annaJoukkueet() {
        return joukkueet;
    }

}
