package tupa.kontrollerit;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Date;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableCell;
import tupa.data.Ottelu;

/**
 * Luokka, joka muodostaa muokattavan ottelutaulukon sen solun, jossa voi
 * muokata ottelun ajankohtaa (päivämäärää).
 *
 * @author Marianne
 * @see Taulukko
 */
class PaivaMuokkausSolu extends TableCell<Ottelu, Date> {

    private DatePicker datePicker;

    public PaivaMuokkausSolu() {
    }

    @Override
    public void startEdit() {
        if (!isEmpty()) {
            super.startEdit();
            createDatePicker();
            setText(null);
            setGraphic(datePicker);
        }
    }

    @Override
    public void cancelEdit() {
        super.cancelEdit();

        setText(getDate().toString());
        setGraphic(null);
    }

    @Override
    public void updateItem(Date item, boolean empty) {
        super.updateItem(item, empty);

        if (empty) {
            setText(null);
            setGraphic(null);
        } else if (isEditing()) {
            if (datePicker != null) {
                datePicker.setValue(getDate());
            }
            setText(null);
            setGraphic(datePicker);
        } else {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

            setText(getDate().format(formatter));
            setGraphic(null);
        }
    }

    private void createDatePicker() {
        datePicker = new DatePicker(getDate());
        datePicker.setMinWidth(this.getWidth() - this.getGraphicTextGap() * 20);
        datePicker.setOnAction((e) -> {

            commitEdit(Date.from(datePicker.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));
        });
//            datePicker.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
//                if (!newValue) {
//                    commitEdit(Date.from(datePicker.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));
//                }
//            });
    }

    private LocalDate getDate() {
        return getItem() == null ? LocalDate.now() : getItem().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }
}
