package nflInterface;

import java.util.List;

import javax.persistence.EntityManager;

import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.DependsOn;
import org.zkoss.bind.annotation.NotifyChange;

import nflpredator.entities.Player;
import nflpredator.entities.Team;
import nflpredator.util.Transaction;
import nflpredator.util.TransactionUtil;
import nflInterface.jpa.DesktopEntityManagerManager;

public class TeamsVM {
	
	private Team currentTeam = null;
	private boolean edit = false;

	public List<Team> getTeams() {
		EntityManager em = DesktopEntityManagerManager.getDesktopEntityManager();
		return em.createQuery("SELECT t FROM Team t",Team.class).getResultList();		
	}
	
	@Command
	@NotifyChange("teams")
	public void delete(@BindingParam("team")Team t){
		EntityManager em = DesktopEntityManagerManager.getDesktopEntityManager();
		TransactionUtil.doTransaction(new Transaction() {
			@Override
			public void run(EntityManager em) {
				List<Player> players = em.createQuery("SELECT t FROM Team t WHERE t.team.name='"+t.getName()+"'", Player.class).getResultList();
				for (Player player : players) {
					player.setTeam(null);
				}
				em.remove(t);
			}
		}, em);
		
	}
	
	@DependsOn("teams")
	public int getCount(){
		
		return this.getTeams().size();
	}
	
	public Team getCurrentTeam(){
		return currentTeam;
	}
	
	@Command
	@NotifyChange("currentTeam")
	public void newTeam(){
		this.currentTeam = new Team();
		this.edit = false;
	}
	
	@Command
	@NotifyChange("currentTeam")
	public void cancel(){
		this.currentTeam = null;
	}
	
	@Command
	@NotifyChange({"currentTeam","teams"})
	public void save(){
		EntityManager em = DesktopEntityManagerManager.getDesktopEntityManager();
		TransactionUtil.doTransaction(new Transaction() {
			@Override
			public void run(EntityManager em) {
				if(!edit)
				{
					em.persist(currentTeam);
				}
			}
		}, em);
		this.currentTeam = null;
	}
	
	@Command
	@NotifyChange("currentTeam")
	public void edit(@BindingParam("team")Team d){
		this.currentTeam = d;
		this.edit = true;
			
	}
	
}