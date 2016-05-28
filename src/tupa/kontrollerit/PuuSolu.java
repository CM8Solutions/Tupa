package tupa.kontrollerit;

/**
 *
 * @author Marianne
 */
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.TreeCell;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import tupa.data.Kohde;
import tupa.data.Sarja;
import tupa.data.Tuomari;
import tupa.nakymat.SarjaNakyma;
import tupa.nakymat.TuomariNakyma;
import tupa.Tupa;

public class PuuSolu extends TreeCell<Kohde> {

    private Tupa ikkuna;

    public PuuSolu(Tupa ikkuna) {
        this.ikkuna = ikkuna;
    }

    @Override
    public void updateItem(Kohde item, boolean empty) {
        super.updateItem(item, empty);

        if (empty) {
            setText(null);
            setGraphic(null);
            setContextMenu(null);
        } else {
            setText(item.toString());
            setGraphic(getTreeItem().getGraphic());

            //treeitemien sisältö pitää tutkia
            //käyttäjäoikeudet tulee tutkia
//			
            boolean onJuuri = false;

            if (ikkuna.annaRootSarjat().getValue().equals(item) || ikkuna.annaRootTuomarit().getValue().equals(item)) {
                onJuuri = true;
            }

            if (!onJuuri && (item instanceof Sarja)) {
                ContextMenu valikko = rakennaSolmuValikkoSarja(item);
                setContextMenu(valikko);
            } else if (!onJuuri && (item instanceof Tuomari)) {
                ContextMenu valikko = rakennaSolmuValikkoTuomari(item);
                setContextMenu(valikko);
            } else if (onJuuri && (item instanceof Sarja)) {
                ContextMenu valikko = rakennaJuuriValikkoSarja(item);
                setContextMenu(valikko);
            } else if (onJuuri && (item instanceof Tuomari)) {
                ContextMenu valikko = rakennaJuuriValikkoTuomari(item);
                setContextMenu(valikko);
            }
        }
    }

    private ContextMenu rakennaSolmuValikkoTuomari(Kohde item) {
        ContextMenu menu = new ContextMenu();

        MenuItem item2 = new MenuItem("Muokkaa");
        menu.getItems().add(item2);
        if (!(ikkuna.annaTaso() == 3 || ikkuna.annaTaso() == 2)) {
            item2.setDisable(true);
        }
        MenuItem item5 = new MenuItem("Poista");
        menu.getItems().add(item5);
        if (!(ikkuna.annaTaso() == 3 || ikkuna.annaTaso() == 2)) {
            item5.setDisable(true);
        }
        Varmistaja varmistaja = new Varmistaja(ikkuna, ikkuna.annaPaaNakyma());

        item5.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                varmistaja.annaPoistoVarmistus(item);

            }
        });

        //tiedostoon tallennus
        MenuItem item3 = new MenuItem("Vie");
        menu.getItems().add(item3);
        if (!(ikkuna.annaTaso() == 3 || ikkuna.annaTaso() == 2)) {
            item3.setDisable(true);
        }

        item2.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Tuomari tuomari = (Tuomari) item;
                TuomariNakyma tuomarinakyma = new TuomariNakyma();
                tuomarinakyma = ikkuna.annaPaaNakyma().annaTuomarinakyma();
                tuomarinakyma.luoTuomariMuokkaus(tuomari);

            }
        });
        item3.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                //TIETOKANTA --> TIEDOSTO
            }
        });

        return menu;
    }

    private ContextMenu rakennaJuuriValikkoTuomari(Kohde item) {
        ContextMenu menu = new ContextMenu();

        MenuItem item1 = new MenuItem("Lisää");
        menu.getItems().add(item1);
        if (!(ikkuna.annaTaso() == 3 || ikkuna.annaTaso() == 2)) {
            item1.setDisable(true);
        }

        //tiedostosta luku, VAIN PÄÄITEMIIN
        MenuItem item4 = new MenuItem("Tuo");
        menu.getItems().add(item4);
        if (!(ikkuna.annaTaso() == 3 || ikkuna.annaTaso() == 2)) {
            item4.setDisable(true);
        }

        item1.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (ikkuna.annaAloitus()) {
                    Tiedottaja tiedottaja = new Tiedottaja(ikkuna);
                    tiedottaja.annaIlmoitus("Avaa ensin turnaus tai luo uusi.");

                } else {
                    TuomariNakyma tuomarinakyma = new TuomariNakyma();
                    tuomarinakyma = ikkuna.annaPaaNakyma().annaTuomarinakyma();
                    tuomarinakyma.luoTuomarinLisaysSivu();
                }

            }
        });

        item4.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (ikkuna.annaAloitus()) {
                    Tiedottaja tiedottaja = new Tiedottaja(ikkuna);
                    tiedottaja.annaIlmoitus("Avaa ensin turnaus tai luo uusi.");

                }
            }
        });

        return menu;
    }

    private ContextMenu rakennaSolmuValikkoSarja(Kohde item) {
        ContextMenu menu = new ContextMenu();

        MenuItem item2 = new MenuItem("Muokkaa");
        menu.getItems().add(item2);
        if (!(ikkuna.annaTaso() == 3 || ikkuna.annaTaso() == 2)) {
            item2.setDisable(true);
        }
        MenuItem item5 = new MenuItem("Poista");
        menu.getItems().add(item5);
        if (!(ikkuna.annaTaso() == 3 || ikkuna.annaTaso() == 2)) {
            item5.setDisable(true);
        }
        Varmistaja varmistaja = new Varmistaja(ikkuna, ikkuna.annaPaaNakyma());

        item5.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                varmistaja.annaPoistoVarmistus(item);

            }
        });

        item2.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                Sarja sarja = (Sarja) item;
                SarjaNakyma sarjanakyma = new SarjaNakyma();
                sarjanakyma = ikkuna.annaPaaNakyma().annaSarjanakyma();
                sarjanakyma.luoSarjaMuokkaus(sarja);

            }
        });

        return menu;
    }

    private ContextMenu rakennaJuuriValikkoSarja(Kohde item) {
        ContextMenu menu = new ContextMenu();

        MenuItem item1 = new MenuItem("Lisää");
        menu.getItems().add(item1);
        if (!(ikkuna.annaTaso() == 3 || ikkuna.annaTaso() == 2)) {
            item1.setDisable(true);
        }

        item1.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (ikkuna.annaAloitus()) {
                    Tiedottaja tiedottaja = new Tiedottaja(ikkuna);
                    tiedottaja.annaIlmoitus("Avaa ensin turnaus tai luo uusi.");

                } else {
                    SarjaNakyma sarjanakyma = new SarjaNakyma();
                    sarjanakyma = ikkuna.annaPaaNakyma().annaSarjanakyma();
                    sarjanakyma.luoSarjanLisaysSivu();
                }

            }
        });

        return menu;
    }

}
