/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tupa.kontrollerit;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import tupa.Tupa;
import tupa.data.Joukkue;
import tupa.data.Kokoonpano;
import tupa.data.Maali;
import tupa.data.Ottelu;
import tupa.data.Pelaaja;
import tupa.data.Sarja;
import tupa.data.Toimihenkilo;
import tupa.data.Tuomari;
import tupa.data.TuomarinRooli;

/**
 *
 * @author Omistaja
 */
public class LaskuriPaivittaja {

    private Connection con = null;
    private Statement st = null;
    private Statement st2 = null;
    private Statement st3 = null;
    private Statement st4 = null;
    private Statement st5 = null;
    private Statement st6 = null;
    private Statement st7 = null;
    private Statement st8 = null;
    private Statement st9 = null;
    private Statement st10 = null;
    private Statement st11 = null;
    private Yhteys yhteys = new Yhteys();
    private String sql = "";
    private Tupa ikkuna;

    public LaskuriPaivittaja() {

    }

    public LaskuriPaivittaja(Tupa ikkuna) {
        this.ikkuna = ikkuna;
    }

    public void paivitaLaskurit() {

        try {
            for (int i = 0; i < ikkuna.annaKohteet().size(); i++) {

                if (ikkuna.annaKohteet().get(i) instanceof Sarja) {
                    Sarja sarja = (Sarja) ikkuna.annaKohteet().get(i);
                    sarja.asetaLaskuri(0);

                } else if (ikkuna.annaKohteet().get(i) instanceof Tuomari) {
                    Tuomari tuomari = (Tuomari) ikkuna.annaKohteet().get(i);
                    tuomari.asetaLaskuri(0);

                } else if (ikkuna.annaKohteet().get(i) instanceof Pelaaja) {

                    Pelaaja pelaaja = (Pelaaja) ikkuna.annaKohteet().get(i);
                    pelaaja.asetaLaskuri(0);

                    for (int j = 0; j < pelaaja.annaKaikkiMaalit().size(); j++) {
                        pelaaja.annaKaikkiMaalit().get(j).asetaLaskuri(0);
                    }

                    break;
                } else if (ikkuna.annaKohteet().get(i) instanceof Joukkue) {
                    Joukkue joukkue = (Joukkue) ikkuna.annaKohteet().get(i);
                    joukkue.asetaLaskuri(0);

                    for (int j = 0; j < joukkue.annaOttelut().size(); j++) {
                        Ottelu ottelu = joukkue.annaOttelut().get(j);
                        ottelu.asetaLaskuri(0);
                        ottelu.annaKotiKokoonpano().asetaLaskuri(0);
                        ottelu.annaVierasKokoonpano().asetaLaskuri(0);
                        for (int k = 0; k < ottelu.annaRoolit().size(); k++) {
                            ottelu.annaRoolit().get(k).asetaLaskuri(0);
                        }

                    }

                } else if (ikkuna.annaKohteet().get(i) instanceof Toimihenkilo) {
                    Toimihenkilo toimari = (Toimihenkilo) ikkuna.annaKohteet().get(i);
                    toimari.asetaLaskuri(0);

                }
            }

            con = yhteys.annaYhteys();
            st = con.createStatement();
            st2 = con.createStatement();
            st3 = con.createStatement();
            st4 = con.createStatement();
            st5 = con.createStatement();
            st6 = con.createStatement();
            st7 = con.createStatement();
            st8 = con.createStatement();
            st9 = con.createStatement();
            st10 = con.createStatement();
            st11 = con.createStatement();

            //päivitetään ensin laskurit
            sql = "SELECT * FROM sarja";

            ResultSet s = st.executeQuery(sql);

            while (s.next()) {
                Sarja sarja = new Sarja();
                sarja.kasvataLaskuria();
            }

            sql = "SELECT * FROM tuomari";

            ResultSet t = st2.executeQuery(sql);

            while (t.next()) {
              Tuomari tuomari = new Tuomari();
              tuomari.kasvataLaskuria();
            }

            sql = "SELECT * FROM joukkue";

            ResultSet j = st3.executeQuery(sql);

            while (j.next()) {
               Joukkue joukkue = new Joukkue();
               joukkue.kasvataLaskuria();

            }

            sql = "SELECT * FROM pelaaja";

            ResultSet p = st4.executeQuery(sql);

            while (p.next()) {
                Pelaaja pelaaja = new Pelaaja();
                pelaaja.kasvataLaskuria();
            }
            
                sql = "SELECT * FROM toimari";

            ResultSet to = st5.executeQuery(sql);

            while (to.next()) {
                Toimihenkilo toimari = new Toimihenkilo();
                toimari.kasvataLaskuria();
            }
            
                sql = "SELECT * FROM ottelu";

            ResultSet o = st6.executeQuery(sql);

            while (o.next()) {
               Ottelu ottelu = new Ottelu();
               ottelu.kasvataLaskuria();
            }
                sql = "SELECT * FROM maali";

            ResultSet m = st7.executeQuery(sql);

            while (m.next()) {
             Maali maali = new Maali();
             maali.kasvataLaskuria();
             
            }
                sql = "SELECT * FROM kokoonpano";

            ResultSet k = st8.executeQuery(sql);

            while (k.next()) {
               Kokoonpano kokoonpano = new Kokoonpano();
               kokoonpano.kasvataLaskuria();
            }
                sql = "SELECT * FROM tuomarinrooli";

            ResultSet tr = st9.executeQuery(sql);

            while (tr.next()) {
             TuomarinRooli rooli = new TuomarinRooli();
             rooli.kasvataLaskuria();
            }
            
            

        } catch (SQLException se) {

            se.printStackTrace();
        } catch (Exception e) {

            e.printStackTrace();
        } finally {

            try {
                if (st != null) {
                    con.close();
                }
            } catch (SQLException se) {
            }
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }

    }

}
