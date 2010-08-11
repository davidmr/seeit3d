package seeit3d.database.analizer;

import java.io.File;
import java.sql.*;
import java.util.*;

import javax.xml.bind.*;

import seeit3d.database.xml.model.Container;
import seeit3d.database.xml.model.Containers;

import com.google.common.collect.Collections2;

public class Analizer {

	public static void main(String[] args) {
		String driverToUse = args[0];
		String urlToConnect = args[1];
		String user = args[2];
		String passwd = args[3];

		try {
			Class.forName(driverToUse);
			Connection conn = DriverManager.getConnection(urlToConnect, user, passwd);
			DatabaseMetaData metaData = conn.getMetaData();

			ResultSet tablesDB = metaData.getTables(null, null, "%", new String[] { "TABLE" });

			List<Table> tables = new ArrayList<Table>();

			while (tablesDB.next()) {
				String tableName = tablesDB.getString("table_name");
				tables.add(new Table(tableName));
			}

			for (Table table : tables) {
				ResultSet foeringKeyTableRS = metaData.getExportedKeys(conn.getCatalog(), null, table.getName());
				while (foeringKeyTableRS.next()) {
					String fkTableName = foeringKeyTableRS.getString("fktable_name");
					for (Table table2 : tables) {
						if (table2.getName().equals(fkTableName)) {
							table2.addFoeringTable(table);
						}
					}
				}
			}

			for (Table table : tables) {
				ResultSet columnsRS = metaData.getColumns(null, null, table.getName(), "%");
				while (columnsRS.next()) {
					String columnName = columnsRS.getString("column_name");
					int type = columnsRS.getInt("data_type");
					table.addColumn(new Column(columnName, type));
				}
			}

			for (Table table : tables) {
				Statement statement = conn.createStatement();
				ResultSet rowNumRS = statement.executeQuery("select count(*) from " + table.getName());
				while (rowNumRS.next()) {
					int rowNum = rowNumRS.getInt(1);
					table.setRowNum(rowNum);
				}
			}

			System.out.println("*******TABLES ANALIZED********");
			for (Table table : tables) {
				System.out.println(table);
			}

			System.out.println("**********TRANSFORMING**********");
			Collection<Container> containers = Collections2.transform(tables, new TranformFromTableToContainer());
			Containers containersXML = new Containers();
			List<Container> containerXMLList = containersXML.getContainer();
			containerXMLList.addAll(containers);

			System.out.println("***********WRITTING XML **********");
			JAXBContext jc = JAXBContext.newInstance("seeit3d.database.xml.model");
			Marshaller m = jc.createMarshaller();
			m.marshal(containersXML, new File("output.xml"));

			System.out.println("********END*******");

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (JAXBException e) {
			e.printStackTrace();
		}

	}
}
