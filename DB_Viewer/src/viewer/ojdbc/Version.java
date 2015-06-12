package viewer.ojdbc;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import oracle.jdbc.pool.OracleDataSource;

public class Version
{
    public static void test() throws SQLException
    {
        OracleDataSource ods = new OracleDataSource();
//        ods.setURL("jdbc:oracle:thin:hr/hr@localhost:1521/oracle");
//        ods.setURL("jdbc:oracle:thin:@ora14.informatik.haw-hamburg.de:1521/inf14.informatik.haw-hamburg.de");
        ods.setURL("jdbc:oracle:thin:@ora14.informatik.haw-hamburg.de:1521:inf14");
        ods.setUser("abp403");
        ods.setPassword("DaV718227");
        Connection conn = ods.getConnection();

        // Create Oracle DatabaseMetaData object
        DatabaseMetaData meta = conn.getMetaData();

        // gets driver info:
        System.out.println("JDBC driver version is " + meta.getDriverVersion());
        
        Statement s = conn.createStatement();
        s.executeQuery("DELETE FROM Kunde WHERE Nachname = 'Posey'");
        s.executeQuery("INSERT INTO Kunde values (10, 'Posey', 'Tyler', 'Beacon Hills')");
        s.close();
        
        System.out.println("Done");
    }
}
