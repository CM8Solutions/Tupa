package tupa.data;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Marianne
 */
public class Turnaus extends Kohde {

    private static int turLaskuri;
    private List<Sarja> sarjat = new ArrayList<>();
    private List<Tuomari> tuomarit = new ArrayList<>();

    public Turnaus() {
        super("Uusi turnaus");
        turLaskuri++;
        asetaID(turLaskuri);
    }

    public List<Sarja> annaSarjat() {
        return sarjat;
    }

    public List<Tuomari> annaTuomarit() {
        return tuomarit;
    }

    public int annaLaskuri() {
        return turLaskuri;
    }

    public void asetaLaskuri(int laskuri) {
        this.turLaskuri = laskuri;
    }
}
