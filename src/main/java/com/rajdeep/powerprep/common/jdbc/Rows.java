package com.rajdeep.powerprep.common.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.RowMapper;

/**
 * Row mappers for Java records: columns map to record components by name
 * ({@code sort_order} or {@code sortOrder} both fill {@code sortOrder}).
 * Adds {@code timestamptz -> Instant}, which the Postgres driver does not do by itself.
 */
public final class Rows {
    private static final Map<Class<?>, RowMapper<?>> CACHE = new ConcurrentHashMap<>();

    private Rows() {}

    @SuppressWarnings("unchecked")
    public static <T> RowMapper<T> of(Class<T> type) {
        return (RowMapper<T>) CACHE.computeIfAbsent(type, InstantAwareMapper::new);
    }

    private static final class InstantAwareMapper<T> extends DataClassRowMapper<T> {
        InstantAwareMapper(Class<T> type) { super(type); }

        @Override
        protected Object getColumnValue(ResultSet rs, int index, Class<?> paramType) throws SQLException {
            if (paramType == Instant.class) {
                OffsetDateTime t = rs.getObject(index, OffsetDateTime.class);
                return t == null ? null : t.toInstant();
            }
            return super.getColumnValue(rs, index, paramType);
        }
    }
}
