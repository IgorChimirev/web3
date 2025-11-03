package com.igor.jsfgraph.db;

import com.igor.jsfgraph.entity.ResultEntity;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Implementation of the ResultDAO interface using pure JDBC.
 * Handles database operations for ResultEntity objects.
 */
public class ResultDAOImpl implements ResultDAO {

    @Override
    public void addNewResult(ResultEntity result) {
        String sql = "INSERT INTO point_model (x, y, r, result) VALUES (?, ?, ?, ?) RETURNING id";
        try (Connection con = JDBCUtils.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            con.setAutoCommit(false);
            ps.setDouble(1, result.getX());
            ps.setDouble(2, result.getY());
            ps.setDouble(3, result.getR());
            ps.setBoolean(4, result.isResult());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    result.setId(rs.getLong("id")); // Set generated ID back to entity
                }
                con.commit();
            } catch (SQLException e) {
                con.rollback();
                throw e;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error adding new result", e);
        }
    }

    @Override
    public void updateResult(Long result_id, ResultEntity result) {
        String sql = "UPDATE point_model SET x = ?, y = ?, r = ?, result = ? WHERE id = ?";
        try (Connection con = JDBCUtils.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            con.setAutoCommit(false);
            ps.setDouble(1, result.getX());
            ps.setDouble(2, result.getY());
            ps.setDouble(3, result.getR());
            ps.setBoolean(4, result.isResult());
            ps.setLong(5, result_id);
            ps.executeUpdate();
            con.commit();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating result", e);
        }
    }

    @Override
    public ResultEntity getResultById(Long result_id) {
        String sql = "SELECT * FROM point_model WHERE id = ?";
        try (Connection con = JDBCUtils.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, result_id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToEntity(rs);
                }
                return null; // Return null if not found (original assumed exists)
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error getting result by ID", e);
        }
    }

    @Override
    public Collection<ResultEntity> getAllResults() {
        String sql = "SELECT * FROM point_model";
        Collection<ResultEntity> results = new ArrayList<>();
        try (Connection con = JDBCUtils.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                results.add(mapResultSetToEntity(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error getting all results", e);
        }
        return results;
    }

    @Override
    public void deleteResult(ResultEntity result) {
        String sql = "DELETE FROM point_model WHERE id = ?";
        try (Connection con = JDBCUtils.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            con.setAutoCommit(false);
            ps.setLong(1, result.getId());
            ps.executeUpdate();
            con.commit();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting result", e);
        }
    }

    @Override
    public void clearResults() {
        String sql = "DELETE FROM point_model";
        try (Connection con = JDBCUtils.getConnection();
             Statement stmt = con.createStatement()) {
            con.setAutoCommit(false);
            try {
                stmt.executeUpdate(sql);
                con.commit();
            } catch (SQLException e) {
                con.rollback();
                throw e;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error clearing results", e);
        }
    }

    private ResultEntity mapResultSetToEntity(ResultSet rs) throws SQLException {
        return ResultEntity.builder()
                .id(rs.getLong("id"))
                .x(rs.getDouble("x"))
                .y(rs.getDouble("y"))
                .r(rs.getDouble("r"))
                .result(rs.getBoolean("result"))
                .build();
    }
}
