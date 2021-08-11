package controllers.like;

import java.io.IOException;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.Employee;
import models.Like;
import models.Report;
import utils.DBUtil;

/**
 * Servlet implementation class LikeCreateServlet
 */
@WebServlet("/like/create")
public class LikeCreateServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public LikeCreateServlet() {
        super();

    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        EntityManager em = DBUtil.createEntityManager();

        // 日報取得
        Report r = em.find(Report.class, Integer.parseInt(request.getParameter("id")));

        // ログインユーザー取得
        Employee e = (Employee) request.getSession().getAttribute("login_employee");

        Like l = new Like();

        l.setReport(r);
        l.setEmployee(e);

        em.getTransaction().begin();
        em.persist(l);
        em.getTransaction().commit();
        em.close();

        response.sendRedirect(request.getContextPath() + "/reports/index");
    }

}
