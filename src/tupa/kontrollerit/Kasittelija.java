package tupa.kontrollerit;

import javafx.scene.control.TreeItem;
import tupa.nakymat.PaaNakyma;
import tupa.nakymat.SarjaNakyma;
import tupa.nakymat.TuomariNakyma;
import tupa.kontrollerit.Tiedottaja;
import tupa.Tupa;
import tupa.data.Kohde;
import tupa.data.Sarja;
import tupa.data.Tuomari;

/**
 *
 * @author Marianne
 */
public class Kasittelija {

    private Tupa ikkuna;
    private Tiedottaja tiedottaja;
    private PaaNakyma nakyma;
    private SarjaNakyma sarjanakyma;
    private TuomariNakyma tuomarinakyma;

    public Kasittelija() {

    }

    public Kasittelija(Tupa ikkuna) {
        this.ikkuna = ikkuna;
        tiedottaja = new Tiedottaja(ikkuna);
        nakyma = new PaaNakyma(ikkuna);
        sarjanakyma = nakyma.annaSarjanakyma();
        tuomarinakyma = nakyma.annaTuomarinakyma();
    }

    public void valittuKohde(TreeItem<Kohde> arvo) {

        String ohje = "";
        if (arvo == ikkuna.annaRootSarjat()) {
            ohje = ("Valitse vasemmalta haluamasi sarja tai lisää uusi.");
            nakyma.luoOhje(ohje, arvo);

        } else if (arvo == ikkuna.annaRootTuomarit()) {
            ohje = ("Valitse vasemmalta haluamasi tuomari tai lisää uusi.");
            nakyma.luoOhje(ohje, arvo);

        } else if (arvo.getParent().getValue() instanceof Sarja) {
            sarjanakyma.luoSarjaSivu(arvo);
        } else if (arvo.getParent().getValue() instanceof Tuomari) {
            tuomarinakyma.luoTuomariSivu(arvo);
        }

    }

    public void branchExpended(TreeItem.TreeModificationEvent<Kohde> event) {
        String nodeValue = event.getSource().getValue().toString();

    }

    public void branchCollapsed(TreeItem.TreeModificationEvent<Kohde> event) {
        String nodeValue = event.getSource().getValue().toString();

    }

}
