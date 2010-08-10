package seeit3d.database.analizer;

import java.sql.*;

public class Analizer {

	public static void main(String[] args) {
		String urlToConnect = args[0];
		String driverToUse = args[1];

		try {
			Class.forName(driverToUse);
			Connection db = DriverManager.getConnection(urlToConnect);
			DatabaseMetaData metaData = db.getMetaData();
			ResultSet tables = metaData.getTables(null, null, "%", null);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

}
