package studinfo.core.ejb;


import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import studinfo.core.entity.Group;
@Stateless
public class GroupsBean {
	@PersistenceContext
	private EntityManager th;//�������
	//�������
	public void save(Group group) {
		th.persist(group);//���������� ������
	}
	
	public List<Group> getAllGroup() {//��� ������
		return th.createQuery("SELECT T FROM Group T", Group.class)
				.getResultList();
	}
	
	
}
