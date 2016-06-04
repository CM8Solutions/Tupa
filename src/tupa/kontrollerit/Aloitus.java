package tupa.kontrollerit;

import java.sql.Connection;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import tupa.data.Turnaus;
import tupa.data.Yhteys;

/**
 * Luokka, joka muodostaa aloitustilanteen turnauksen.
 *
 * @author Marianne
 */
public class Aloitus {

    private Turnaus turnaus = new Turnaus();
    private Connection con = null;
    private Statement st = null;
    private Yhteys yhteys = new Yhteys();
    private String sql = "";

    public Aloitus() {

    }

    public Turnaus luoAlkuTurnaus() {

        turnaus.asetaID(0);

        turnaus.asetaNimi("Uusi turnaus");
        turnaus.asetaLuomispvm(LocalDate.now().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)));
        return turnaus;
    }

}
