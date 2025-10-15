package br.com.erudio.integrationtests.response.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
/**
 * Wrapper genérico para desserializar respostas HATEOAS (_embedded)
 * Exemplo de JSON que esse wrapper consegue tratar:
 *
 * {
 *   "_embedded": {
 *     "books": [
 *       { "id": 1, "author": "Steve McConnell", ... },
 *       { "id": 2, "author": "Ralph Johnson", ... }
 *     ]
 *   }
 * }
 *
 * @param <T> Tipo de DTO que estará dentro do _embedded (ex: PersonDTO)
 */
public class EmbeddedResponseBookJson<T> {

	@JsonProperty("_embedded")
    private Embedded<T> embedded;

    public Embedded<T> getEmbedded() {
        return embedded;
    }

    // Classe estática para mapear o objeto dentro de "_embedded"
    public static class Embedded<T> {
        private List<T> books; // chave fixa do JSON que você mostrou

        public List<T> getBooks() {
            return books;
        }

        public void setBooks(List<T> books) {
            this.books = books;
        }
    }
}
