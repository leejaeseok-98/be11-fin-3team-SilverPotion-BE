//package silverpotion.postserver.opensearch;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import lombok.RequiredArgsConstructor;
//import org.opensearch.action.index.IndexRequest;
//import org.opensearch.action.search.SearchRequest;
//import org.opensearch.action.search.SearchResponse;
//import org.opensearch.client.RequestOptions;
//import org.opensearch.client.RestHighLevelClient;
//import org.opensearch.common.xcontent.XContentType;
//import org.opensearch.index.query.BoolQueryBuilder;
//import org.opensearch.index.query.QueryBuilders;
//import org.opensearch.search.builder.SearchSourceBuilder;
//import org.springframework.stereotype.Service;
//import silverpotion.postserver.gathering.domain.Gathering;
//import silverpotion.postserver.meeting.domain.Meeting;
//
//import java.io.IOException;
//import java.util.Arrays;
//import java.util.List;
//import java.util.Map;
//import java.util.stream.Collectors;
//
//@Service
//@RequiredArgsConstructor
//public class OpenSearchService {
//    private final RestHighLevelClient client;
//    private final ObjectMapper objectMapper;
//    private final RestHighLevelClient openSearchClient;
//
//    public void indexGathering(Gathering gathering) {
//        GatheringIndexDto dto = GatheringIndexDto.fromEntity(gathering); // 여기서 변환
//        IndexRequest request = new IndexRequest("gatherings")
//                .id(dto.getId().toString())
//                .source(objectMapper.convertValue(dto, Map.class));
//
//        try {
//            client.index(request, RequestOptions.DEFAULT);
//        } catch (IOException e) {
//            throw new RuntimeException("Failed to index gathering", e);
//        }
//    }
//
//    public void indexMeeting(Meeting meeting) throws IOException {
//        MeetingIndexDto dto = MeetingIndexDto.builder()
//                .id(meeting.getId())
//                .name(meeting.getName())
//                .place(meeting.getPlace())
//                .build();
//
//        IndexRequest request = new IndexRequest("meetings")
//                .id(dto.getId().toString())
//                .source(objectMapper.writeValueAsString(dto), XContentType.JSON);
//
//        client.index(request, RequestOptions.DEFAULT);
//    }
//
//    public List<GatheringSearchResultDto> searchGatherings(GatheringSearchRequest request) {
//        SearchRequest searchRequest = new SearchRequest("gatherings");
//
//        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
//
//        if (request.getKeyword() != null && !request.getKeyword().isEmpty()) {
//            boolQuery.must(
//                    QueryBuilders.multiMatchQuery(request.getKeyword())
//                            .field("gatheringName")
//                            .field("region")
//                            .field("introduce")
//                            .type(org.opensearch.index.query.MultiMatchQueryBuilder.Type.PHRASE_PREFIX)
//            );
//        }
//
//        if (request.getRegion() != null) {
//            boolQuery.filter(QueryBuilders.termQuery("region.keyword", request.getRegion()));
//        }
//
//        if (request.getCategoryId() != null) {
//            boolQuery.filter(QueryBuilders.termQuery("categoryId", request.getCategoryId()));
//        }
//
//        boolQuery.filter(QueryBuilders.termQuery("delYN.keyword", "N"));
//
//        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
//        sourceBuilder.query(boolQuery);
//        sourceBuilder.size(20);
//
//        searchRequest.source(sourceBuilder);
//
//        try {
//            SearchResponse response = openSearchClient.search(searchRequest, RequestOptions.DEFAULT);
//
//            return Arrays.stream(response.getHits().getHits())
//                    .map(hit -> {
//                        Map<String, Object> source = hit.getSourceAsMap();
//                        return GatheringSearchResultDto.builder()
//                                .id(Long.valueOf(source.get("id").toString()))
//                                .gatheringName((String) source.get("gatheringName"))
//                                .region((String) source.get("region"))
//                                .imageUrl((String) source.get("imageUrl"))
//                                .introduce((String) source.get("introduce"))
//                                .build();
//                    })
//                    .collect(Collectors.toList());
//
//        } catch (IOException e) {
//            throw new RuntimeException("검색 중 오류 발생", e);
//        }
//    }
//
//    public List<String> suggestGatherings(String prefix) {
//        SearchRequest searchRequest = new SearchRequest("gatherings");
//
//        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
//        sourceBuilder.query(QueryBuilders.prefixQuery("gatheringName", prefix));
//        sourceBuilder.size(10); // 최대 10개까지 자동완성 결과
//
//        searchRequest.source(sourceBuilder);
//
//        try {
//            SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);
//
//            return Arrays.stream(response.getHits().getHits())
//                    .map(hit -> (String) hit.getSourceAsMap().get("gatheringName"))
//                    .collect(Collectors.toList());
//
//        } catch (IOException e) {
//            throw new RuntimeException("자동완성 중 오류 발생", e);
//        }
//    }
//}
