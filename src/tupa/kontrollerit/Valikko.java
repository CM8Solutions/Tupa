/*
Yläpalkin valikon muodostava luokka 
 */
package tupa.kontrollerit;

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
        
        if(!(ikkuna.annaTaso() == 3 || ikkuna.annaTaso() == 2))
             uusi.setDisable(true);
        
        MenuItem avaa = new MenuItem("Avaa");
        avaa.setAccelerator(new KeyCharacterCombination("O", KeyCombination.SHORTCUT_DOWN));

        MenuItem tallenna = new MenuItem("Tallenna");
        tallenna.setAccelerator(new KeyCharacterCombination("S", KeyCombination.SHORTCUT_DOWN));
    if(!(ikkuna.annaTaso() == 3 || ikkuna.annaTaso() == 2 || ikkuna.annaTaso() == 1))
             tallenna.setDisable(true);
   
        MenuItem vie = new MenuItem("Vie");
        vie.setAccelerator(new KeyCharacterCombination("E", KeyCombination.SHORTCUT_DOWN));
     if(!(ikkuna.annaTaso() == 3 || ikkuna.annaTaso() == 2))
             vie.setDisable(true);
        MenuItem tuo = new MenuItem("Tuo");
        tuo.setAccelerator(new KeyCharacterCombination("I", KeyCombination.SHORTCUT_DOWN));
     if(!(ikkuna.annaTaso() == 3 || ikkuna.annaTaso() == 2))
             tuo.setDisable(true);
     
     MenuItem admin = new MenuItem("Lisää lisenssi");
        admin.setAccelerator(new KeyCharacterCombination("A", KeyCombination.SHORTCUT_DOWN));
     if(!(ikkuna.annaTaso() == 3))
             admin.setDisable(true);
     
     
        MenuItem lopeta = new MenuItem("Lopeta");
        lopeta.setAccelerator(new KeyCharacterCombination("Q", KeyCombination.SHORTCUT_DOWN));

        uusi.setOnAction(this);
        avaa.setOnAction(this);
        tallenna.setOnAction(this);
        vie.setOnAction(this);
        tuo.setOnAction(this);
  admin.setOnAction(this);
        
        lopeta.setOnAction(this);

        menuTiedosto.getItems().addAll(uusi, avaa, tallenna, vie, tuo, new SeparatorMenuItem(), admin, lopeta);

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
            }
            return;
        }

    }

    public void valikostaValittu(String teksti, ActionEvent e) throws InstantiationException, SQLException, IllegalAccessException, ClassNotFoundException {

        switch (teksti) {
            case "Uusi": {
                //tsekataan ensin, onko käyttäjä tehnyt muutoksia
                if (ikkuna.muutettu()) {
                    Varmistaja varmista = new Varmistaja(ikkuna.annaKohteet(), ikkuna);
                    varmista.annaUudenVarmistus();
                } else {
                    
                    //tarkistetaan onko käyttäjällä oikeuksia lisätä vielä uusia turnauksia (max. 5)
                    if(ikkuna.annaTaso() == 2){
                        Tarkistaja tarkistaja = new Tarkistaja(ikkuna, (Turnaus) ikkuna.annaTurnaus());
                    boolean ok = tarkistaja.tarkistaTurnausMaara();
                   
                    if(ok){
                          //tyhjennetään kaikki tiedot 
                    ikkuna.annaKohteet().clear();

                    Aloitus aloitus = new Aloitus();
                    Turnaus turnaus = aloitus.luoAlkuTurnaus();
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
                    else{
                        tiedottaja.annaVaroitus("Sinulla ei ole enää oikeuksia uusien turnausten lisäämiseen. Voit uusia lisenssisi ottamalla yhteyttä TUPA-ohjelman yleiseen ylläpitäjään, ks. (Valikko -> Ohje -> Tietoa ohjelmasta)");
                    }
                        
                    }
                    
                    //yleisellä ylläpitäjällä ei rajoituksia (muilla toiminto ei ole edes käytössä)
                    else{
                        
                              //tyhjennetään kaikki tiedot 
                    ikkuna.annaKohteet().clear();

                    Aloitus aloitus = new Aloitus();
                    Turnaus turnaus = aloitus.luoAlkuTurnaus();
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
                    varmista.annaUudenVarmistus();
                } else {
                    //tyhjennetään kaikki tiedot 
                    ikkuna.annaKohteet().clear();

                    //vielä pitää tyhjentää puu
                    TreeItem<Kohde> parentSarjat = ikkuna.annaRootSarjat();
                    TreeItem<Kohde> parentTuomarit = ikkuna.annaRootTuomarit();
                    parentSarjat.getChildren().clear();
                    parentTuomarit.getChildren().clear();

                    //sitten vasta avaukseen
                    
                    TurnausValitsin valitsija = new TurnausValitsin(ikkuna);
                    valitsija.annaTurnausLuettelo();

                }
                 ikkuna.asetaAloitus(false);
                break;
            }

            case "Tallenna": {
             Tarkistaja tarkistaja = new Tarkistaja(ikkuna, (Turnaus) ikkuna.annaTurnaus());
             tarkistaja.tarkistaTurnaustiedot();
             break;
            }
            
            case "Lisää lisenssi": {
             Kirjautuminen kirjautuja = new Kirjautuminen(ikkuna);
             kirjautuja.luoYleinenHallintaLisays();
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

        }
    }
}
