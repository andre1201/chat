package studinfo.core.ejb;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import studinfo.core.ejb.StudentGroupBean;
import studinfo.core.entity.Grade;
import studinfo.core.entity.Student;
import studinfo.core.entity.Group;



@Stateless
public class StudentGroupBean {
	@PersistenceContext
	private EntityManager em;//
	
	//----------------------------------------------
	public void save(Student student) {//Добавление студентов
		em.persist(student);
	}

	public List<Student> getAllStudents() {//вернуть всех студентов
		return em.createQuery("SELECT S FROM Student S", Student.class)
				.getResultList();
	}
	
	public List<Grade> getAllGrade(Student student) {//Оценки
		return em.createQuery("SELECT G FROM Grade G WHERE G.student=:student", Grade.class)
				.setParameter("student", student)
				.getResultList();
	}

	public void save(Group group) {
		em.persist(group);
	}

	public Group getGroup(Long id) {
		return em.find(Group.class, id);
	}

	public List<Group> getAllGroups() {
		return em.createQuery("SELECT G FROM Group G", Group.class)
				.getResultList();
	}
	
	public void setStudentsInGroup(String s) {
		Group group = new Group();
		group.setGroupName(s);
		
	}
	//Поиск группы по id студента
	public Group getGroupByStudent(Long studentId) {
		return em
				.createQuery("SELECT S.group FROM Student S WHERE S.id=:id",
						Group.class).setParameter("id", studentId)
				.getSingleResult();
	}
	//Студенты в группе
	public List<Student> getStudentsInGroup(Group group) {
		return em
				.createQuery("SELECT S FROM Student S WHERE S.group=:group",
						Student.class).setParameter("group", group)
				.getResultList();
	}
	
	
}
