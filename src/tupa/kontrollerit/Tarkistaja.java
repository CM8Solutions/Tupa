package tupa.kontrollerit;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import tupa.Tupa;
import tupa.data.Tuomari;
import tupa.data.Joukkue;
import tupa.data.Ottelu;
import tupa.data.Turnaus;
import tupa.data.Yhteys;

/**
 * Luokka, joka hoitaa tarvittavien tietojen tarkistamisen.
 *
 * @author Marianne
 */
public class Tarkistaja {

    private Tupa ikkuna;
    private Tiedottaja tiedottaja;
    private Turnaus turnaus;
    private Yhteys yhteys = new Yhteys();
    private Connection con = null;
    private Statement st = null;
    private String sql = "";

    public Tarkistaja() {

    }

    public Tarkistaja(Tupa ikkuna) {
        this.ikkuna = ikkuna;
        this.tiedottaja = new Tiedottaja(ikkuna);
    }

    public Tarkistaja(Tupa ikkuna, Turnaus turnaus) {
        this.ikkuna = ikkuna;
        this.turnaus = turnaus;
        this.tiedottaja = new Tiedottaja(ikkuna);
    }

    public boolean nimiOK(String nimi) {
        if (onTyhja(nimi)) {
            return false;
        } else if (nimi.length() > 64) {
            tiedottaja.annaVirhe("Sekä etu- että sukunimi saa sisältää korkeintaan 64 merkkiä.");
            return false;
        } else {

            for (char c : nimi.toCharArray()) {

                if (!Character.isLetter(c)) {
                    if (!Character.toString(c).equals("-")) {

                        tiedottaja.annaVirhe("Sekä etu- että sukunimi saa sisältää vain kirjaimia ja tavuviivoja.");
                        return false;
                    }

                }
            }
            return true;
        }
    }

    public boolean onTyhja(String merkkijono) {
        if (merkkijono.trim().isEmpty()) {
            tiedottaja.annaVirhe("Et voi antaa tyhjää kenttää.");
            return true;
        } else {
            return false;
        }
    }

    public boolean pelinumeroOK(Integer nro, Joukkue joukkue) {
        for (int i = 0; i < joukkue.annaPelaajat().size(); i++) {

            if (joukkue.annaPelaajat().get(i).annaPelinumero() == nro) {

                tiedottaja.annaVirhe("Valitsemasi pelinumero on jo käytössä toisella joukkueen pelaajalla.");
                return false;
            }

        }
        return true;
    }

    public boolean kotijoukkueOK(Joukkue joukkue, Ottelu ottelu) {

        if (joukkue == null) {
            tiedottaja.annaVirhe("Et voi antaa tyhjää kenttää.");
            return false;
        } else if (ottelu.annaVierasjoukkue().equals(joukkue)) {

            tiedottaja.annaVirhe("Koti- ja vierasjoukkue eivät voi olla samoja.");
            return false;
        } else {
            return true;
        }
    }

    public boolean vierasjoukkueOK(Joukkue joukkue, Ottelu ottelu) {

        if (joukkue == null) {
            tiedottaja.annaVirhe("Et voi antaa tyhjää kenttää.");
            return false;
        } else if (ottelu.annaKotijoukkue().equals(joukkue)) {

            tiedottaja.annaVirhe("Koti- ja vierasjoukkue eivät voi olla samoja.");
            return false;
        } else {
            return true;
        }
    }

    public boolean erotuomariOK(Tuomari tuomari, Ottelu ottelu) {

        if (tuomari != null) {
            if (ottelu.annaAvustava1().equals(tuomari)) {
                tiedottaja.annaVirhe("Sama henkilö ei voi olla kuin yhdessä tuomarin roolissa.");
                return false;
            } else if (ottelu.annaAvustava2().equals(tuomari)) {
                tiedottaja.annaVirhe("Sama henkilö ei voi olla kuin yhdessä tuomarin roolissa.");
                return false;
            }
        }
        return true;
    }

    public boolean avustava1OK(Tuomari tuomari, Ottelu ottelu) {

        if (tuomari != null) {
            if (ottelu.annaErotuomari().equals(tuomari)) {
                tiedottaja.annaVirhe("Sama henkilö ei voi olla kuin yhdessä tuomarin roolissa.");
                return false;
            } else if (ottelu.annaAvustava2().equals(tuomari)) {
                tiedottaja.annaVirhe("Sama henkilö ei voi olla kuin yhdessä tuomarin roolissa.");
                return false;
            }
        }
        return true;
    }

    public boolean avustava2OK(Tuomari tuomari, Ottelu ottelu) {

        if (tuomari != null) {
            if (ottelu.annaErotuomari().equals(tuomari)) {
                tiedottaja.annaVirhe("Sama henkilö ei voi olla kuin yhdessä tuomarin roolissa.");
                return false;
            } else if (ottelu.annaAvustava1().equals(tuomari)) {
                tiedottaja.annaVirhe("Sama henkilö ei voi olla kuin yhdessä tuomarin roolissa.");
                return false;
            }
        }
        return true;
    }

    public void tarkistaTurnaustiedot(boolean jatko, boolean avaus, boolean uusi) {

        int turnaus_id = turnaus.annaID();
        try {

            con = yhteys.annaYhteys();
            st = con.createStatement();

            sql = "SELECT DISTINCT * FROM turnaus WHERE tupaid = '" + turnaus_id + "'";

            ResultSet haetut_rivit = st.executeQuery(sql);
            int laskuri = 0;
            while (haetut_rivit.next()) {
                laskuri++;

            }

            if (laskuri == 1) {
                Tallennus tallenna = new Tallennus(ikkuna);
                tallenna.suoritaTallennus(jatko, avaus, uusi);
                tiedottaja.kirjoitaLoki("Turnaus " + turnaus.toString() + " tallennettu.");
            } else {

                Kirjautuminen kirjautuja = new Kirjautuminen(turnaus, ikkuna);
                kirjautuja.luoTurnauksenSalasananSyotto(jatko, avaus, uusi);
            }

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
    }

    public boolean tarkistaTurnausMaara() {
        boolean ok = false;
        int kayttaja_id = ikkuna.annaKayttajaID();
        try {

            con = yhteys.annaYhteys();
            st = con.createStatement();

            sql = "SELECT DISTINCT * FROM kayttajan_turnaus WHERE kayttaja_id = '" + kayttaja_id + "'";

            ResultSet haetut_rivit = st.executeQuery(sql);
            int laskuri = 0;
            while (haetut_rivit.next()) {
                laskuri++;

            }

            if (laskuri < 5) {
                ok = true;
            } else {
                ok = false;
            }

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

        return ok;
    }

    public boolean annaVientiOK(int id) {
        int arvo = 0;

        try {

            con = yhteys.annaYhteys();
            st = con.createStatement();

            sql = "SELECT DISTINCT viety_tiedostoon FROM tuomari WHERE tupaid = '" + id + "'";

            ResultSet haetut_rivit = st.executeQuery(sql);

            while (haetut_rivit.next()) {
                arvo = haetut_rivit.getInt("viety_tiedostoon");

            }

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
        if (arvo == 1) {
            return false;
        } else {
            return true;
        }
    }

}
