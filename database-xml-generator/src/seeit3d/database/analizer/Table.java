package seeit3d.database.analizer;

import java.util.ArrayList;
import java.util.List;

public class Table {

	private final String name;

	private int rowNum;

	private final List<Column> columns;

	private final List<Table> foeringKeysTable;

	public Table(String name) {
		super();
		this.name = name;
		foeringKeysTable = new ArrayList<Table>();
		columns = new ArrayList<Column>();
	}

	public void addFoeringTable(Table table) {
		if (!foeringKeysTable.contains(table)) {
			foeringKeysTable.add(table);
		}
	}

	public void addColumn(Column column) {
		columns.add(column);
	}

	public void setRowNum(int rowNum) {
		this.rowNum = rowNum;
	}

	public int getRowNum() {
		return rowNum;
	}

	public String getName() {
		return name;
	}

	public List<Table> getFoeringKeysTable() {
		return foeringKeysTable;
	}

	public List<Column> getColumns() {
		return columns;
	}

	@Override
	public String toString() {
		String toString = "Table [columns=" + columns + ", foeringKeysTable=";
		for (Table table : foeringKeysTable) {
			toString += table.getName() + " ";
		}
		toString += ", name=" + name + ", rowNum=" + rowNum + "]";
		return toString;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (name == null ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Table other = (Table) obj;
		if (name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!name.equals(other.name)) {
			return false;
		}
		return true;
	}

}
