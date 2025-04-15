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
//import org.opensearch.index.query.MultiMatchQueryBuilder;
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
//    public void indexMeeting(Meeting meeting) {
//        MeetingIndexDto dto = MeetingIndexDto.fromEntity(meeting); // 여기서 변환
//        IndexRequest request = new IndexRequest("meetings")
//                .id(dto.getId().toString())
//                .source(objectMapper.convertValue(dto, Map.class));
//
//        try {
//            client.index(request, RequestOptions.DEFAULT);
//        } catch (IOException e) {
//            throw new RuntimeException("Failed to index meeting", e);
//        }
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
//                                .categoryId(Long.valueOf(source.get("categoryId").toString()))
//                                .build();
//                    })
//                    .collect(Collectors.toList());
//
//        } catch (IOException e) {
//            throw new RuntimeException("검색 중 오류 발생", e);
//        }
//    }
//
//    public List<MeetingSearchResultDto> searchMeetings(MeetingSearchRequest request) {
//        String keyword = request.getKeyword();
//
//        System.out.println("🔍 [Meeting 검색] 키워드: " + keyword);
//
//        SearchRequest searchRequest = new SearchRequest("meetings");
//
//        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder()
//                .size(20)
//                .query(QueryBuilders.boolQuery()
//                        .must(QueryBuilders.multiMatchQuery(keyword)
//                                .field("name", 1.0f)
//                                .field("name._2gram", 1.0f)
//                                .field("name._3gram", 1.0f)
//                                .field("place", 1.0f)
//                                .type(MultiMatchQueryBuilder.Type.BOOL_PREFIX)
//                        )
//                        .filter(QueryBuilders.termQuery("delYN", "N"))
//                );
//
//        searchRequest.source(searchSourceBuilder);
//
//        System.out.println("🔍 Meeting 쿼리 DSL: \n" + searchSourceBuilder.toString());
//
//        try {
//            SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
//
//            return Arrays.stream(searchResponse.getHits().getHits())
//                    .map(hit -> {
//                        Map<String, Object> sourceAsMap = hit.getSourceAsMap();
//                        return MeetingSearchResultDto.builder()
//                                .id(Long.valueOf(sourceAsMap.get("id").toString()))
//                                .name((String) sourceAsMap.get("name"))
//                                .place((String) sourceAsMap.get("place"))
//                                .imageUrl((String) sourceAsMap.get("imageUrl"))
//                                .meetingDate((String) sourceAsMap.get("meetingDate"))
//                                .meetingTime((String) sourceAsMap.get("meetingTime"))
//                                .cost(Long.valueOf(sourceAsMap.get("cost").toString()))
//                                .maxPeople(Long.valueOf(sourceAsMap.get("maxPeople").toString()))
//                                .gatheringId(Long.valueOf(sourceAsMap.get("gatheringId").toString()))
//                                .build();
//                    })
//                    .collect(Collectors.toList());
//
//        } catch (IOException e) {
//            throw new RuntimeException("OpenSearch 조회 실패", e);
//        }
//    }
//
//    public List<String> suggestAll(String prefix) {
//        SearchRequest searchRequest = new SearchRequest("gatherings", "meetings");
//
//        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
//        sourceBuilder.size(20);
//        sourceBuilder.query(QueryBuilders.boolQuery()
//                .should(QueryBuilders.prefixQuery("gatheringName", prefix))
//                .should(QueryBuilders.prefixQuery("name", prefix))
//        );
//
//        searchRequest.source(sourceBuilder);
//
//        try {
//            SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);
//
//            return Arrays.stream(response.getHits().getHits())
//                    .map(hit -> {
//                        Map<String, Object> source = hit.getSourceAsMap();
//                        if (source.containsKey("gatheringName")) {
//                            return (String) source.get("gatheringName");
//                        } else {
//                            return (String) source.get("name");
//                        }
//                    })
//                    .distinct()
//                    .limit(10)
//                    .collect(Collectors.toList());
//
//        } catch (IOException e) {
//            throw new RuntimeException("자동완성 통합 검색 중 오류 발생", e);
//        }
//    }
//}
