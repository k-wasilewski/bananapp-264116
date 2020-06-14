package bananapp.controllers;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "UsernameController", value = "/auth/user")
public class UsernameController extends HttpServlet {
    private static String username;

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        this.username = request.getParameter("uname");
    }

    public static String getUsername() {
        return username;
    }
}
