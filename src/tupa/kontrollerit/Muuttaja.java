package tupa.kontrollerit;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Vector;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TreeItem;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import tupa.Tupa;
import tupa.data.Henkilo;
import tupa.nakymat.PaaNakyma;
import tupa.nakymat.SarjaNakyma;
import tupa.data.Kohde;
import tupa.data.Turnaus;
import tupa.data.Sarja;
import tupa.data.Tuomari;
import tupa.data.Pelaaja;
import tupa.data.Toimihenkilo;
import tupa.data.Joukkue;
import tupa.data.Ottelu;
import tupa.data.Maali;
import tupa.data.Kokoonpano;
import tupa.data.TuomarinRooli;
import tupa.data.Yhteys;

/**
 * Luokka, joka lisää ja poistaa eri olioita, sekä muodostaa automaattisen
 * otteluluettolon.
 *
 * @author Marianne
 */
public class Muuttaja {

    private Tupa ikkuna;
    private Tiedottaja tiedottaja;
    private PaaNakyma nakyma;
    private SarjaNakyma sarjanakyma;
    private Connection con = null;
    private Statement st = null;
    private Statement st2 = null;
    private Statement st3 = null;
    private Statement st4 = null;
    private Statement st5 = null;

    private Yhteys yhteys = new Yhteys();

    private String sql = "";

    public Muuttaja() {

    }

    public Muuttaja(Tupa ikkuna, PaaNakyma nakyma) {
        this.ikkuna = ikkuna;
        tiedottaja = new Tiedottaja(ikkuna);
        this.nakyma = nakyma;
        sarjanakyma = nakyma.annaSarjanakyma();
    }

    public void lisaaKohde(Kohde arvo) {

        if (arvo instanceof Sarja) {
            // tallennetaan turnaus, johon kuuluu
            Sarja sarja = (Sarja) arvo;
            for (int i = 0; i < ikkuna.annaKohteet().size(); i++) {

                Turnaus turnaus = (Turnaus) ikkuna.annaTurnaus();
                sarja.asetaTurnaus(turnaus);

            }
            sarja.kasvataLaskuria();

            sarja.asetaID(sarja.annaLaskuri());

        } else if (arvo instanceof Tuomari) {

            // tallennetaan turnaus, johon kuuluu
            Tuomari tuomari = (Tuomari) arvo;
            for (int i = 0; i < ikkuna.annaKohteet().size(); i++) {

                Turnaus turnaus = (Turnaus) ikkuna.annaTurnaus();
                tuomari.asetaTurnaus(turnaus);

                tuomari.annaKaikkiTurnaukset().add(turnaus);

            }

            tuomari.kasvataLaskuria();

            tuomari.asetaID(tuomari.annaLaskuri());
            tuomari.asetaJulkinenId(tuomari.annaLaskuri() + 88);

        }
        // kohteen omat:
        //yleiseen tietokantaan:
        if (arvo instanceof Sarja) {
            ikkuna.annaSarjatk().add((Sarja) arvo);
            tiedottaja.kirjoitaLoki("Sarja " + arvo.toString() + " lisätty turnaukseen " + ((Sarja) arvo).annaTurnaus().toString() + ".");

        } else if (arvo instanceof Tuomari) {
            ikkuna.annaTuomaritk().add((Tuomari) arvo);

            tiedottaja.kirjoitaLoki("Tuomari " + arvo.toString() + " lisätty turnaukseen " + ((Tuomari) arvo).annaTurnaus().toString() + ".");
        }

        ikkuna.annaKohteet().add(arvo);

        TreeItem<Kohde> parent = new TreeItem<>();

        if (arvo instanceof Sarja) {

            parent = ikkuna.annaRootSarjat();
            TreeItem<Kohde> uusi = new TreeItem<Kohde>(arvo);
            parent.getChildren().add(uusi);

        } else if (arvo instanceof Tuomari) {
            parent = ikkuna.annaRootTuomarit();

            //tuomarit puuhun aakkosjärjestykseen
            Vector<Henkilo> tuomaritV = new Vector<>();

            for (int i = 0; i < ikkuna.annaTuomaritk().size(); i++) {
                tuomaritV.add((Henkilo) ikkuna.annaTuomaritk().get(i));

            }

            Collections.sort(tuomaritV);

            TreeItem<Kohde> parentT = new TreeItem<>();
            parentT = ikkuna.annaRootTuomarit();
            parentT.getChildren().clear();

            for (Henkilo tuomari : tuomaritV) {
                TreeItem<Kohde> uusiKohde = new TreeItem<Kohde>((Kohde) tuomari);
                parentT.getChildren().add(uusiKohde);

            }

        }

//avataan se valikko, mihin uusi kohde on lisätty
        if (!parent.isExpanded()) {
            parent.setExpanded(true);
        }

    }

    public void poistaKohde(Kohde arvo) {

        //jos kyseessä sarja poistetaan turnauksen sarjoista ja tuomari turnauksen tuomareista
        if (arvo instanceof Sarja) {

            Sarja sarja = (Sarja) arvo;
            Turnaus turnaus = sarja.annaTurnaus();
            for (int i = 0; i < turnaus.annaSarjat().size(); i++) {
                if (turnaus.annaSarjat().get(i).equals(sarja)) {
                    turnaus.annaSarjat().remove(i);
                }

            }

        } else if (arvo instanceof Tuomari) {

            Tuomari tuomari = (Tuomari) arvo;
            Turnaus turnaus = tuomari.annaTurnaus();
            for (int i = 0; i < turnaus.annaTuomarit().size(); i++) {
                if (turnaus.annaTuomarit().get(i).equals(tuomari)) {
                    turnaus.annaTuomarit().remove(i);
                }

            }

        }

        //poisto puusta
        TreeItem<Kohde> parent = new TreeItem<>();

        if (arvo instanceof Sarja) {
            parent = ikkuna.annaRootSarjat();

            for (int i = 0; i < parent.getChildren().size(); i++) {
                if (parent.getChildren().get(i).getValue().equals(arvo)) {
                    parent.getChildren().remove(i);
                }

            }

        } else if (arvo instanceof Tuomari) {
            parent = ikkuna.annaRootTuomarit();
            for (int i = 0; i < parent.getChildren().size(); i++) {
                if (parent.getChildren().get(i).getValue().equals(arvo)) {
                    parent.getChildren().remove(i);
                }
            }
        }

        //poisto yleisistä "tietokannoista" eli listoista
        if (arvo instanceof Sarja) {
            for (int i = 0; i < ikkuna.annaSarjatk().size(); i++) {

                if (ikkuna.annaSarjatk().get(i).annaID() == arvo.annaID()) {
                    ikkuna.annaSarjatk().remove(i);
                    tiedottaja.kirjoitaLoki("Sarja " + arvo.toString() + " poistettu turnauksesta " + ikkuna.annaTurnaus().toString() + ".");

                }

            }
        } else if (arvo instanceof Tuomari) {

            for (int i = 0; i < ikkuna.annaTuomaritk().size(); i++) {

                if (ikkuna.annaTuomaritk().get(i).annaID() == arvo.annaID()) {
                    ikkuna.annaTuomaritk().remove(i);
                    tiedottaja.kirjoitaLoki("Tuomari " + arvo.toString() + " poistettu turnauksesta " + ikkuna.annaTurnaus().toString() + ".");

                }

            }

        }

        // ja lopuksi poisto kohdelistasta
        for (int i = 0; i < ikkuna.annaKohteet().size(); i++) {
            if (ikkuna.annaKohteet().get(i).annaID() == arvo.annaID()) {

                ikkuna.annaKohteet().remove(i);

            }
        }

        ikkuna.asetaMuutos(true);
        nakyma.luoEtusivu();
    }

    public void poistaOttelu(Ottelu ottelu) {

        Sarja sarja = ottelu.annaSarja();

        Joukkue joukkue1 = ottelu.annaKotijoukkue();
        Joukkue joukkue2 = ottelu.annaVierasjoukkue();

        //poistetaan tuomareiden roolilistasta ko ottelun roolit
        for (int i = 0; i < ottelu.annaRoolit().size(); i++) {
            if (ottelu.annaRoolit().get(i).annaRooli().equals("Erotuomari")) {
                Tuomari erotuomari = ottelu.annaRoolit().get(i).annaTuomari();
                for (int j = 0; j < erotuomari.annaTuomarinRoolit().size(); j++) {
                    if (erotuomari.annaTuomarinRoolit().get(j).annaOttelu().equals(ottelu)) {

                        erotuomari.annaTuomarinRoolit().remove(erotuomari.annaTuomarinRoolit().get(j));

                    }

                }
            }
            if (ottelu.annaRoolit().get(i).annaRooli().equals("1. Avustava erotuomari")) {
                Tuomari avustava1 = ottelu.annaRoolit().get(i).annaTuomari();
                for (int j = 0; j < avustava1.annaTuomarinRoolit().size(); j++) {
                    if (avustava1.annaTuomarinRoolit().get(j).annaOttelu().equals(ottelu)) {

                        avustava1.annaTuomarinRoolit().remove(avustava1.annaTuomarinRoolit().get(j));

                    }

                }
            }
            if (ottelu.annaRoolit().get(i).annaRooli().equals("2. Avustava erotuomari")) {
                Tuomari avustava2 = ottelu.annaRoolit().get(i).annaTuomari();
                for (int j = 0; j < avustava2.annaTuomarinRoolit().size(); j++) {
                    if (avustava2.annaTuomarinRoolit().get(j).annaOttelu().equals(ottelu)) {

                        avustava2.annaTuomarinRoolit().remove(avustava2.annaTuomarinRoolit().get(j));

                    }

                }
            }
        }

        //päivitetään maalilaskuria, poistetaan ko ottelun maalit pelaajien maalitilastoista (ei ehkä välttämätön..)
        //kotijoukkue
        for (int i = 0; i < joukkue1.annaPelaajat().size(); i++) {
            Pelaaja pelaaja = joukkue1.annaPelaajat().get(i);

            for (int j = 0; j < pelaaja.annaMaaliLista().size(); j++) {
                Maali maali = pelaaja.annaMaaliLista().get(j);
                if (maali.annaOttelu().equals(ottelu)) {

                    pelaaja.annaMaaliLista().remove(maali);
                }
            }
        }

        //vierasjoukkue
        for (int i = 0; i < joukkue2.annaPelaajat().size(); i++) {
            Pelaaja pelaaja = joukkue2.annaPelaajat().get(i);

            for (int j = 0; j < pelaaja.annaMaaliLista().size(); j++) {
                Maali maali = pelaaja.annaMaaliLista().get(j);
                if (maali.annaOttelu().equals(ottelu)) {

                    pelaaja.annaMaaliLista().remove(maali);
                }
            }
        }

        //poistetaan ko ottelun kokoonpano-olio pelaajien kokoonpanolistasta
        //kotijoukkue
        for (int i = 0; i < joukkue1.annaPelaajat().size(); i++) {
            Pelaaja pelaaja = joukkue1.annaPelaajat().get(i);

            for (int j = 0; j < pelaaja.annaKokoonpanot().size(); j++) {
                Kokoonpano kokoonpano = pelaaja.annaKokoonpanot().get(j);
                if (kokoonpano.annaOttelu().equals(ottelu)) {
                    pelaaja.annaKokoonpanot().remove(kokoonpano);
                }
            }
        }

        //vierasjoukkue
        for (int i = 0; i < joukkue2.annaPelaajat().size(); i++) {
            Pelaaja pelaaja = joukkue2.annaPelaajat().get(i);

            for (int j = 0; j < pelaaja.annaKokoonpanot().size(); j++) {
                Kokoonpano kokoonpano = pelaaja.annaKokoonpanot().get(j);
                if (kokoonpano.annaOttelu().equals(ottelu)) {
                    pelaaja.annaKokoonpanot().remove(kokoonpano);
                }
            }
        }

        //päivitetään koti- ja vierasjoukkueen maali- ja ottelutiedot
        if (!ottelu.annaTulos().equals("-")) {
            //kotijoukkue voittanut
            if (ottelu.annaKotimaalit() > ottelu.annaVierasmaalit()) {
                joukkue1.annaVoitot().remove(0);
                joukkue2.annaHaviot().remove(0);
            }
            //vierasjoukkue voittanut
            if (ottelu.annaKotimaalit() < ottelu.annaVierasmaalit()) {
                joukkue1.annaHaviot().remove(0);
                joukkue2.annaVoitot().remove(0);
            }
            //tasapeli
            if (ottelu.annaKotimaalit() == ottelu.annaVierasmaalit()) {
                joukkue1.annaTasapelit().remove(0);
                joukkue2.annaTasapelit().remove(0);
            }

            joukkue1.annaTehdyt().add(ottelu.annaKotimaalit() * (-1));
            joukkue1.annaPaastetyt().add(ottelu.annaVierasmaalit() * (-1));
            joukkue2.annaTehdyt().add(ottelu.annaVierasmaalit() * (-1));
            joukkue2.annaPaastetyt().add(ottelu.annaKotimaalit() * (-1));
        }

        //poistetaan ko ottelu sekä joukkueiden että sarjan ottelulistasta
        joukkue1.annaOttelut().remove(ottelu);
        joukkue2.annaOttelut().remove(ottelu);
        sarja.annaOttelut().remove(ottelu);

        ikkuna.asetaMuutos(true);

    }

    public void lisaaJoukkue(String nimi, Sarja sarja) {

        Joukkue joukkue = new Joukkue(nimi);
        joukkue.kasvataLaskuria();
        joukkue.asetaID(joukkue.annaLaskuri());
        sarja.annaJoukkueet().add(joukkue);
        ikkuna.annaJoukkuetk().add(joukkue);

        ikkuna.annaKohteet().add((Kohde) joukkue);

        joukkue.asetaSarja(sarja);
        tiedottaja.kirjoitaLoki("Joukkue " + joukkue.toString() + " lisätty sarjaan " + sarja + ".");
        ikkuna.asetaMuutos(true);

    }

    public void poistaPelaaja(Pelaaja pelaaja, Joukkue joukkue) {

        ikkuna.annaKohteet().remove((Kohde) pelaaja);

        joukkue.annaPelaajat().remove(pelaaja);
        tiedottaja.kirjoitaLoki("Pelaaja " + pelaaja.toString() + " poistettu joukkueesta " + joukkue + ".");
        ikkuna.asetaMuutos(true);
    }

    public void poistaToimari(Toimihenkilo toimari, Joukkue joukkue) {

        ikkuna.annaKohteet().remove((Kohde) toimari);
        joukkue.annaToimarit().remove(toimari);
        tiedottaja.kirjoitaLoki("Toimihenkilö " + toimari.toString() + " poistettu joukkueesta " + joukkue + ".");
        ikkuna.asetaMuutos(true);
    }

    public void poistaJoukkue(Joukkue joukkue, Sarja sarja) {

        for (int j = 0; j < joukkue.annaPelaajat().size(); j++) {

            ikkuna.annaKohteet().remove((Kohde) joukkue.annaPelaajat().get(j));

        }

        ikkuna.annaKohteet().remove(joukkue);

        sarja.annaJoukkueet().remove(joukkue);
        tiedottaja.kirjoitaLoki("Joukkue " + joukkue.toString() + " poistettu sarjasta " + sarja + ".");
        ikkuna.asetaMuutos(true);

    }

    public void lisaaPelaaja(String etunimi, String sukunimi, String pelipaikka, int pelinumero, Joukkue joukkue) {

        Pelaaja pelaaja = new Pelaaja(etunimi, sukunimi);
        pelaaja.kasvataLaskuria();
        pelaaja.asetaID(pelaaja.annaLaskuri());
        pelaaja.asetaJulkinenID(pelaaja.annaLaskuri() + 100);
        pelaaja.asetaPelipaikka(pelipaikka);
        pelaaja.asetaPelinumero(pelinumero);
        pelaaja.asetaJoukkue(joukkue);

        joukkue.annaPelaajat().add(pelaaja);
        ikkuna.annaPelaajatk().add(pelaaja);
        ikkuna.annaKohteet().add((Kohde) pelaaja);
        tiedottaja.kirjoitaLoki("Pelaaja " + pelaaja.toString() + " lisätty joukkueeseen " + joukkue + ".");
        ikkuna.asetaMuutos(true);

    }

    public void lisaaOttelu(Joukkue koti, Joukkue vieras, int kierros, LocalDate aika, String tunnit, String minuutit, String paikka, Tuomari erotuomari, Tuomari avustava1, Tuomari avustava2, Sarja sarja) {

        Ottelu ottelu = new Ottelu(sarja);
        ottelu.kasvataLaskuria();
        ottelu.asetaID(ottelu.annaLaskuri());
        ottelu.asetaOttelunumero(ottelu.annaLaskuri() + 990);
        ottelu.asetaJoukkueet(koti, vieras);
        ottelu.asetaPaikka(paikka);
        ottelu.asetaKierros(kierros);
        ottelu.asetaAika(aika, tunnit, minuutit);

        //lisätään kokoonpanot
        Kokoonpano koti_kokoonpano = new Kokoonpano(ottelu, koti);
        koti_kokoonpano.kasvataLaskuria();
        koti_kokoonpano.asetaID(koti_kokoonpano.annaLaskuri());
        ottelu.asetaKotiKokoonpano(koti_kokoonpano);

        Kokoonpano vieras_kokoonpano = new Kokoonpano(ottelu, vieras);
        vieras_kokoonpano.kasvataLaskuria();
        vieras_kokoonpano.asetaID(vieras_kokoonpano.annaLaskuri());
        ottelu.asetaVierasKokoonpano(vieras_kokoonpano);

        //lisätään tuomarinrooli
        TuomarinRooli rooli_erotuomari = new TuomarinRooli("Erotuomari", ottelu);
        rooli_erotuomari.kasvataLaskuria();
        rooli_erotuomari.asetaID(rooli_erotuomari.annaLaskuri());
        ottelu.annaRoolit().add(rooli_erotuomari);

        TuomarinRooli rooli_avustava1 = new TuomarinRooli("1. Avustava erotuomari", ottelu);
        rooli_avustava1.kasvataLaskuria();
        rooli_avustava1.asetaID(rooli_avustava1.annaLaskuri());
        ottelu.annaRoolit().add(rooli_avustava1);

        TuomarinRooli rooli_avustava2 = new TuomarinRooli("2. Avustava erotuomari", ottelu);
        rooli_avustava2.kasvataLaskuria();
        rooli_avustava2.asetaID(rooli_avustava2.annaLaskuri());
        ottelu.annaRoolit().add(rooli_avustava2);

        //tuomarit kohdilleen (jos annettu)
        if (erotuomari != null) {

            for (int i = 0; i < ottelu.annaRoolit().size(); i++) {

                if (ottelu.annaRoolit().get(i).annaRooli().equals("Erotuomari")) {
                    TuomarinRooli erotuomariR = ottelu.annaRoolit().get(i);
                    erotuomariR.asetaTuomari(erotuomari);
                    erotuomari.annaTuomarinRoolit().add(erotuomariR);
                    break;
                }
            }

        }

        if (avustava1 != null) {

            for (int i = 0; i < ottelu.annaRoolit().size(); i++) {

                if (ottelu.annaRoolit().get(i).annaRooli().equals("1. Avustava erotuomari")) {
                    TuomarinRooli avustava1R = ottelu.annaRoolit().get(i);
                    avustava1R.asetaTuomari(avustava1);
                    avustava1.annaTuomarinRoolit().add(avustava1R);
                    break;
                }
            }
        }

        if (avustava2 != null) {
            for (int i = 0; i < ottelu.annaRoolit().size(); i++) {

                if (ottelu.annaRoolit().get(i).annaRooli().equals("2. Avustava erotuomari")) {
                    TuomarinRooli avustava2R = ottelu.annaRoolit().get(i);
                    avustava2R.asetaTuomari(avustava2);
                    avustava2.annaTuomarinRoolit().add(avustava2R);
                    break;
                }
            }
        }
        tiedottaja.kirjoitaLoki("Ottelu " + ottelu.toString() + " lisätty sarjaan " + sarja + ".");
        sarja.annaOttelut().add(ottelu);
        ikkuna.asetaMuutos(true);

    }

    public void lisaaToimari(String etunimi, String sukunimi, String rooli, String sposti, String puh, Joukkue joukkue) {

        Toimihenkilo toimari = new Toimihenkilo(etunimi, sukunimi);
        toimari.kasvataLaskuria();
        toimari.asetaID(toimari.annaLaskuri());
        toimari.asetaRooli(rooli);
        toimari.asetaSposti(sposti);
        toimari.asetaPuh(puh);
        toimari.asetaJoukkue(joukkue);

        joukkue.annaToimarit().add(toimari);

        ikkuna.annaToimaritk().add(toimari);
        ikkuna.annaKohteet().add((Kohde) toimari);
        tiedottaja.kirjoitaLoki("Toimihenkilö " + toimari.toString() + " lisätty joukkueeseen " + joukkue + ".");

        ikkuna.asetaMuutos(true);

    }

    public void lisaaMaali(int aika, Pelaaja maalintekija, Pelaaja syottaja, Ottelu ottelu, Joukkue joukkue) {

        if (!maalintekija.annaEtuNimi().equals("Valitse") && !syottaja.annaEtuNimi().equals("Valitse")) {
            Maali maali = new Maali(ottelu);
            maali.kasvataLaskuria();
            maali.asetaID(maali.annaLaskuri());
            if (maalintekija.annaEtuNimi().equals("Oma")) {

                maalintekija.asetaJoukkue(joukkue);
                ikkuna.annaKohteet().add(maalintekija);
            }
            if (syottaja.annaEtuNimi().equals("Ei")) {
                syottaja.asetaJoukkue(joukkue);
                ikkuna.annaKohteet().add(syottaja);
            }
            maali.asetaTiedot(aika, maalintekija, syottaja);
        } else if (!maalintekija.annaEtuNimi().equals("Valitse") && !syottaja.annaEtuNimi().equals("Valitse")) {
            Maali maali = new Maali(ottelu);
            maali.kasvataLaskuria();
            maali.asetaID(maali.annaLaskuri());
            if (maalintekija.annaEtuNimi().equals("Oma")) {

                maalintekija.asetaJoukkue(joukkue);
                ikkuna.annaKohteet().add(maalintekija);
            }
            Pelaaja uusisyottaja = new Pelaaja("", "");
            uusisyottaja.asetaJoukkue(joukkue);
            ikkuna.annaKohteet().add(uusisyottaja);
            maali.asetaTiedot(aika, maalintekija, uusisyottaja);
        }
        tiedottaja.kirjoitaLoki("Ottelun " + ottelu.toString() + " maalitilastoa päivitetty. ");
    }

    public void lisaaTulos(int kotimaalit, int vierasmaalit, Ottelu ottelu) {
        ottelu.asetaTulos(kotimaalit, vierasmaalit);
        tiedottaja.kirjoitaLoki("Ottelun " + ottelu.toString() + " tulosta päivitetty. ");
    }

    public void lisaaKokoonpano(Pelaaja[] pelaajat, String[] roolit, Joukkue joukkue, Ottelu ottelu) {

        for (int i = 0; i < pelaajat.length; i++) {

            if (roolit[i].equals("Kokoonpanossa")) {
                //katsotaan onko koti- vai vierasjoukkue
                if (joukkue.equals(ottelu.annaKotijoukkue())) {
                    Kokoonpano kotikokoonpano = ottelu.annaKotiKokoonpano();
                    kotikokoonpano.asetaPelaaja(pelaajat[i]);

                    pelaajat[i].annaKokoonpanot().add(kotikokoonpano);
                } else if (joukkue.equals(ottelu.annaVierasjoukkue())) {

                    Kokoonpano vieraskokoonpano = ottelu.annaVierasKokoonpano();
                    vieraskokoonpano.asetaPelaaja(pelaajat[i]);
                    pelaajat[i].annaKokoonpanot().add(vieraskokoonpano);

                }
            } else if (joukkue.equals(ottelu.annaKotijoukkue())) {
                Kokoonpano kotikokoonpano = ottelu.annaKotiKokoonpano();
                kotikokoonpano.annaPelaajat().remove(pelaajat[i]);
                pelaajat[i].annaKokoonpanot().remove(kotikokoonpano);
            } else if (joukkue.equals(ottelu.annaVierasjoukkue())) {
                Kokoonpano vieraskokoonpano = ottelu.annaVierasKokoonpano();
                vieraskokoonpano.annaPelaajat().remove(pelaajat[i]);
                pelaajat[i].annaKokoonpanot().remove(vieraskokoonpano);
            }

        }
        tiedottaja.kirjoitaLoki("Ottelun " + ottelu.toString() + " kokoonpanoa päivitetty.");
    }

    public void suoritaAutoOtteluLista(Sarja sarja) {

        DropShadow dropShadow = new DropShadow();
        dropShadow.setOffsetX(5);
        dropShadow.setOffsetY(5);

        Label otsikko = new Label("Luodaan automaattista otteluohjelmaa..");
        otsikko.setFont(Font.font("Papyrus", FontWeight.BOLD, 16));
        otsikko.setEffect(dropShadow);

        ProgressBar edistyminen = new ProgressBar();
        edistyminen.setPrefWidth(200);
        edistyminen.setPrefHeight(30);

        VBox palkki = new VBox();
        palkki.setPadding(new Insets(10));
        palkki.setSpacing(10);
        palkki.setStyle("-fx-background-color:  linear-gradient(to bottom, #00ff00, 	#ccffcc)");
        palkki.getChildren().addAll(otsikko, edistyminen);

        Stage tehtavastage = new Stage(StageStyle.UTILITY);
        Scene scene = new Scene(palkki, 300, 100);

        scene.getStylesheets().add("css/tyylit.css");

        tehtavastage.setScene(scene);
        tehtavastage.show();

        Task tehtava = new Task<Void>() {
            @Override
            protected Void call() throws Exception {

                // poistetaan kaikki aiemmin luodut ottelut ensin
                while (!sarja.annaOttelut().isEmpty()) {
                    for (int i = 0; i < sarja.annaOttelut().size(); i++) {
                        poistaOttelu(sarja.annaOttelut().get(i));
                    }
                }

                int maara = sarja.annaJoukkueet().size();
                boolean parillinen_maara = true;
                //jos joukkeita on pariton määrä, lisätään yksi ylimääräinen "dummy" joukkue, jota vastaan olevaa peliä ei oikeasti pelata

                Joukkue dummy = new Joukkue();
                if (maara % 2 != 0) {
                    parillinen_maara = false;

                    maara = maara + 1;

                }
                int puolikas = maara / 2;
                //PARILLINEN MÄÄRÄ?!
                Joukkue[] joukkueet = new Joukkue[maara];
                Joukkue[] joukkueet1 = new Joukkue[maara / 2];
                Joukkue[] joukkueet2 = new Joukkue[maara / 2];

                if (parillinen_maara) {
                    for (int i = 0; i < maara; i++) {

                        joukkueet[i] = sarja.annaJoukkueet().get(i);

                    }

                    for (int i = 0; i < puolikas; i++) {

                        joukkueet1[i] = joukkueet[i];

                    }

                    int kohta = puolikas;
                    for (int i = puolikas - 1; i >= 0; i--) {

                        joukkueet2[i] = joukkueet[kohta];
                        kohta++;

                    }
                } else {
                    joukkueet[0] = dummy;
                    for (int i = 1; i < maara; i++) {

                        joukkueet[i] = sarja.annaJoukkueet().get(i - 1);

                    }

                    for (int i = 0; i < puolikas; i++) {

                        joukkueet1[i] = joukkueet[i];

                    }

                    int kohta = puolikas;
                    for (int i = puolikas - 1; i >= 0; i--) {

                        joukkueet2[i] = joukkueet[kohta];
                        kohta++;

                    }

                }

                //yksinkertaisten sarjojen otteluiden lkm (aritmeettinen summa, n = maara - 1): 
                int ottelut = 0;

                ottelut = ((maara - 1 + 1) / 2) * (maara - 1);

                //joukkueet taulukossa
                //muodostetaan otteluiden lukumaara * 2- matriisi siten, että yhdellä rivillä on yksi ottelu
                Joukkue[][] ottelutaulu = new Joukkue[ottelut][2];

                Joukkue[] apu1 = new Joukkue[maara / 2];
                Joukkue[] apu2 = new Joukkue[maara / 2];

                //kiinnitetään vika, joka ei siis liiku
                Joukkue kiinni = joukkueet[maara - 1];

                int kierrosten_lkm = maara - 1;

                int j = 0;

                int kierros = 1;
                for (int i = 1; i <= kierrosten_lkm; i++) {

                    for (int k = 0; k < puolikas; k++) {

                        ottelutaulu[j][0] = joukkueet1[k];
                        ottelutaulu[j][1] = joukkueet2[k];

                        j++;
                    }
                    kierros++;
                    //kiinnitys
                    if (kierros % 2 == 0) {

                        apu1[0] = kiinni;
                        apu2[0] = joukkueet2[puolikas - 1];

                        int kohta2 = puolikas - 1;
                        for (int k = 1; k <= puolikas - 1; k++) {
                            apu2[k] = joukkueet1[kohta2];
                            kohta2--;

                        }
                        int kohta3 = puolikas - 2;
                        for (int k = 1; k < puolikas - 1; k++) {
                            apu1[k] = joukkueet2[kohta3];
                            kohta3--;

                        }
                        apu1[puolikas - 1] = joukkueet1[0];

                    } else {

                        apu2[0] = kiinni;
                        apu1[0] = joukkueet2[puolikas - 1];

                        int kohta2 = puolikas - 1;
                        for (int k = 1; k <= puolikas - 1; k++) {
                            apu2[k] = joukkueet1[kohta2];
                            kohta2--;

                        }
                        int kohta3 = puolikas - 2;
                        for (int k = 1; k < puolikas - 1; k++) {
                            apu1[k] = joukkueet2[kohta3];
                            kohta3--;

                        }
                        apu1[puolikas - 1] = joukkueet2[0];
                    }

                    for (int k = 0; k <= puolikas - 1; k++) {
                        joukkueet1[k] = apu1[k];
                        joukkueet2[k] = apu2[k];

                    }

                }

                //jos joukkueita pariton määrä, varmistetaan, ettei koti- ja vieraspelit jakaudu epätasaisesti
                if (!parillinen_maara) {
                    List<Joukkue> kotiyli = new ArrayList<>();
                    List<Joukkue> kotiali = new ArrayList<>();

                    //käydään kaikki joukkueet läpi
                    for (int i = 0; i < joukkueet.length; i++) {

                        if (!joukkueet[i].equals(dummy)) {
                            int kotiottelut = 0;
                            int vierasottelut = 0;
                            //käydään ottelutaulu läpi
                            for (int k = 0; k < ottelut; k++) {

                                if (ottelutaulu[k][0].equals(joukkueet[i]) && !ottelutaulu[k][1].equals(dummy)) {
                                    kotiottelut++;
                                } else if (ottelutaulu[k][1].equals(joukkueet[i]) && !ottelutaulu[k][0].equals(dummy)) {
                                    vierasottelut++;
                                }

                            }

                            if (!parillinen_maara && kotiottelut >= maara / 2) {

                                kotiyli.add(joukkueet[i]);

                            } else if (!parillinen_maara && vierasottelut >= maara / 2) {

                                kotiali.add(joukkueet[i]);

                            }

                        }

                    }

                    for (int k = 0; k < ottelut; k++) {
                        if (kotiyli.size() > 0 && kotiali.size() > 0) {

                            for (int kotiylialku = 0; kotiylialku < kotiyli.size(); kotiylialku++) {

                                for (int m = 0; m < kotiali.size(); m++) {

                                    if (ottelutaulu[k][0].equals(kotiyli.get(kotiylialku)) && ottelutaulu[k][1].equals(kotiali.get(m))) {

                                        ottelutaulu[k][0] = kotiali.get(m);
                                        ottelutaulu[k][1] = kotiyli.get(kotiylialku);
                                        kotiali.remove(kotiali.get(m));
                                        kotiyli.remove(kotiyli.get(kotiylialku));

                                        break;
                                    }
                                }

                            }

                        }

                    }

                }

                //matriisin sisällön pohjalta luodaan ottelut
                for (int i = 0; i < ottelut; i++) {

                    if (!(ottelutaulu[i][0].equals(dummy) || ottelutaulu[i][1].equals(dummy))) {
                        Ottelu ottelu = new Ottelu(sarja);
                        ottelu.kasvataLaskuria();
                        ottelu.asetaID(ottelu.annaLaskuri());
                        ottelu.asetaOttelunumero(ottelu.annaLaskuri() + 990);

                        Joukkue kotijoukkue = ottelutaulu[i][0];
                        Joukkue vierasjoukkue = ottelutaulu[i][1];
                        ottelu.asetaJoukkueet(kotijoukkue, vierasjoukkue);
                        //lisätään kokoonpanot
                        Kokoonpano koti_kokoonpano = new Kokoonpano(ottelu, kotijoukkue);
                        koti_kokoonpano.kasvataLaskuria();
                        koti_kokoonpano.asetaID(koti_kokoonpano.annaLaskuri());
                        ottelu.asetaKotiKokoonpano(koti_kokoonpano);

                        Kokoonpano vieras_kokoonpano = new Kokoonpano(ottelu, vierasjoukkue);
                        vieras_kokoonpano.kasvataLaskuria();
                        vieras_kokoonpano.asetaID(vieras_kokoonpano.annaLaskuri());
                        ottelu.asetaVierasKokoonpano(vieras_kokoonpano);

                        //lisätään tuomarinrooli
                        TuomarinRooli rooli_erotuomari = new TuomarinRooli("Erotuomari", ottelu);
                        rooli_erotuomari.kasvataLaskuria();
                        rooli_erotuomari.asetaID(rooli_erotuomari.annaLaskuri());
                        ottelu.annaRoolit().add(rooli_erotuomari);

                        TuomarinRooli rooli_avustava1 = new TuomarinRooli("1. Avustava erotuomari", ottelu);
                        rooli_avustava1.kasvataLaskuria();
                        rooli_avustava1.asetaID(rooli_avustava1.annaLaskuri());
                        ottelu.annaRoolit().add(rooli_avustava1);

                        TuomarinRooli rooli_avustava2 = new TuomarinRooli("2. Avustava erotuomari", ottelu);
                        rooli_avustava2.kasvataLaskuria();
                        rooli_avustava2.asetaID(rooli_avustava2.annaLaskuri());
                        ottelu.annaRoolit().add(rooli_avustava2);
                        sarja.annaOttelut().add(ottelu);

                    }

                }
                int ottelua_per_kierros = 0;
                if (parillinen_maara) {
                    ottelua_per_kierros = maara / 2;
                } else {
                    maara = maara - 1;
                    ottelua_per_kierros = maara / 2;
                }

                List<Ottelu> lisatyt_ottelut = sarja.annaOttelut();

                int ottelulaskuri = 0;
                int kierroslaskuri = 1;
                for (int i = 0; i < lisatyt_ottelut.size(); i++) {

                    ottelulaskuri++;

                    if (ottelulaskuri > ottelua_per_kierros) {

                        kierroslaskuri++;
                        ottelulaskuri = 1;

                    }

                    lisatyt_ottelut.get(i).asetaKierros(kierroslaskuri);
                    lisatyt_ottelut.get(i).asetaTaulukkokierros();

                }
                return null;
            }
        };
        tehtava.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent t) {
                Tiedottaja tiedottaja2 = new Tiedottaja(ikkuna);
                tiedottaja2.kirjoitaLoki("Otteluluettelo laadittu sarjaan " + sarja.toString() + ".");
                sarjanakyma = nakyma.annaSarjanakyma();

                sarjanakyma.luoOtteluLuetteloMuokkaus(sarja);
                ikkuna.asetaMuutos(true);

                tehtavastage.hide();
            }
        });

        edistyminen.progressProperty().bind(tehtava.progressProperty());

        tehtavastage.show();

        Thread th = new Thread(tehtava);
        th.start();

    }

    public void poistaKaikkiOttelut(List<Ottelu> ottelut, Sarja sarja) {

        while (!sarja.annaOttelut().isEmpty()) {
            for (int i = 0; i < ottelut.size(); i++) {

                poistaOttelu(ottelut.get(i));

            }
        }

        sarja.annaOttelut().clear();
        tiedottaja.kirjoitaLoki("Kaikki ottelut poistettu sarjasta " + sarja.toString() + ".");
    }

    public void poistaKaikkiJoukkueet(List<Joukkue> joukkueet, Sarja sarja) {

        while (!sarja.annaJoukkueet().isEmpty()) {
            for (int i = 0; i < joukkueet.size(); i++) {
                poistaJoukkue(joukkueet.get(i), sarja);

            }
        }
        sarja.annaJoukkueet().clear();
        tiedottaja.kirjoitaLoki("Kaikki joukkueet poistettu sarjasta " + sarja.toString() + ".");
        ikkuna.asetaMuutos(true);
    }

    void poistaKaikkiPelaajat(List<Pelaaja> poistettavat, Joukkue joukkue) {

        while (!joukkue.annaPelaajat().isEmpty()) {
            for (int i = 0; i < poistettavat.size(); i++) {
                poistaPelaaja(poistettavat.get(i), joukkue);

            }
        }
        joukkue.annaPelaajat().clear();

        tiedottaja.kirjoitaLoki("Kaikki pelaajat poistettu joukkueesta " + joukkue.toString() + ".");
        ikkuna.asetaMuutos(true);

    }

    public void poistaKaikkiToimarit(List<Toimihenkilo> poistettavat, Joukkue joukkue) {

        while (!joukkue.annaToimarit().isEmpty()) {
            for (int i = 0; i < poistettavat.size(); i++) {
                poistaToimari(poistettavat.get(i), joukkue);

            }
        }
        joukkue.annaToimarit().clear();

        tiedottaja.kirjoitaLoki("Kaikki toimihenkilöt poistettu joukkueesta " + joukkue.toString() + ".");
        ikkuna.asetaMuutos(true);

    }

    public void poistaToimarinOikeus(Toimihenkilo toimari, Joukkue joukkue) {
        try {
            int kayttaja_id = toimari.annaHallintaID();

            con = yhteys.annaYhteys();
            st = con.createStatement();
            st.executeUpdate("DELETE FROM kayttaja WHERE id='" + kayttaja_id + "'");
            st.executeUpdate("DELETE FROM kayttajan_turnaus WHERE kayttaja_id='" + kayttaja_id + "'");
        } catch (SQLException se) {

            Tiedottaja tiedottaja = new Tiedottaja();
            tiedottaja.annaVirhe("" + se);
        } catch (Exception e) {

            Tiedottaja tiedottaja = new Tiedottaja();
            tiedottaja.annaVirhe("" + e);
        } finally {

            try {
                if (st != null) {
                    con.close();
                }
            } catch (SQLException se) {
                Tiedottaja tiedottaja = new Tiedottaja();
                tiedottaja.annaVirhe("" + se);
            }
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException se) {
                Tiedottaja tiedottaja = new Tiedottaja();
                tiedottaja.annaVirhe("" + se);
            }
        }
        toimari.asetaHallinta(0);
        tiedottaja.kirjoitaLoki("Toimihenkilön " + toimari.toString() + " ylläpito-oikeudet poistettu.");
    }

    public void poistaTurnaus(Turnaus turnaus) {
        DropShadow dropShadow = new DropShadow();
        dropShadow.setOffsetX(5);
        dropShadow.setOffsetY(5);

        Label otsikko = new Label("Poistetaan turnausta..");
        otsikko.setFont(Font.font("Papyrus", FontWeight.BOLD, 16));
        otsikko.setEffect(dropShadow);

        ProgressBar edistyminen = new ProgressBar();
        edistyminen.setPrefWidth(200);
        edistyminen.setPrefHeight(30);

        VBox palkki = new VBox();
        palkki.setPadding(new Insets(10));
        palkki.setSpacing(10);
        palkki.setStyle("-fx-background-color:  linear-gradient(to bottom, #00ff00, 	#ccffcc)");
        palkki.getChildren().addAll(otsikko, edistyminen);

        Stage tehtavastage = new Stage(StageStyle.UTILITY);
        Scene scene = new Scene(palkki);

        scene.getStylesheets().add("css/tyylit.css");

        tehtavastage.setScene(scene);
        tehtavastage.show();

        Task tehtava = new Task<Void>() {
            @Override
            protected Void call() throws Exception {

                try {
                    int turnaus_id = turnaus.annaID();

                    con = yhteys.annaYhteys();
                    st = con.createStatement();
                    st2 = con.createStatement();
                    st3 = con.createStatement();
                    st4 = con.createStatement();
                    st5 = con.createStatement();

                    //tyhjennetään pelaajat
                    sql = "SELECT DISTINCT pelaaja.tupaid as pid FROM sarja, joukkue, pelaaja WHERE sarja.turnaus_id='" + turnaus_id + "' AND joukkue.sarja_id = sarja.tupaid AND pelaaja.joukkue_id = joukkue.tupaid";
                    ResultSet pelaajat = st2.executeQuery(sql);

                    while (pelaajat.next()) {

                        int id = pelaajat.getInt("pid");
                        sql = "DELETE FROM pelaaja WHERE tupaid='" + id + "'";
                        st.executeUpdate(sql);
                        sql = "DELETE FROM pelaajan_kokoonpano WHERE pelaaja_id='" + id + "'";
                        st.executeUpdate(sql);
                    }

                    //tyhjennetään toimarit
                    sql = "SELECT DISTINCT toimari.tupaid as tid FROM sarja, joukkue, toimari WHERE sarja.turnaus_id='" + turnaus_id + "' AND joukkue.sarja_id = sarja.tupaid AND toimari.joukkue_id = joukkue.tupaid";
                    ResultSet toimarit = st3.executeQuery(sql);

                    while (toimarit.next()) {

                        int id = toimarit.getInt("tid");
                        sql = "DELETE FROM toimari WHERE tupaid='" + id + "'";
                        st.executeUpdate(sql);
                    }

                    //tyhjennetään ottelut
                    sql = "SELECT DISTINCT ottelu.tupaid as oid FROM sarja, joukkue, ottelu WHERE sarja.turnaus_id='" + turnaus_id + "' AND joukkue.sarja_id = sarja.tupaid AND (ottelu.kotijoukkue_id = joukkue.tupaid OR ottelu.vierasjoukkue_id = joukkue.tupaid)";
                    ResultSet ottelut = st4.executeQuery(sql);

                    while (ottelut.next()) {
                        int id = ottelut.getInt("oid");

                        //tyhjennetään kokoonpanot
                        sql = "DELETE FROM kokoonpano WHERE ottelu_id='" + id + "'";
                        st.executeUpdate(sql);

                        //tyhjennetään ensin maalit
                        sql = "DELETE FROM maali WHERE ottelu_id='" + id + "'";
                        st.executeUpdate(sql);
                        //ja tuomarinroolit

                        sql = "DELETE FROM tuomarinrooli WHERE ottelu_id='" + id + "'";
                        st.executeUpdate(sql);

                        sql = "DELETE FROM ottelu WHERE tupaid='" + id + "'";
                        st.executeUpdate(sql);
                    }

                    //tyhjennetään joukkueet
                    sql = "SELECT DISTINCT joukkue.tupaid as jid FROM sarja, joukkue WHERE sarja.turnaus_id='" + turnaus_id + "' AND joukkue.sarja_id = sarja.tupaid";
                    ResultSet joukkueet = st5.executeQuery(sql);

                    while (joukkueet.next()) {

                        int id = joukkueet.getInt("jid");
                        sql = "DELETE FROM joukkue WHERE tupaid='" + id + "'";
                        st.executeUpdate(sql);
                    }

                    //tyhjennetään sarjat
                    sql = "DELETE FROM sarja WHERE turnaus_id='" + turnaus_id + "'";
                    st.executeUpdate(sql);

                    //tyhjennetään tuomarit
                    sql = "DELETE FROM tuomari WHERE turnaus_id='" + turnaus_id + "'";
                    st.executeUpdate(sql);

                    sql = "DELETE FROM turnauksen_salasana WHERE turnaus_id='" + turnaus_id + "'";
                    st.executeUpdate(sql);

                    sql = "DELETE FROM kayttajan_turnaus WHERE turnaus_id='" + turnaus_id + "'";
                    st.executeUpdate(sql);
                    sql = "DELETE FROM turnaus WHERE tupaid='" + turnaus_id + "'";
                    st.executeUpdate(sql);

                } catch (SQLException se) {

                    Tiedottaja tiedottaja = new Tiedottaja();
                    tiedottaja.annaVirhe("" + se);
                } catch (Exception e) {

                    Tiedottaja tiedottaja = new Tiedottaja();
                    tiedottaja.annaVirhe("" + e);
                } finally {

                    try {
                        if (st != null) {
                            con.close();
                        }
                    } catch (SQLException se) {
                        Tiedottaja tiedottaja = new Tiedottaja();
                        tiedottaja.annaVirhe("" + se);
                    }
                    try {
                        if (con != null) {
                            con.close();
                        }
                    } catch (SQLException se) {
                        Tiedottaja tiedottaja = new Tiedottaja();
                        tiedottaja.annaVirhe("" + se);
                    }
                }
                return null;
            }
        };
        tehtava.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent t) {

                Tiedottaja tiedottaja = new Tiedottaja(ikkuna);
                tiedottaja.annaIlmoitus("Turnaus " + turnaus.toString() + " poistettu.");
                ikkuna.asetaAloitus(true);

                tehtavastage.hide();
            }
        });
        edistyminen.progressProperty().bind(tehtava.progressProperty());

        tehtavastage.show();

        Thread th = new Thread(tehtava);
        th.start();

    }

}
