package net.bridgesplash.sidebar.state;

import java.util.function.Supplier;

/**
 * Utility class providing hook-like functions for managing
 * side effects in response to state changes.
 */
public class Hooks {

    /**
     * Runs the given effect whenever any of the specified dependencies change.
     * The effect should return a cleanup {@link Runnable}
     *
     * <p>
     * The effect is run immediately, and then again whenever any dependency changes.
     * The previous cleanup (if any) is run before the new effect is executed.
     * </p>
     *
     * @param effect a supplier that returns a cleanup runnable
     * @param deps the dependencies to watch for changes
     */
    public static void useEffect(Supplier<Runnable> effect, State<?>... deps) {
        final Runnable[] cleanup = {null};

        Runnable runEffect = () -> {
            if (cleanup[0] != null) {
                cleanup[0].run();
            }
            cleanup[0] = effect.get();
        };

        // Run immediately
        runEffect.run();

        // Subscribe to each dep
        for (State<?> dep : deps) {
            dep.subscribeSilent(v -> runEffect.run());
        }
    }

}