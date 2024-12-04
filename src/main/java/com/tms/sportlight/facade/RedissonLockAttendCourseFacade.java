package com.tms.sportlight.facade;

import com.tms.sportlight.controller.PaymentController;
import com.tms.sportlight.domain.User;
import com.tms.sportlight.dto.ReserveCourseDTO;
import com.tms.sportlight.service.AttendCourseService;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RedissonLockAttendCourseFacade {
  private final RedissonClient redissonClient;
  private final AttendCourseService attendCourseService;
  @Value("${toss-payment.secret-key}")
  private String WIDGET_SECRET_KEY;
  private final Logger logger = LoggerFactory.getLogger(PaymentController.class);

  public ResponseEntity<JSONObject> decrease(String jsonBody ,User user, ReserveCourseDTO reserveCourseDTO) {
    RLock lock = redissonClient.getLock(reserveCourseDTO.getScheduleId().toString());
    ResponseEntity<JSONObject> response = null;

    try {
      boolean available = lock.tryLock(10, 1, TimeUnit.SECONDS);
      if (!available) {
        JSONObject errorResponse = new JSONObject();
        errorResponse.put("error", "Unable to acquire lock");
        return ResponseEntity.status(503).body(errorResponse); // 서비스 이용 불가 응답

      }

      response = confirmTossPayment(jsonBody);
      JSONObject responseBody = response.getBody();
      if (responseBody == null || responseBody.containsKey("error")) {
        return response; // 결제 실패 시 반환
      }

      try {
        attendCourseService.decrease(reserveCourseDTO.getScheduleId(), user, reserveCourseDTO.getUserCouponId(), reserveCourseDTO.getParticipantNum(), reserveCourseDTO.getFinalAmount());
      } catch (Exception e) {
        // 수강 신청 실패 시 결제 취소
        logger.error("수강 신청 처리 중 오류 발생. 결제를 취소합니다.", e);
        cancelTossPayment(responseBody.get("paymentKey").toString(), Long.parseLong(reserveCourseDTO.getAmount()));
        JSONObject errorResponse = new JSONObject();
        errorResponse.put("error", "수강 신청 실패: " + e.getMessage());
        return ResponseEntity.status(500).body(errorResponse);
      }

    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      logger.error("Interrupted exception occurred", e);
      throw new RuntimeException("결제 실패: " + e.getMessage(), e);
    } catch (Exception e) {
      logger.error("Exception occurred", e);
      throw new RuntimeException("결제 실패: " + e.getMessage(), e);
    } finally {
      lock.unlock();
    }
    return response;
  }

  private ResponseEntity<JSONObject> confirmTossPayment(String jsonBody) throws Exception {
    JSONObject response = sendRequest(parseRequestData(jsonBody), WIDGET_SECRET_KEY, "https://api.tosspayments.com/v1/payments/confirm");
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

  private void cancelTossPayment(String paymentKey, long amount) {
    try {
      JSONObject cancelRequest = new JSONObject();
      cancelRequest.put("paymentKey", paymentKey);
      cancelRequest.put("cancelReason", "수강 신청 실패");
      cancelRequest.put("cancelAmount", amount);

      sendRequest(cancelRequest, WIDGET_SECRET_KEY, "https://api.tosspayments.com/v1/payments/cancel");
      logger.info("결제가 취소되었습니다. paymentKey: {}", paymentKey);
    } catch (Exception e) {
      logger.error("결제 취소 중 오류 발생", e);
      throw new RuntimeException("결제 취소 실패: " + e.getMessage(), e);
    }
  }
}
