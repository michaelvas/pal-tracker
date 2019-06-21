package io.pivotal.pal.tracker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryTimeEntryRepository implements TimeEntryRepository {

    private Map<Long, TimeEntry> dataStore = new HashMap<>();

    private long id = 0;


    public TimeEntry create(TimeEntry timeEntry) {
        timeEntry.setId(++id);
        dataStore.put(id, timeEntry);
        return timeEntry;
    }

    public TimeEntry find(long id) {
        return dataStore.get(id);
    }

    public List<TimeEntry> list() {
        return new ArrayList<>(dataStore.values());
    }

    public TimeEntry update(long id, TimeEntry timeEntry) {
        TimeEntry updated = null;
        if (dataStore.containsKey(id)) {
            dataStore.put(id, timeEntry);
            timeEntry.setId(id);
            updated = timeEntry;
        }
        return updated;
    }

    public TimeEntry delete(long id) {
        return dataStore.remove(id);
    }
}
