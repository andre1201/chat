package studinfo.web;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.text.html.HTML;
import javax.validation.constraints.Null;

import studinfo.core.ejb.GradeBean;
import studinfo.core.ejb.GroupsBean;
import studinfo.core.ejb.PeriodBean;
import studinfo.core.ejb.StudentGroupBean;
import studinfo.core.ejb.TeacherSubjectBean;
import studinfo.core.entity.Grade;
import studinfo.core.entity.Group;
import studinfo.core.entity.Period;
import studinfo.core.entity.Student;
import studinfo.core.entity.Subject;
import studinfo.core.entity.Teacher;
import studinfo.web.HTMLUtil.HTMLFormInputType;
import studinfo.web.HTMLUtil.HTMLTable;

/**
 * Servlet implementation class StudentGroupServlet
 */
@WebServlet("/index")
public class IndexServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final SimpleDateFormat format = new SimpleDateFormat(
			"yyyy-MM-dd");

	@EJB
	private StudentGroupBean studentsBean;
	@EJB
	private TeacherSubjectBean teachersBean;
	@EJB
	private PeriodBean periodBean;
	@EJB
	private GradeBean gradeBean;
	@EJB
	private GroupsBean groupBean;

	public IndexServlet() {
		super();
	}
	HTMLUtil html = new HTMLUtil();
	
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String method = request.getParameter("method");
		styleMenu();
		html.echo("<div class='menu'>");
		html.html().title("������ �� ����������")//�������� ��������� ��������
				.a("index?method=student", "��������")//������� �� ��������
				.a("index?method=group", "������")//������� �� ������
				.a("index?method=teacher", "�������������")//������� �� �������������
				.a("index?method=subject", "��������")//������� �� ��������
				.a("index?method=period", "�������")//������� �� �������
				.a("index?method=grade", "������")
				.a("index?method=getList", "���������");//������� �� ���������
		html.echo("</div><div style='float:both;'></div><div class='content'>");
		if ("student".equals(method)) {
			viewStudent();
		} else if ("group".equals(method)) {
			viewGroup();
		} else if ("teacher".equals(method)) {
			viewTeacher();
		} else if ("subject".equals(method)) {
			viewSubject();		
		} else if ("period".equals(method)) {
			 viewPeriod();
		}else if("getList".equals(method)){
				viewVedomost(request);
		}
		else if ("grade".equals(method)) {
			 viewGrade(request);
		}else{
			html.echo("<h3>�������� ����</h3>");
		}
		html.echo("</div>");
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html; charset=utf-8");
		response.getWriter().println(html.done());
	}

	protected void doPost(HttpServletRequest request,
		HttpServletResponse response) throws ServletException, IOException {
		String method = request.getParameter("method");
		
		//���������� ��������
		if ("newStudent".equals(method)) {
			Student student = new Student();
			student.setFirstName(request.getParameter("firstName"));
			student.setMiddleName(request.getParameter("middleName"));
			student.setLastName(request.getParameter("lastName"));
			student.setGroup(studentsBean.getGroup(new Long(request
					.getParameter("groupId"))));

			try {
				student.setBirthDate(format.parse(request
						.getParameter("dateOfBirth")));
			} catch (ParseException e) {
				// nothing
			}
			studentsBean.save(student);
			response.sendRedirect("index?method=student");
			
		} else if ("newGroup".equals(method)) {
			//���������� ������
			Group group = new Group();
			group.setGroupLevel(new Integer(request.getParameter("groupLevel")));
			group.setGroupName(request.getParameter("groupName"));
			studentsBean.save(group);
			response.sendRedirect("index?method=group");
		} else if ("newTeacher".equals(method)) {
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
			teachersBean.save(teacher);
			response.sendRedirect("index?method=teacher");
		} else if ("newSubject".equals(method)) {
			//���������� �������
			Subject subject = new Subject();
			subject.setSubjectDescription(request
					.getParameter("subjectDescription"));
			subject.setSubjectName(request.getParameter("subjectName"));
			teachersBean.save(subject);
			response.sendRedirect("index?method=subject");
			//���������� �������
		} else if ("newPeriod".equals(method)) {
			Period period = new Period();
			try {
				period.setPeriodName(request.getParameter("periodName"));
				period.setStartDate(format.parse(request
						.getParameter("periodStart")));
				period.setEndDate(format.parse(request
						.getParameter("periodEnd")));
				periodBean.save(period);
			} catch (ParseException e) {

			}
			response.sendRedirect("index?method=period");
		} else if ("newGrade".equals(method)) {
			//���������� ������
			try {
				Long studentId = new Long(request.getParameter("student"));
				Long teacherId = new Long(request.getParameter("teacher"));
				Long periodId = new Long(request.getParameter("period"));
				Long subjectId = new Long(request.getParameter("subject"));
				Integer gradeLevel = new Integer(request.getParameter("grade"));
				gradeBean.createGrade(studentId, teacherId, subjectId,
						periodId, gradeLevel);
				try {
					Long groupId = studentsBean.getGroupByStudent(studentId)
							.getId();
					response.sendRedirect("index?method=grade&group=" + groupId
							+ "&period=" + periodId);
				} catch (RuntimeException e) {
					response.sendRedirect("index?method=grade");
				}
			} catch (NumberFormatException e) {

			}

		}
	
  }

	//======================================================================
	// ���������� �������� ��������
	//======================================================================
	void viewSubject(){
		html.echo("<h3>��������</h3>");
		HTMLTable table = html.table().tr().th("�������").th("��������");
		for (Subject s : teachersBean.getAllSubjects()) {
			table.tr().td(s.getSubjectName()).td(s.getSubjectDescription());
		}
		table.done();
		html.br().br();
		html.echo("<h3>�������� �������</h3>");
		html.form("index", "POST")
				.input(HTMLFormInputType.HIDDEN, "method", "newSubject")
				.echo("<table><tr><td>�������<td>")
				.input(HTMLFormInputType.TEXT, "subjectName", "")
				.echo("<tr><td>��������<td>")
				.input(HTMLFormInputType.TEXT, "subjectDescription", "")
				.echo("<tr><td colspan=2>")
				.input(HTMLFormInputType.SUBMIT, "s", "�������� �������")
				.echo("</table>").done();
	}
			//======================================================================
			// ���������� �������� �������
			//======================================================================
	void viewStudent(){
		html.echo("<h3>��������</h3>");
		HTMLTable table = html.table().tr().th("������� ��� ��������")
				.th("���� ��������").th("������");
		for (Student s : studentsBean.getAllStudents()) {
			table.tr()
					.td(s.getLastName() + " " + s.getFirstName() + " "
							+ s.getMiddleName())
					.td(format.format(s.getBirthDate()))
					.td(s.getGroup().getGroupName());
		}
		table.done();
		html.br().br();
		html.echo("<h3>�������� ��������</h3>");
		Map<String, Object> groupMap = new HashMap<>();
		for (Group g : studentsBean.getAllGroups()) {
			groupMap.put(g.getGroupName(), g.getId());
		}
		
		html.form("index", "POST")
				.input(HTMLFormInputType.HIDDEN, "method", "newStudent")
				//����� = newStudent
				.echo("<table><tr><td>�������<td>")
				//���� ��� �������
				.input(HTMLFormInputType.TEXT, "lastName", "")
				.echo("<tr><td>���<td>")
				//���� ��� ���
				.input(HTMLFormInputType.TEXT, "firstName", "")
				.echo("<tr><td>��������<td>")
				//���� ��� ��������
				.input(HTMLFormInputType.TEXT, "middleName", "")
				.echo("<tr><td>���� ��������<td>")
				.input(HTMLFormInputType.TEXT, "dateOfBirth", "1980-12-28")
				.echo("<tr><td>������<td>").select("groupId", groupMap)
				.echo("<tr><td colspan=2>")
				.input(HTMLFormInputType.SUBMIT, "s", "�������� ��������")
				.echo("</table>").done();
	}
	
	//======================================================================
	// ���������� �������� ������
	//======================================================================	
	void viewGroup(){
		html.echo("<h3>������</h3>");
		HTMLTable table = html.table().tr().th("����")
				.th("�������� ������");
		for (Group g : studentsBean.getAllGroups()) {
			table.tr().td("" + g.getGroupLevel()).td(g.getGroupName());
		}
		table.done();

		html.br().br();
		html.echo("<h3>�������� ������</h3>");
		html.form("index", "POST")
				.input(HTMLFormInputType.HIDDEN, "method", "newGroup")
				.echo("<table><tr><td>����<td>")
				.input(HTMLFormInputType.TEXT, "groupLevel", "")
				.echo("<tr><td>�������� ������<td>")
				.input(HTMLFormInputType.TEXT, "groupName", "")
				.echo("<tr><td colspan=2>")
				.input(HTMLFormInputType.SUBMIT, "s", "�������� ������")
				.echo("</table>").done();
	}
	
	//======================================================================
	// ���������� �������� �������
	//======================================================================
	void viewTeacher(){
		html.echo("<h3>�������������</h3>");
		HTMLTable table = html.table().tr().th("������� ��� ��������")
				.th("���� ��������");
		for (Teacher s : teachersBean.getAllTeachers()) {
			table.tr()
					.td(s.getLastName() + " " + s.getFirstName() + " "
							+ s.getMiddleName())
					.td(format.format(s.getBirthDate()));
		}
		table.done();
		html.br().br();
		html.echo("<h3>�������� �������������</h3>");
		html.form("index", "POST")
				.input(HTMLFormInputType.HIDDEN, "method", "newTeacher")
				.echo("<table><tr><td>�������<td>")
				.input(HTMLFormInputType.TEXT, "lastName", "")
				.echo("<tr><td>���<td>")
				.input(HTMLFormInputType.TEXT, "firstName", "")
				.echo("<tr><td>��������<td>")
				.input(HTMLFormInputType.TEXT, "middleName", "")
				.echo("<tr><td>���� ��������<td>")
				.input(HTMLFormInputType.TEXT, "dateOfBirth", "1980-12-28")
				.echo("<tr><td colspan=2>")
				.input(HTMLFormInputType.SUBMIT, "s",
						"�������� �������������").echo("</table>").done();
	}
	
	void viewPeriod(){
		html.echo("<h3>�������</h3>");
		HTMLTable table = html.table().tr().th("��������").th("������")
				.th("���������");
		for (Period p : periodBean.getAllPeriods()) {
			table.tr().td(p.getPeriodName())
					.td(format.format(p.getStartDate()))
					.td(format.format(p.getEndDate()));
		}
		table.done();
		html.br().br();
		html.echo("<h3>�������� ������</h3>");
		html.form("index", "POST")
				.input(HTMLFormInputType.HIDDEN, "method", "newPeriod")
				.echo("<table><tr><td>��������<td>")
				.input(HTMLFormInputType.TEXT, "periodName", "")
				.echo("<tr><td>���� ������<td>")
				.input(HTMLFormInputType.TEXT, "periodStart", "2015-01-01")
				.echo("<tr><td>���� ���������<td>")
				.input(HTMLFormInputType.TEXT, "periodEnd", "2015-01-28")
				.echo("<tr><td colspan=2>")
				.input(HTMLFormInputType.SUBMIT, "s", "�������� ������")
				.echo("</table>").done();
	}
	//======================================================================
	// ���������� �������� ���������
	//======================================================================	
	void viewVedomost(HttpServletRequest request){
		
		html.echo("<h3>������� ���������</h3>");
		Map<String, Object> groupMap = new LinkedHashMap<>();
		for (Group g : studentsBean.getAllGroups()) {
			groupMap.put(g.getGroupName(), g.getId());
		}
		html.form("index", "GET").
		input(HTMLFormInputType.HIDDEN, "method", "getList").
		echo("�������� ������ ").
		select("group", groupMap).
		input(HTMLFormInputType.SUBMIT, "view", "��������").done();
		String groupId = request.getParameter("group");
		String view = request.getParameter("view");
		
		if ( groupId == null && view ==null) {
			//html.echo("<b>�������� ������ </b>");
		} else{
			Group g = studentsBean.getGroup(new Long(groupId));
			html.echo("������� ��������� �� ������ "+g.getGroupName()).br();
			//��������� �������

				HTMLTable table = html.table().tr().th("�").th("���");
				for(Subject subj : gradeBean.getSubject()){
					table.th(subj.getSubjectName());
				}
			//���� �������
			int i = 1;
			for(Student s:studentsBean.getStudentsInGroup(g)){	
				table.tr().
						td(""+i++).td(s.getLastName()+" "+
						s.getFirstName()+" "+
						s.getMiddleName());
				
				for(Grade grades:studentsBean.getAllGrade(s)){
						if(grades.getGradeLevel()!=null)
							table.td(""+grades.getGradeLevel());
						else{
							table.td("n/a");
						}
				}			
			}	
			if(i==1){
				table.tr().th("������� ���!");
			}	
			//����� �������
			table.done();
			
		}
		
	}
	//======================================================================
	// ���������� �������� ������
	//======================================================================
	void viewGrade(HttpServletRequest request){

		html.echo("<h3>������ � ������</h3>");
		Map<String, Object> groupMap = new LinkedHashMap<>();
		for (Group g : studentsBean.getAllGroups()) {
			groupMap.put(g.getGroupName(), g.getId());
		}
		Map<String, Object> periodMap = new LinkedHashMap<>();
		periodMap.put("���", "all");
		for (Period p : periodBean.getAllPeriods()) {
			periodMap.put(p.getPeriodName(), p.getId());
		}
		html.form("index", "GET")
				.input(HTMLFormInputType.HIDDEN, "method", "grade")
				.echo("<table><tr><td>������: <td>")
				.select("group", groupMap).echo("<td>������: <td>")
				.select("period", periodMap).echo("<td>")
				.input(HTMLFormInputType.SUBMIT, "s", "��������")
				.echo("</table>").done();
		// zzz
		html.br().br();
		String period = request.getParameter("period");
		String group = request.getParameter("group");
		if (period == null && group == null) {
			html.echo("<b>�������� ������ � ������</b>");
		} else {
			List<Grade> grades;
			Long groupId = new Long(group);
			if ("all".equals(period)) {
				grades = gradeBean.getGradeByGroup(groupId);
			} else {
				Long periodId = new Long(period);
				grades = gradeBean.getGradeByGroupAndPeriod(groupId,
						periodId);
			}
			HTMLTable table = html.table().tr().th("������")
					.th("��� ��������").th("��� �������������")
					.th("�������").th("������").th("����");
			for (Grade g : grades) {
				table.tr()
						.td(g.getPeriod().getPeriodName())
						.td(g.getStudent().getLastName() + " "
								+ g.getStudent().getFirstName() + " "
								+ g.getStudent().getMiddleName())
						.td(g.getTeacher().getLastName() + " "
								+ g.getTeacher().getFirstName() + " "
								+ g.getTeacher().getMiddleName())
						.td(g.getSubject().getSubjectName())
						.td("" + g.getGradeLevel())
						.td(format.format(g.getGradeDate()));
			}
			table.done();

		}
		html.br().br();
		html.echo("<h3>�������� ������</h3>");
		Map<String, Object> studentsMap = new LinkedHashMap<>();
		for (Student s : studentsBean.getAllStudents()) {
			studentsMap.put(
					s.getGroup().getGroupName() + "/" + s.getLastName()
							+ " " + s.getFirstName() + " "
							+ s.getMiddleName(), s.getId());
		}

		Map<String, Object> teacherMap = new LinkedHashMap<>();
		for (Teacher t : teachersBean.getAllTeachers()) {
			teacherMap.put(t.getLastName() + " " + t.getFirstName() + " "
					+ t.getMiddleName(), t.getId());
		}
		Map<String, Object> subjectMap = new LinkedHashMap<>();
		for (Subject s : teachersBean.getAllSubjects()) {
			subjectMap.put(s.getSubjectName(), s.getId());
		}
		periodMap.remove("���");
		Map<String, Object> gradeMap = new LinkedHashMap<String, Object>();
		gradeMap.put("�������", new Integer(5));
		gradeMap.put("������", new Integer(4));
		gradeMap.put("�����������������", new Integer(3));
		gradeMap.put("�������������������", new Integer(2));
		gradeMap.put("�����", new Integer(1));
		html.form("index", "POST")
				.input(HTMLFormInputType.HIDDEN, "method", "newGrade")
				.echo("<table><tr><td>�������<td>")
				.select("subject", subjectMap)
				.echo("<td>�������������<td>")
				.select("teacher", teacherMap).echo("<tr><td>������<td>")
				.select("period", periodMap).echo("<td>�������<td>")
				.select("student", studentsMap).echo("<tr><td>������<td>")
				.select("grade", gradeMap).echo("<td colspan=2>")
				.input(HTMLFormInputType.SUBMIT, "s", "�������� ������")
				.echo("</table>").done();
	
	}
	
	void styleMenu(){
		html.echo("<style>");
		html.echo("a{"
				+ "display:block;"
				+ "margin-left:3px;"
				+ "padding:5px;"
				+ "font-size:20px;"
				+ "}");
		html.echo("a:hover{"
				+ "border-top:4px solid red;"
				+ "color:#D5547B;"
				+ "}");
		html.echo(".menu{"
				+ "margin-top:20px;"
				+ "float:left;"
				+ "border:4px solid rgb(226, 163, 163);"
				+ "}");
		html.echo(".content{"
				+ "margin-right:27%;"
				+ "margin-left:15px;"
				+ "width:59%;"
				+ "float:left;"
				+ "border-right:1px solid rgb(226, 163, 163);"
				+ "}");
		html.echo("body{"
				+ "background:#ECECEC;"
				+ "}");
		html.echo("table td,th {"
				+ "padding:10px;"
				+ "text-align:center;"
				+ "}");
		html.echo("input,select {"
				+ "height:30px;"
				+ "}");
		html.echo("</style>");
	}
}