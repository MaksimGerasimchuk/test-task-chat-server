/*
    © Герасимчук М. Ю. 2023
 */
package com.gerasimchuk.chat.rest.servlet;

import com.gerasimchuk.chat.config.JsonMapper;
import com.gerasimchuk.chat.rest.response.ErrorResponse;
import com.google.inject.Inject;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class AbstractHttpServlet extends HttpServlet {

    protected static final String JSON_CONTENT_TYPE = "application/json";
    protected static final String OCTET_STREAM_CONTENT_TYPE = "application/octet-stream";
    protected static final Logger log = Logger.getLogger(AbstractHttpServlet.class.getName());
    protected JsonMapper jsonMapper;

    @Inject
    public void setJsonMapper(JsonMapper jsonMapper) {
        this.jsonMapper = jsonMapper;
    }

    protected void writeAndFlush(HttpServletResponse resp, Object responseBody) {
        try {
            resp.setContentType(JSON_CONTENT_TYPE);
            resp.getWriter().write(jsonMapper.toJson(responseBody));
            resp.getWriter().flush();
        } catch (IOException e) {
            log.log(Level.INFO, () -> "Error while writing response");
        }
    }

    protected void writeError(HttpServletResponse resp, String message) {
        if (!resp.isCommitted()) {
            resp.setContentType(JSON_CONTENT_TYPE);
            try {
                resp.getWriter().write(jsonMapper.toJson(new ErrorResponse(HttpServletResponse.SC_BAD_REQUEST,
                        message)));
                resp.getWriter().flush();
            } catch (IOException e) {
                log.log(Level.INFO, () -> "Error while sending error response " + resp + ", reason: " + e.getMessage());
            }
        } else {
            log.log(Level.INFO, () -> "Could not write error: response already committed. Error: " + message);
        }
    }


    protected <T> T getBody(HttpServletRequest req, Class<T> bodyClass) {
        String bodyString;
        try {
            log.log(Level.INFO, () -> "Trying to parse request body");
            bodyString = req.getReader().lines().collect(Collectors.joining());
            log.log(Level.INFO, () -> "String parsed: " + bodyString);
            return jsonMapper.fromJson(bodyString, bodyClass);
        } catch (IOException e) {
            log.log(Level.INFO, () -> "Error while parsing request body: " + req + "reason: " + e.getMessage());
            return null;
        }
    }

    protected String getRequestParamOrWriteError(HttpServletRequest req, HttpServletResponse resp, String paramName) {
        String param = req.getParameter(paramName);
        if (param == null) {
            writeError(resp, "Param " + paramName + " is mandatory");
        }
        return param;
    }
}
