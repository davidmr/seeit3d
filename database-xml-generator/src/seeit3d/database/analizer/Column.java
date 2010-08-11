package seeit3d.database.analizer;

public class Column {

	private final String name;

	private final String type;

	public Column(String name, int SQLType) {
		super();
		this.name = name;
		type = Util.SQLTypeIntToString(SQLType);
	}

	public String getName() {
		return name;
	}

	public String getType() {
		return type;
	}

	@Override
	public String toString() {
		return "Column [name=" + name + ", type=" + type + "]";
	}

}
