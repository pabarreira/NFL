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

public class PlayersVM {
	
	private Player currentPlayer = null;
	private boolean edit = false;

	public List<Player> getPlayers() {
		EntityManager em = DesktopEntityManagerManager.getDesktopEntityManager();
		return em.createQuery("select p from Player p", Player.class).getResultList();
		
		
	}
	
	public List<Team> getTeams() {
		EntityManager em = DesktopEntityManagerManager.getDesktopEntityManager();
		return em.createQuery("select d from Team d",Team.class).getResultList();
	}
	
	@Command
	@NotifyChange("players")
	public void delete(@BindingParam("player") Player e){
		EntityManager em = DesktopEntityManagerManager.getDesktopEntityManager();
		TransactionUtil.doTransaction(new Transaction() {
			@Override
			public void run(EntityManager em) {
				em.remove(e);
			}
		}, em);
		
	}
	
	@DependsOn("players")
	public int getCount(){
		
		return this.getPlayers().size();
	}
	
	public Player getCurrentPlayer(){
		return currentPlayer;
	}
	
	@Command
	@NotifyChange("currentPlayer")
	public void newPlayer(){
		this.currentPlayer = new Player();
		this.edit = false;
	}
	
	@Command
	@NotifyChange("currentPlayer")
	public void cancel(){
		this.currentPlayer = null;
	}
	
	@Command
	@NotifyChange({"currentPlayer","players"})
	public void save(){
		EntityManager em = DesktopEntityManagerManager.getDesktopEntityManager();
		TransactionUtil.doTransaction(new Transaction() {
			@Override
			public void run(EntityManager em) {
				if(!edit)
				{
					em.persist(currentPlayer);
				}
			}
		}, em);
		this.currentPlayer = null;
	}
	
	@Command
	@NotifyChange("currentPlayer")
	public void edit(@BindingParam("player") Player e){
		this.currentPlayer = e;
		this.edit = true;
			
	}
	
}