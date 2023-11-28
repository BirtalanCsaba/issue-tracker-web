package com.issue.tracker.infra.web.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.issue.tracker.api.auth.AuthInput;
import com.issue.tracker.api.auth.LoginRequestModel;
import com.issue.tracker.api.logger.LogType;
import com.issue.tracker.api.logger.LoggerBuilder;
import com.issue.tracker.infra.web.common.ServletUtils;
import jakarta.ejb.EJB;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = {"/login"})
public class LoginServlet extends HttpServlet {

    @EJB
    private AuthInput authInteractor;

    @EJB
    private LoggerBuilder loggerBuilderBase;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        String body;
        try {
            body = ServletUtils.readBody(req.getReader());
        } catch (Exception ex) {
            loggerBuilderBase.create(getClass(), LogType.ERROR, ex.getMessage())
                    .build()
                    .print();
            resp.setStatus(500);
            return;
        }


        LoginRequestModel requestBody;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            requestBody = objectMapper.readValue(body, LoginRequestModel.class);
        } catch (Exception ex) {
            loggerBuilderBase.create(getClass(), LogType.ERROR, ex.getMessage())
                    .build()
                    .print();
            resp.setStatus(500);
            return;
        }

        if (requestBody == null) {
            loggerBuilderBase.create(getClass(), LogType.ERROR, "Request body is null")
                    .build()
                    .print();
            resp.setStatus(500);
            return;
        }

        try {
            boolean authenticated = authInteractor.login(requestBody);
            if (!authenticated) {
                resp.setStatus(403);
                return;
            }
            resp.setStatus(200);
        } catch (RuntimeException ex) {
            loggerBuilderBase.create(getClass(), LogType.ERROR, ex.getMessage())
                    .build()
                    .print();
            resp.setStatus(500);
        }
    }
}
