package com.example.domain.util;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author xiaoaa
 * @date 2023/6/7 22:00
 **/
public class WebUtils {

    public static String renderString(HttpServletResponse response, String string) {
        response.setStatus(200);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        try {
            response.getWriter().print(string);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
