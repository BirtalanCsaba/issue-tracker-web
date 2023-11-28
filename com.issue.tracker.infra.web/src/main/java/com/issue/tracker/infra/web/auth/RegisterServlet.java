package com.issue.tracker.infra.web.auth;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.issue.tracker.api.auth.AuthInput;
import com.issue.tracker.api.auth.RegisterRequestModel;
import com.issue.tracker.api.auth.UserAlreadyExistException;
import com.issue.tracker.api.auth.UserResponseModel;
import com.issue.tracker.api.logger.LogType;
import com.issue.tracker.api.logger.LoggerBuilder;
import com.issue.tracker.infra.web.common.ErrorServletResponse;
import com.issue.tracker.infra.web.common.ServletUtils;
import jakarta.ejb.EJB;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Objects;

@WebServlet(urlPatterns = "/register")
public class RegisterServlet extends HttpServlet {

    @EJB
    private AuthInput authInteractor;

    @EJB
    private LoggerBuilder loggerBuilder;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String body;
        try {
            body = ServletUtils.readBody(req.getReader());
        } catch (Exception ex) {
            loggerBuilder.create(getClass(), LogType.ERROR, ex.getMessage())
                    .build()
                    .print();
            sendErrorResponse(resp, 500, null);
            return;
        }

        RegisterRequestModel requestBody;
        try {
            requestBody = objectMapper.readValue(body, RegisterRequestModel.class);
        } catch (Exception ex) {
            sendErrorResponse(resp, 404, "Username already taken");
            return;
        }

        try {
            UserResponseModel createdUser = authInteractor.register(requestBody);
            resp.getWriter().print(objectMapper.writeValueAsString(createdUser));
        } catch (RuntimeException ex) {
            sendErrorResponse(resp, 404, "Username already taken");
        }

        resp.setContentType("application/json");
    }

    private void sendErrorResponse(HttpServletResponse resp, int errorStatusCode, String error) throws IOException {
        resp.setStatus(errorStatusCode);
        ErrorServletResponse response;
        response = new ErrorServletResponse(Objects.requireNonNullElse(error, "Something went wrong"));
        resp.getWriter().print(objectMapper.writeValueAsString(response));
    }
}
