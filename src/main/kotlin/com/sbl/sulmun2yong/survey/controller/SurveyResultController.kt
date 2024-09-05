package com.sbl.sulmun2yong.survey.controller

import com.sbl.sulmun2yong.global.annotation.LoginUser
import com.sbl.sulmun2yong.survey.controller.doc.SurveyResultApiDoc
import com.sbl.sulmun2yong.survey.service.SurveyResultService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/api/v1/surveys/result")
class SurveyResultController(
    private val surveyResultService: SurveyResultService,
) : SurveyResultApiDoc {
    @GetMapping("/{survey-id}")
    override fun getSurveyResult(
        @PathVariable("survey-id") surveyId: UUID,
        @LoginUser id: UUID,
    ) = ResponseEntity.ok(surveyResultService.getSurveyResult(surveyId, id))
}
