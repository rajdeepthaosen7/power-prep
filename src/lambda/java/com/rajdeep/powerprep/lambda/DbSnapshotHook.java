package com.rajdeep.powerprep.lambda;

import com.zaxxer.hikari.HikariDataSource;
import org.crac.Core;
import org.crac.Resource;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

/**
 * SnapStart: DB connections open at snapshot time would be dead after a restore,
 * so close them all just before the snapshot. New ones open on the first request.
 */
@Component
@Profile("lambda")
class DbSnapshotHook implements Resource {
    private final DataSource dataSource;

    DbSnapshotHook(DataSource dataSource) {
        this.dataSource = dataSource;
        Core.getGlobalContext().register(this);
    }

    @Override
    public void beforeCheckpoint(org.crac.Context<? extends Resource> ctx) {
        if (dataSource instanceof HikariDataSource h && h.getHikariPoolMXBean() != null) {
            h.getHikariPoolMXBean().softEvictConnections();
        }
    }

    @Override
    public void afterRestore(org.crac.Context<? extends Resource> ctx) { }
}
