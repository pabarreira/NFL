package nflpredator.test;

import static org.junit.Assert.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import nflpredator.entities.Player;
import nflpredator.entities.Team;
import nflpredator.entities.Player.Player_pos;
import nflpredator.entities.Player.Player_status;
import nflpredator.util.Transaction;
import nflpredator.util.TransactionUtil;

public class TeamTest {
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
	public void testCreateTeam() {

		EntityManager em = emf.createEntityManager();

		Team team = new Team();
		team.setName("Eagles");
		team.setCity("Philadelphia");
		TransactionUtil.doTransaction(new Transaction() {
			@Override
			public void run(EntityManager em) {
				em.persist(team);
			}
		}, em);

		Team teamRecuperado = em.createQuery("SELECT t FROM Team t WHERE t.name = 'Eagles'", Team.class)
				.getSingleResult();
		assertEquals("Eagles", teamRecuperado.getName());
	}
	
	@Test
	public void createTeamWithPlayers()
	{
		EntityManager em = emf.createEntityManager();
		
		Team team = new Team();
		team.setName("Giants");
		team.setCity("New York");
		TransactionUtil.doTransaction(new Transaction() {
			@Override
			public void run(EntityManager em) {
				em.persist(team);
			}
		}, em);

		Team teamRecuperado = em.createQuery("SELECT t FROM Team t WHERE t.name = 'Giants'", Team.class)
				.getSingleResult();
		assertEquals("Giants", teamRecuperado.getName());
		
		Player player = new Player();
		player.setFull_name("Diego");
		player.setPosition(Player_pos.FS);
		player.setTeam(teamRecuperado);
		player.setStatus(Player_status.PUP);
		TransactionUtil.doTransaction(new Transaction() {
			@Override
			public void run(EntityManager em) {
				em.persist(player);
			}
		}, em);
		
		Player playerRecuperado = em.createQuery("SELECT p FROM Player p WHERE p.full_name = 'Diego'", Player.class)
				.getSingleResult();
		assertEquals(Player_pos.FS, playerRecuperado.getPosition());
	}
	
	@Test
	public void getPlayers(){
		EntityManager em = emf.createEntityManager();
		
		Team team = new Team();
		team.setName("Cowboys");
		team.setCity("Dallas");
		TransactionUtil.doTransaction(new Transaction() {
			@Override
			public void run(EntityManager em) {
				em.persist(team);
			}
		}, em);

		Team teamRecuperado = em.createQuery("SELECT t FROM Team t WHERE t.name = 'Cowboys'", Team.class)
				.getSingleResult();
		assertEquals("Cowboys", teamRecuperado.getName());
		
		Player player = new Player();
		player.setFull_name("Toni");
		player.setPosition(Player_pos.SAF);
		player.setTeam(teamRecuperado);
		player.setStatus(Player_status.Exempt);
		TransactionUtil.doTransaction(new Transaction() {
			@Override
			public void run(EntityManager em) {
				em.persist(player);
			}
		}, em);
		
		Team teamToni = em.createQuery("SELECT t FROM Team t WHERE t.name = 'Cowboys'", Team.class)
				.getSingleResult();
		assertEquals("Cowboys", teamToni.getName());
		
		System.out.println(teamToni + " " + teamRecuperado);
		
		assertEquals(1,teamToni.getPlayers().size());
	}
}
