package nflpredator.test;

import static org.junit.Assert.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import nflpredator.entities.Player;
import nflpredator.entities.Player.Player_pos;
import nflpredator.entities.Player.Player_status;
import nflpredator.util.Transaction;
import nflpredator.util.TransactionUtil;

public class PlayerTest {
	private static EntityManagerFactory emf;
	
	@BeforeClass
	public static void createEntityManagerFactory() {
		emf = Persistence.createEntityManagerFactory("si-database");
	}

	@AfterClass
	public static void closeEntityManagerFactory() {
		emf.close();
	}
	
	@Test
	public void testCreatePlayer() {
		EntityManager em = emf.createEntityManager();
		
		Player player = new Player();
		player.setFull_name("Juan");
		player.setPosition(Player_pos.CB);
//		player.setTeam("Eagles");
		player.setStatus(Player_status.Active);
		TransactionUtil.doTransaction(new Transaction() {
			@Override
			public void run(EntityManager em) {
				em.persist(player);
			}
		}, em);
		
		Player playerRecuperado = em.createQuery("SELECT p FROM Player p WHERE p.full_name = 'juan'", Player.class)
				.getSingleResult();
		assertEquals(Player_pos.CB, playerRecuperado.getPosition());
		
	}
	
	@Test
	public void testUpdatePlayer() {
		EntityManager em = emf.createEntityManager();
		Player player = new Player();

		player.setFull_name("Ruben");
		player.setPosition(Player_pos.C);
		player.setStatus(Player_status.Active);
		TransactionUtil.doTransaction(new Transaction() {
			@Override
			public void run(EntityManager em) {
				em.persist(player);
			}
		}, em);

		TransactionUtil.doTransaction(new Transaction() {
			@Override
			public void run(EntityManager em) {
				Player playerRecuperado = em
						.createQuery("SELECT e FROM Player e WHERE e.full_name = 'Ruben'", Player.class)
						.getSingleResult();
				assertEquals(Player_pos.C, playerRecuperado.getPosition());
				playerRecuperado.setFull_name("Pedro");
//				playerRecuperado.setPosition(Player_pos.CB);
			}
		}, em);

//		Player playerRecuperadoModificado = em
//				.createQuery("SELECT p FROM Player p WHERE p.full_name='Pedro'AND p.position='"+Player_pos.CB+"'", Player.class)
//				.getSingleResult();
		Player playerRecuperadoModificado = em
				.createQuery("SELECT p FROM Player p WHERE p.full_name='Pedro'", Player.class)
				.getSingleResult();
		assertFalse(playerRecuperadoModificado == null);
	}
}
