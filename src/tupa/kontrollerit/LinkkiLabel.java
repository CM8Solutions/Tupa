package tupa.kontrollerit;

import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import tupa.Tupa;
import tupa.nakymat.PaaNakyma;

/**
 *
 * @author Marianne
 */
public class LinkkiLabel extends Label implements EventHandler<MouseEvent> {

    private String nimi;
    private Tupa ikkuna;
    private PaaNakyma nakyma;

    public LinkkiLabel() {
        this.getStyleClass().add("linkkilabel");

    }

    public LinkkiLabel(Tupa ikkuna) {
        this.getStyleClass().add("linkkilabel");
        this.ikkuna = ikkuna;
        nakyma = new PaaNakyma(ikkuna);
    }

    public void linkkiaKlikattu() {
        setOnMouseClicked(this);
    }

    @Override
    public void handle(MouseEvent e) {
        nimi = this.getText();

        if (nimi.equals("Etusivu")) {
            nakyma.luoEtusivu();
            return;
        }

    }

}
