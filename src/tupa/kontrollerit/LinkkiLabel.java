package tupa.kontrollerit;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import tupa.Tupa;
import tupa.data.Joukkue;
import tupa.data.Sarja;
import tupa.data.Turnaus;
import tupa.nakymat.JoukkueNakyma;
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

        if (nimi.equals("Etusivu") && !ikkuna.annaAloitus()) {
            ikkuna.asetaValittuTuomari(null);
            nakyma.luoEtusivu();
            return;
        } else if (nimi.equals("Oma joukkue") && !ikkuna.annaAloitus()) {
            ikkuna.asetaValittuTuomari(null);
            Turnaus turnaus = (Turnaus) ikkuna.annaTurnaus();
            Joukkue joukkue = new Joukkue();
            for (int i = 0; i < turnaus.annaSarjat().size(); i++) {
                Sarja sarja = turnaus.annaSarjat().get(i);

                for (int j = 0; j < sarja.annaJoukkueet().size(); j++) {
                    if (sarja.annaJoukkueet().get(j).annaID() == ikkuna.annaJoukkueID()) {
                        joukkue = sarja.annaJoukkueet().get(j);
                    }
                }
            }

            JoukkueNakyma joukkuenakyma = nakyma.annaJoukkuenakyma();
            joukkuenakyma.luoJoukkueSivu(joukkue);
            return;
        } else if (nimi.equals("Unohtunut tunnus/salasana?")) {
            Ohjeistus ohje = new Ohjeistus();
            try {
                ohje.annaUnohtunutOhje();
            } catch (IOException ex) {
                Logger.getLogger(LinkkiLabel.class.getName()).log(Level.SEVERE, null, ex);
            }
            return;
        }

    }

}
