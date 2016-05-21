package tupa.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Marianne
 */
public class Kokoonpano implements Serializable {

    private static int laskuri;
    private int id;
    private List<Pelaaja> pelaajat = new ArrayList<Pelaaja>();
    private Joukkue joukkue;
    private Ottelu ottelu;

    public Kokoonpano() {
        laskuri++;
        id = laskuri;
    }

    public Kokoonpano(Ottelu ottelu, Joukkue joukkue) {
        laskuri++;
        id = laskuri;
        this.ottelu = ottelu;
        this.joukkue = joukkue;
    }

    public List<Pelaaja> annaPelaajat() {
        return pelaajat;
    }

    public void asetaPelaaja(Pelaaja pelaaja) {
        this.pelaajat.add(pelaaja);
     
    }

    public Joukkue annaJoukkue() {
        return joukkue;
    }

    public Ottelu annaOttelu() {
        return ottelu;
    }

    public int annaID() {

        return id;
    }

    public void asetaID(int id) {
        this.id = id;
    }

}
