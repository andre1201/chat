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
		html.html().title("Работа со студентами")//Название титульной страницы
				.a("index?method=student", "Студенты")//Переход на Студенты
				.a("index?method=group", "Группы")//Переход на Группы
				.a("index?method=teacher", "Преподаватели")//Переход на Преподаватели
				.a("index?method=subject", "Предметы")//Переход на Предметы
				.a("index?method=period", "Периоды")//Переход на Периоды
				.a("index?method=grade", "Оценки")
				.a("index?method=getList", "Ведомость");//Переход на ведомость
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
			html.echo("<h3>Выберите меню</h3>");
		}
		html.echo("</div>");
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html; charset=utf-8");
		response.getWriter().println(html.done());
	}

	protected void doPost(HttpServletRequest request,
		HttpServletResponse response) throws ServletException, IOException {
		String method = request.getParameter("method");
		
		//Добавление студента
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
			//Добавление группы
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
			//Добавление предмет
			Subject subject = new Subject();
			subject.setSubjectDescription(request
					.getParameter("subjectDescription"));
			subject.setSubjectName(request.getParameter("subjectName"));
			teachersBean.save(subject);
			response.sendRedirect("index?method=subject");
			//Добавление периуда
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
			//Добавление оценки
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
	// генерируем страницу ПРЕДМЕТЫ
	//======================================================================
	void viewSubject(){
		html.echo("<h3>Предметы</h3>");
		HTMLTable table = html.table().tr().th("Предмет").th("Описание");
		for (Subject s : teachersBean.getAllSubjects()) {
			table.tr().td(s.getSubjectName()).td(s.getSubjectDescription());
		}
		table.done();
		html.br().br();
		html.echo("<h3>Добавить предмет</h3>");
		html.form("index", "POST")
				.input(HTMLFormInputType.HIDDEN, "method", "newSubject")
				.echo("<table><tr><td>Предмет<td>")
				.input(HTMLFormInputType.TEXT, "subjectName", "")
				.echo("<tr><td>Описание<td>")
				.input(HTMLFormInputType.TEXT, "subjectDescription", "")
				.echo("<tr><td colspan=2>")
				.input(HTMLFormInputType.SUBMIT, "s", "Добавить предмет")
				.echo("</table>").done();
	}
			//======================================================================
			// генерируем страницу СТУДЕНТ
			//======================================================================
	void viewStudent(){
		html.echo("<h3>Студенты</h3>");
		HTMLTable table = html.table().tr().th("Фамилия Имя Отчество")
				.th("Дата рождения").th("Группа");
		for (Student s : studentsBean.getAllStudents()) {
			table.tr()
					.td(s.getLastName() + " " + s.getFirstName() + " "
							+ s.getMiddleName())
					.td(format.format(s.getBirthDate()))
					.td(s.getGroup().getGroupName());
		}
		table.done();
		html.br().br();
		html.echo("<h3>Добавить студента</h3>");
		Map<String, Object> groupMap = new HashMap<>();
		for (Group g : studentsBean.getAllGroups()) {
			groupMap.put(g.getGroupName(), g.getId());
		}
		
		html.form("index", "POST")
				.input(HTMLFormInputType.HIDDEN, "method", "newStudent")
				//Метод = newStudent
				.echo("<table><tr><td>Фамилия<td>")
				//Поле для фамилии
				.input(HTMLFormInputType.TEXT, "lastName", "")
				.echo("<tr><td>Имя<td>")
				//Поле для Имя
				.input(HTMLFormInputType.TEXT, "firstName", "")
				.echo("<tr><td>Отчество<td>")
				//Поле для Отчество
				.input(HTMLFormInputType.TEXT, "middleName", "")
				.echo("<tr><td>Дата рождения<td>")
				.input(HTMLFormInputType.TEXT, "dateOfBirth", "1980-12-28")
				.echo("<tr><td>Группа<td>").select("groupId", groupMap)
				.echo("<tr><td colspan=2>")
				.input(HTMLFormInputType.SUBMIT, "s", "Добавить студента")
				.echo("</table>").done();
	}
	
	//======================================================================
	// генерируем страницу ГРУППА
	//======================================================================	
	void viewGroup(){
		html.echo("<h3>Группы</h3>");
		HTMLTable table = html.table().tr().th("Курс")
				.th("Название группы");
		for (Group g : studentsBean.getAllGroups()) {
			table.tr().td("" + g.getGroupLevel()).td(g.getGroupName());
		}
		table.done();

		html.br().br();
		html.echo("<h3>Добавить группу</h3>");
		html.form("index", "POST")
				.input(HTMLFormInputType.HIDDEN, "method", "newGroup")
				.echo("<table><tr><td>Курс<td>")
				.input(HTMLFormInputType.TEXT, "groupLevel", "")
				.echo("<tr><td>Название группы<td>")
				.input(HTMLFormInputType.TEXT, "groupName", "")
				.echo("<tr><td colspan=2>")
				.input(HTMLFormInputType.SUBMIT, "s", "Добавить группу")
				.echo("</table>").done();
	}
	
	//======================================================================
	// генерируем страницу УЧИТЕЛЬ
	//======================================================================
	void viewTeacher(){
		html.echo("<h3>Преподаватели</h3>");
		HTMLTable table = html.table().tr().th("Фамилия Имя Отчество")
				.th("Дата рождения");
		for (Teacher s : teachersBean.getAllTeachers()) {
			table.tr()
					.td(s.getLastName() + " " + s.getFirstName() + " "
							+ s.getMiddleName())
					.td(format.format(s.getBirthDate()));
		}
		table.done();
		html.br().br();
		html.echo("<h3>Добавить преподавателя</h3>");
		html.form("index", "POST")
				.input(HTMLFormInputType.HIDDEN, "method", "newTeacher")
				.echo("<table><tr><td>Фамилия<td>")
				.input(HTMLFormInputType.TEXT, "lastName", "")
				.echo("<tr><td>Имя<td>")
				.input(HTMLFormInputType.TEXT, "firstName", "")
				.echo("<tr><td>Отчество<td>")
				.input(HTMLFormInputType.TEXT, "middleName", "")
				.echo("<tr><td>Дата рождения<td>")
				.input(HTMLFormInputType.TEXT, "dateOfBirth", "1980-12-28")
				.echo("<tr><td colspan=2>")
				.input(HTMLFormInputType.SUBMIT, "s",
						"Добавить преподавателя").echo("</table>").done();
	}
	
	void viewPeriod(){
		html.echo("<h3>Периоды</h3>");
		HTMLTable table = html.table().tr().th("Название").th("Начало")
				.th("Окончание");
		for (Period p : periodBean.getAllPeriods()) {
			table.tr().td(p.getPeriodName())
					.td(format.format(p.getStartDate()))
					.td(format.format(p.getEndDate()));
		}
		table.done();
		html.br().br();
		html.echo("<h3>Добавить период</h3>");
		html.form("index", "POST")
				.input(HTMLFormInputType.HIDDEN, "method", "newPeriod")
				.echo("<table><tr><td>Название<td>")
				.input(HTMLFormInputType.TEXT, "periodName", "")
				.echo("<tr><td>Дата начала<td>")
				.input(HTMLFormInputType.TEXT, "periodStart", "2015-01-01")
				.echo("<tr><td>Дата окончания<td>")
				.input(HTMLFormInputType.TEXT, "periodEnd", "2015-01-28")
				.echo("<tr><td colspan=2>")
				.input(HTMLFormInputType.SUBMIT, "s", "Добавить период")
				.echo("</table>").done();
	}
	//======================================================================
	// генерируем страницу ведомость
	//======================================================================	
	void viewVedomost(HttpServletRequest request){
		
		html.echo("<h3>Сводная ведомость</h3>");
		Map<String, Object> groupMap = new LinkedHashMap<>();
		for (Group g : studentsBean.getAllGroups()) {
			groupMap.put(g.getGroupName(), g.getId());
		}
		html.form("index", "GET").
		input(HTMLFormInputType.HIDDEN, "method", "getList").
		echo("Выберите группу ").
		select("group", groupMap).
		input(HTMLFormInputType.SUBMIT, "view", "Показать").done();
		String groupId = request.getParameter("group");
		String view = request.getParameter("view");
		
		if ( groupId == null && view ==null) {
			//html.echo("<b>Выберите группу </b>");
		} else{
			Group g = studentsBean.getGroup(new Long(groupId));
			html.echo("Сводная ведомость по группе "+g.getGroupName()).br();
			//Заголовки таблицы

				HTMLTable table = html.table().tr().th("№").th("ФИО");
				for(Subject subj : gradeBean.getSubject()){
					table.th(subj.getSubjectName());
				}
			//Тело таблицы
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
				table.tr().th("Записей нет!");
			}	
			//Футер таблицы
			table.done();
			
		}
		
	}
	//======================================================================
	// генерируем страницу ОЦЕНКИ
	//======================================================================
	void viewGrade(HttpServletRequest request){

		html.echo("<h3>Оценки и отчеты</h3>");
		Map<String, Object> groupMap = new LinkedHashMap<>();
		for (Group g : studentsBean.getAllGroups()) {
			groupMap.put(g.getGroupName(), g.getId());
		}
		Map<String, Object> periodMap = new LinkedHashMap<>();
		periodMap.put("Все", "all");
		for (Period p : periodBean.getAllPeriods()) {
			periodMap.put(p.getPeriodName(), p.getId());
		}
		html.form("index", "GET")
				.input(HTMLFormInputType.HIDDEN, "method", "grade")
				.echo("<table><tr><td>Группа: <td>")
				.select("group", groupMap).echo("<td>Период: <td>")
				.select("period", periodMap).echo("<td>")
				.input(HTMLFormInputType.SUBMIT, "s", "Показать")
				.echo("</table>").done();
		// zzz
		html.br().br();
		String period = request.getParameter("period");
		String group = request.getParameter("group");
		if (period == null && group == null) {
			html.echo("<b>Выберите группу и период</b>");
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
			HTMLTable table = html.table().tr().th("Период")
					.th("ФИО Студента").th("ФИО Преподавателя")
					.th("Предмет").th("Оценка").th("Дата");
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
		html.echo("<h3>Добавить оценку</h3>");
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
		periodMap.remove("Все");
		Map<String, Object> gradeMap = new LinkedHashMap<String, Object>();
		gradeMap.put("Отлично", new Integer(5));
		gradeMap.put("Хорошо", new Integer(4));
		gradeMap.put("Удволетворительно", new Integer(3));
		gradeMap.put("Неудволитворительно", new Integer(2));
		gradeMap.put("Плохо", new Integer(1));
		html.form("index", "POST")
				.input(HTMLFormInputType.HIDDEN, "method", "newGrade")
				.echo("<table><tr><td>Предмет<td>")
				.select("subject", subjectMap)
				.echo("<td>Преподаватель<td>")
				.select("teacher", teacherMap).echo("<tr><td>Период<td>")
				.select("period", periodMap).echo("<td>Студент<td>")
				.select("student", studentsMap).echo("<tr><td>Оценка<td>")
				.select("grade", gradeMap).echo("<td colspan=2>")
				.input(HTMLFormInputType.SUBMIT, "s", "Добавить оценку")
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