package studinfo.core.ejb;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import studinfo.core.entity.Subject;
import studinfo.core.entity.Teacher;

/**
 * Session Bean implementation class TeacherSubjectBean
 */
@Stateless
public class TeacherSubjectBean {
	@PersistenceContext
	private EntityManager em;

	public void save(Teacher teacher) {
		em.persist(teacher);
	}

	public void save(Subject subject) {
		em.persist(subject);
	}

	public List<Teacher> getAllTeachers() {
		return em.createQuery("SELECT T From Teacher T", Teacher.class)
				.getResultList();
	}

	public List<Subject> getAllSubjects() {
		return em.createQuery("SELECT S From Subject S", Subject.class)
				.getResultList();
	}
}