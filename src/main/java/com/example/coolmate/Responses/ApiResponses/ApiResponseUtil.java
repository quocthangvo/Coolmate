package com.example.coolmate.Responses.ApiResponses;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ApiResponseUtil {
    public static <T> ResponseEntity<ApiResponse<T>> successResponse(String message, T data) {
        ApiResponse<T> response = new ApiResponse<>(message, data);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public static <T> ResponseEntity<ApiResponse<T>> errorResponse(String message) {
        ApiResponse<T> response = new ApiResponse<>();

        response.setMessage(message);
        response.setData(null); // or you can set an appropriate default value if needed

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR) // Set HTTP status code to 500
                .body(response);
    }

}
