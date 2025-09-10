package br.com.erudio.integrationtests.response.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
/**
 * Wrapper genérico para desserializar respostas HATEOAS (_embedded)
 * Exemplo de JSON que esse wrapper consegue tratar:
 *
 * {
 *   "_embedded": {
 *     "people": [
 *       { "id": 1, "firstName": "John", ... },
 *       { "id": 2, "firstName": "Jane", ... }
 *     ]
 *   }
 * }
 *
 * @param <T> Tipo de DTO que estará dentro do _embedded (ex: PersonDTO)
 */
public class EmbeddedResponsePersonJson<T> {

	@JsonProperty("_embedded")
    private Embedded<T> embedded;

    public Embedded<T> getEmbedded() {
        return embedded;
    }

    // Classe estática para mapear o objeto dentro de "_embedded"
    public static class Embedded<T> {
        private List<T> people; // chave fixa do JSON que você mostrou

        public List<T> getPeople() {
            return people;
        }

        public void setPeople(List<T> people) {
            this.people = people;
        }
    }
}
