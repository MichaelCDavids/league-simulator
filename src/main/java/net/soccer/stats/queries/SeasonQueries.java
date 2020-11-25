package net.soccer.stats.queries;

import net.soccer.stats.pojo.*;
import net.soccer.stats.services.MatchDay;
import org.jdbi.v3.core.Jdbi;

import java.util.List;

public class SeasonQueries {

    Jdbi jdbi;

    public SeasonQueries(Jdbi jdbi) {
        this.jdbi = jdbi;
    }

    public boolean addTeam(Team team) {
        jdbi.withHandle(handle -> handle.execute("insert into team (team_name, number_of_players) values (?,?)", team.getTeamName(), team.getNumberOfPlayers()));
        return true;
    }

    public boolean addTeamToLeague(int teamID, int leagueID) {
        jdbi.withHandle(handle -> handle.execute("insert into team_league (team_id,league_id) values (?,?)", teamID, leagueID));
        return true;
    }

    public boolean addPlayer(Player player) {
        jdbi.useHandle(handle -> handle.execute("insert into player (first_name, last_name, field_position, team_id) values (?,?,?,?)",
                player.getFirstName(),
                player.getLastName(),
                player.getFieldPosition(),
                player.getTeamId()
        ));
        return true;
    }

    public boolean createLeague(String leagueName, int numberOfTeams) {
        jdbi.withHandle(handle -> handle.execute("insert into league (league_name, number_of_teams) values (?,?)", leagueName, numberOfTeams));
        return true;
    }

    public List<Team> getAllTeams() {
        List<Team> allTeams = jdbi.withHandle((h) -> h.createQuery("select team_id, team_name, number_of_players from team")
                .mapToBean(Team.class)
                .list()
        );
        return allTeams;
    }

    public List<Team> getAllTeamsInLeague(int leagueId) {
        List<Team> allTeams = jdbi.withHandle((h) -> h.createQuery("select team.team_id, team.team_name, team.number_of_players from team join team_league on team.team_id=team_league.team_id where team_league.league_id=" + leagueId)
                .mapToBean(Team.class)
                .list()
        );
        return allTeams;
    }

    public List<League> getAllLeagues() {
        List<League> allLeagues = jdbi.withHandle((h) -> h.createQuery("select * from league")
                .mapToBean(League.class)
                .list()
        );
        return allLeagues;
    }

    public Player getPlayerById(int playerId) {
        String query = "select * from player where id=" + playerId;
        return (Player) jdbi.withHandle((h) -> h.createQuery(query)
                .mapToBean(Team.class));
    }

    public List<FieldPosition> getAllFieldPositions() {
        List<FieldPosition> allPositions = jdbi.withHandle((h) -> h.createQuery("select * from field_position")
                .mapToBean(FieldPosition.class)
                .list()
        );
        return allPositions;
    }

    public boolean addMatchResult(String resultText, int homeTeamID, int awayTeamID, int leagueID) {
        jdbi.useHandle(handle -> handle.execute("insert into match_result (result, home_team_id, away_team_id, league_id) values (?,?,?,?)",
                resultText,
                homeTeamID,
                awayTeamID,
                leagueID
        ));
        return true;
    }

    public boolean addMatchesPlayed(int teamID, int leagueID) {
        jdbi.useHandle(handle -> handle.execute("insert into matches_played (team_id, league_id) values (?,?)",
                teamID,
                leagueID
        ));
        return true;
    }

    public boolean addLoss(int teamID, int leagueID) {
        jdbi.useHandle(handle -> handle.execute("insert into losses (team_id, league_id) values (?,?)",
                teamID,
                leagueID
        ));
        return true;

    }

    public boolean addDraw(int teamID, int leagueID) {
        jdbi.useHandle(handle -> handle.execute("insert into draws (team_id, league_id) values (?,?)",
                teamID,
                leagueID
        ));
        return true;
    }

    public boolean addWin(int teamID, int leagueID) {
        jdbi.useHandle(handle -> handle.execute("insert into wins (team_id, league_id) values (?,?)",
                teamID,
                leagueID
        ));
        return true;
    }

    public boolean addGoalsAgainst(int teamID, int leagueID) {
        jdbi.useHandle(handle -> handle.execute("insert into goals_against (team_id, league_id) values (?,?)",
                teamID,
                leagueID
        ));
        return true;
    }

    public boolean addGoalsFor(int teamID, int leagueID) {
        jdbi.useHandle(handle -> handle.execute("insert into goals_for (team_id, league_id) values (?,?)",
                teamID,
                leagueID
        ));
        return true;
    }

    public boolean simulateSeason(int leagueId) {
        List<Team> allTeams = getAllTeamsInLeague(leagueId);
        for (int i = 0; i < allTeams.size(); i++) {
            Team homeTeam = allTeams.get(i);
            for (int j = 0; j < allTeams.size(); j++) {
                if (i == j) {
                    continue;
                }
                Team awayTeam = allTeams.get(j);
                MatchDay matchDay = new MatchDay(homeTeam, awayTeam, leagueId);
                String result = matchDay.simulate();
                int[] scores = MatchDay.getIntegerResults(result.split("-"));
                for (int k = 0; k < scores.length; k++) {
                    if (k == 0) {
                        for (int l = 0; l < scores[0]; l++) {
                            addGoalsFor(homeTeam.getTeamId(), leagueId);
                        }
                        for (int l = 0; l < scores[1]; l++) {
                            addGoalsAgainst(homeTeam.getTeamId(), leagueId);
                        }
                    }
                    if (k == 1) {
                        for (int l = 0; l < scores[1]; l++) {
                            addGoalsFor(awayTeam.getTeamId(), leagueId);
                        }
                        for (int l = 0; l < scores[0]; l++) {
                            addGoalsAgainst(awayTeam.getTeamId(), leagueId);
                        }
                    }
                }
                if (scores[0] > scores[1]) {
                    addWin(homeTeam.getTeamId(), leagueId);
                    addLoss(awayTeam.getTeamId(), leagueId);
                } else if (scores[1] > scores[0]) {
                    addWin(awayTeam.getTeamId(), leagueId);
                    addLoss(homeTeam.getTeamId(), leagueId);
                } else {
                    addDraw(homeTeam.getTeamId(), leagueId);
                    addDraw(awayTeam.getTeamId(), leagueId);
                }
                addMatchesPlayed(homeTeam.getTeamId(), leagueId);
                addMatchesPlayed(awayTeam.getTeamId(), leagueId);
                addMatchResult(result, homeTeam.getTeamId(), awayTeam.getTeamId(), leagueId);
            }
        }
        System.out.println("Done... \n you can now view the league standings");
        return true;
    }

    public Team getTeamById(int teamId) {
        List<Team> teams = jdbi.withHandle((h) -> h.createQuery("select team_id, team_name, number_of_players from team where team_id=" + teamId)
                .mapToBean(Team.class).list()
        );
        return teams.get(0);
    }

    public League getLeagueById(int leagueId) {
        List<League> leagues = jdbi.open().createQuery("select league_id, league_name, number_of_teams from league where league_id=" + leagueId).mapToBean(League.class).list();
        return leagues.get(0);
    }

    public int getNumberOfMatchesPlayed(int teamID, int leagueID) {
        List<MatchCounter> matchCounters = jdbi.withHandle(handle -> handle.createQuery("select count(*) from matches_played where (team_id=? and league_id=?)")
                .bind(0, teamID)
                .bind(1, leagueID)
                .mapToBean(MatchCounter.class)
                .list()
        );
        return matchCounters.get(0).getCount();
    }

    public int getNumberOfWins(int teamID, int leagueID) {
        List<WinCounter> winCounters = jdbi.withHandle(handle -> handle.createQuery("select count(*) from wins where (team_id=? and league_id=?)")
                .bind(0, teamID)
                .bind(1, leagueID)
                .mapToBean(WinCounter.class)
                .list()
        );
        return winCounters.get(0).getCount();
    }

    public int getNumberOfLosses(int teamID, int leagueID) {
        List<LossCounter> lossCounters = jdbi.withHandle(handle -> handle.createQuery("select count(*) from losses where (team_id=? and league_id=?)")
                .bind(0, teamID)
                .bind(1, leagueID)
                .mapToBean(LossCounter.class)
                .list()
        );
        return lossCounters.get(0).getCount();
    }

    public int getNumberOfDraws(int teamID, int leagueID) {
        List<DrawCounter> drawCounters = jdbi.withHandle(handle -> handle.createQuery("select count(*) from losses where (team_id=? and league_id=?)")
                .bind(0, teamID)
                .bind(1, leagueID)
                .mapToBean(DrawCounter.class)
                .list()
        );
        return drawCounters.get(0).getCount();
    }

    public int getNumberOfGoalsFor(int teamID, int leagueID) {
        List<GoalsForCounter> goalsForCounters = jdbi.withHandle(handle -> handle.createQuery("select count(*) from goals_for where (team_id=? and league_id=?)")
                .bind(0, teamID)
                .bind(1, leagueID)
                .mapToBean(GoalsForCounter.class)
                .list()
        );
        return goalsForCounters.get(0).getCount();
    }

    public int getNumberOfGoalsAgainst(int teamID, int leagueID) {
        List<GoalsAgainstCounter> goalsAgainstCounters = jdbi.withHandle(handle -> handle.createQuery("select count(*) from goals_against where (team_id=? and league_id=?)")
                .bind(0, teamID)
                .bind(1, leagueID)
                .mapToBean(GoalsAgainstCounter.class)
                .list()
        );
        return goalsAgainstCounters.get(0).getCount();
    }

    public boolean clearStatisticsForLeague(int leagueId) {


        jdbi.useHandle(handle -> handle.execute("delete from wins where league_id=?",
                leagueId
        ));
        jdbi.useHandle(handle -> handle.execute("delete from draws where league_id=?",
                leagueId
        ));
        jdbi.useHandle(handle -> handle.execute("delete from losses where league_id=?",
                leagueId
        ));
        jdbi.useHandle(handle -> handle.execute("delete from matches_played where league_id=?",
                leagueId
        ));
        jdbi.useHandle(handle -> handle.execute("delete from goals_for where league_id=?",
                leagueId
        ));
        jdbi.useHandle(handle -> handle.execute("delete from goals_against where league_id=?",
                leagueId
        ));
        jdbi.useHandle(handle -> handle.execute("delete from match_result where league_id=?",
                leagueId
        ));


        return true;
    }
}

