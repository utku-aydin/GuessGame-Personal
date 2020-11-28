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
import java.util.List;
import mthree.guessgamerest.models.Game;
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
public class GameDaoDB implements GameDao {
    
    private final JdbcTemplate jdbc;
    
    public GameDaoDB(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    } 

    @Override
    public List<Game> getAllGames() {
        final String sql = "SELECT id, answer, finished FROM game;";
        return jdbc.query(sql, new GameMapper());
    }

    @Override
    public Game addGame(Game game) {

        final String sql = "INSERT INTO game(answer,finished) VALUES(?,?);";
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();

        jdbc.update((Connection conn) -> {

            PreparedStatement statement = conn.prepareStatement(
                sql, 
                Statement.RETURN_GENERATED_KEYS);

            statement.setInt(1, game.getAnswer());
            statement.setBoolean(2, game.isFinished());
            return statement;

        }, keyHolder);

        game.setId(keyHolder.getKey().intValue());

        return game;
    }
    
    @Override
    public boolean updateGame(Game game) {

        final String sql = "UPDATE game SET "
                + "finished = ? "
                + "WHERE id = ?;";

        return jdbc.update(sql,
                game.isFinished(),
                game.getId()) > 0;
    }

    @Override
    public Game getGameById(int id) {

        final String sql = "SELECT id, answer, finished "
                + "FROM game WHERE id = ?;";

        return jdbc.queryForObject(sql, new GameMapper(), id);
    }
    
    @Override
    public void deleteAll() {
        final String DELETE_GAME = "DELETE FROM game";
        jdbc.update(DELETE_GAME);
    }
    
    public static final class GameMapper implements RowMapper<Game> {

        @Override
        public Game mapRow(ResultSet rs, int index) throws SQLException {
            Game game = new Game(rs.getInt("answer"), rs.getBoolean("finished"));
            game.setId(rs.getInt("id"));
            return game;
        }
    }
    
}
