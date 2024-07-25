package com.sbl.sulmun2yong.drawing.repository

import com.sbl.sulmun2yong.survey.entity.SurveyDocument
import org.springframework.data.mongodb.repository.MongoRepository
import java.util.UUID

interface DrawingHistoryRepository : MongoRepository<SurveyDocument, UUID>
