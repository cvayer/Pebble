package com.apollo.managers;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class TeamManager extends Manager {
	private Map<Team, Set<Player>> teams;
	private Map<Player, Team> teamByPlayer;

	public TeamManager() {
		teams = new HashMap<Team, Set<Player>>();
		teamByPlayer = new HashMap<Player, Team>();
	}

	public void setPlayerToTeam(Player player, Team team) {
		teamByPlayer.put(player, team);

		Set<Player> players = teams.get(team);
		if (players == null) {
			players = new HashSet<Player>();
			teams.put(team, players);
		}
		players.add(player);
	}

	public Set<Player> getPlayersOfTeam(Team team) {
		return teams.get(team);
	}
	
	public Set<Player> getPlayersOnSameTeamAsPlayer(Player player) {
		return getPlayersOfTeam(teamByPlayer.get(player));
	}

	public Team getTeamOfPlayer(Player player) {
		return teamByPlayer.get(player);
	}
	
	public boolean areTeamMates(Player player1, Player player2) {
		return teamByPlayer.get(player1) == teamByPlayer.get(player2);
	}

}
