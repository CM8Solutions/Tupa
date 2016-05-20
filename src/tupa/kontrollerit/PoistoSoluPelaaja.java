package tupa.kontrollerit;

import com.sun.prism.impl.Disposer.Record;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import tupa.data.Pelaaja;

/**
 *
 * @author Marianne
 */
public class PoistoSoluPelaaja extends TableCell<Record, Boolean> {

    final Button cellButton = new Button("X");

    public PoistoSoluPelaaja() {

    }

    public PoistoSoluPelaaja(ObservableList<Pelaaja> data, Varmistaja varmistaja) {
        cellButton.setId("button-poisto");

        cellButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent t) {

                Pelaaja pelaaja = (Pelaaja) PoistoSoluPelaaja.this.getTableView().getItems().get(PoistoSoluPelaaja.this.getIndex());

                varmistaja.annaPelaajanPoistoVarmistus(pelaaja);
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
