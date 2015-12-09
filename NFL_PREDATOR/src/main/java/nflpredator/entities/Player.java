package nflpredator.entities;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Player {
	@Id
	@GeneratedValue
	private int player_id;
	private String full_name;
	@ManyToOne
	private Team team;
	private Player_pos position;
	private Player_status status;
	
	public static enum Player_pos {
		C, FB, RB, DT, OG, T, DB, DE, CB, FS, SAF, LB, TE, SS, OT, QB, NT, UNK, G, MLB, WR, ILB, K, LS, P, OLB, OL
	}
	
	public static enum Player_status {
		InjuredReserve, Active, Unknown, PUP, Exempt
	}
	
	public int getPlayer_id() {
		return player_id;
	}
	
	public String getFull_name() {
		return full_name;
	}
	
	public void setFull_name(String full_name) {
		this.full_name = full_name;
	}
	
	public Team getTeam() {
		return team;
	}
	
	public void setTeam(Team team) {
		this.team = team;
		if(this.team!=null){
			this.team.internalRemovePlayer(this);
		}
		this.team = team;
		if(team!=null){
			team.internalAddPlayer(this);
		}
	}
	
	@Enumerated(EnumType.STRING)
	public Player_pos getPosition() {
		return position;
	}
	
	public void setPosition(Player_pos position) {
		this.position = position;
	}
	
	@Enumerated(EnumType.STRING)
	public Player_status getStatus() {
		return status;
	}
	
	public void setStatus(Player_status status) {
		this.status = status;
	}	
	
}
