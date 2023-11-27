package com.issue.tracker.infra.web.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.issue.tracker.api.auth.AuthInput;
import com.issue.tracker.api.auth.LoginRequestModel;
import jakarta.ejb.EJB;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(urlPatterns = {"/login"})
public class LoginServlet extends HttpServlet {

    @EJB
    private AuthInput authInteractor;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String body = ServletUtils.readBody(req.getReader());

        LoginRequestModel requestBody;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            requestBody = objectMapper.readValue(body, LoginRequestModel.class);
        } catch (RuntimeException ex) {
            resp.setStatus(404);
            return;
        }

        if (requestBody == null) {
            resp.setStatus(404);
            return;
        }

        try {
            authInteractor.login(requestBody);
        } catch (RuntimeException ex) {
            resp.setStatus(404);
        }
    }
}
