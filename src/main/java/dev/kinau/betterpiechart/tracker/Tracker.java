package dev.kinau.betterpiechart.tracker;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public abstract class Tracker<T> {

    private final Map<String, Long> counts = new HashMap<>();

    public void add(T tracked) {
        synchronized (counts) {
            addCount(tracked);
        }
    }

    public void clear() {
        synchronized (counts) {
            counts.clear();
        }
    }

    public Map<String, Long> counts() {
        synchronized (counts) {
            return counts;
        }
    }

    protected void addCount(T tracked) {
        counts.merge(getName(tracked), 1L, Long::sum);
        Optional<String> optTag = getTag(tracked);
        optTag.ifPresent(tag -> counts.merge(tag, 1L, Long::sum));
    }

    public abstract String getName(T tracked);

    public abstract Optional<String> getTag(T tracked);

}
