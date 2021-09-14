package browsrhtml;

import domain.Contentspan;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

class BrowsrDocumentValidatorTest {

	@Test
	void testWithString() {
		BrowsrDocumentValidator.assertIsValidBrowsrDocument("""
				<table>
				  <tr><td>HTML elements partially supported by Browsr:
				  <tr><td>
				    <table>
				      <tr><td><a href="a.html">a</a><td>Hyperlink anchors
				      <tr><td><a href="table.html">table</a><td>Tables
				      <tr><td><a href="tr.html">tr</a><td>Table rows
				      <tr><td><a href="td.html">td</a><td>Table cells containing table data
				    </table>
				</table>
				""");
	}

	@Test
	void testWithStringIt2(){
		BrowsrDocumentValidator.assertIsValidBrowsrDocument("""
				<form action="browsrformactiontest.php">
					<table>
						<tr><td>List words from the Woordenlijst Nederlandse Taal
						<tr><td>
							<table>
								<tr>
									<td>Starts with:
									<td><input type="text" name="starts_with">
								<tr>
									<td>Max. results:
									<td><input type="text" name="max_nb_results">
							</table>
						<tr><td><input type="submit">
					</table>
				</form>
				""");
	}
	
	@Test
	void testWithURL() throws MalformedURLException, IOException {
		BrowsrDocumentValidator.assertIsValidBrowsrDocument(new URL(new URL("https://people.cs.kuleuven.be/~bart.jacobs/index.html"), "browsrtest.html"));
	}

}
