package tupa.kontrollerit;

import com.sun.prism.impl.Disposer.Record;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import tupa.data.Toimihenkilo;

/**
 * Luokka, joka muodostaa muokattavan toimihenkilötaulukon toimihenkilön
 * poistomahdollisuuden.
 *
 * @author Marianne
 * @see Taulukko
 */
public class PoistoSoluToimari extends TableCell<Record, Boolean> {

    final Button cellButton = new Button("X");

    public PoistoSoluToimari() {

    }

    public PoistoSoluToimari(ObservableList<Toimihenkilo> data, Varmistaja varmistaja) {

        cellButton.setId("button-poisto");
        cellButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent t) {

                Toimihenkilo toimari = (Toimihenkilo) PoistoSoluToimari.this.getTableView().getItems().get(PoistoSoluToimari.this.getIndex());

                varmistaja.annaToimarinPoistoVarmistus(toimari);
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
