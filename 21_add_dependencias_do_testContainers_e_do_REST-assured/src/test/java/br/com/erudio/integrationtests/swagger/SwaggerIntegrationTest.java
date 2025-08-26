package br.com.erudio.integrationtests.swagger;



import static org.junit.Assert.assertTrue;
import org.springframework.boot.test.context.SpringBootTest;

import br.com.erudio.config.TestConfigs;
import br.com.erudio.integrationtests.testcontainers.AbstractIntegrationTest;
import net.bytebuddy.NamingStrategy.Suffixing.BaseNameResolver.ForGivenType;

import io.restassured.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class SwaggerIntegrationTest extends AbstractIntegrationTest {

	@Test
	void shouldDisplaySwaggerUIPage() {
		var content = given()
				.basePath("swagger-ui/index.html")
					.port(TestConfigs.SERVER_PORT)
				.when()
					.get()
				.then()
					.statusCode(200)
				.extract()
					.body()
						.asString();
				
		assertTrue(content.contains("Swagger UI"));
	}

}
