package scheduler;

import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by kevin on 11/8/16.
 */
public class DataManager
{
    private static String CONNECTION_URI = null;
    private static Connection conn = null;

    public static void setDBPath(Path path)
    {
        CONNECTION_URI = "jdbc:sqlite:" + path.toString();
    }

    public static Connection getConnection() throws SQLException
    {
        if (conn == null)
            conn = DriverManager.getConnection(CONNECTION_URI);

        return conn;
    }

    public static void updateDatabase() throws SQLException
    {
        int roomsAffected = 0;
        int timesAffected = 0;
        int studentsAffected = 0;
        int professorsAffected = 0;
        int sectionsAffected = 0;

        try
        {
            // insert entities, count number of inserts
            roomsAffected = DataInsert.insertRooms(DataLoad.rooms);
            timesAffected = DataInsert.insertTimes(DataLoad.times);
            studentsAffected = DataInsert.insertStudents(DataLoad.students);
            professorsAffected = DataInsert.insertProfessors(DataLoad.professors);
            sectionsAffected = DataInsert.insertSections(DataLoad.sections);

            // build associative entites
            DataInsert.insertEnrolls(DataLoad.students);
            DataInsert.insertAssigns(DataLoad.professors);
        }
        catch (SQLException e)
        {
            System.err.println(e);
        }
        finally
        {
            if (conn != null)
                conn.close();
        }
    }
}
