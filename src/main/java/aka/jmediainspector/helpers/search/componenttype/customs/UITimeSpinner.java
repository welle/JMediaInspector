package aka.jmediainspector.helpers.search.componenttype.customs;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import org.eclipse.jdt.annotation.NonNull;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextFormatter;
import javafx.scene.input.InputEvent;
import javafx.util.StringConverter;

/**
 * Time Spinner component.
 *
 * @author charlottew
 */
public class UITimeSpinner extends Spinner<LocalTime> {

    // Property containing the current editing mode:
    private final ObjectProperty<Mode> mode = new SimpleObjectProperty<>(Mode.HOURS);

    /**
     * Mode represents the unit that is currently being edited.
     * For convenience expose methods for incrementing and decrementing that unit, and for selecting the appropriate portion in a spinner's editor
     */
    enum Mode {
        /**
         * Hours.
         */
        HOURS {
            @Override
            LocalTime increment(final LocalTime time, final int steps) {
                return time.plusHours(steps);
            }

            @Override
            void select(final UITimeSpinner spinner) {
                final int index = spinner.getEditor().getText().indexOf(':');
                spinner.getEditor().selectRange(0, index);
            }
        },
        /**
         * Minutes.
         */
        MINUTES {
            @Override
            LocalTime increment(final LocalTime time, final int steps) {
                return time.plusMinutes(steps);
            }

            @Override
            void select(final UITimeSpinner spinner) {
                final int hrIndex = spinner.getEditor().getText().indexOf(':');
                final int minIndex = spinner.getEditor().getText().indexOf(':', hrIndex + 1);
                spinner.getEditor().selectRange(hrIndex + 1, minIndex);
            }
        },
        /**
         * Seconds.
         */
        SECONDS {
            @Override
            LocalTime increment(final LocalTime time, final int steps) {
                return time.plusSeconds(steps);
            }

            @Override
            void select(final UITimeSpinner spinner) {
                final int index = spinner.getEditor().getText().lastIndexOf(':');
                spinner.getEditor().selectRange(index + 1, spinner.getEditor().getText().length());
            }
        };

        /**
         * Increment given local time.
         *
         * @param time
         * @param steps
         * @return new local time
         */
        abstract LocalTime increment(LocalTime time, int steps);

        /**
         * Select the time spinner.
         *
         * @param spinner
         */
        abstract void select(UITimeSpinner spinner);

        /**
         * Decrement given local time.
         *
         * @param time
         * @param steps
         * @return new local time
         */
        LocalTime decrement(final LocalTime time, final int steps) {
            return increment(time, -steps);
        }
    }

    /**
     * Constructor.
     *
     * @param time starting time
     */
    public UITimeSpinner(@NonNull final LocalTime time) {
        setEditable(true);

        // Create a StringConverter for converting between the text in the
        // editor and the actual value:

        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");

        final StringConverter<LocalTime> localTimeConverter = new StringConverter<LocalTime>() {

            @Override
            public String toString(final LocalTime timeToString) {
                return formatter.format(timeToString);
            }

            @Override
            public LocalTime fromString(final String string) {
                final String[] tokens = string.split(":");
                final int hours = getIntField(tokens, 0);
                final int minutes = getIntField(tokens, 1);
                final int seconds = getIntField(tokens, 2);
                final int totalSeconds = (hours * 60 + minutes) * 60 + seconds;
                return LocalTime.of((totalSeconds / 3600) % 24, (totalSeconds / 60) % 60, seconds % 60);
            }

            private int getIntField(final String[] tokens, final int index) {
                if (tokens.length <= index || tokens[index].isEmpty()) {
                    return 0;
                }
                return Integer.parseInt(tokens[index]);
            }

        };

        // The textFormatter both manages the text <-> LocalTime conversion,
        // and vetoes any edits that are not valid. We just make sure we have
        // two colons and only digits in between:

        final TextFormatter<@NonNull LocalTime> textFormatter = new TextFormatter<>(localTimeConverter, time, c -> {
            final String newText = c.getControlNewText();
            if (newText.matches("[0-9]{0,2}:[0-9]{0,2}:[0-9]{0,2}")) {
                return c;
            }
            return null;
        });

        // The spinner value factory defines increment and decrement by
        // delegating to the current editing mode:

        final SpinnerValueFactory<LocalTime> valueFactory = new SpinnerValueFactory<LocalTime>() {
            {
                setConverter(localTimeConverter);
                setValue(time);
            }

            @Override
            public void decrement(final int steps) {
                setValue(UITimeSpinner.this.mode.get().decrement(getValue(), steps));
                UITimeSpinner.this.mode.get().select(UITimeSpinner.this);
            }

            @Override
            public void increment(final int steps) {
                setValue(UITimeSpinner.this.mode.get().increment(getValue(), steps));
                UITimeSpinner.this.mode.get().select(UITimeSpinner.this);
            }

        };
        valueFactory.setValue(time);
        setValueFactory(valueFactory);
        getEditor().setTextFormatter(textFormatter);

        // Update the mode when the user interacts with the editor.
        // This is a bit of a hack, e.g. calling spinner.getEditor().positionCaret()
        // could result in incorrect state. Directly observing the caretPostion
        // didn't work well though; getting that to work properly might be
        // a better approach in the long run.
        getEditor().addEventHandler(InputEvent.ANY, e -> {
            final int caretPos = this.getEditor().getCaretPosition();
            final int hrIndex = this.getEditor().getText().indexOf(':');
            final int minIndex = this.getEditor().getText().indexOf(':', hrIndex + 1);
            if (caretPos <= hrIndex) {
                this.mode.set(Mode.HOURS);
            } else if (caretPos <= minIndex) {
                this.mode.set(Mode.MINUTES);
            } else {
                this.mode.set(Mode.SECONDS);
            }
        });

        // When the mode changes, select the new portion:
        this.mode.addListener((obs, oldMode, newMode) -> newMode.select(this));

    }

    /**
     * Get Mode property.
     *
     * @return mode property
     */
    public ObjectProperty<Mode> modeProperty() {
        return this.mode;
    }

    /**
     * Get Mode.
     *
     * @return mode
     * @see Mode
     */
    public final Mode getMode() {
        return modeProperty().get();
    }

    /**
     * Set Mode.
     *
     * @param mode
     * @see Mode
     */
    public final void setMode(@NonNull final Mode mode) {
        modeProperty().set(mode);
    }
}
