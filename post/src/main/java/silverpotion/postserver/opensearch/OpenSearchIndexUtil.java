package silverpotion.postserver.opensearch;

import org.opensearch.client.indices.CreateIndexRequest;
import org.opensearch.client.indices.GetIndexRequest;
import org.opensearch.client.indices.CreateIndexResponse;
import org.opensearch.client.RequestOptions;
import org.opensearch.client.RestHighLevelClient;
import org.opensearch.common.xcontent.XContentType;

import java.io.IOException;

public class OpenSearchIndexUtil {
    private final RestHighLevelClient client;

    public OpenSearchIndexUtil(RestHighLevelClient client) {
        this.client = client;
    }

    public void createIndexIfNotExists(String indexName, String mappingsJson) throws IOException {
        GetIndexRequest getIndexRequest = new GetIndexRequest(indexName);
        boolean exists = client.indices().exists(getIndexRequest, RequestOptions.DEFAULT);

        if (!exists) {
            CreateIndexRequest createIndexRequest = new CreateIndexRequest(indexName);

            createIndexRequest.settings("""
            {
                "number_of_shards": 1,
                "number_of_replicas": 1
            }
        """, XContentType.JSON);

            createIndexRequest.mapping(mappingsJson, XContentType.JSON);

            CreateIndexResponse response = client.indices().create(createIndexRequest, RequestOptions.DEFAULT);
            if (response.isAcknowledged()) {
                System.out.println("Index [" + indexName + "] created successfully.");
            } else {
                System.err.println("Index creation not acknowledged for [" + indexName + "].");
            }
        }
    }

    public void createGatheringIndexIfNotExists() throws IOException {
        createIndexIfNotExists("gatherings", """
        {
          "properties": {
            "gatheringName": { "type": "search_as_you_type" },
            "introduce": { "type": "text" },
            "region": { "type": "text" },
            "categoryId": { "type": "long" },
            "delYN": { "type": "keyword" }
          }
        }
    """);
    }

    public void createMeetingIndexIfNotExists() throws IOException {
        createIndexIfNotExists("meetings", """
        {
          "properties": {
            "name": { "type": "search_as_you_type" },
            "place": { "type": "text" },
            "imageUrl": { "type": "text" },
            "meetingDate": { "type": "text" },
            "meetingTime": { "type": "text" },
            "cost": { "type": "long" },
            "maxPeople": { "type": "long" },
            "gatheringId": { "type": "long" },
            "delYN": { "type": "keyword" }
          }
        }
    """);
    }
}
