

package memento.guistate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Memento implements IMemento {

    private final int[] options;
    private final boolean isSelected;
    private final LocalDateTime savedAt;

    public Memento(int[] options, boolean isSelected) {
        this.options = options.clone();
        this.isSelected = isSelected;
        this.savedAt = LocalDateTime.now();

        System.out.println("Memento created");
    }

    public int[] getOptions() {
        return options.clone();
    }

    public boolean isSelected() {
        return isSelected;
    }

    @Override
    public String getDescription() {
        String time = savedAt.format(
                DateTimeFormatter.ofPattern("HH:mm:ss")
        );

        return time
                + " | Colors: "
                + options[0] + ", "
                + options[1] + ", "
                + options[2]
                + " | Checkbox: " + isSelected;
    }

    @Override
    public String toString() {
        return getDescription();
    }
}
