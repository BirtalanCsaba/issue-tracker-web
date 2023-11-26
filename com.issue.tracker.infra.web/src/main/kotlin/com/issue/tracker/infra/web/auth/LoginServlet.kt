package com.issue.tracker.infra.web.auth

import com.fasterxml.jackson.databind.ObjectMapper
import com.issue.tracker.api.auth.AuthInput
import com.issue.tracker.api.auth.LoginRequestModel
import jakarta.ejb.EJB
import jakarta.servlet.annotation.WebServlet
import jakarta.servlet.http.HttpServlet
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse

@WebServlet(urlPatterns = ["/login"])
open class LoginServlet : HttpServlet() {

    @EJB
    private lateinit var authInteractor: AuthInput

    override fun doPost(req: HttpServletRequest, resp: HttpServletResponse) {
        val body = ServletUtils.readBody(req.reader)

        val requestBody: LoginRequestModel?
        try {
            val objectMapper = ObjectMapper()
            requestBody = objectMapper.readValue(body, LoginRequestModel::class.java)
        } catch (ex: RuntimeException) {
            resp.status = 404
            return
        }

        if (requestBody == null) {
            resp.status = 404
            return
        }

        try {
            authInteractor.login(requestBody)
        } catch (ex: RuntimeException) {
            resp.status = 404
            return
        }
    }
}