
package memento.guistate;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

import java.util.List;

public class Gui extends Application {

    private Controller controller;
    private ColorBox colorBox1;
    private ColorBox colorBox2;
    private ColorBox colorBox3;
    private CheckBox checkBox;

    @Override
    public void start(Stage stage) {

        controller = new Controller(this);

        Insets insets = new Insets(10);

        colorBox1 = new ColorBox(1, controller);
        colorBox2 = new ColorBox(2, controller);
        colorBox3 = new ColorBox(3, controller);

        checkBox = new CheckBox("Click me!");
        checkBox.setPadding(insets);

        HBox hBox = new HBox(
                colorBox1.getRectangle(),
                colorBox2.getRectangle(),
                colorBox3.getRectangle()
        );

        hBox.setSpacing(10);

        HBox.setMargin(colorBox1.getRectangle(), insets);
        HBox.setMargin(colorBox2.getRectangle(), insets);
        HBox.setMargin(colorBox3.getRectangle(), insets);

        Label label = new Label(
                "Ctrl+Z: Undo | Ctrl+Y: Redo"
        );
        label.setPadding(insets);

        Button historyButton = new Button("Show History");
        historyButton.setOnAction(
                event -> showHistoryWindow()
        );

        VBox vBox = new VBox(
                10, hBox, checkBox, label, historyButton
        );
        vBox.setPadding(insets);

        checkBox.setOnAction(event ->
                controller.setIsSelected(checkBox.isSelected())
        );

        Scene scene = new Scene(vBox);

        scene.setOnKeyPressed(event -> {
            if (event.isControlDown()
                    && event.getCode() == KeyCode.Z) {
                controller.undo();
                event.consume();
            } else if (event.isControlDown()
                    && event.getCode() == KeyCode.Y) {
                controller.redo();
                event.consume();
            }
        });

        stage.setScene(scene);
        stage.setTitle("Memento Pattern Example");
        stage.show();
    }

    // Opens a separate window showing saved states
    private void showHistoryWindow() {

        Stage historyStage = new Stage();
        historyStage.setTitle("State History");

        ListView<String> listView = new ListView<>();

        List<IMemento> states = controller.getHistory();

        for (int i = 0; i < states.size(); i++) {
            listView.getItems().add(
                    "State " + i + ": "
                            + states.get(i).getDescription()
            );
        }

        Label instruction = new Label(
                "Select a state to restore it."
        );

        listView.getSelectionModel()
                .selectedIndexProperty()
                .addListener((obs, oldValue, newValue) -> {

                    int index = newValue.intValue();

                    // The last entry represents current state
                    if (index >= 0 && index < states.size() - 1) {
                        controller.restoreFromHistory(index);
                        historyStage.close();
                    }
                });

        VBox root = new VBox(10, instruction, listView);
        root.setPadding(new Insets(15));

        Scene historyScene = new Scene(root, 480, 350);

        historyStage.setScene(historyScene);
        historyStage.show();
    }

    public void updateGui() {
        colorBox1.setColor(controller.getOption(1));
        colorBox2.setColor(controller.getOption(2));
        colorBox3.setColor(controller.getOption(3));
        checkBox.setSelected(controller.getIsSelected());
    }
}

