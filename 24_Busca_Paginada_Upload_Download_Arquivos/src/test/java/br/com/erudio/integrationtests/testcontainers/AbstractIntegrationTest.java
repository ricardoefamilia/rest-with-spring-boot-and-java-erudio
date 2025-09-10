package br.com.erudio.integrationtests.testcontainers;

import java.util.Map;
import java.util.stream.Stream;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.lifecycle.Startables;

@ContextConfiguration(initializers = AbstractIntegrationTest.Initializer.class)
public class AbstractIntegrationTest {

	static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext>{
		
		static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:9.1.0");
		
		private static void startConainers() {
			Startables.deepStart(Stream.of(mysql)).join();
		}

		@Override
		public void initialize(ConfigurableApplicationContext applicationContext) {
			startConainers();
			ConfigurableEnvironment environment = applicationContext.getEnvironment();
			MapPropertySource testcontainers = new MapPropertySource("testcontainers", 
					(Map) createConnectionConfiguration());
			environment.getPropertySources().addFirst(testcontainers);
		}
		
		// configuração de conn bd dinâmica (muda a cada tentativa)
		private Map<String, String> createConnectionConfiguration() {
			return Map.of(
				// definição das propriedades de conexão com o banco mysql
				"spring.datasource.url", mysql.getJdbcUrl(),
				"spring.datasource.username", mysql.getUsername(),
				"spring.datasource.password", mysql.getPassword()
			);
		}
		
	}
}
