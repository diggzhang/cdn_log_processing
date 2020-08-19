package org.xingze.etl;

import org.xingze.etl.utils.MySQLJDBCUtil;

import java.sql.Connection;
import java.sql.SQLException;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author mysqltutorial.org
 */
public class MysqlTest {

    public static void main(String[] args) {
        // create a new connection from MySQLJDBCUtil
        try (Connection conn = MySQLJDBCUtil.getConnection()) {

            // print out a message
            System.out.println(String.format("Connected to database %s "
                    + "successfully.", conn.getCatalog()));

            int id = insertEtlLog(1111L, 111, 11, 1);

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }


    /**
     * Insert a new log
     * @return
     * create table xingze.etl_log (
     * insert_time long, rows int, success int, failed int);
     */
    public static int insertEtlLog(Long insert_time,
                                   Integer row,
                                   Integer success,
                                   Integer failed) {
        // for insert a new candidate
        ResultSet rs = null;
        int candidateId = 0;

        String sql = "INSERT INTO xingze.etl_log(insert_time, rows, success, failed) "
                + "VALUES(?,?,?,?)";

        try (Connection conn = MySQLJDBCUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);) {

            // set parameters for statement
            pstmt.setLong(1, insert_time);
            pstmt.setInt(2, row);
            pstmt.setInt(3, success);
            pstmt.setInt(4, failed);

            int rowAffected = pstmt.executeUpdate();
            if(rowAffected == 1)
            {
                // get candidate id
                rs = pstmt.getGeneratedKeys();
                if(rs.next())
                    candidateId = rs.getInt(1);

            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        } finally {
            try {
                if(rs != null)  rs.close();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }

        return candidateId;
    }
}
