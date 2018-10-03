package servlets;

import vk.VKAuth;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class MainServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        VKAuth auth;

        if(req.getSession().getAttribute("VKA") == null) {
            auth = new VKAuth();
            req.getSession().setAttribute("VKA", auth);
        } else {
            auth = (VKAuth) req.getSession().getAttribute("VKA");
        }

        String link = auth.getAuthLink();
        req.setAttribute("link", link);

        if(!auth.isCodeSet() && req.getParameter("code") != null){
            auth.setCode(req.getParameter("code"));
            Cookie cookie = new Cookie("isAuth", "true");
            cookie.setMaxAge(2592000);
            resp.addCookie(cookie);
        }

        if(auth.isCodeSet()){
            String userName = auth.getUserName(auth.getActorID());
            String friends = auth.getFriendsNames(auth.getFriendsIDs(auth.getActorID(), 5)).toString();
            req.setAttribute("isButton", false);
            req.setAttribute("userName", userName);
            req.setAttribute("friends", friends);
        } else {
            req.setAttribute("isButton", true);

            Cookie[] cookies = req.getCookies();
            req.setAttribute("isAuth", false);
            if (cookies != null) {
                for(Cookie c : cookies){
                    if (c.getName().compareTo("isAuth") == 0 && c.getValue().compareTo("true") == 0){
                        req.setAttribute("isAuth", true);
                    }
                }
            }
        }

        req.getRequestDispatcher("mainpage.jsp").forward(req, resp);


    }
}
