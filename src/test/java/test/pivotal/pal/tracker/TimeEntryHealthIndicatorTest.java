package test.pivotal.pal.tracker;

import io.pivotal.pal.tracker.TimeEntry;
import io.pivotal.pal.tracker.TimeEntryHealthIndicator;
import io.pivotal.pal.tracker.TimeEntryRepository;
import org.junit.Test;
import org.springframework.boot.actuate.health.Health;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TimeEntryHealthIndicatorTest {

    private TimeEntryHealthIndicator healthIndicator;

    @Test
    public void testHealthStatusUp() {
        TimeEntryRepository repository = mock(TimeEntryRepository.class);

        when(repository.list()).thenReturn(Collections.emptyList());

        healthIndicator = new TimeEntryHealthIndicator(repository);

        Health health = healthIndicator.health();

        assertThat(health.getStatus().getCode()).isEqualTo("UP");
    }

    @Test
    public void testHealthStatusDown() {
        TimeEntryRepository repository = mock(TimeEntryRepository.class);

        List<TimeEntry> timeEntryList = new ArrayList<>();
        for (int i = 0; i < 5; ++i) {
            timeEntryList.add(new TimeEntry());
        }

        when(repository.list()).thenReturn(timeEntryList);

        healthIndicator = new TimeEntryHealthIndicator(repository);

        Health health = healthIndicator.health();

        assertThat(health.getStatus().getCode()).isEqualTo("DOWN");
    }
}
