package domain;

import browsrhtml.BrowsrDocumentValidator;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class DocumentControllerTest {
    DocumentController controller = new DocumentController();

    @Test
    void getUrl() {
        controller.updateURL("https://people.cs.kuleuven.be/~bart.jacobs/browsrtest.html");
        assertEquals("https://people.cs.kuleuven.be/~bart.jacobs/browsrtest.html", controller.getUrl());
    }


    @Test
    void getContentSpan() {
        controller.updateURL("https://people.cs.kuleuven.be/~bart.jacobs/browsrtest.html");
        assertTrue(controller.getContentSpan() instanceof Table);
    }

    @Test
    void updateURL() {
        controller.updateURL("https://people.cs.kuleuven.be/~bart.jacobs/browsrtest.html");
        assertEquals("https://people.cs.kuleuven.be/~bart.jacobs/browsrtest.html", this.controller.getUrl());
        assertTrue(controller.getContentSpan() instanceof Table);
        assertTrue(((Table)controller.getContentSpan()).getRows().size() == 2);
        assertTrue(((Table)controller.getContentSpan()).getRows().get(0).getElements().get(0) instanceof Text);
        assertTrue(((Table)controller.getContentSpan()).getRows().get(1).getElements().get(0) instanceof Table);
    }

    @Test
    void updateURLNonValidDocument() {
        String error = "";
        try{
            BrowsrDocumentValidator.assertIsValidBrowsrDocument(new URL("somerandomletters"));
        } catch (Exception exception){
            error = exception.toString();
        }
         controller.updateURL("somerandomletters");
         assertEquals(error, ((Text)controller.getContentSpan()).getText());
    }

    @Test
    void clickLink() {
        String url = "https://people.cs.kuleuven.be/~bart.jacobs/index.html";
        String link = "browsrtest.html";
        this.controller.updateURL(url);
        this.controller.clickLink(link);
        assertEquals("https://people.cs.kuleuven.be/~bart.jacobs/browsrtest.html", this.controller.getUrl());
        assertTrue(controller.getContentSpan() instanceof Table);
        assertTrue(((Table)controller.getContentSpan()).getRows().size() == 2);
        assertTrue(((Table)controller.getContentSpan()).getRows().get(0).getElements().get(0) instanceof Text);
        assertTrue(((Table)controller.getContentSpan()).getRows().get(1).getElements().get(0) instanceof Table);
    }

    @Test
    void clickLinkNonValidDocument() {
        controller.updateURL("https://people.cs.kuleuven.be/~bart.jacobs/browsrtest.html");
        String error = "";
        try{
            BrowsrDocumentValidator.assertIsValidBrowsrDocument(new URL(new URL("https://people.cs.kuleuven.be/~bart.jacobs/browsrtest.html"), "somerandomletters"));
        } catch (Exception exception){
            System.out.println(error);
            error = exception.toString();
        }
        controller.clickLink("somerandomletters");
        assertEquals(error, ((Text)controller.getContentSpan()).getText());
    }

    @Test
    void writeHtmlOfContentSpanTest() throws IOException {
        controller.updateURL("https://people.cs.kuleuven.be/~bart.jacobs/browsrtest.html");
        System.out.println(controller.getContentSpan().getHtmlString());
        String html = "<table><tr><td>HTML elements partially supported by Browsr:<tr><td><table><tr><td><a href=\"a.html\">a.html</a><td>Hyperlink anchors<tr><td><a href=\"table.html\">table.html</a><td>Tables<tr><td><a href=\"tr.html\">tr.html</a><td>Table rows<tr><td><a href=\"td.html\">td.html</a><td>Table cells containing table data</table></table>";
        assertEquals(html, controller.getContentSpan().getHtmlString());

        controller.writeHtmlOfContentSpan("savetest");

        Path workingDir = Path.of("", "src/");
        Path file = workingDir.resolve("savetest.html");
        String content = Files.readString(file);
        assertEquals(html, content);
    }

    @Test
    void writeHtmlOfNullContentSpanTest() {
        controller.setContentSpan(null);
        String error = "java.lang.NullPointerException: Cannot invoke \"domain.Contentspan.getHtmlString()\" because \"this.content\" is null";
        controller.writeHtmlOfContentSpan("savetest");
        assertEquals(error, ((Text)controller.getContentSpan()).getText());
    }
}