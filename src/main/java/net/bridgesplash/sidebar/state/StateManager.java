package net.bridgesplash.sidebar.state;

import java.util.concurrent.ConcurrentHashMap;
import net.bridgesplash.sidebar.sidebar.CustomSidebar;
import org.jetbrains.annotations.Nullable;

/**
 * Manages the state instances.
 */
public final class StateManager {

    // state_id -> State
    private final ConcurrentHashMap<String, State<?>> states = new ConcurrentHashMap<>();
    private final CustomSidebar sidebar;

    /**
     * Constructs a new StateManager for the given sidebar.
     *
     * @param sidebar A sidebar to connect the state to
     */
    public StateManager(CustomSidebar sidebar) {
        this.sidebar = sidebar;
    }

    /**
     * Gets the state for the given key.
     *
     * @param key The unique key for the state
     * @return The state if it exists, null otherwise
     */
    @Nullable
    public State<?> getState(String key) {
        return states.get(key);
    }

    /**
     * Adds a state to the manager.
     *
     * @param key The unique key for the state
     * @param state State of any type
     */
    public void putState(String key, State<?> state) {
        states.put(key, state);
    }

}
