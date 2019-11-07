package cn.choleece.base.db.pool.hikari.metrics.micrometer;

import cn.choleece.base.db.pool.hikari.metrics.IMetricsTracker;
import cn.choleece.base.db.pool.hikari.metrics.MetricsTrackerFactory;
import cn.choleece.base.db.pool.hikari.metrics.PoolStats;
import io.micrometer.core.instrument.MeterRegistry;

public class MicrometerMetricsTrackerFactory implements MetricsTrackerFactory
{

   private final MeterRegistry registry;

   public MicrometerMetricsTrackerFactory(MeterRegistry registry)
   {
      this.registry = registry;
   }

   @Override
   public IMetricsTracker create(String poolName, PoolStats poolStats)
   {
      return new MicrometerMetricsTracker(poolName, poolStats, registry);
   }
}
