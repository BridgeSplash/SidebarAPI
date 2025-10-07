package net.bridgesplash.sidebar.state;

import java.util.function.Supplier;

/**
 * Utility class for creating memoized state and callback suppliers based on dependencies.
 */
public final class Memos {

    private Memos() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Returns a memoized {@link State} whose value is recomputed whenever any dependency changes.
     *
     * @param compute the supplier to compute the value
     * @param deps the dependencies to watch for changes
     * @param <T> the type of the value
     * @return a memoized state that updates when dependencies change
     */
    public static <T> State<T> useMemo(Supplier<T> compute, State<?>... deps) {
        State<T> memoized = new State<>(compute.get());

        Runnable recompute = () -> memoized.set(compute.get());
        for (State<?> dep : deps) {
            dep.subscribeSilent(v -> recompute.run()); // no initial trigger
        }

        return memoized;
    }

    /**
     * Returns a memoized {@link Supplier} that is updated whenever any dependency changes.
     *
     * @param fn the supplier to memoize
     * @param deps the dependencies to watch for changes
     * @param <T> the type of the value returned by the supplier
     * @return a memoized supplier that updates when dependencies change
     */
    public static <T> Supplier<T> useCallback(Supplier<T> fn, State<?>... deps) {
        State<Supplier<T>> memoized = new State<>(fn);

        Runnable recompute = () -> memoized.set(fn);
        for (State<?> dep : deps) {
            dep.subscribeSilent(v -> recompute.run()); // no initial trigger
        }

        return () -> memoized.get().get();
    }
}
