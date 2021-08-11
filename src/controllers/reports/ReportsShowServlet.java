package controllers.reports;

import java.io.IOException;

import javax.persistence.EntityManager;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.Employee;
import models.Report;
import utils.DBUtil;

/**
 * Servlet implementation class ReportsShowServlet
 */
@WebServlet("/reports/show")
public class ReportsShowServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public ReportsShowServlet() {
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

        //ログインユーザーのいいねの数取得
        long like_count = em.createNamedQuery("getLikeCount2", Long.class)
                .setParameter("report", r)
                .setParameter("employee", e)
                .getSingleResult();

        int Likecount = 0;

        if (r.getEmployee().getId() == e.getId()) {
            //①ログインユーザー自身の日報の場合
            Likecount = 1;
        } else if (like_count > 0) {
            //②すでにいいねを押した日報の場合
            Likecount = 1;
        }
        //①と②の処理の反映
        request.setAttribute("Likecount", Likecount);

        em.close();

        request.setAttribute("report", r);
        request.setAttribute("_token", request.getSession().getId());

        RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/reports/show.jsp");
        rd.forward(request, response);
    }

}
