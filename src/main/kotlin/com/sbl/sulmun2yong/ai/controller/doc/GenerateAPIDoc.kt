package com.sbl.sulmun2yong.ai.controller.doc

import com.sbl.sulmun2yong.ai.dto.request.SurveyGenerateRequest
import com.sbl.sulmun2yong.ai.dto.response.SurveyGenerateResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody

@Tag(name = "AI 생성 기능", description = "AI 생성 기능 관련 API")
interface GenerateAPIDoc {
    @Operation(summary = "설문 생성")
    @PostMapping("/survey")
    fun generateSurvey(
        @RequestBody request: SurveyGenerateRequest,
    ): ResponseEntity<SurveyGenerateResponse>
}
