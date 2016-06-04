package tupa.data;

import java.io.Serializable;

/**
 * Luokkien Henkilo, Joukkue, Sarja ja Turnaus yliluokka.
 *
 * @author Marianne
 * @see Henkilo
 * @see Joukkue
 * @see Sarja
 * @see Turnaus
 */
public class Kohde implements Serializable {

    private String nimi;
    private int id;

    public Kohde() {

    }

    public Kohde(String nimi) {
        this.nimi = nimi;

    }

    public String toString() {
        return nimi;
    }

    public void asetaNimi(String nimi) {

        this.nimi = nimi;
    }

    public int annaID() {
        return id;
    }

    public void asetaID(int id) {
        this.id = id;
    }

}
