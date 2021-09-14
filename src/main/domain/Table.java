package domain;
import java.util.List;

public class Table extends Contentspan{
	/**
	 * Rows of the table
	 */
	private List<TableRow> rows;

	/**
	 * Constructor for the Table class
	 * @param rows rows of the Table
	 */
	public Table(List<TableRow> rows) {
		setRows(rows);
	}

	/**
	 * Getter for the rows of the table
	 * @return the rows of the table
	 */
	public List<TableRow> getRows(){
		return this.rows;
	}

	/**
	 * Setter for the rows of the table
	 * @param rows
	 * @throws IllegalArgumentException if the given rows are null
	 */
	public void setRows(List<TableRow> rows){
		if(rows == null){
			throw new IllegalArgumentException("Rows are null.");
		}
		this.rows = List.copyOf(rows);
	}

	@Override
	public String getHtmlString(){
		String table = "<table>";
		for(TableRow row: rows){
			table += row.getHtmlString();
		}
		table += "</table>";
		return table;
	}
}
