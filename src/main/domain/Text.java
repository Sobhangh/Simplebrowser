package domain;

public class Text extends Contentspan{
	/**
	 * Text
	 */
	private String text;

	/**
	 * Constructor of the Text class
	 * @param text text of the Text
	 */
	public Text(String text) {
		setText(text);
	}

	/**
	 * Getter for the text field
	 * @return text
	 */
	public String getText(){
		return this.text;
	}

	/**
	 * Setter for the text field
	 * @param text
	 * @throws IllegalArgumentException if the given text is null
	 */
	public void setText(String text){
		if(text == null){
			throw new IllegalArgumentException("Text is null.");
		}
		this.text = text;
	}

	@Override
	public String getHtmlString(){
		return this.text;
	}
}
