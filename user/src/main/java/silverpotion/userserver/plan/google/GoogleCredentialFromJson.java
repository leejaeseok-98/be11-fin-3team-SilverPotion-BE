//package silverpotion.userserver.plan.google;
//
//import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
//
//import java.io.InputStream;
//import java.util.Collection;
//
//public class GoogleCredentialFromJson {
//    public static GoogleCredential getCredential(InputStream jsonStream, Collection<String> scopes) throws Exception {
//        return GoogleCredential.fromStream(jsonStream)
//                .createScoped(scopes);
//    }
//}