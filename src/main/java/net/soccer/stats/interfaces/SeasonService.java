package net.soccer.stats.interfaces;

import net.soccer.stats.pojo.FieldPosition;
import net.soccer.stats.pojo.League;
import net.soccer.stats.pojo.Player;
import net.soccer.stats.pojo.Team;

import java.util.List;

public interface SeasonService {

    boolean addTeam(Team team);

    boolean addPlayer(Player player);

    boolean createLeague(String leagueName, int numberOfTeams);

    boolean addTeamToLeague(int teamID, int leagueID);


    boolean addGoalsFor(int teamID, int leagueID);

    boolean addGoalsAgainst(int teamID, int leagueID);

    boolean addWin(int teamID, int leagueID);

    boolean addDraw(int teamID, int leagueID);

    boolean addLoss(int teamID, int leagueID);

    boolean addMatchesPlayed(int teamID, int leagueID);

    boolean addMatchResult(String textResult, int homeTeamID, int awayTeamID, int leagueID);


    boolean clearStatisticsForLeague(int leagueId);

    int getNumberOfMatchesPlayed(int teamID, int leagueID);

    int getNumberOfWins(int teamID, int leagueID);

    int getNumberOfLosses(int teamID, int leagueID);

    int getNumberOfDraws(int teamID, int leagueID);
    int getNumberOfGoalsFor(int teamID, int leagueID);
    int getNumberOfGoalsAgainst(int teamID, int leagueID);


    List<Team> getAllTeams();

    List<Team> getAllTeamsInLeague(int leagueId);

    List<League> getAllLeagues();

    List<FieldPosition> getAllFieldPositions();

    Player getPlayerById(int playerId);

    Team getTeamById(int teamId);

    League getLeagueById(int leagueId);

    boolean simulateSeason(int leagueId);
}
