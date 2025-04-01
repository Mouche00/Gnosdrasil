package org.yc.gnosdrasil.gduserservice.utils.helpers;

import org.springframework.http.ResponseEntity;
import org.yc.gnosdrasil.gduserservice.utils.response.ApiResponse;

import java.util.Map;

public class ResponseHelper {

    public static <T> ResponseEntity<ApiResponse<T>> successResponse(int status, String message, T data) {
        return ResponseEntity.status(status).body(
                new ApiResponse<>(true, status, message, data)
        );
    }

    public static <T> ResponseEntity<ApiResponse<T>> errorResponse(int status, String message, T errors) {
        return ResponseEntity.status(status).body(
                new ApiResponse<>(false, status, message, errors)
        );
    }

    // Used for mixed responses, incorporating errors and data at the same time, such as the case for batch deal creation
    public static <T> ResponseEntity<ApiResponse<Map<String, T>>> mixedResponse(int status, String message, Map<String, T> mixedData) {
        return ResponseEntity.status(status).body(
                new ApiResponse<>(true, status, message, mixedData)
        );
    }
}
