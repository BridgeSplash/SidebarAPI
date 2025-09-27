package net.bridgesplash.sidebar.state;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertEquals;

public final class HookTest {


    @Test
    void testStateSetAndGet() {
        State<Integer> count = new State<>(0);

        assertEquals(0, count.get());
        count.set(5);
        assertEquals(5, count.get());
    }

    @Test
    void testStateNotifiesSubscribers() {
        State<Integer> count = new State<>(0);
        List<Integer> values = new ArrayList<>();

        count.subscribe(values::add);
        count.set(1);
        count.set(2);

        assertEquals(List.of(0, 1, 2), values);
    }

    @Test
    void testUseEffectRunsOnInitAndChange() {
        State<Integer> count = new State<>(0);
        AtomicInteger runs = new AtomicInteger(0);

        Hooks.useEffect(() -> {
            runs.incrementAndGet();
            return null; // no cleanup
        }, count);

        assertEquals(1, runs.get()); // ran once initially
        count.set(1);
        count.set(2);
        assertEquals(3, runs.get());
    }

    @Test
    void testUseEffectRunsCleanup() {
        State<Integer> count = new State<>(0);
        List<String> log = new ArrayList<>();

        Hooks.useEffect(() -> {
            final int current = count.get();
            log.add("Effect " + current);
            return () -> log.add("Cleanup " + current);
        }, count);

        count.set(1);
        count.set(2);

        assertEquals(List.of(
                "Effect 0",
                "Cleanup 0",
                "Effect 1",
                "Cleanup 1",
                "Effect 2"
        ), log);
    }

    @Test
    void testUseMemoRecomputesOnlyWhenDepsChange() {
        State<Integer> count = new State<>(0);
        AtomicInteger computeRuns = new AtomicInteger(0);

        State<String> memo = Memos.useMemo(() -> {
            computeRuns.incrementAndGet();
            return "val=" + count.get();
        }, count);

        assertEquals("val=0", memo.get());
        assertEquals(1, computeRuns.get());

        // same value, should not recompute
        count.set(0);
        assertEquals(1, computeRuns.get());

        // different value, recomputes
        count.set(1);
        assertEquals("val=1", memo.get());
        assertEquals(2, computeRuns.get());
    }

    @Test
    void testUseCallbackUpdatesWhenDepsChange() {
        State<Integer> count = new State<>(0);

        Supplier<String> callback = Memos.useCallback(
                () -> "Count=" + count.get(),
                count
        );

        assertEquals("Count=0", callback.get());

        count.set(1);
        assertEquals("Count=1", callback.get());
    }

}
