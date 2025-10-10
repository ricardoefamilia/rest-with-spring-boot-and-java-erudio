package br.com.erudio.integrationtests.controllers.withxml;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import br.com.erudio.config.TestConfigs;
import br.com.erudio.integrationtests.dto.PersonDTO;
import br.com.erudio.integrationtests.response.xml_yaml.PagedModelPerson;
import br.com.erudio.integrationtests.testcontainers.AbstractIntegrationTest;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PersonControllerXmlTest extends AbstractIntegrationTest {

    @LocalServerPort
    private int port;

    private static RequestSpecification specification;
    private static XmlMapper objectMapper;
    private static PersonDTO person;

    @BeforeAll
    static void setUp() {
        objectMapper = new XmlMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        person = new PersonDTO(); // DTO simplificado
    }

    private void mockPerson() {
        person.setFirstName("Winston");
        person.setLastName("Churchill");
        person.setAddress("Oxfordshire - England");
        person.setGender("Male");
        person.setEnabled(true);
    }

    @Test
    @Order(1)
    void testCreate() throws JsonProcessingException {
        mockPerson();

        specification = new RequestSpecBuilder()
                .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_ERUDIO)
                .setBasePath("/api/person/v1")
                .setPort(port)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        String content = given(specification)
                .contentType(MediaType.APPLICATION_XML_VALUE)
                .accept(MediaType.APPLICATION_XML_VALUE)
                .body(person)
                .when()
                .post()
                .then()
                .statusCode(200)
                .contentType(MediaType.APPLICATION_XML_VALUE)
                .extract()
                .body()
                .asString();

        PersonDTO createdPerson = objectMapper.readValue(content, PersonDTO.class);
        person = createdPerson;

        assertNotNull(createdPerson.getId());
        assertTrue(createdPerson.getId() > 0);
        assertEquals("Winston", createdPerson.getFirstName());
        assertEquals("Churchill", createdPerson.getLastName());
        assertEquals("Oxfordshire - England", createdPerson.getAddress());
        assertEquals("Male", createdPerson.getGender());
        assertTrue(createdPerson.getEnabled());
    }

    @Test
    @Order(2)
    void testUpdate() throws JsonProcessingException {
        person.setLastName("Leonard Spencer-Churchill");

        String content = given(specification)
                .contentType(MediaType.APPLICATION_XML_VALUE)
                .accept(MediaType.APPLICATION_XML_VALUE)
                .body(person)
                .when()
                .put()
                .then()
                .statusCode(200)
                .contentType(MediaType.APPLICATION_XML_VALUE)
                .extract()
                .body()
                .asString();

        PersonDTO updatedPerson = objectMapper.readValue(content, PersonDTO.class);
        person = updatedPerson;

        assertEquals("Leonard Spencer-Churchill", updatedPerson.getLastName());
    }

    @Test
    @Order(3)
    void testFindById() throws JsonProcessingException {
        String content = given(specification)
                .contentType(MediaType.APPLICATION_XML_VALUE)
                .accept(MediaType.APPLICATION_XML_VALUE)
                .pathParam("id", person.getId())
                .when()
                .get("{id}")
                .then()
                .statusCode(200)
                .contentType(MediaType.APPLICATION_XML_VALUE)
                .extract()
                .body()
                .asString();

        PersonDTO fetchedPerson = objectMapper.readValue(content, PersonDTO.class);
        person = fetchedPerson;

        assertEquals(person.getId(), fetchedPerson.getId());
    }

    @Test
    @Order(4)
    void testDisable() throws JsonProcessingException {
        String content = given(specification)
                .accept(MediaType.APPLICATION_XML_VALUE)
                .pathParam("id", person.getId())
                .when()
                .patch("{id}")
                .then()
                .statusCode(200)
                .contentType(MediaType.APPLICATION_XML_VALUE)
                .extract()
                .body()
                .asString();

        PersonDTO disabledPerson = objectMapper.readValue(content, PersonDTO.class);
        person = disabledPerson;

        assertFalse(disabledPerson.getEnabled());
    }

    @Test
    @Order(5)
    void testDelete() {
        given(specification)
                .pathParam("id", person.getId())
                .when()
                .delete("{id}")
                .then()
                .statusCode(204);
    }

    @Test
    @Order(6)
    void testFindAll() throws JsonProcessingException {
        specification = new RequestSpecBuilder()
                .setBasePath("/api/person/v1")
                .setPort(port)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        String content = given(specification)
                .accept(MediaType.APPLICATION_XML_VALUE)
                .queryParams("page", 0, "size", 12, "direction", "asc")
                .when()
                .get()
                .then()
                .statusCode(200)
                .contentType(MediaType.APPLICATION_XML_VALUE)
                .extract()
                .body()
                .asString();

        PagedModelPerson wrapper = objectMapper.readValue(content, PagedModelPerson.class);
        List<PersonDTO> people = wrapper.getContent();

        assertNotNull(people);
        assertFalse(people.isEmpty());
    }
}
