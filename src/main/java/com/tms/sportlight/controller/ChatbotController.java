package com.tms.sportlight.controller;



import com.tms.sportlight.exception.BizException;
import com.tms.sportlight.exception.ErrorCode;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.ZoneId;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/chatbot")
public class ChatbotController {

   @PostMapping("/open")
    public String openChatbot() {
        log.info("open chatbot");
        return transmitMessage("", true);
    }


    @PostMapping("/send")
    public String sendChatbot(@RequestBody String question){
        log.info("send chatbot");
        log.info("question: " + question);
      String message ="";
      try {
        JSONParser parser = new JSONParser();
        JSONObject jsonObject = null;
        jsonObject = (JSONObject) parser.parse(question);
        message = jsonObject.getAsString("question");
      } catch (ParseException e) {
        throw new BizException(ErrorCode.INTERNAL_SERVER_ERROR);
      }

        log.info("message: " + message);

        return transmitMessage(message, false);

    }

    @Value("${cloud.ncp.chatbot.screct-key}")
    private String secretKey;

    /**
     * 챗봇에게 메시지 전달
     * @param question 질문
     * @param isEvent 타입(true: open, false: send)
     * @return
     */
    private String transmitMessage(String question, boolean isEvent) {
      String chatbotMessage = "";

      try {
        String apiURL = "https://ssjj7muvrm.apigw.ntruss.com/custom/v1/16215/851ae207917e36a23409dd38553a9324c1f75314b002502cc08b034339716878";

        URL url = new URL(apiURL);
        String message = getReqMessage(question, isEvent);
        log.info("message: " + message);

        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("X-NCP-CHATBOT_SIGNATURE", secretKey);

        //요청
        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.write(message.getBytes("UTF-8"));
        wr.flush();
        wr.close();

        //응답
        int responseCode = con.getResponseCode();

        if(responseCode == 200) {
          log.info("getResponseMessage : " + con.getResponseMessage());

          BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));

          String decodedString;

          while ((decodedString = br.readLine()) != null) {
            chatbotMessage = decodedString;
          }
          br.close();
        }else {
          chatbotMessage = con.getResponseMessage();
        }

      }catch (Exception e) {
//        e.printStackTrace();
        throw new BizException(ErrorCode.INTERNAL_SERVER_ERROR);
      }
      log.info("chatbotMessage: " + chatbotMessage);
      return chatbotMessage;
    }



    /**
     * 요청 메시지 생성
     * @param question 질문
     * @param isEvent 타입(true: open, false: send)
     * @return
     */
  private String getReqMessage(String question, boolean isEvent) {
     String requestBody = "";
     try {
       JSONObject obj = new JSONObject();
       long timestamp = LocalDateTime.now().atZone(ZoneId.of("Asia/Seoul")).toEpochSecond();
       log.info("timestamp: " + timestamp);

       obj.put("version", "v1");
       obj.put("userId", "사용자");
       obj.put("timestamp", timestamp);

       JSONObject content = new JSONObject();
       content.put("type", "text");

       JSONObject data = new JSONObject();
       data.put("details", question);
       content.put("data", data);

       JSONArray contents = new JSONArray();
       contents.add(content);

       obj.put("content", contents);

       if(isEvent) {
         obj.put("event", "open");
       } else {
         obj.put("event", "send");
       }

       requestBody = obj.toString();
     }catch (Exception e) {
       throw new BizException(ErrorCode.INTERNAL_SERVER_ERROR);
     }
     log.info("requestBody: " + requestBody);
     return requestBody;
  }

}
