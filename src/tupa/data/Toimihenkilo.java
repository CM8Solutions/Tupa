package tupa.data;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Marianne
 */
public class Toimihenkilo extends Henkilo {

    private static int toLaskuri;
    private Joukkue joukkue;
    private String sposti = "";
    private String puh = "";
    private String rooli = "";
    //o=ei hallintaoikeuksia, 1=hallintaoikeudet
    private int hallinta;
    private int hallinta_id;

    private transient StringProperty taulukkosposti = new SimpleStringProperty();
    private transient StringProperty taulukkonimi = new SimpleStringProperty();
    private transient StringProperty taulukkoetunimi = new SimpleStringProperty();
    private transient StringProperty taulukkosukunimi = new SimpleStringProperty();
    private transient StringProperty taulukkopuh = new SimpleStringProperty();
    private transient StringProperty taulukkorooli = new SimpleStringProperty();

    public Toimihenkilo() {

    }

    public Toimihenkilo(String etunimi, String sukunimi) {
        super(etunimi, sukunimi, etunimi + " " + sukunimi);
    }

    public int annaLaskuri() {
        return toLaskuri;
    }

    public void vahennaLaskuria() {
        toLaskuri--;
    }
   
    public void asetaLaskuri(int laskuri) {
        this.toLaskuri = laskuri;
    }

    public int annaToimariMaara() {

        return toLaskuri;
    }

    public void asetaJoukkue(Joukkue joukkue) {
        this.joukkue = joukkue;
    }

    public void kasvataLaskuria() {
        toLaskuri++;
    }

    public Joukkue annaJoukkue() {
        return joukkue;
    }

    public void asetaSposti(String sposti) {
        this.sposti = sposti;

    }

    public String annaSposti() {
        return sposti;
    }

    public void asetaPuh(String puh) {
        this.puh = puh;
    }

    public String annaPuh() {
        return puh;
    }

    public void asetaRooli(String rooli) {
        this.rooli = rooli;
    }

    public String annaRooli() {
        return rooli;
    }

    public StringProperty taulukkonimiProperty() {
        return taulukkonimi;
    }

    public void asetaTaulukkonimi() {
        this.taulukkonimi = new SimpleStringProperty(this.annaKokoNimi());
    }

    public StringProperty taulukkoetunimiProperty() {
        return taulukkoetunimi;
    }

    public void asetaTaulukkoetunimi() {

        this.taulukkoetunimi = new SimpleStringProperty(this.annaEtuNimi());
    }

    public StringProperty taulukkosukunimiProperty() {
        return taulukkosukunimi;
    }

    public void asetaTaulukkosukunimi() {

        this.taulukkosukunimi = new SimpleStringProperty(this.annaSukuNimi());
    }

    public StringProperty taulukkorooliProperty() {
        return taulukkorooli;
    }

    public void asetaTaulukkorooli() {
        this.taulukkorooli = new SimpleStringProperty(this.annaRooli());
    }

    public StringProperty taulukkospostiProperty() {
        return taulukkosposti;
    }

    public void asetaTaulukkosposti() {
        this.taulukkosposti = new SimpleStringProperty(this.annaSposti());
    }

    public StringProperty taulukkopuhProperty() {
        return taulukkopuh;
    }

    public void asetaTaulukkopuh() {
        this.taulukkopuh = new SimpleStringProperty(this.annaPuh());
    }

    public void asetaHallinta(int arvo) {
        this.hallinta = arvo;
    }

    public int annaHallinta() {
        return hallinta;
    }

    public void asetaHallintaID(int arvo) {
        this.hallinta_id = arvo;
    }

    public int annaHallintaID() {
        return hallinta_id;
    }
}
