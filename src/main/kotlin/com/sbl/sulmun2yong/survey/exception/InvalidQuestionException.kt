package com.sbl.sulmun2yong.survey.exception

import com.sbl.sulmun2yong.global.error.BusinessException
import com.sbl.sulmun2yong.global.error.ErrorCode

class InvalidQuestionException : BusinessException(ErrorCode.INVALID_QUESTION)
