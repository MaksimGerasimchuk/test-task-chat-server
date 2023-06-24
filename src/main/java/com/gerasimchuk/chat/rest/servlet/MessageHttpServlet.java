/*
    © Герасимчук М. Ю. 2023
 */
package com.gerasimchuk.chat.rest.servlet;

import com.gerasimchuk.chat.common.HttpRequestParamName;
import com.gerasimchuk.chat.service.api.ChatService;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet(name = "messageServlet", urlPatterns = "/v1.0/messages")
@Singleton
public class MessageHttpServlet extends AbstractHttpServlet {

    private static final Logger log = Logger.getLogger(MessageHttpServlet.class.getName());

    private static final String EXPORT_FILE_EXTENSION = ".txt";

    private ChatService chatService;

    @Inject
    public void setChatService(ChatService chatService) {
        this.chatService = chatService;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        log.log(Level.INFO, () -> "Performing get request for messages fetching" + req);
        var userFrom = getRequestParamOrWriteError(req, resp, HttpRequestParamName.USER_FROM.getParamName());
        var userTo = getRequestParamOrWriteError(req, resp, HttpRequestParamName.USER_TO.getParamName());
        var export = getRequestParamOrWriteError(req, resp, HttpRequestParamName.EXPORT.getParamName());
        if (userFrom == null || userTo == null || export == null) {
            log.log(Level.INFO, () -> "Mandatory params not found ");
            return;
        }
        var history = chatService.getHistory(userFrom, userTo);
        boolean forExport = Boolean.parseBoolean(export);
        if (forExport) {
            log.log(Level.INFO, () -> "Export requested");
            writeForDownloadAndFlush(resp, jsonMapper.toJson(history), userFrom, userTo);
        } else {
            log.log(Level.INFO, () -> "Regular response requested ");
            writeAndFlush(resp, history);
        }
    }

    private void writeForDownloadAndFlush(HttpServletResponse resp, String responseBody, String userFrom,
                                          String userTo) {
        try {
            resp.setContentType(OCTET_STREAM_CONTENT_TYPE);
            resp.setHeader("Content-Disposition",
                    "attachment; filename=" + userFrom + "_" + userTo + "_" +
                            ZonedDateTime.now().toEpochSecond() + EXPORT_FILE_EXTENSION);
            resp.getWriter().write(responseBody);
            resp.getWriter().flush();
        } catch (IOException e) {
            log.log(Level.INFO, () -> "Error while writing response, reason: " + e.getMessage());
        }
    }

}
