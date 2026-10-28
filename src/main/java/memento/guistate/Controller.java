
package memento.guistate;

import java.util.ArrayList;
import java.util.List;

public class Controller {

    private final Model model;
    private final Gui gui;

    private final List<IMemento> history;
    private final List<IMemento> redoHistory;

    public Controller(Gui gui) {
        this.model = new Model();
        this.gui = gui;
        this.history = new ArrayList<>();
        this.redoHistory = new ArrayList<>();
    }

    public void setOption(int optionNumber, int choice) {
        saveToHistory();
        model.setOption(optionNumber, choice);
    }

    public int getOption(int optionNumber) {
        return model.getOption(optionNumber);
    }

    public void setIsSelected(boolean isSelected) {
        saveToHistory();
        model.setIsSelected(isSelected);
    }

    public boolean getIsSelected() {
        return model.getIsSelected();
    }

    // Undo: Ctrl+Z
    public void undo() {
        if (!history.isEmpty()) {
            redoHistory.add(model.createMemento());

            IMemento previousState =
                    history.remove(history.size() - 1);

            model.restoreState(previousState);
            gui.updateGui();
        }
    }

    // Redo: Ctrl+Y
    public void redo() {
        if (!redoHistory.isEmpty()) {
            history.add(model.createMemento());

            IMemento nextState =
                    redoHistory.remove(redoHistory.size() - 1);

            model.restoreState(nextState);
            gui.updateGui();
        }
    }

    // Save a new change
    private void saveToHistory() {
        history.add(model.createMemento());
        redoHistory.clear();
    }

    // Return available states for the history window
    public List<IMemento> getHistory() {
        List<IMemento> states = new ArrayList<>(history);
        states.add(model.createMemento());
        return states;
    }

    // Restore a selected history state
    public void restoreFromHistory(int index) {
        if (index < 0 || index >= history.size()) {
            return;
        }

        // Save current state for Undo
        IMemento currentState = model.createMemento();

        List<IMemento> states = new ArrayList<>(history);
        IMemento selectedState = states.get(index);

        history.add(currentState);
        redoHistory.clear();

        model.restoreState(selectedState);
        gui.updateGui();
    }
}


