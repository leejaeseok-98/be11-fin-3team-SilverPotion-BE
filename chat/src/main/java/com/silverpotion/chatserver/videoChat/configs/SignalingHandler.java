package com.silverpotion.chatserver.videoChat.configs;

import com.fasterxml.jackson.databind.util.JSONPObject;
import com.silverpotion.chatserver.chat.service.UserFeign;
import org.json.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;


import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class SignalingHandler extends TextWebSocketHandler {

    private final Map<String,WebSocketSession> userSessions = new ConcurrentHashMap<>();
    private final UserFeign userFeign;

    public SignalingHandler(UserFeign userFeign) {
        this.userFeign = userFeign;
    }

    //로그인 아이디와 세션을 맵 구조로 저장하기  위함
    //ConcurrentHashMap<>()은 은 멀티스레드 작동을 위한 Map

   //1. 세션 연결
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
       String loginId = getUserId(session);
        System.out.println("세션연결" + loginId);
       if(userSessions.containsKey(loginId)){
           WebSocketSession oldSession = userSessions.get(loginId);
           if(oldSession != null && oldSession.isOpen()){
               oldSession.close(); //기존 세션에 있는 로그인 유저는 세션을 끊음
           }
       }
       userSessions.put(loginId,session); // 세션집합에 추가. 기존 세션이 연결되있던 사용자는 위의 로직으로 인해 기존 세션 끊고 다시 세션연결
        System.out.println(loginId + "님이 세션에 연결");
        session.getAttributes().put("loginId",loginId); //세션의 getAttributes()는  세션에다가 key-value형식으로 따로 저장할 수 있는 map을 가져와주는 메서드
                                                        //굳이 이 session의 Attribute에도 넣는 것은 연결을 끊을 때 여기에 있는 로그인 아이디를 이용하여 확실히 끊기 위함
    }

    //2.sdp세션,ice candidates 정보가 담긴 메시지를 받아서 상대방에게 전달
    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {

        String fromLoginId =(String)session.getAttributes().get("loginId"); //화상통화 건 사람 아이디

        JSONObject json = new JSONObject(message.getPayload());
        String toLoginId = json.getString("to"); //화상통화 받을 사람 아이디,우리가 프론트에서 sdp 전송할때 to:아이디 추가해놓아야함
        String type = json.optString("type"); //알람 보내기 위함, optString은 getString과 같긴한데 예외를 던지지않고 기본값을 반환함 기본값은 빈문자열

        WebSocketSession receiverSession = userSessions.get(toLoginId); //상대방 세션

        //알람 추가(즉 sdp offer메세지 보내면서 알람도 같이 보내겠다는 것)
//        if ("offer".equals(type)) {
//            userFeign.sendVedioCallNotification(toLoginId);
//        }
     // 여기까지 알람 추가

        if(receiverSession != null && receiverSession.isOpen()){ // 상대방 세션이 연결되어있으면
            receiverSession.sendMessage(message); //메세지(sdp세션,iceCandidates)전달
        }
    }

    //3.세션 종료
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
      String loginId = (String) session.getAttributes().get("loginId");
      userSessions.remove(loginId);// getUserId로 세션의 로그인아이디를 꺼내지 않는 이유는 비정상적으로 연결이 종료되었을때 session.geturi가 null이거나 쿼리 정보가 없어질 수 있기 때문
    }



    //0. 아이디 뽑아내기 위한 메서드
    // 클라이언트에서 웹소켓 연결을 요청했을 시 아이디를 뽑아내기 위한 메서드(ex. "ws://localhost:8080/signal?userId=abc123)처럼 요청을 보내게 설계
    private String getUserId(WebSocketSession session){
        String query = session.getUri().getQuery(); // 웹소켓 연결 요청URI에서 ?뒤에 붙은 쿼리 파라미터 전체를 문자열로 가져옴
        System.out.println(query);
        if(query != null){
            String[] params = query.split("&"); //파라미터가 여러개일 경우 대비

            for(String param : params){
                String[] keyValue = param.split("="); //이러면 keyValue[0]=userId , keyValue[1]=abc123이 됨.
                if(keyValue.length ==2 && keyValue[0].equals("userId")){
                    return keyValue[1]; //원하는 abc123이 리턴되는 것
                }
            }
        }
        throw new IllegalArgumentException("파라미터의 아이디값이 잘못되었습니다");
    }

}
