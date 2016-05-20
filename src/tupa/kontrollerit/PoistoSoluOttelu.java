package tupa.kontrollerit;

import com.sun.prism.impl.Disposer.Record;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import tupa.data.Ottelu;

/**
 *
 * @author Marianne
 */
public class PoistoSoluOttelu extends TableCell<Record, Boolean> {

    final Button cellButton = new Button("X");

    public PoistoSoluOttelu() {

    }

    public PoistoSoluOttelu(ObservableList<Ottelu> data, Varmistaja varmistaja) {
        cellButton.setId("button-poisto");
        cellButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent t) {

                Ottelu ottelu = (Ottelu) PoistoSoluOttelu.this.getTableView().getItems().get(PoistoSoluOttelu.this.getIndex());

                varmistaja.annaOtteluPoistoVarmistus(ottelu);
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
