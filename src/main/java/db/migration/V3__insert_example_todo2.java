package db.migration;

import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

public class V3__insert_example_todo2 extends BaseJavaMigration
{
    @Override
    public void migrate(final Context context)
    {
        new JdbcTemplate(new SingleConnectionDataSource(context.getConnection(), true))
                .execute("INSERT INTO TASKS (description, done) VALUES ('Task from migration v3', true)");

    }
}
