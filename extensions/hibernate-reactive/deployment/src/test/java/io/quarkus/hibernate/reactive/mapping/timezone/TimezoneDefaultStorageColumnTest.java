package io.quarkus.hibernate.reactive.mapping.timezone;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import io.quarkus.hibernate.reactive.SchemaUtil;
import io.quarkus.test.QuarkusUnitTest;
import io.quarkus.test.vertx.RunOnVertxContext;
import io.quarkus.test.vertx.UniAsserter;

public class TimezoneDefaultStorageColumnTest extends AbstractTimezoneDefaultStorageTest {

    @RegisterExtension
    static QuarkusUnitTest TEST = new QuarkusUnitTest()
            .withApplicationRoot((jar) -> jar
                    .addClasses(EntityWithTimezones.class)
                    .addClasses(SchemaUtil.class))
            .withConfigurationResource("application.properties")
            .overrideConfigKey("quarkus.hibernate-orm.mapping.timezone.default-storage", "column");

    @Test
    public void schema() {
        assertThat(SchemaUtil.getColumnNames(ormSessionFactory, EntityWithTimezones.class))
                .contains("zonedDateTime_tz", "offsetDateTime_tz")
                // For some reason we don't get a TZ column for OffsetTime
                .doesNotContain("offsetTime_tz");
        assertThat(SchemaUtil.getColumnTypeName(ormSessionFactory, EntityWithTimezones.class, "zonedDateTime"))
                .isEqualTo("TIMESTAMP_UTC");
        assertThat(SchemaUtil.getColumnTypeName(ormSessionFactory, EntityWithTimezones.class, "offsetDateTime"))
                .isEqualTo("TIMESTAMP_UTC");
    }

    @Test
    @RunOnVertxContext
    public void persistAndLoad(UniAsserter asserter) {
        // For some reason column storage preserves the offset, but not the zone ID.
        assertPersistedThenLoadedValues(asserter,
                PERSISTED_ZONED_DATE_TIME.withZoneSameInstant(PERSISTED_ZONED_DATE_TIME.getOffset()),
                PERSISTED_OFFSET_DATE_TIME);
    }
}
