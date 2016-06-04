package tupa.kontrollerit;

import javafx.scene.control.TreeView;
import javafx.scene.control.TreeCell;
import javafx.util.Callback;
import tupa.data.Kohde;
import tupa.Tupa;

/**
 * Luokka, joka liittyy vasemman puolen puuvalikon ominaisuuteen, jonka avulla
 * käyttäjä voi klikata tiettyä sarjaa tai tuomaria, tai niiden pääotsikoita,
 * hiiren oikealla painikkeella.
 *
 * @author Marianne
 * @see PuuSolu
 */
public class PuuSoluTehdas implements Callback<TreeView<Kohde>, TreeCell<Kohde>> {

    private Tupa ikkuna;

    public PuuSoluTehdas(Tupa ikkuna) {
        this.ikkuna = ikkuna;
    }

    @Override
    public TreeCell<Kohde> call(TreeView<Kohde> p) {
        return new PuuSolu(ikkuna);
    }
}
