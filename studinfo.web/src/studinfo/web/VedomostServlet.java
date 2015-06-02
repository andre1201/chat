package studinfo.web;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import java.util.List;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import studinfo.core.ejb.StudentGroupBean;
import studinfo.core.entity.Group;
import studinfo.core.entity.Student;

/**
 * Servlet implementation class VedomostServlet
 */
@WebServlet("/VedomostServlet")
public class VedomostServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final SimpleDateFormat format = new SimpleDateFormat(
			"yyyy-MM-dd");

	@EJB
	private StudentGroupBean bean;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public VedomostServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		StringBuilder resp = new StringBuilder();
		String method = request.getParameter("method");
		if ("getAllGroupStud".equals(method)) {
			List<Student> students = bean.getAllStudents();
			resp.append("<table border='1'><tr><th>Имя<th>Группа<th>Дата рождения");
			for (Student s : students) {
				Long longg=new Long(request.getParameter("groupLevel"));
				if(s.getGroup().getId()==longg)
				resp.append("<tr ><td>"
						+ s.getFirstName()
						+ " "
						+ s.getMiddleName()
						+ " "
						+ s.getLastName()
						+ "<td>"
						+ s.getGroup().getGroupName()
						+ "<td>"
						+ ((s.getBirthDate() != null) ? format.format(s
								.getBirthDate()) : "-"));
			}
			resp.append("</table>");
		}
		response.setContentType("text/html; charset=utf-8");
		response.getWriter().print(resp.toString());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		StringBuilder resp = new StringBuilder();
		String method = request.getParameter("method");
		//прове
		if ("saveGroup".equals(method)) {
			List<Student> students = bean.getAllStudents();
			resp.append("<table border='1'><tr><th>Имя<th>Группа<th>Дата рождения");
			for (Student s : students) {
					resp.append("<tr ><td>"
							+ s.getFirstName()
							+ " "
							+ s.getMiddleName()
							+ " "
							+ s.getLastName()
							+ "<td>"
							+ s.getGroup().getGroupName()
							+ "<td>"
							+ ((s.getBirthDate() != null) ? format.format(s
									.getBirthDate()) : "-"));	
				
				
			}
			resp.append("</table>");
		}
		response.setContentType("text/html; charset=utf-8");
		response.getWriter().print(resp.toString());
		response.sendRedirect("/studinfo.web/vedomost.html");
	}
	}


