package studinfo.core.ejb;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import studinfo.core.entity.Period;

@Stateless
public class PeriodBean {
	@PersistenceContext
	private EntityManager em;

	public void save(Period period) {
		em.persist(period);
	}
	//Достать весь периуд
	public List<Period> getAllPeriods() {
		return em.createQuery("SELECT P FROM Period P", Period.class)
				.getResultList();
	}
}