package com.sbl.sulmun2yong.user.controller

import com.sbl.sulmun2yong.global.config.oauth2.CustomOAuth2User
import com.sbl.sulmun2yong.user.controller.doc.UserApiDoc
import com.sbl.sulmun2yong.user.dto.response.UserProfileResponse
import com.sbl.sulmun2yong.user.service.UserService
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import java.util.UUID

@Controller
@RequestMapping("/user")
class UserController(
    private val userService: UserService,
) : UserApiDoc {
    @GetMapping("/profile")
    @ResponseBody
    override fun getUserProfile(
        @AuthenticationPrincipal customOAuth2User: CustomOAuth2User,
    ): ResponseEntity<UserProfileResponse> {
        val id = UUID.fromString(customOAuth2User.name)
        return ResponseEntity.ok(userService.getUserProfile(id))
    }
}
