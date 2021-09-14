package browsrhtml;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.net.URL;
import java.util.ArrayList;

import browsrhtml.HtmlLexer.TokenType;
import domain.*;

public class BrowsrDocumentValidator {
	
	HtmlLexer lexer;
	
	BrowsrDocumentValidator(Reader reader) {
		lexer = new HtmlLexer(reader);
	}
	
	void eatToken() {
		lexer.eatToken();
	}
	
	void fail() {
		throw new RuntimeException("The given document is not a valid Browsr document.");
	}
	
	void assertTrue(boolean b) {
		if (!b)
			fail();
	}
	
	void assertToken(TokenType tokenType) {
		assertTrue(lexer.getTokenType() == tokenType);
	}
	
	void assertTokenValue(String value) {
		assertTrue(lexer.getTokenValue().equals(value));
	}
	
	String consumeToken(TokenType tokenType) {
		assertToken(tokenType);
		String result = lexer.getTokenValue();
		eatToken();
		return result;
	}
	
	void consumeToken(TokenType tokenType, String tokenValue) {
		assertToken(tokenType);
		assertTokenValue(tokenValue);
		eatToken();
	}
	
	void consumeOpenStartTag(String tagName) {
		consumeToken(TokenType.OPEN_START_TAG, tagName);
	}
	
	String consumeAttribute(String attributeName) {
		consumeToken(TokenType.IDENTIFIER, attributeName);
		consumeToken(TokenType.EQUALS);
		return consumeToken(TokenType.QUOTED_STRING);
	}
	
	Contentspan consumeBrowsrDocument() {
		Contentspan c = consumeContentSpan();
		assertToken(TokenType.END_OF_FILE);
		return c;
	}
	
	String consumeTextSpan() {
		// When interpreting a text span as a single text string,
		// whitespace between text tokens is replaced by a single space character.
		StringBuilder text = new StringBuilder();
		boolean first = true;
		while (lexer.getTokenType() == TokenType.TEXT) {
			if (first)
				first = false;
			else
				text.append(' ');
			text.append(lexer.getTokenValue());
			eatToken();
		}
		return text.toString();
	}
	
	void consumeEndTag(String tagName) {
		consumeToken(TokenType.OPEN_END_TAG, tagName);
		consumeToken(TokenType.CLOSE_TAG);
	}
	
	String consumeHyperlink() {
		consumeOpenStartTag("a");
		String href = consumeAttribute("href");
		consumeToken(TokenType.CLOSE_TAG);
		consumeTextSpan();
		consumeEndTag("a");
		return href;
	}
	
	void consumeStartTag(String tagName) {
		consumeOpenStartTag(tagName);
		consumeToken(TokenType.CLOSE_TAG);
	}
	
	Contentspan consumeTableCell() {
		consumeStartTag("td");
		return consumeContentSpan();
	}
	
	TableRow consumeTableRow() {
		ArrayList<Contentspan> cells = new ArrayList<>();
		consumeStartTag("tr");
		while (lexer.getTokenType() == TokenType.OPEN_START_TAG && lexer.getTokenValue().equals("td"))
			cells.add(consumeTableCell());
		return new TableRow(cells);
	}
	
	Table consumeTable() {
		ArrayList<TableRow> rows = new ArrayList<>();
 		consumeStartTag("table");
		while (lexer.getTokenType() == TokenType.OPEN_START_TAG && lexer.getTokenValue().equals("tr"))
			rows.add(consumeTableRow());
		consumeEndTag("table");
		return new Table(rows);
	}

	Input consumeInput(){
		Input input = null;
		consumeOpenStartTag("input");
		String type = consumeAttribute("type");
		if(type.equals("text")){
			String name = consumeAttribute("name");
			input = new TextInputField(name);
		}

		if(type.equals("submit")){
			input = new Submit();
		}
		consumeToken(TokenType.CLOSE_TAG);
		return input;
	}

	Form consumeForm(){
		consumeOpenStartTag("form");
		String action = consumeAttribute("action");
		consumeToken(TokenType.CLOSE_TAG);
		Contentspan content = consumeContentSpan();
		consumeEndTag("form");
		return new Form(action, content);
	}

	
	Contentspan consumeContentSpan() {
		switch (lexer.getTokenType()) {
			case TEXT -> {
				return new Text(consumeTextSpan());
			}
			case OPEN_START_TAG -> {
			switch (lexer.getTokenValue()) {
				case "a" -> {
					return new Link(consumeHyperlink());
				}
				case "table" -> {
					return consumeTable();
				}
				case "form" -> {
					return consumeForm();
				}
				case "input" -> {
					return consumeInput();
				}
				default -> throw new RuntimeException("The given document is not a valid Browsr document.");
			}
		}
		default -> throw new RuntimeException("The given document is not a valid Browsr document.");
		}
	}
	
	public static Contentspan assertIsValidBrowsrDocument(Reader reader) {
		return new BrowsrDocumentValidator(reader).consumeBrowsrDocument();
	}
	
	public static Contentspan assertIsValidBrowsrDocument(String document) {
		return new BrowsrDocumentValidator(new StringReader(document)).consumeBrowsrDocument();
	}
	
	public static Contentspan assertIsValidBrowsrDocument(URL url) throws IOException {
		return new BrowsrDocumentValidator(new BufferedReader(new InputStreamReader(url.openStream()))).consumeBrowsrDocument();
	}
	
	

}
