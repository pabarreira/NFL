package nflpredator.util;

import javax.persistence.EntityManager;

public interface Transaction {

	public void run(EntityManager em);
}
