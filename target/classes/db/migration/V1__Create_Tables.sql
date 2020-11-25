drop table if exists team, player,league, team_league,field_position, goals_for, goals_against, wins, losses, draws, matches_played, match_result;

create table team (
    team_id serial not null primary key,
    team_name text not null,
    number_of_players int not null
);

insert into team (team_name, number_of_players) values ('Man United',30);
insert into team (team_name, number_of_players) values ('Leicester City',30);
insert into team (team_name, number_of_players) values ('Tottenham',30);
insert into team (team_name, number_of_players) values ('Liverpool',30);
insert into team (team_name, number_of_players) values ('Southampton',30);
insert into team (team_name, number_of_players) values ('Chelsea',30);
insert into team (team_name, number_of_players) values ('Aston VIlla',30);
insert into team (team_name, number_of_players) values ('Everton',30);
insert into team (team_name, number_of_players) values ('Crystal Palace',30);
insert into team (team_name, number_of_players) values ('Wolves',30);
insert into team (team_name, number_of_players) values ('Man. City',30);
insert into team (team_name, number_of_players) values ('Arsenal',30);
insert into team (team_name, number_of_players) values ('West Ham',30);
insert into team (team_name, number_of_players) values ('Newcastle',30);
insert into team (team_name, number_of_players) values ('Leeds United',30);
insert into team (team_name, number_of_players) values ('Brighton',30);
insert into team (team_name, number_of_players) values ('Fulham',30);
insert into team (team_name, number_of_players) values ('West Brom',30);
insert into team (team_name, number_of_players) values ('Burnley',30);
insert into team (team_name, number_of_players) values ('Sheffield United',30);

insert into team (team_name, number_of_players) values ('Real Madrid',30);
insert into team (team_name, number_of_players) values ('Atletico Madrid',30);
insert into team (team_name, number_of_players) values ('Barcelona',30);
insert into team (team_name, number_of_players) values ('Villareal',30);
insert into team (team_name, number_of_players) values ('Sevilla',30);
insert into team (team_name, number_of_players) values ('Real Sociedad',30);
insert into team (team_name, number_of_players) values ('Real Betis',30);
insert into team (team_name, number_of_players) values ('Athletic Bilbao',30);
insert into team (team_name, number_of_players) values ('Valencia',30);
insert into team (team_name, number_of_players) values ('Celta de Vigo',30);
insert into team (team_name, number_of_players) values ('CD Laganes',30);
insert into team (team_name, number_of_players) values ('Getafe CF',30);
insert into team (team_name, number_of_players) values ('Eiche',30);
insert into team (team_name, number_of_players) values ('Granada',30);
insert into team (team_name, number_of_players) values ('Osasuna',30);
insert into team (team_name, number_of_players) values ('Alaves',30);
insert into team (team_name, number_of_players) values ('Real Valladolid',30);
insert into team (team_name, number_of_players) values ('Levante',30);
insert into team (team_name, number_of_players) values ('SD Huesca',30);
insert into team (team_name, number_of_players) values ('Celta Vigo',30);

create table field_position (
    position_id serial not null primary key,
    position_name text not null
);

insert into field_position (position_name) values ('Goal Keeper');
insert into field_position (position_name) values ('Defender');
insert into field_position (position_name) values ('Midfielder');
insert into field_position (position_name) values ('Attacker');



create table league (
    league_id serial not null primary key,
    league_name text not null,
    number_of_teams int not null
);
insert into league (league_name, number_of_teams) values ('Premier League',20);
insert into league (league_name, number_of_teams) values ('La Liga Santander',20);

create table player (
    player_id serial not null primary key,
    first_name text not null,
    last_name text not null,
    field_position_id int not null,
    team_id int not null,
    foreign key (field_position_id) references field_position (position_id),
    foreign key (team_id) references team (team_id)
);

create table team_league(
    id serial not null primary key,
    team_id  int not null,
    league_id  int not null,
    foreign key (team_id) references team (team_id),
    foreign key (league_id) references team (team_id)
);

insert into team_league (team_id, league_id) values (1,1);
insert into team_league (team_id, league_id) values (2,1);
insert into team_league (team_id, league_id) values (3,1);
insert into team_league (team_id, league_id) values (4,1);
insert into team_league (team_id, league_id) values (5,1);
insert into team_league (team_id, league_id) values (6,1);
insert into team_league (team_id, league_id) values (7,1);
insert into team_league (team_id, league_id) values (8,1);
insert into team_league (team_id, league_id) values (9,1);
insert into team_league (team_id, league_id) values (10,1);
insert into team_league (team_id, league_id) values (11,1);
insert into team_league (team_id, league_id) values (12,1);
insert into team_league (team_id, league_id) values (13,1);
insert into team_league (team_id, league_id) values (14,1);
insert into team_league (team_id, league_id) values (15,1);
insert into team_league (team_id, league_id) values (16,1);
insert into team_league (team_id, league_id) values (17,1);
insert into team_league (team_id, league_id) values (18,1);
insert into team_league (team_id, league_id) values (19,1);
insert into team_league (team_id, league_id) values (20,1);

insert into team_league (team_id, league_id) values (21,2);
insert into team_league (team_id, league_id) values (22,2);
insert into team_league (team_id, league_id) values (23,2);
insert into team_league (team_id, league_id) values (24,2);
insert into team_league (team_id, league_id) values (25,2);
insert into team_league (team_id, league_id) values (26,2);
insert into team_league (team_id, league_id) values (27,2);
insert into team_league (team_id, league_id) values (28,2);
insert into team_league (team_id, league_id) values (29,2);
insert into team_league (team_id, league_id) values (30,2);
insert into team_league (team_id, league_id) values (31,2);
insert into team_league (team_id, league_id) values (32,2);
insert into team_league (team_id, league_id) values (33,2);
insert into team_league (team_id, league_id) values (34,2);
insert into team_league (team_id, league_id) values (35,2);
insert into team_league (team_id, league_id) values (36,2);
insert into team_league (team_id, league_id) values (37,2);
insert into team_league (team_id, league_id) values (38,2);
insert into team_league (team_id, league_id) values (39,2);
insert into team_league (team_id, league_id) values (40,2);


create table wins(
    id serial not null primary key,
    team_id int not null,
    league_id int not null,
    foreign key (team_id) references team (team_id),
    foreign key (league_id) references league (league_id)
);


create table losses(
    id serial not null primary key,
    team_id int not null,
    league_id int not null,
    foreign key (team_id) references team (team_id),
    foreign key (league_id) references league (league_id)
);


create table draws(
    id serial not null primary key,
    team_id int not null,
    league_id int not null,
    foreign key (team_id) references team (team_id),
    foreign key (league_id) references league (league_id)
);



create table goals_for(
    id serial not null primary key,
    team_id int not null,
    league_id int not null,
    foreign key (team_id) references team (team_id),
    foreign key (league_id) references league (league_id)
);

create table goals_against(
    id serial not null primary key,
    team_id int not null,
    league_id int not null,
    foreign key (team_id) references team (team_id),
    foreign key (league_id) references league (league_id)
);

create table matches_played(
    id serial not null primary key,
    team_id int not null,
    league_id int not null,
    foreign key (team_id) references team (team_id),
    foreign key (league_id) references league (league_id)
);


create table match_result(
    id serial not null primary key,
    result text not null,
    home_team_id int not null,
    away_team_id int not null,
    league_id int not null,
    foreign key (home_team_id) references team (team_id),
    foreign key (away_team_id) references team (team_id),
    foreign key (league_id) references league (league_id)
);











