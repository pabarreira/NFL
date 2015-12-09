package nflpredator.entities;

import java.util.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Team {
	@Id
	@GeneratedValue
	private int team_id;
	private String city;
	private String name;
	@OneToMany(mappedBy="team")
	private List<Player> players = new ArrayList<Player>();
	
	public int getTeam_id() {
		return team_id;
	}

	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public List<Player> getPlayers() {
		return players;
	}

	public void setPlayers(List<Player> players) {
		this.players = players;
	}

	void internalAddPlayer(Player player) {
		players.add(player);
	}
	
	void internalRemovePlayer(Player player) {
		players.remove(player);
	}
}
