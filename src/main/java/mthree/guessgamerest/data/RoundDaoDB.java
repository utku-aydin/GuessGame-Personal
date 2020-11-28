/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mthree.guessgamerest.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.List;
import mthree.guessgamerest.data.GameDaoDB.GameMapper;
import mthree.guessgamerest.models.Game;
import mthree.guessgamerest.models.Round;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

/**
 *
 * @author utkua
 */
@Repository
@Profile("database")
public class RoundDaoDB implements RoundDao {
    
    private final JdbcTemplate jdbc;
    
    public RoundDaoDB(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public List<Round> getRoundsForGame(int gameId) {
        final String sql = "SELECT id, time, guess, result FROM round WHERE gameId = ?;";
        List<Round> roundsForGame = jdbc.query(sql, new RoundMapper(), gameId);
        
        roundsForGame.forEach((round) -> {
            round.setGame(getGameForRound(round));
        });
        
        return roundsForGame;
    }

    @Override
    public Round addRound(Round round) {

        final String sql = "INSERT INTO round(time, guess, result, gameId) VALUES(?,?,?,?);";
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();

        jdbc.update((Connection conn) -> {

            PreparedStatement statement = conn.prepareStatement(
                sql, 
                Statement.RETURN_GENERATED_KEYS);

            statement.setTimestamp(1, Timestamp.valueOf(round.getTime()));
            statement.setInt(2, round.getGuess());
            statement.setString(3, round.getResult());
            statement.setInt(4, round.getGame().getId());
            return statement;

        }, keyHolder);

        round.setId(keyHolder.getKey().intValue());

        return round;
    }
    
    @Override
    public Round getRoundById(int id) {

        final String sql = "SELECT guess, time, result, id"
                + " FROM round WHERE id = ?;";

        Round round = jdbc.queryForObject(sql, new RoundMapper(), id);
        round.setGame(getGameForRound(round));
        
        return round;
    }

    @Override
    public void deleteAll() {
        final String DELETE_GAME = "DELETE FROM round";
        jdbc.update(DELETE_GAME);
    }
    
    private Game getGameForRound(Round round) {
        final String SELECT_GAME_FOR_ROUND = "SELECT * FROM game "
                + "WHERE id = (SELECT gameId FROM round WHERE id = ?)";
        return jdbc.queryForObject(SELECT_GAME_FOR_ROUND, new GameMapper(), 
                round.getId());
    }
    
    private static final class RoundMapper implements RowMapper<Round> {

        @Override
        public Round mapRow(ResultSet rs, int index) throws SQLException {
            Round round = new Round(rs.getInt("guess"), rs.getString("result"));
            round.setId(rs.getInt("id"));
            round.setTime(rs.getTimestamp("time").toLocalDateTime());
            return round;
        }
    }
    
}
