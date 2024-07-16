package com.sbl.sulmun2yong.user.controller

import com.sbl.sulmun2yong.user.service.AdminService
import org.springframework.security.core.session.SessionRegistry
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody

@Controller
@RequestMapping("/admin")
class AdminController(
    private val sessionRegistry: SessionRegistry,
    private val adminService: AdminService,
) {
    @GetMapping("/sessions/logged-in-users")
    @ResponseBody
    fun getAllLoggedInUsers() {
        val allLoggedInUsers = adminService.getAllLoggedInUsers(sessionRegistry.allPrincipals)
        return allLoggedInUsers
    }
}
