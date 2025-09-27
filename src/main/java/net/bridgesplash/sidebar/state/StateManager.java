package net.bridgesplash.sidebar.state;

import java.util.concurrent.ConcurrentHashMap;
import net.bridgesplash.sidebar.sidebar.CustomSidebar;

/**
 * Manages the state instances.
 */
public final class StateManager {

    // state_id -> State
    private final ConcurrentHashMap<String, State<?>> states = new ConcurrentHashMap<>();
    private final CustomSidebar sidebar;

    public StateManager(CustomSidebar sidebar) {
        this.sidebar = sidebar;
    }

    public State<?> getState(String key) {
        return states.get(key);
    }

    public void putState(String key, State<?> state) {
        states.put(key, state);
    }

}
