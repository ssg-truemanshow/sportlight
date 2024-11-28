package com.tms.sportlight.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

  private final Logger logger = LoggerFactory.getLogger(PaymentController.class);

  // 결제 위젯 및 API 비밀키 (테스트용)
  private static final String WIDGET_SECRET_KEY = "test_gsk_docs_OaPz8L5KdmQXkzRz3y47BMw6";
  private static final String API_SECRET_KEY = "test_sk_zXLkKEypNArWmo50nX3lmeaxYG5R";


  // 결제 확인 요청을 처리하는 엔드포인트
  @PostMapping("/confirm")
  public ResponseEntity<JSONObject> confirmPayment(HttpServletRequest request, @RequestBody String jsonBody) throws Exception {
    // 위젯 비밀키와 API 비밀키를 요청 경로에 따라 선택
    String secretKey = request.getRequestURI().contains("widget") ? WIDGET_SECRET_KEY : API_SECRET_KEY;
    // 요청 데이터를 파싱하고 Tosspayments API로 전송하여 응답 수신
    JSONObject response = sendRequest(parseRequestData(jsonBody), secretKey, "https://api.tosspayments.com/v1/payments/confirm");
    // 응답 상태에 따라 HTTP 200 또는 400 반환
    System.out.println("===========================================================================================================================");
    System.out.println(ResponseEntity.status(response.containsKey("error") ? 400 : 200).body(response));
    System.out.println("===========================================================================================================================");
    return ResponseEntity.status(response.containsKey("error") ? 400 : 200).body(response);
  }

  // JSON 데이터 파싱 메서드
  private JSONObject parseRequestData(String jsonBody) {
    try {
      return (JSONObject) new JSONParser().parse(jsonBody);
    } catch (ParseException e) {
      logger.error("JSON Parsing Error", e);
      return new JSONObject();
    }
  }

  // Tosspayments API로 요청 전송
  private JSONObject sendRequest(JSONObject requestData, String secretKey, String urlString) throws IOException {
    // HTTP 연결 생성
    HttpURLConnection connection = createConnection(secretKey, urlString);
    try (OutputStream os = connection.getOutputStream()) {
      os.write(requestData.toString().getBytes(StandardCharsets.UTF_8));
    }

    // 응답 스트림을 읽어 JSONObject로 변환
    try (InputStream responseStream = connection.getResponseCode() == 200 ? connection.getInputStream() : connection.getErrorStream();
        Reader reader = new InputStreamReader(responseStream, StandardCharsets.UTF_8)) {
      return (JSONObject) new JSONParser().parse(reader);
    } catch (Exception e) {
      logger.error("Error reading response", e);
      JSONObject errorResponse = new JSONObject();
      errorResponse.put("error", "Error reading response");
      return errorResponse;
    }
  }

  // HTTP 연결을 생성하는 메서드
  private HttpURLConnection createConnection(String secretKey, String urlString) throws IOException {
    URL url = new URL(urlString);
    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
    connection.setRequestProperty("Authorization", "Basic " + Base64.getEncoder().encodeToString((secretKey + ":").getBytes(StandardCharsets.UTF_8)));
    connection.setRequestProperty("Content-Type", "application/json");
    connection.setRequestMethod("POST");
    connection.setDoOutput(true);
    return connection;
  }
}
