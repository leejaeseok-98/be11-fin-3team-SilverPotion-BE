//package silverpotion.userserver.plan.google;
//
//import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
//import com.google.api.client.util.DateTime;
//import jakarta.annotation.PostConstruct;
//import org.springframework.stereotype.Service;
//
//import java.io.InputStream;
//import java.time.LocalDateTime;
//import java.time.ZoneId;
//import java.util.*;
//
//@Service
//public class GoogleCalendarService {
//
//    private Calendar calendar;
//
//    @PostConstruct
//    public void init() throws Exception {
//        InputStream credentialsStream = getClass().getResourceAsStream("/google-credentials.json");
//        var credential = GoogleCredentialFromJson.getCredential(credentialsStream, Collections.singleton(CalendarScopes.CALENDAR));
//        this.calendar = new Calendar.Builder(
//                GoogleNetHttpTransport.newTrustedTransport(),
//                JacksonFactory.getDefaultInstance(),
//                credential
//        ).setApplicationName("SilverPotion").build();
//    }
//
//    // ◼ 경년 커뮤 생성
//    public String createGoogleCalendarEvent(String title, String content, LocalDateTime startTime, LocalDateTime endTime) {
//        try {
//            Event event = new Event()
//                    .setSummary(title)
//                    .setDescription(content);
//
//            DateTime start = new DateTime(Date.from(startTime.atZone(ZoneId.systemDefault()).toInstant()));
//            DateTime end = new DateTime(Date.from(endTime.atZone(ZoneId.systemDefault()).toInstant()));
//
//            event.setStart(new EventDateTime().setDateTime(start).setTimeZone("Asia/Seoul"));
//            event.setEnd(new EventDateTime().setDateTime(end).setTimeZone("Asia/Seoul"));
//
//            Event createdEvent = calendar.events().insert("primary", event).execute();
//            return createdEvent.getId();
//        } catch (Exception e) {
//            System.err.println("❌ create 실패: " + e.getMessage());
//            return null;
//        }
//    }
//
//    // ◼ 수정
//    public void updateGoogleCalendarEvent(String eventId, String newTitle, String newContent, LocalDateTime newStart, LocalDateTime newEnd) {
//        try {
//            Event event = calendar.events().get("primary", eventId).execute();
//            event.setSummary(newTitle);
//            event.setDescription(newContent);
//
//            event.setStart(new EventDateTime().setDateTime(new DateTime(Date.from(newStart.atZone(ZoneId.systemDefault()).toInstant()))).setTimeZone("Asia/Seoul"));
//            event.setEnd(new EventDateTime().setDateTime(new DateTime(Date.from(newEnd.atZone(ZoneId.systemDefault()).toInstant()))).setTimeZone("Asia/Seoul"));
//
//            calendar.events().update("primary", eventId, event).execute();
//        } catch (Exception e) {
//            System.err.println("❌ update 실패: " + e.getMessage());
//        }
//    }
//
//    // ◼ 삭제
//    public void deleteGoogleCalendarEvent(String eventId) {
//        try {
//            calendar.events().delete("primary", eventId).execute();
//        } catch (Exception e) {
//            System.err.println("❌ delete 실패: " + e.getMessage());
//        }
//    }
//
//    // ◼ 공휴일 조회 오픈 API 전달에서 사용
//    public List<String> getHolidays(String startISO8601, String endISO8601) {
//        try {
//            Events events = calendar.events().list("ko.south_korea#holiday@group.v.calendar.google.com")
//                    .setTimeMin(new DateTime(startISO8601))
//                    .setTimeMax(new DateTime(endISO8601))
//                    .setSingleEvents(true)
//                    .execute();
//
//            List<String> holidayTitles = new ArrayList<>();
//            for (Event event : events.getItems()) {
//                holidayTitles.add(event.getSummary());
//            }
//            return holidayTitles;
//        } catch (Exception e) {
//            System.err.println("❌ 공휴일 조회 실패: " + e.getMessage());
//            return List.of();
//        }
//    }
//}
