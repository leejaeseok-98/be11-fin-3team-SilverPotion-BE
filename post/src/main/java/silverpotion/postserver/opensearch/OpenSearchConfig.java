//package silverpotion.postserver.opensearch;
//
//import lombok.Data;
//import org.apache.http.HttpHost;
//import org.apache.http.auth.AuthScope;
//import org.apache.http.auth.UsernamePasswordCredentials;
//import org.apache.http.client.CredentialsProvider;
//import org.apache.http.impl.client.BasicCredentialsProvider;
//import org.opensearch.client.RestClient;
//import org.opensearch.client.RestClientBuilder;
//import org.opensearch.client.RestHighLevelClient;
//import org.springframework.boot.context.properties.ConfigurationProperties;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//@ConfigurationProperties(prefix = "opensearch")
//@Data
//public class OpenSearchConfig {
//    private String host;
//    private int port;
//    private String scheme;
//    private String username;
//    private String password;
//
//    @Bean
//    public RestHighLevelClient openSearchClient() {
//
//        CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
//        credentialsProvider.setCredentials(AuthScope.ANY,
//                new UsernamePasswordCredentials(username, password));
//
//        RestClientBuilder builder = RestClient.builder(new HttpHost(host, port, scheme))
//                .setHttpClientConfigCallback(httpClientBuilder ->
//                        httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider)
//                );
//        return new RestHighLevelClient(builder);
//    }
//}
