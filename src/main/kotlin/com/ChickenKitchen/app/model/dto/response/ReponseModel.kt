package com.ChickenKitchen.app.model.dto.response

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "Standard API response model")
data class ResponseModel(
    @Schema(description = "HTTP status code", example = "200")
    val statusCode: Int,

    @Schema(description = "Response message", example = "Success")
    val message: String,

    @Schema(description = "Response data, can be object or null")
    val data: Any? = null
) {
    companion object {
        fun success(data: Any? = null, message: String = "Success"): ResponseModel {
            return ResponseModel(200, message, data)
        }

        fun error(statusCode: Int = 400, message: String = "Failed"): ResponseModel {
            return ResponseModel(statusCode, message, null)
        }
    }
}
