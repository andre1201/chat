package studinfo.web;

import java.io.IOException;
import java.util.List;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import studinfo.core.ejb.GroupsBean;

import studinfo.core.entity.Group;

/**
 * Servlet implementation class GroupServlet
 */
@WebServlet("/GroupServlet")
public class GroupServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	@EJB
	private GroupsBean bean;

	public GroupServlet() {
		super();
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		StringBuilder resp = new StringBuilder();
		String method = request.getParameter("method");
		if ("getAllGroup".equals(method)) {
			if (request.getParameter("groupLevel") == ""
					|| request.getParameter("groupName") == "") {
				// пишем что введите значения...
			} else {
				List<Group> group = bean.getAllGroup();
				resp.append("<table border='1'><tr><th>Курс<th>Название курса");
				for (Group s : group) {
					resp.append("<tr ><td>" + s.getGroupLevel() + "</td><td> "
							+ s.getGroupName() + " </tr>");
				}
				resp.append("</table>");
			}
		}
		response.setContentType("text/html; charset=utf-8");
		response.getWriter().print(resp.toString());
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String method = request.getParameter("method");
		// прове
		if ("saveGroup".equals(method)) {
			Group group = new Group();
			Integer integr = new Integer(request.getParameter("groupLevel"));
			group.setGroupLevel(integr);
			// group.setGroupLevel(4);
			group.setGroupName(request.getParameter("groupName"));
			// group.setGroupName("4444");
			bean.save(group);
		}
		response.sendRedirect("/studinfo.web/group.html");
	}

}
