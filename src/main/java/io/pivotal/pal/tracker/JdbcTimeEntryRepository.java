package io.pivotal.pal.tracker;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class JdbcTimeEntryRepository implements TimeEntryRepository {

    private final JdbcTemplate jdbc;

    public JdbcTimeEntryRepository(DataSource dataSource) {
        jdbc = new JdbcTemplate(dataSource);
    }

    @Override
    public TimeEntry create(TimeEntry entry) {
        jdbc.update("insert into time_entries (project_id, user_id, date, hours) values (?, ?, ?, ?)",
                entry.getProjectId(), entry.getUserId(), entry.getDate(), entry.getHours());

        long newId = jdbc.queryForObject("select id from time_entries where project_id = ? and user_id = ? and date = ? and hours = ?",
                new Object[] {entry.getProjectId(), entry.getUserId(), entry.getDate(), entry.getHours()}, Long.class);

        entry.setId(newId);

        return entry;
    }

    @Override
    public TimeEntry find(long timeEntryId) {
        List<TimeEntry> results = jdbc.query("select * from time_entries where id = ?", new Object[]{timeEntryId}, new TimeEntryMapper());
        return results.isEmpty() ? null : results.get(0);
    }

    @Override
    public List<TimeEntry> list() {
        return jdbc.query("select * from time_entries", new TimeEntryMapper());
    }

    @Override
    public TimeEntry update(long id, TimeEntry entry) {
        jdbc.update("update time_entries set project_id = ?, user_id = ?, date = ?, hours = ? where id = ?",
                entry.getProjectId(), entry.getUserId(), entry.getDate(), entry.getHours(), id);
        entry.setId(id);
        return entry;
    }

    @Override
    public TimeEntry delete(long timeEntryId) {
        TimeEntry response = find(timeEntryId);
        if (response != null) {
            jdbc.update("delete from time_entries where id = ?", timeEntryId);
        }
        return response;
    }

    private static final class TimeEntryMapper implements RowMapper<TimeEntry> {

        @Override
        public TimeEntry mapRow(ResultSet rs, int rowNum) throws SQLException {
            TimeEntry entry = new TimeEntry();
            entry.setId(rs.getLong("id"));
            entry.setProjectId(rs.getLong("project_id"));
            entry.setUserId(rs.getLong("user_id"));
            entry.setDate(rs.getDate("date").toLocalDate());
            entry.setHours(rs.getInt("hours"));
            return entry;
        }
    }
}
