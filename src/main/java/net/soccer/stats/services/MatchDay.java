package net.soccer.stats.services;

import com.github.javafaker.Faker;
import net.soccer.stats.pojo.Team;

public   class MatchDay {

    Team home;
    Team away;
    String result;
    int leagueId;


    public MatchDay(Team home, Team away,int leagueId) {
        this.leagueId = leagueId;
        this.home = home;
        this.away = away;
    }

    public String simulate() {

        Faker faker = new Faker();

        this.result = faker.random().nextInt(0, 4).toString().concat("-").concat(faker.random().nextInt(0, 4).toString());

        updateStandings(this.result);

        return this.result;
    }


    public static int[] getIntegerResults(String[] scores) {
        int[] intScores = new int[scores.length];
        for (int i = 0; i < scores.length; i++) {
            intScores[i] = Integer.parseInt(scores[i]);
        }
        return intScores;
    }

    public void updateStandings(String result) {
        int[] intScores = getIntegerResults(result.split("-"));
//
//        this.home.setMatchesPlayed(1);
//        this.away.setMatchesPlayed(1);
//
//        this.home.setGoalsFor(intScores[0]);
//        this.away.setGoalsFor(intScores[1]);
//
//        this.home.setGoalsAgainst(intScores[1]);
//        this.away.setGoalsAgainst(intScores[0]);
//
//        this.home.setGoalDifference(this.home.getGoalsFor() - this.home.getGoalsAgainst());
//        this.away.setGoalDifference(this.away.getGoalsFor() - this.away.getGoalsAgainst());
//
//
//        if (intScores[0] == intScores[1]) {
//            this.home.setDraws(1);
//            this.away.setDraws(1);
//            this.home.setPoints(1);
//            this.away.setPoints(1);
//        }
//
//        if (intScores[0] > intScores[1]) {
//            this.home.setWins(1);
//            this.home.setPoints(3);
//            this.away.setLosses(1);
//        }
//
//        if (intScores[1] > intScores[0]) {
//            this.away.setWins(1);
//            this.away.setPoints(3);
//            this.home.setLosses(1);
//        }
    }

}
