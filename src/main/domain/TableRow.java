package domain;
import java.util.List;

public class TableRow {
	/**
	 * Elements of the row
	 */
	private List<Contentspan> elements;

	/**
	 * Constructor for the TableRow class
	 * @param elements elements of the TableRow
	 */
	public TableRow(List<Contentspan> elements) {
		setElements(elements);
	}

	/**
	 * Getter for the elements of the row
	 * @return the elements of the row
	 */
	public List<Contentspan> getElements(){
		return this.elements;
	}

	/**
	 * Setter for the elements of the row
	 * @param elements
	 * @throws IllegalArgumentException if the given elements are null
	 */
	public void setElements(List<Contentspan> elements){
		if(elements == null){
			throw new IllegalArgumentException("Elements are null.");
		}
		this.elements = List.copyOf(elements);
	}

	/**
	 * HTML representation of this table row
	 * @return HTML string of this table row
	 */
	public String getHtmlString(){
		String row = "<tr>";
		for(Contentspan elem: elements){
			row += "<td>" + elem.getHtmlString();
		}
		return row;
	}
}
