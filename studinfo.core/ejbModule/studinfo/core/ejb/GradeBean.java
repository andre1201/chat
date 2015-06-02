package studinfo.core.ejb;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;

import studinfo.core.entity.Grade;
import studinfo.core.entity.Period;
import studinfo.core.entity.Student;
import studinfo.core.entity.Subject;
import studinfo.core.entity.Teacher;

@Stateless
public class GradeBean {
	@PersistenceContext
	private EntityManager em;

	public Grade createGrade(Long studentId, Long teacherId, Long subjectId,
			Long periodId, Integer gradeLevel) {
		Grade grade = new Grade();
		try {
			grade.setGradeLevel(gradeLevel);
			grade.setStudent(em.find(Student.class, studentId));
			grade.setTeacher(em.find(Teacher.class, teacherId));
			grade.setSubject(em.find(Subject.class, subjectId));
			grade.setPeriod(em.find(Period.class, periodId));
		} catch (PersistenceException e) {
			e.printStackTrace();
			return null;
		}
		em.persist(grade);
		return grade;
	}

	public void updateGradeLevel(Long gradeId, Integer gradeLevel) {
		try {
			Grade grade = em.find(Grade.class, gradeId);
			grade.setGradeLevel(gradeLevel);
			em.merge(grade);
		} catch (PersistenceException e) {
			e.printStackTrace();
		}
	}

	public List<Grade> getGradeByGroup(Long groupId) {
		return em
				.createQuery(
						"SELECT G FROM Grade G WHERE G.student.group.id=:id ORDER BY G.period.endDate,G.student.id",
						Grade.class).setParameter("id", groupId)
				.getResultList();
	}
	
	public List<Subject> getSubject() {
		return em
				.createQuery(
						"SELECT S FROM Subject S ",
						Subject.class)
				.getResultList();
	}

	public List<Grade> getGradeByGroupAndPeriod(Long groupId, Long periodId) {
		return em
				.createQuery(
						"SELECT G FROM Grade G WHERE G.student.group.id=:groupId AND G.period.id=:periodId ORDER BY G.student.id",
						Grade.class).setParameter("groupId", groupId)
				.setParameter("periodId", periodId).getResultList();
	}
}