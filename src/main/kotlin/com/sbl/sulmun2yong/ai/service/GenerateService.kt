package com.sbl.sulmun2yong.ai.service

import com.sbl.sulmun2yong.ai.domain.SurveyGeneratedByAI
import com.sbl.sulmun2yong.ai.exception.GenerationByAIFailed
import com.sbl.sulmun2yong.global.util.FileValidator
import com.sbl.sulmun2yong.survey.dto.response.SurveyMakeInfoResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

@Service
class GenerateService(
    @Value("\${ai-server.base-url}")
    private val aiServerBaseUrl: String,
    private val fileValidator: FileValidator,
) {
    fun generateSurvey(
        job: String,
        groupName: String,
        fileUrl: String,
    ): SurveyMakeInfoResponse {
        val allowedExtensions = mutableListOf(".txt", ".pdf")
        fileValidator.validateFileUrlOf(fileUrl, allowedExtensions)

        val response = postRequestToAIServer(job, groupName, fileUrl)
        return SurveyMakeInfoResponse.of(response)
    }

    private fun postRequestToAIServer(
        job: String,
        groupName: String,
        fileUrl: String,
    ): SurveyGeneratedByAI {
        val url = "$aiServerBaseUrl/generate/survey"
        val requestBody =
            mapOf(
                "job" to job,
                "group_name" to groupName,
                "file_url" to fileUrl,
            )

        val response =
            RestTemplate()
                .postForEntity(
                    url,
                    requestBody,
                    SurveyGeneratedByAI::class.java,
                )

        val responseBody = response.body

        if (response.statusCode != HttpStatus.OK ||
            responseBody == null
        ) {
            throw GenerationByAIFailed()
        }

        return responseBody
    }
}
