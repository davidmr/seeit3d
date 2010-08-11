package seeit3d.database.analizer;

import java.sql.*;

public class Analizer {

	public static void main(String[] args) {
		String driverToUse = args[0];
		String urlToConnect = args[1];
		String user = args[2];
		String passwd = args[3];

		try {
			Class.forName(driverToUse);
			Connection conn = DriverManager.getConnection(urlToConnect, user, passwd);
			DatabaseMetaData meta = conn.getMetaData();
			// The Oracle database stores its table names as Upper-Case,
			// if you pass a table name in lowercase characters, it will not
			// work.
			// MySQL database does not care if table name is
			// uppercase/lowercase.
			//
			// ResultSet rs = meta.getExportedKeys(conn.getCatalog(), null, "zipcode");
			// while (rs.next()) {
			// String fkTableName = rs.getString("FKTABLE_NAME");
			// String fkColumnName = rs.getString("FKCOLUMN_NAME");
			// int fkSequence = rs.getInt("KEY_SEQ");
			// System.out.println("getExportedKeys(): fkTableName=" + fkTableName);
			// System.out.println("getExportedKeys(): fkColumnName=" + fkColumnName);
			// System.out.println("getExportedKeys(): fkSequence=" + fkSequence);
			// }
			DatabaseMetaData metaData = conn.getMetaData();
			ResultSet tables = metaData.getTables(null, "public", "%", new String[] { "TABLE" });
			// ResultSetMetaData data = tables.getMetaData();
			// int columnCount = data.getColumnCount();
			// for(int i = 1; i <= columnCount; i++){
			// String columnName = data.getColumnName(i);
			// System.out.println(columnName);
			// }
			System.out.println("*******TABLES********");
			while (tables.next()) {
				System.out.println(tables.getString("table_name"));
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

}
