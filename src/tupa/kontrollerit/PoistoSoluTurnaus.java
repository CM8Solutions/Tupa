package tupa.kontrollerit;

import com.sun.prism.impl.Disposer.Record;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.stage.Stage;
import tupa.Tupa;
import tupa.data.Turnaus;

/**
 *
 * @author Marianne
 */
public class PoistoSoluTurnaus extends TableCell<Record, Boolean> {

    final Button cellButton = new Button("X");

    public PoistoSoluTurnaus() {

    }

    public PoistoSoluTurnaus(ObservableList<Turnaus> data, Tupa ikkuna, Stage stage) {
        cellButton.setId("button-poisto");
        cellButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent t) {

                Turnaus turnaus = (Turnaus) PoistoSoluTurnaus.this.getTableView().getItems().get(PoistoSoluTurnaus.this.getIndex());
                Varmistaja varmistaja = new Varmistaja(ikkuna);
                varmistaja.annaTurnauksenPoistoVarmistus(turnaus);
                stage.close();
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
