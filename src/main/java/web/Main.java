package web;

import net.soccer.stats.interfaces.SeasonService;
import net.soccer.stats.pojo.FieldPosition;
import net.soccer.stats.pojo.League;
import net.soccer.stats.pojo.Player;
import net.soccer.stats.pojo.Team;
import net.soccer.stats.queries.SeasonQueries;
import net.soccer.stats.services.LeagueService;
import org.jdbi.v3.core.Jdbi;
import spark.ModelAndView;
import spark.template.handlebars.HandlebarsTemplateEngine;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.*;

import static java.lang.Integer.parseInt;
import static spark.Spark.*;

public class Main {

    static int getHerokuAssignedPort() {
        ProcessBuilder processBuilder = new ProcessBuilder();
        if (processBuilder.environment().get("PORT") != null) {
            return parseInt(processBuilder.environment().get("PORT"));
        }
        return 4569;
    }

    static Jdbi getJdbiDatabaseConnection(String defaultJdbcUrl) throws URISyntaxException, SQLException {
        ProcessBuilder processBuilder = new ProcessBuilder();
        String database_url = processBuilder.environment().get("DATABASE_URL");
        if (database_url != null) {
            URI uri = new URI(database_url);
            String[] hostParts = uri.getUserInfo().split(":");
            String username = hostParts[0];
            String password = hostParts[1];
            String host = uri.getHost();
            int port = uri.getPort();
            String path = uri.getPath();
            String url = String.format("jdbc:postgresql://%s:%s%s", host, port, path);
            System.out.println("Connected");
            return Jdbi.create(url, username, password);
        }
        return Jdbi.create(defaultJdbcUrl);
    }

    public static void main(String[] args) {
        try {
            staticFiles.location("/public");
            port(getHerokuAssignedPort());
            Jdbi jdbi = getJdbiDatabaseConnection("jdbc:postgresql://localhost/soccer_statistics_db?user=mike&password=mike123");
            SeasonService seasonService = new LeagueService(new SeasonQueries(jdbi));

            get("/", (req, res) -> {
                Map<String, Object> map = new HashMap<>();
                return new ModelAndView(map, "index.handlebars");
            }, new HandlebarsTemplateEngine());

            get("/standings", (req, res) -> {
                Map<String, Object> map = new HashMap<>();
                int leagueId = 1;
                List<League> leagues = seasonService.getAllLeagues();
                List<Team> teams = seasonService.getAllTeamsInLeague(leagueId);

                List<Map<String, Object>> tableRows = new ArrayList<>();
                List<Map<String, Object>> tableRowsWithPos = new ArrayList<>();

                for (Team team : teams) {
                    Map<String, Object> data = new HashMap<>();
                    data.put("played", seasonService.getNumberOfMatchesPlayed(team.getTeamId(), leagueId));
                    data.put("club", team.getTeamName());
                    data.put("wins", seasonService.getNumberOfWins(team.getTeamId(), leagueId));
                    data.put("draws", seasonService.getNumberOfDraws(team.getTeamId(), leagueId));
                    data.put("losses", seasonService.getNumberOfLosses(team.getTeamId(), leagueId));
                    data.put("goalsFor", seasonService.getNumberOfGoalsFor(team.getTeamId(), leagueId));
                    data.put("goalsAgainst", seasonService.getNumberOfGoalsAgainst(team.getTeamId(), leagueId));
                    data.put("goalDifference", (seasonService.getNumberOfGoalsFor(team.getTeamId(), leagueId) - seasonService.getNumberOfGoalsAgainst(team.getTeamId(), leagueId)));
                    data.put("points", ((seasonService.getNumberOfWins(team.getTeamId(), leagueId) * 3) + (seasonService.getNumberOfDraws(team.getTeamId(), leagueId))));
                    tableRows.add(data);
                }

                tableRows.sort(Comparator.comparing(stringObjectMap -> (Integer) stringObjectMap.get("points")));
                Collections.reverse(tableRows);

                for (int l = 0; l < tableRows.size(); l++) {
                    tableRows.get(l).put("pos", l + 1);
                    tableRowsWithPos.add(tableRows.get(l));
                }
                League league = seasonService.getLeagueById(leagueId);

                map.put("leagueName", league.getLeagueName());
                map.put("winner", tableRowsWithPos.get(0).get("club"));

                map.put("tableRows", tableRowsWithPos);
                map.put("league", leagues);

                return new ModelAndView(map, "standings.handlebars");
            }, new HandlebarsTemplateEngine());
            post("/standings", (req, res) -> {
                Map<String, Object> map = new HashMap<>();
                res.redirect("/standings/".concat(req.queryParams("league-id")));
                return new ModelAndView(map, "standings.handlebars");
            }, new HandlebarsTemplateEngine());
            get("/standings/:league_id", (req, res) -> {
                Map<String, Object> map = new HashMap<>();
                int leagueId = Integer.parseInt(req.params("league_id"));
                List<League> leagues = seasonService.getAllLeagues();
                List<Team> teams = seasonService.getAllTeamsInLeague(leagueId);

                List<Map<String, Object>> tableRows = new ArrayList<>();
                List<Map<String, Object>> tableRowsWithPos = new ArrayList<>();

                for (Team team : teams) {
                    Map<String, Object> data = new HashMap<>();
                    data.put("played", seasonService.getNumberOfMatchesPlayed(team.getTeamId(), leagueId));
                    data.put("club", team.getTeamName());
                    data.put("wins", seasonService.getNumberOfWins(team.getTeamId(), leagueId));
                    data.put("draws", seasonService.getNumberOfDraws(team.getTeamId(), leagueId));
                    data.put("losses", seasonService.getNumberOfLosses(team.getTeamId(), leagueId));
                    data.put("goalsFor", seasonService.getNumberOfGoalsFor(team.getTeamId(), leagueId));
                    data.put("goalsAgainst", seasonService.getNumberOfGoalsAgainst(team.getTeamId(), leagueId));
                    data.put("goalDifference", (seasonService.getNumberOfGoalsFor(team.getTeamId(), leagueId) - seasonService.getNumberOfGoalsAgainst(team.getTeamId(), leagueId)));
                    data.put("points", ((seasonService.getNumberOfWins(team.getTeamId(), leagueId) * 3) + (seasonService.getNumberOfDraws(team.getTeamId(), leagueId))));
                    tableRows.add(data);
                }

                tableRows.sort(Comparator.comparing(stringObjectMap -> (Integer) stringObjectMap.get("points")));
                Collections.reverse(tableRows);

                for (int l = 0; l < tableRows.size(); l++) {
                    tableRows.get(l).put("pos", l + 1);
                    tableRowsWithPos.add(tableRows.get(l));
                }


                League league = seasonService.getLeagueById(leagueId);

                map.put("tableRows", tableRowsWithPos);
                map.put("league", leagues);
                map.put("leagueName", league.getLeagueName());
                map.put("winner", tableRowsWithPos.get(0).get("club"));
                return new ModelAndView(map, "standings.handlebars");
            }, new HandlebarsTemplateEngine());

            post("/clear/league", (req, res) -> {
                Map<String, Object> map = new HashMap<>();
                seasonService.clearStatisticsForLeague(Integer.parseInt(req.queryParams("league-id")));
                res.redirect("/standings/".concat(req.queryParams("league-id")));
                return new ModelAndView(map, "standings.handlebars");
            }, new HandlebarsTemplateEngine());

            get("/leaderboards", (req, res) -> {

//                List<Player> topGoalScorers = leagueService.getTopGoalScorers();
//                List<Player> mostAssists = leagueService.getMostAssists();
//                List<Player> mostCleanSheets = leagueService.getMostCleanSheets();
//                List<Player> mostYellowCards = leagueService.getMostYellowCards();
//                List<Player> mostRedCards = leagueService.getMostRedCards();


                Map<String, Object> map = new HashMap<>();
                return new ModelAndView(map, "leaderboards.handlebars");
            }, new HandlebarsTemplateEngine());

            get("/simulate", (req, res) -> {
                Map<String, Object> map = new HashMap<>();
                List<League> leagues = seasonService.getAllLeagues();
                map.put("league", leagues);
                return new ModelAndView(map, "simulate.handlebars");
            }, new HandlebarsTemplateEngine());

            post("/simulate/season", (req, res) -> {
                Map<String, Object> map = new HashMap<>();

                int leagueId = Integer.parseInt(req.queryParams("league-id"));

                boolean finished = seasonService.simulateSeason(leagueId);
                List<League> leagues = seasonService.getAllLeagues();

                map.put("league", leagues);
                map.put("finished", finished);
                return new ModelAndView(map, "simulate.handlebars");
            }, new HandlebarsTemplateEngine());

            get("/create", (req, res) -> {
                Map<String, Object> map = new HashMap<>();
                return new ModelAndView(map, "create.handlebars");
            }, new HandlebarsTemplateEngine());

            get("/create/team", (req, res) -> {
                Map<String, Object> map = new HashMap<>();
                return new ModelAndView(map, "create_team.handlebars");
            }, new HandlebarsTemplateEngine());
            post("/create/team", (req, res) -> {

                String teamName = req.queryParams("team-name");
                int numberOfPlayers = Integer.parseInt(req.queryParams("number-of-players"));

                Team newTeam = new Team();


                newTeam.setTeamName(teamName);
                newTeam.setNumberOfPlayers(numberOfPlayers);
                seasonService.addTeam(newTeam);

                Map<String, Object> map = new HashMap<>();


                return new ModelAndView(map, "create_team.handlebars");
            }, new HandlebarsTemplateEngine());

            get("/create/league", (req, res) -> {
                Map<String, Object> map = new HashMap<>();
                return new ModelAndView(map, "create_league.handlebars");
            }, new HandlebarsTemplateEngine());
            post("/create/league", (req, res) -> {
                String leagueName = req.queryParams("team-name");
                int numberOfTeams = Integer.parseInt(req.queryParams("number-of-teams"));
                seasonService.createLeague(leagueName, numberOfTeams);
                Map<String, Object> map = new HashMap<>();
                return new ModelAndView(map, "create_league.handlebars");
            }, new HandlebarsTemplateEngine());

            get("/add/player", (req, res) -> {
                Map<String, Object> map = new HashMap<>();
                List<Team> teams = seasonService.getAllTeams();
                List<FieldPosition> fieldPositions = seasonService.getAllFieldPositions();

                map.put("team", teams);
                map.put("positions", fieldPositions);
                return new ModelAndView(map, "add_player.handlebars");
            }, new HandlebarsTemplateEngine());
            post("/add/player", (req, res) -> {
                Map<String, Object> map = new HashMap<>();

                String firstName = req.queryParams("first-name");
                String lastName = req.queryParams("last-name");
                int fieldPosition = Integer.parseInt(req.queryParams("field-position"));
                int teamId = Integer.parseInt(req.queryParams("team"));

                Player newPlayer = new Player();

                newPlayer.setFieldPosition(fieldPosition);
                newPlayer.setFirstName(firstName);
                newPlayer.setLastName(lastName);
                newPlayer.setTeamId(teamId);

                boolean addedPlayer = seasonService.addPlayer(newPlayer);
                List<Team> teams = seasonService.getAllTeams();
                List<FieldPosition> fieldPositions = seasonService.getAllFieldPositions();

                map.put("team", teams);
                map.put("positions", fieldPositions);
                map.put("success", addedPlayer);
                return new ModelAndView(map, "add_player.handlebars");
            }, new HandlebarsTemplateEngine());

            get("/add/team", (req, res) -> {
                Map<String, Object> map = new HashMap<>();
                List<Team> teams = seasonService.getAllTeams();
                List<League> leagues = seasonService.getAllLeagues();

                map.put("team", teams);
                map.put("league", leagues);

                return new ModelAndView(map, "edit_league.handlebars");
            }, new HandlebarsTemplateEngine());
            post("/add/team", (req, res) -> {
                Map<String, Object> map = new HashMap<>();

                boolean addedTeam = seasonService.addTeamToLeague(
                        Integer.parseInt(req.queryParams("team-id")),
                        Integer.parseInt(req.queryParams("league-id"))
                );

                List<Team> teams = seasonService.getAllTeams();
                List<League> leagues = seasonService.getAllLeagues();

                map.put("team", teams);
                map.put("league", leagues);
                map.put("success", addedTeam);

                return new ModelAndView(map, "edit_league.handlebars");
            }, new HandlebarsTemplateEngine());


        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
