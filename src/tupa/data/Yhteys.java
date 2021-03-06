package tupa.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import tupa.kontrollerit.Tiedottaja;

/**
 * Luokka, joka hoitaa yhteyden tietokantaan.
 *
 * @author Marianne
 */
public class Yhteys {

    private static String url = "jdbc:mysql://tite.work:3306/";
    private static String driver = "com.mysql.jdbc.Driver";
    private static String dbName = "tupa";
    private static String userName = "root";
    private static String password = "asdlol";
    private static Connection yhteys;

    public Yhteys() {
    }

    public static Connection annaYhteys() {
        try {
            Class.forName(driver);
            try {
                yhteys = DriverManager.getConnection(url + dbName, userName, password);
            } catch (SQLException ex) {
                Tiedottaja tiedottaja = new Tiedottaja();
                tiedottaja.annaVirhe("Tarkista internetyhteys!");
            }
        } catch (ClassNotFoundException ex) {

            Tiedottaja tiedottaja = new Tiedottaja();
            tiedottaja.annaVirhe("" + ex);
        }
        return yhteys;
    }
}
