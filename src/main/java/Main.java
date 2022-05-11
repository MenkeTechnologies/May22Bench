import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;

public class Main {
    public static void main(String[] args) {

        RestClient restClient = RestClient.builder(new HttpHost("localhost", 9200)).build();
        ElasticsearchTransport transport = new RestClientTransport( restClient, new JacksonJsonpMapper());
        ElasticsearchClient client = new ElasticsearchClient(transport);

        SearchResponse<Product> search = client.search(s -> s
                        .index("products")
                        .query(q -> q
                                .term(t -> t
                                        .field("name")
                                        .value(v -> v.stringValue("bicycle"))
                                )),
                Product.class);

        for (Hit<Product> hit: search.hits().hits()) {
            processProduct(hit.source());
        }
    }
}
