package net.soccer.stats.services;

import net.soccer.stats.interfaces.SeasonService;
import net.soccer.stats.pojo.League;
import net.soccer.stats.queries.SeasonQueries;
import net.soccer.stats.pojo.FieldPosition;
import net.soccer.stats.pojo.Player;
import net.soccer.stats.pojo.Team;

import java.util.List;

public class LeagueService implements SeasonService{

    SeasonQueries seasonQueries;

    public LeagueService(SeasonQueries seasonQueries) {
        this.seasonQueries = seasonQueries;
    }

    @Override
    public boolean addTeam(Team team) {
        return this.seasonQueries.addTeam(team);
    }

    @Override
    public boolean createLeague(String leagueName, int numberOfTeams) {
        return this.seasonQueries.createLeague(leagueName,numberOfTeams);
    }

    @Override
    public boolean addTeamToLeague(int teamID, int leagueID) {
        return this.seasonQueries.addTeamToLeague(teamID,leagueID);
    }

    @Override
    public boolean addGoalsFor(int teamID, int leagueID) {
        return this.seasonQueries.addGoalsFor(teamID,leagueID);
    }

    @Override
    public boolean addGoalsAgainst(int teamID, int leagueID) {
        return this.seasonQueries.addGoalsAgainst(teamID,leagueID);
    }

    @Override
    public boolean addWin(int teamID, int leagueID) {
        return this.seasonQueries.addWin(teamID,leagueID);
    }

    @Override
    public boolean addDraw(int teamID, int leagueID) {
        return this.seasonQueries.addDraw(teamID,leagueID);
    }

    @Override
    public boolean addLoss(int teamID, int leagueID) {
        return this.seasonQueries.addLoss(teamID,leagueID);
    }

    @Override
    public boolean addMatchesPlayed(int teamID, int leagueID) {
        return this.seasonQueries.addMatchesPlayed(teamID,leagueID);
    }

    @Override
    public boolean addMatchResult(String resultText, int homeTeamID, int awayTeamID, int leagueID) {
        return this.seasonQueries.addMatchResult(resultText,homeTeamID,awayTeamID,leagueID);
    }

    @Override
    public boolean clearStatisticsForLeague(int leagueId) {
        return this.seasonQueries.clearStatisticsForLeague(leagueId);
    }

    @Override
    public int getNumberOfMatchesPlayed(int teamID, int leagueID) {
        return this.seasonQueries.getNumberOfMatchesPlayed(teamID, leagueID);
    }

    @Override
    public int getNumberOfWins(int teamID, int leagueID) {
        return this.seasonQueries.getNumberOfWins(teamID, leagueID);
    }

    @Override
    public int getNumberOfLosses(int teamID, int leagueID) {
        return this.seasonQueries.getNumberOfLosses(teamID,leagueID);
    }

    @Override
    public int getNumberOfDraws(int teamID, int leagueID) {
        return this.seasonQueries.getNumberOfDraws(teamID,leagueID);
    }

    @Override
    public int getNumberOfGoalsFor(int teamID, int leagueID) {
        return this.seasonQueries.getNumberOfGoalsFor(teamID,leagueID);
    }

    @Override
    public int getNumberOfGoalsAgainst(int teamID, int leagueID) {
        return this.seasonQueries.getNumberOfGoalsAgainst(teamID,leagueID);
    }


    @Override
    public boolean addPlayer(Player player) {
        return this.seasonQueries.addPlayer(player);
    }

    @Override
    public Player getPlayerById(int playerId) {
        return this.seasonQueries.getPlayerById(playerId);
    }

    @Override
    public Team getTeamById(int teamId) {
        return this.seasonQueries.getTeamById(teamId);
    }

    @Override
    public League getLeagueById(int leagueId) {
        return this.seasonQueries.getLeagueById(leagueId);
    }

    @Override
    public boolean simulateSeason(int leagueId) {
        return this.seasonQueries.simulateSeason(leagueId);
    }

    @Override
    public List<Team> getAllTeams() {
        return this.seasonQueries.getAllTeams();
    }

    @Override
    public List<Team> getAllTeamsInLeague(int leagueId) {
        return this.seasonQueries.getAllTeamsInLeague(leagueId);
    }

    @Override
    public List<League> getAllLeagues() {
        return this.seasonQueries.getAllLeagues();
    }

    @Override
    public List<FieldPosition> getAllFieldPositions() {
        return this.seasonQueries.getAllFieldPositions();
    }

}
