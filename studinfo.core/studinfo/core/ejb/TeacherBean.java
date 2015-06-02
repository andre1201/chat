package studinfo.core.ejb;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import studinfo.core.entity.Teacher;

@Stateless
public class TeacherBean {
	@PersistenceContext
	private EntityManager th;// �������

	// �������
	public void save(Teacher teacher) {
		th.persist(teacher);
	}

	public List<Teacher> getAllTeachers() {// ������� ���� ���������
		return th.createQuery("SELECT T FROM Teacher T", Teacher.class)
				.getResultList();
	}
}
