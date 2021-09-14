package domain;

import browsrhtml.BrowsrDocumentValidator;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.net.URL;
import java.util.List;

public class DocumentController{
	/**
	 * Url of the document
	 */
	private String url;

	/**
	 * Contentspan of the document
	 */
	private Contentspan content;

	/**
	 * Constructor of the DocumentController class
	 */
	public DocumentController() {
		this.content = this.getstartpage();
		this.url = "https://";
	}

	/**
	 * Getter for url of the document
	 * @return url of the document
	 */
	public String getUrl() {
		return this.url;
	}

	/**
	 * Getter for the contentspan of the document
	 * @return contentspan of the document
	 */
	public Contentspan getContentSpan() {
		return content;
	}

	public void setContentSpan(Contentspan content){
		this.content = content;
	}

	/**
	 * Method to update the url and the contentspan of the document with a new url
	 * @param url the new url of the document to load the contentspan
	 */
	public void updateURL(String url) {
		this.url = url;
		try{
			this.content = BrowsrDocumentValidator.assertIsValidBrowsrDocument(new URL(url));
		} catch (Exception exception){
			this.content = new Text(exception.toString());
		}
	}

	/**
	 * Method to update the url and the contentspan of the document with a link
	 * @param link the new link to load the contentspan
	 */
	public void clickLink(String link){
		System.out.println("CONTROLLER: LINK WAS CLICKED: " + link);
		URL url_ = null;
		try{
			url_ = new URL(new URL(this.url), link);
			this.content = BrowsrDocumentValidator.assertIsValidBrowsrDocument(url_);
			this.url = url_.toString();
			System.out.println("URL CONTROLLER:" + this.url);
		} catch (Exception exception){
			this.content = new Text(exception.toString());
		}

	}

	public void writeHtmlOfContentSpan(String filename){
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter("src/" + filename + ".html"));
			writer.write(this.content.getHtmlString());
			writer.close();
		} catch(Exception exception){
			this.content = new Text(exception.toString());
		}
	}

	public Contentspan getstartpage() {
		Text welcomeMsg = new Text("Welcome to Browsr!");
		Text text = new Text("This is a valid link:");
		Link demolink = new Link("https://people.cs.kuleuven.be/~bart.jacobs/browsrtest.html");
		Link formlink = new Link("https://people.cs.kuleuven.be/~bart.jacobs/swop/browsrformtest.html");
		Link wronglink = new Link("https://people.cs.kuleuven.be/~bart.jacobs/swop/rowsrormest.html");
		TableRow rowOne = new TableRow(List.of(welcomeMsg));
		TableRow rowTwo = new TableRow(List.of(text));
		TableRow rowThree = new TableRow(List.of(demolink));
		TableRow rowFour = new TableRow(List.of(formlink));
		TableRow rowFive = new TableRow(List.of(wronglink));
		return new Table(List.of(rowOne,rowTwo,rowThree, rowFour,rowFive));
	}
}
