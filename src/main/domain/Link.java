package domain;

public class Link extends Contentspan {
	/**
	 * Href of the link
	 */
	private String href;

	/**
	 * Constructor for the Link class
	 * @param href href of the Link
	 */
	public Link(String href) {
		setHref(href);
	}

	/**
	 * Setter for the href of the link
	 * @param href
	 * @throws IllegalArgumentException if the given href is null
	 */
	public void setHref(String href){
		if(href == null){
			throw new IllegalArgumentException("Href is null.");
		}
		this.href = href;
	}

	/**
	 * Getter for the href of the link
	 * @return the href of the link
	 */
	public String getHref(){
		return this.href;
	}

	@Override
	public String getHtmlString(){
		return "<a href=\"" + this.href + "\">"+ this.href +"</a>";
	}

}
