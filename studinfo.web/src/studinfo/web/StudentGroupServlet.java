package studinfo.web;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class StudentGroupServlet
 */
@WebServlet("/StudentGroupServlet")
public class StudentGroupServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final SimpleDateFormat format = new SimpleDateFormat(
			"yyyy-MM-dd");

	@EJB
	private StudentGroupBean bean;

	public StudentGroupServlet() {
		super();
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		StringBuilder resp = new StringBuilder();
		String method = request.getParameter("method");
		if ("getAllStudents".equals(method)) {
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
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String method = request.getParameter("method");
		//прове
		if ("saveStudent".equals(method)) {
			Student student = new Student();
			student.setFirstName(request.getParameter("firstName"));
			student.setMiddleName(request.getParameter("middleName"));
			student.setLastName(request.getParameter("lastName"));
			
		    Long idGroups = new Long(request.getParameter("GroupId"));	
		    student.setGroup(bean.getGroup(idGroups));

			try {
				student.setBirthDate(format.parse(request
						.getParameter("dateOfBirth")));
			} catch (ParseException e) {
				// nothing
			}
			bean.save(student);
		}
		response.sendRedirect("/studinfo.web/index.html");
	}
}
