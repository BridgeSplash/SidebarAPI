package net.bridgesplash.sidebar.state;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Represents a mutable state container that notifies listeners on value changes.
 *
 * @param <T> the type of the value held by this state
 */
public class State<T> {

    /**
     * The current value of the state.
     */
    private T value;
    /**
     * Listeners subscribed to value changes.
     */
    private final List<Consumer<T>> listeners = new ArrayList<>();

    /**
     * Constructs a new State with the given initial value.
     *
     * @param initialValue the initial value of the state
     */
    public State(T initialValue) {
        this.value = initialValue;
    }

    /**
     * Returns the current value of the state.
     *
     * @return the current value
     */
    public T get() {
        return value;
    }

    /**
     * Sets the value of the state. Notifies listeners if the value changes.
     *
     * @param newValue the new value to set
     */
    public void set(T newValue) {
        if (!Objects.equals(value, newValue)) {
            value = newValue;
            notifyListeners();
        }
    }

    public void setPrev(Function<T, T> update) {
        set(update.apply(get()));
    }


    /**
     * Subscribes a listener to value changes.
     * The listener is called immediately with the current value.
     *
     * @param listener the listener to subscribe
     */
    public void subscribe(Consumer<T> listener) {
        listeners.add(listener);
        listener.accept(value); // run once immediately
    }

    /**
     * Subscribes a listener to value changes without an immediate call.
     *
     * @param listener the listener to subscribe
     */
    public void subscribeSilent(Consumer<T> listener) {
        listeners.add(listener); // no immediate call
    }

    /**
     * Unsubscribes a listener from value changes.
     *
     * @param listener the listener to unsubscribe
     */
    public void unsubscribe(Consumer<T> listener) {
        listeners.remove(listener);
    }

    /**
     * Notifies all listeners of the current value.
     */
    private void notifyListeners() {
        for (Consumer<T> l : new ArrayList<>(listeners)) {
            l.accept(value);
        }
    }
}
