package silverpotion.postserver.opensearch;

import org.opensearch.client.indices.CreateIndexRequest;
import org.opensearch.client.indices.GetIndexRequest;
import org.opensearch.client.indices.CreateIndexResponse;
import org.opensearch.client.RequestOptions;
import org.opensearch.client.RestHighLevelClient;
import java.io.IOException;

public class OpenSearchIndexUtil {
    private final RestHighLevelClient client;

    public OpenSearchIndexUtil(RestHighLevelClient client) {
        this.client = client;
    }

    public void createIndexIfNotExists(String indexName) throws IOException {
        GetIndexRequest getIndexRequest = new GetIndexRequest(indexName);
        boolean exists = client.indices().exists(getIndexRequest, RequestOptions.DEFAULT);

        if (!exists) {
            CreateIndexRequest createIndexRequest = new CreateIndexRequest(indexName);

            // Optional: settings
            createIndexRequest.settings("""
                {
                    "number_of_shards": 1,
                    "number_of_replicas": 1
                }
            """, org.opensearch.common.xcontent.XContentType.JSON);

            // mappings (자동완성 포함)
            createIndexRequest.mapping("""
            {
              "properties": {
                "gatheringName": {
                  "type": "search_as_you_type"
                },
                "introduce": {
                  "type": "text"
                },
                "region": {
                  "type": "text"
                },
                "categoryId": {
                  "type": "long"
                }
              }
            }
            """, org.opensearch.common.xcontent.XContentType.JSON);

            CreateIndexResponse response = client.indices().create(createIndexRequest, RequestOptions.DEFAULT);
            if (response.isAcknowledged()) {
                System.out.println("Index [" + indexName + "] created successfully.");
            } else {
                System.err.println("Index creation not acknowledged for [" + indexName + "].");
            }
        }
    }
}
