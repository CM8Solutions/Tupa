package tupa.data;
/**
 * Luokkien Pelaaja, Toimihenkilo ja Tuomari yliluokka.
 * 
 * @author Marianne
 * @see Pelaaja
 * @see Toimihenkilo
 * @see Tuomari
 */
public class Henkilo extends Kohde implements Comparable<Henkilo> {


    private String etunimi;  
    private String sukunimi;

    public Henkilo() {

    }

    public Henkilo(String etunimi, String sukunimi, String nimi) {
        super(nimi);
        this.etunimi = etunimi;
        this.sukunimi = sukunimi;

    }

    public Henkilo(String nimi) {
        super(nimi);
    }

    public void asetaEtuNimi(String etu) {
        etunimi = etu;
    }

    public void asetaSukuNimi(String suku) {

        sukunimi = suku;

    }

    public void asetaKokoNimi(String etu, String suku) {
        etunimi = etu;
        sukunimi = suku;

    }

    public String annaEtuNimi() {

        return etunimi;

    }

    public String annaSukuNimi() {

        return sukunimi;

    }

    public String annaKokoNimi() {
        return (etunimi + " " + sukunimi);
    }

    public int compareTo(Henkilo henkilo) {
        int tulos = 0;
        if (henkilo.annaSukuNimi() != null && sukunimi != null) {
            tulos = sukunimi.compareTo(henkilo.annaSukuNimi());
            if (tulos != 0) {
                return tulos;
            }
            if (henkilo.annaEtuNimi() != null && etunimi != null) {
                tulos = etunimi.compareTo(henkilo.annaEtuNimi());

            }
            return tulos;
        } else {
            return tulos;
        }
    }
}
