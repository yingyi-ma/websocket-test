package com.test.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 登录
 */
public class LoginServlet extends HttpServlet {


    private static Map<String, String> userInfoMap = new HashMap<>();

    static {
        userInfoMap.put("admin", "123456");
        userInfoMap.put("user1", "123456");
        userInfoMap.put("user2", "123456");

    }


    public void doGet(HttpServletRequest request,
                      HttpServletResponse response)
            throws ServletException, IOException {
        //设置编码表
        response.setContentType("text/html;charset=utf-8");
        //获取表单数据
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        if (login(username,password)) {
            response.getWriter().write("登录失败!");
            //结束方法后面不再执行
            return;
        }

        //设置Session 用户名称
        HttpSession httpSession = request.getSession();
        httpSession.setAttribute("SESSION_USERNAME",username);

        //登录成功时
        response.getWriter().write("登录成功,3秒后跳转");
        response.sendRedirect(request.getContextPath() + "/test.html");
    }

    /**
     * 登录
     */
    private boolean login(String username, String password) {
        if (userInfoMap.get(username) == null) {
            return true;
        }
        if (!password.equals(userInfoMap.get(username))) {
            return true;
        }
        return false;
    }


    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
