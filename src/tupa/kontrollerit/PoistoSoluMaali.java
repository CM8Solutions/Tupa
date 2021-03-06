package tupa.kontrollerit;

import com.sun.prism.impl.Disposer.Record;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import tupa.Tupa;
import tupa.data.Maali;
import tupa.nakymat.OtteluNakyma;

/**
 * Luokka, joka muodostaa muokattavan maalitaulukon maalin poistomahdollisuuden.
 *
 * @author Marianne
 * @see Taulukko
 */
public class PoistoSoluMaali extends TableCell<Record, Boolean> {

    final Button cellButton = new Button("X");

    public PoistoSoluMaali() {

    }

    public PoistoSoluMaali(ObservableList<Maali> data, OtteluNakyma nakyma, Tupa ikkuna) {

        cellButton.setId("button-poisto");
        cellButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent t) {

                Maali maali = (Maali) PoistoSoluMaali.this.getTableView().getItems().get(PoistoSoluMaali.this.getIndex());
                if (maali.annaMaalinTekija() != null) {
                    maali.annaMaalinTekija().annaMaaliLista().remove(maali);
                }
                if (maali.annaSyottaja() != null) {
                    maali.annaSyottaja().annaMaaliLista().remove(maali);
                }
                ikkuna.asetaMuutos(true);
                maali.annaOttelu().annaMaalit().remove(maali);
                nakyma.luoOttelunMaalisivu(maali.annaOttelu());
            }
        });
    }

    @Override
    protected void updateItem(Boolean t, boolean empty) {
        super.updateItem(t, empty);
        if (!empty) {
            setGraphic(cellButton);
        }
    }
}
