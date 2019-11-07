package cn.choleece.base.db.pool.hikari.hibernate;

import cn.choleece.base.db.pool.hikari.HikariConfig;
import org.hibernate.cfg.AvailableSettings;

import java.util.Map;
import java.util.Properties;

/**
 * Utility class to map Hibernate properties to HikariCP configuration properties.
 *
 * @author Brett Wooldridge, Luca Burgazzoli
 */
public class HikariConfigurationUtil
{
   public static final String CONFIG_PREFIX = "hibernate.hikari.";
   public static final String CONFIG_PREFIX_DATASOURCE = "hibernate.hikari.dataSource.";

   /**
    * Create/load a HikariConfig from Hibernate properties.
    *
    * @param props a map of Hibernate properties
    * @return a HikariConfig
    */
   @SuppressWarnings("rawtypes")
   public static HikariConfig loadConfiguration(Map props)
   {
      Properties hikariProps = new Properties();
      copyProperty(AvailableSettings.ISOLATION, props, "transactionIsolation", hikariProps);
      copyProperty(AvailableSettings.AUTOCOMMIT, props, "autoCommit", hikariProps);
      copyProperty(AvailableSettings.DRIVER, props, "driverClassName", hikariProps);
      copyProperty(AvailableSettings.URL, props, "jdbcUrl", hikariProps);
      copyProperty(AvailableSettings.USER, props, "username", hikariProps);
      copyProperty(AvailableSettings.PASS, props, "password", hikariProps);

      for (Object keyo : props.keySet()) {
         String key = (String) keyo;
         if (key.startsWith(CONFIG_PREFIX)) {
            hikariProps.setProperty(key.substring(CONFIG_PREFIX.length()), (String) props.get(key));
         }
      }

      return new HikariConfig(hikariProps);
   }

   @SuppressWarnings("rawtypes")
   private static void copyProperty(String srcKey, Map src, String dstKey, Properties dst)
   {
      if (src.containsKey(srcKey)) {
         dst.setProperty(dstKey, (String) src.get(srcKey));
      }
   }
}