package com.tms.sportlight.exception;

import com.tms.sportlight.dto.common.ErrorField;
import com.tms.sportlight.dto.common.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;

@Slf4j
@RestControllerAdvice
public class APIResponseExceptionHandler extends ResponseEntityExceptionHandler {
    /**
     * 비지니스 로직 수행 도중 발생한 예외 처리
     * ErrorResponse을 생성할 때 예외가 발생했을 때 초기화한 메세지를 기본으로 사용하고,
     * 초기화한 메세지가 없다면 ErrorCode.DefaultMessage 사용
     * Http Status: ErrorCode.status
     *
     * @param e BizException
     * @return 예외 API Response
     */
    @ExceptionHandler(BizException.class)
    public ResponseEntity<ErrorResponse> handleBizException(BizException e) {
        log.error("APIResponseExceptionHandler.handleBizException", e);
        ErrorCode errorCode = e.getCode();
        String message = e.getMessage();
        return ResponseEntity
                .status(errorCode.getStatus())
                .body(ErrorResponse.of(errorCode, message));
    }

    /**
     * RequestBody, RequestPart binding error
     * BindingResult 로부터 binding error 가 발생한 field 정보를 response 에 포함하여 반환
     *
     * @param ex MethodArgumentNotValidException
     * @return 예외 API Response
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        ErrorCode errorCode = ErrorCode.INVALID_INPUT_VALUE;
        List<ErrorField> errorFields = ErrorField.of(ex.getBindingResult());
        return this.handleExceptionInternal(ex, ErrorResponse.of(errorCode, errorFields), headers, status, request);
    }

    /**
     * common exception handler
     * ResponseEntityExceptionHandler와 PortalExceptionHandler에서 명시한 예외가 아닌 예외 처리
     * HTTP Status: 500 (Internal Server Error)
     *
     * @param e Exception
     * @return 예외 API Response
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleCommonException(Exception e) {
        log.error("APIResponseExceptionHandler.handleCommonException", e);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ErrorResponse.of(ErrorCode.INTERNAL_SERVER_ERROR));
    }

    /**
     * 실제 예외 응답 생성 및 반환
     *
     * @return
     */
    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        log.error("APIResponseExceptionHandler.handleExceptionInternal", ex);
        if (body == null) {
            ErrorCode errorCode = ErrorCode.INTERNAL_SERVER_ERROR;
            ErrorResponse errorResponse = ErrorResponse.of(errorCode, ex.getMessage());
            return new ResponseEntity<>(errorResponse, headers, status);
        }
        return new ResponseEntity<>(body, headers, status);
    }
}
