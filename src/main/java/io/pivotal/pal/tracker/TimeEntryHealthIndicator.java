package io.pivotal.pal.tracker;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
public class TimeEntryHealthIndicator implements HealthIndicator {
    private static final int MAX_TIME_ENTRIES = 5;

    private final TimeEntryRepository repository;

    public TimeEntryHealthIndicator(TimeEntryRepository repository) {
        this.repository = repository;
    }

    @Override
    public Health health() {
        Health.Builder healthBuilder = new Health.Builder();
        int count = repository.list().size();

        if (count < MAX_TIME_ENTRIES) {
            healthBuilder.up();
        } else {
            healthBuilder.down();
        }

        return healthBuilder.build();
    }
}
