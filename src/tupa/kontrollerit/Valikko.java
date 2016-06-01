/*
Yläpalkin valikon muodostava luokka 
 */
package tupa.kontrollerit;

import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.TreeItem;
import javafx.scene.input.KeyCharacterCombination;
import javafx.scene.input.KeyCombination;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import tupa.Tupa;
import tupa.nakymat.PaaNakyma;
import tupa.data.Kohde;
import tupa.data.Turnaus;

/**
 *
 * @author Marianne
 */
public class Valikko implements EventHandler<ActionEvent> {

    private MenuBar menuBar;
    private Tupa ikkuna;
    private Tiedottaja tiedottaja;
    private PaaNakyma nakyma;

    public Valikko() {

    }

    public Valikko(MenuBar menu, Tupa ikkuna) {

        this.menuBar = menu;
        this.ikkuna = ikkuna;
        tiedottaja = new Tiedottaja(ikkuna);
        nakyma = new PaaNakyma(ikkuna);
    }

    public MenuBar rakennaValikko() {

        Menu menuTiedosto = new Menu("Tiedosto");
        MenuItem uusi = new MenuItem("Uusi");
        uusi.setAccelerator(new KeyCharacterCombination("N", KeyCombination.SHORTCUT_DOWN));

        if (!(ikkuna.annaTaso() == 3 || ikkuna.annaTaso() == 2)) {
            uusi.setDisable(true);
        }

        MenuItem avaa = new MenuItem("Avaa");
        avaa.setAccelerator(new KeyCharacterCombination("O", KeyCombination.SHORTCUT_DOWN));

        MenuItem tallenna = new MenuItem("Tallenna");
        tallenna.setAccelerator(new KeyCharacterCombination("S", KeyCombination.SHORTCUT_DOWN));
        if (!(ikkuna.annaTaso() == 3 || ikkuna.annaTaso() == 2 || ikkuna.annaTaso() == 1)) {
            tallenna.setDisable(true);
        }

        MenuItem vie = new MenuItem("Vie");
        vie.setAccelerator(new KeyCharacterCombination("E", KeyCombination.SHORTCUT_DOWN));
        if (!(ikkuna.annaTaso() == 3 || ikkuna.annaTaso() == 2)) {
            vie.setDisable(true);
        }
        MenuItem tuo = new MenuItem("Tuo");
        tuo.setAccelerator(new KeyCharacterCombination("I", KeyCombination.SHORTCUT_DOWN));
        if (!(ikkuna.annaTaso() == 3 || ikkuna.annaTaso() == 2)) {
            tuo.setDisable(true);
        }

        MenuItem admin = new MenuItem("Lisää lisenssi");
        admin.setAccelerator(new KeyCharacterCombination("A", KeyCombination.SHORTCUT_DOWN));
        if (!(ikkuna.annaTaso() == 3)) {
            admin.setDisable(true);
        }

        MenuItem admin2 = new MenuItem("Palauta tunnus");
        admin2.setAccelerator(new KeyCharacterCombination("P", KeyCombination.SHORTCUT_DOWN));
        if (!(ikkuna.annaTaso() == 3)) {
            admin2.setDisable(true);
        }

        MenuItem lopeta = new MenuItem("Lopeta");
        lopeta.setAccelerator(new KeyCharacterCombination("Q", KeyCombination.SHORTCUT_DOWN));

        uusi.setOnAction(this);
        avaa.setOnAction(this);
        tallenna.setOnAction(this);
        vie.setOnAction(this);
        tuo.setOnAction(this);
        admin.setOnAction(this);
        admin2.setOnAction(this);

        lopeta.setOnAction(this);

        menuTiedosto.getItems().addAll(uusi, avaa, tallenna, vie, tuo, new SeparatorMenuItem(), admin, admin2, lopeta);

        Menu menuOhje = new Menu("Ohje");
        MenuItem ohje = new MenuItem("Ohje");
        MenuItem tietoa = new MenuItem("Tietoa ohjelmasta");
        ohje.setAccelerator(new KeyCharacterCombination("F8"));
        tietoa.setAccelerator(new KeyCharacterCombination("F9"));
        ohje.setOnAction(this);
        tietoa.setOnAction(this);
        menuOhje.getItems().addAll(ohje, new SeparatorMenuItem(), tietoa);

        menuBar.getMenus().addAll(menuTiedosto, menuOhje);

        return menuBar;
    }

    @Override
    public void handle(ActionEvent e) {
        Object lahde = e.getSource();

        if (lahde instanceof MenuItem) {

            try {
                valikostaValittu(
                        ((MenuItem) lahde).getText(), e);
            } catch (InstantiationException ex) {
                Logger.getLogger(Valikko.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SQLException ex) {
                Logger.getLogger(Valikko.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalAccessException ex) {
                Logger.getLogger(Valikko.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(Valikko.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(Valikko.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InterruptedException ex) {
                Logger.getLogger(Valikko.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ParserConfigurationException ex) {
                Logger.getLogger(Valikko.class.getName()).log(Level.SEVERE, null, ex);
            } catch (TransformerException ex) {
                Logger.getLogger(Valikko.class.getName()).log(Level.SEVERE, null, ex);
            }
            return;
        }

    }

    public void valikostaValittu(String teksti, ActionEvent e) throws InstantiationException, SQLException, IllegalAccessException, ClassNotFoundException, IOException, InterruptedException, ParserConfigurationException, TransformerException {

        switch (teksti) {
            case "Uusi": {
                boolean ok = true;
                if (ikkuna.annaTaso() == 2) {
                    Tarkistaja tarkistaja = new Tarkistaja(ikkuna, (Turnaus) ikkuna.annaTurnaus());
                    ok = tarkistaja.tarkistaTurnausMaara();

                }
                if (!ok) {
                    tiedottaja.annaVirhe("Olet luonut maksimimäärän turnauksia. Voit uusia lisenssisi ottamalla yhteyttä TUPA-ohjelman yleiseen ylläpitäjään, ks. (Valikko -> Ohje -> Tietoa ohjelmasta)");
                } else if (ok) {
                    if (ikkuna.muutettu()) {
                        Varmistaja varmista = new Varmistaja(ikkuna.annaKohteet(), ikkuna);
                        varmista.annaUudenVarmistus();

                    } else {
                        Turnaus turnausv = (Turnaus)ikkuna.annaTurnaus();
                        LaskuriPaivittaja paivittaja = new LaskuriPaivittaja(turnausv, ikkuna);
                        paivittaja.paivitaLaskurit();

                        ikkuna.annaKohteet().clear();
                        ikkuna.annaTuomaritk().clear();
                        ikkuna.annaSarjatk().clear();
                      
                        Turnaus turnaus = new Turnaus();
                        turnaus.kasvataLaskuria();
                        turnaus.asetaID(turnaus.annaID());
                               turnaus.asetaNimi("Uusi turnaus");
                    
                        Kohde uusiTurnaus = (Kohde) turnaus;
                        ikkuna.asetaTurnaus(uusiTurnaus);
                        ikkuna.annaKohteet().add(uusiTurnaus);

                        //vielä pitää tyhjentää puu
                        TreeItem<Kohde> parentSarjat = ikkuna.annaRootSarjat();
                        TreeItem<Kohde> parentTuomarit = ikkuna.annaRootTuomarit();
                        parentSarjat.getChildren().clear();
                        parentTuomarit.getChildren().clear();

                        nakyma.luoEtusivu();
                        tiedottaja.kirjoitaLoki("Uusi turnaus avattu.");
                        ikkuna.asetaAloitus(false);
                    }
                }
                break;
            }
            case "Avaa": {
                //tsekataan ensin, onko käyttäjä tehnyt muutoksia

                if (ikkuna.muutettu()) {
                    Varmistaja varmista = new Varmistaja(ikkuna.annaKohteet(), ikkuna);
                    varmista.annaAvausVarmistus();

                } else {

                    ikkuna.annaKohteet().clear();
                    ikkuna.annaTuomaritk().clear();
                    ikkuna.annaSarjatk().clear();
                    //vielä pitää tyhjentää puu
                    TreeItem<Kohde> parentSarjat = ikkuna.annaRootSarjat();
                    TreeItem<Kohde> parentTuomarit = ikkuna.annaRootTuomarit();
                    parentSarjat.getChildren().clear();
                    parentTuomarit.getChildren().clear();

                    //sitten vasta avaukseen
                    TurnausValitsin valitsija = new TurnausValitsin(ikkuna);
                    valitsija.annaTurnausLuettelo();

                }

                break;
            }

            case "Tallenna": {

                if (!ikkuna.annaAloitus()) {
                    Tarkistaja tarkistaja = new Tarkistaja(ikkuna, (Turnaus) ikkuna.annaTurnaus());
                    tarkistaja.tarkistaTurnaustiedot(true, false, false);

                } else {
                    tiedottaja.annaIlmoitus("Avaa ensin turnaus tai luo uusi.");

                }
                break;
            }

            case "Lisää lisenssi": {
                Kirjautuminen kirjautuja = new Kirjautuminen(ikkuna);
                kirjautuja.luoYleinenHallintaLisays();
                break;
            }

            case "Palauta tunnus": {

                Kirjautuminen kirjautuja = new Kirjautuminen(ikkuna);
                kirjautuja.luoTunnuksenPalautus();
                break;
            }

            case "Lopeta": {

                if (ikkuna.muutettu()) {

                    Varmistaja varmista = new Varmistaja(ikkuna.annaKohteet(), ikkuna);
                    varmista.annaVarmistus();
                } else {
                    Platform.exit();
                }
                break;
            }
            case "Ohje": {

                Ohjeistus ohje = new Ohjeistus();
                ohje.annaYleisOhje();
                break;
            }
            case "Tietoa ohjelmasta": {

                Ohjeistus ohje = new Ohjeistus();
                ohje.annaTietoa();
                break;
            }
            case "Tuo": {

                if (!ikkuna.annaAloitus()) {

                    TuomariValitsin valitsin = new TuomariValitsin(ikkuna);
                    valitsin.annaTuomariLuetteloTuotavat();

                } else {
                    tiedottaja.annaIlmoitus("Avaa ensin turnaus tai luo uusi.");

                }
                break;
            }

            case "Vie": {

                if (!ikkuna.annaAloitus()) {

                    if (ikkuna.muutettu()) {
                        Tallennus tallentaja = new Tallennus();
                        tallentaja.suoritaTallennus(true, false, false);

                    }

                    TuomariValitsin valitsin = new TuomariValitsin(ikkuna);
                    valitsin.annaTuomariLuetteloVietavat();

                } else {
                    tiedottaja.annaIlmoitus("Avaa ensin turnaus tai luo uusi.");

                }

                break;
            }

        }
    }
}
