package com.paypal.notifications.configuration;

import com.paypal.notifications.model.entity.NotificationEntity;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

/**
 * Datasource setup for notifications module.
 */
@Configuration
@PropertySource({ "classpath:notifications_db.properties" })
@EnableJpaRepositories(basePackages = "com.paypal.notifications.repository",
		entityManagerFactoryRef = "notificationsEntityManagerFactory",
		transactionManagerRef = "notificationsTransactionManager")
public class NotificationsDataSourceConfig {

	/**
	 * Creates a bean to retrieve configuration properties for
	 * {@link DataSourceProperties}
	 * @return the {@link DataSourceProperties}
	 */
	@Bean
	@ConfigurationProperties(prefix = "notifications.db.datasource")
	public DataSourceProperties notificationsDataSourceDataSourceProperties() {
		return new DataSourceProperties();
	}

	/**
	 * Creates a bean to setup the {@link DataSource} according with
	 * {@link NotificationsDataSourceConfig#notificationsDataSourceDataSourceProperties()}
	 * properties
	 * @return the {@link DataSource}
	 */
	@Bean(name = "notificationsDataSource")
	public DataSource notificationsDataSource(
			final @Qualifier("notificationsDataSourceDataSourceProperties") DataSourceProperties notificationsDataSourceProperties) {
		return notificationsDataSourceProperties.initializeDataSourceBuilder().build();
	}

	/**
	 * Creates a new {@link LocalContainerEntityManagerFactoryBean} that holds the
	 * packages within entities inside application module
	 * @param builder the {@link EntityManagerFactoryBuilder}
	 * @return the {@link LocalContainerEntityManagerFactoryBean}
	 */
	@Bean(name = "notificationsEntityManagerFactory")
	public LocalContainerEntityManagerFactoryBean notificationsEntityManagerFactory(
			final EntityManagerFactoryBuilder builder,
			final @Qualifier("notificationsDataSource") DataSource notificationsDataSource) {
		return builder.dataSource(notificationsDataSource).packages(NotificationEntity.class).build();
	}

	/**
	 * Creates a new {@link PlatformTransactionManager} using the bean generated by
	 * {@link NotificationsDataSourceConfig#notificationsEntityManagerFactory(EntityManagerFactoryBuilder, DataSource)}
	 * @param notificationsTransactionManager the
	 * {@link LocalContainerEntityManagerFactoryBean}
	 * @return the {@link PlatformTransactionManager}
	 */
	@SuppressWarnings("java:S4449")
	@Bean(name = "notificationsTransactionManager")
	public PlatformTransactionManager notificationsTransactionManager(
			final @Qualifier("notificationsEntityManagerFactory") LocalContainerEntityManagerFactoryBean notificationsTransactionManager) {
		return new JpaTransactionManager(notificationsTransactionManager.getObject());
	}

}