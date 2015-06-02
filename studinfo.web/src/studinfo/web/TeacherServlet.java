

package studinfo.web;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.List;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import studinfo.core.ejb.TeacherBean;
import studinfo.core.entity.Teacher;

/**
 * Servlet implementation class TeacherServlet
 */
@WebServlet("/TeacherServlet")
public class TeacherServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	@EJB
    private TeacherBean bean; 
	private static final SimpleDateFormat format = new SimpleDateFormat(
			"yyyy-MM-dd");
    public TeacherServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		StringBuilder resp = new StringBuilder();
		String method = request.getParameter("method");
		if ("getAllTeacher".equals(method)) {
			List<Teacher> teachers = bean.getAllTeachers();
			resp.append("<table border='1'><tr><th>Имя<th>Группа<th>Дата рождения");
			for (Teacher s : teachers) {
				resp.append("<tr ><td>"
						+ s.getFirstName()
						+ " "
						+ s.getMiddleName()
						+ " "
						+ s.getLastName()
						+ "<td>"
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
		// TODO Auto-generated method stub
		String method = request.getParameter("method");
		if ("saveTeacher".equals(method)) {
			Teacher teacher = new Teacher();
			teacher.setFirstName(request.getParameter("firstName"));
			teacher.setMiddleName(request.getParameter("middleName"));
			teacher.setLastName(request.getParameter("lastName"));
			try {
				teacher.setBirthDate(format.parse(request
						.getParameter("dateOfBirth")));
			} catch (ParseException e) {
				// nothing
			}
			bean.save(teacher);
		}
		response.sendRedirect("/studinfo.web/teacher.html");
	}
	}



