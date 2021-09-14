package ui.util;

import domain.*;
import ui.component.*;
import ui.component.Component;
import ui.component.TextComponent;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ToComponentUtil {

    private ToComponentUtil(){}

    /**
     * Method to convert a contentspan to the appropriate component with a given x and y coordinate
     *
     * @param x
     * @param y
     * @param contentSpan
     *
     * @return
     */
    // TODO: DESIGN PATTERN?
    public static Component toComponent(int x, int y, Contentspan contentSpan, FontMetrics metrics){
        if(contentSpan instanceof Link){
            String href = ((Link)contentSpan).getHref();
            return new LinkComponent(x, y, metrics.stringWidth(href), metrics.getHeight(), href);
        }

        else if(contentSpan instanceof Text){
            String text = ((Text)contentSpan).getText();
            return new TextComponent(x, y, metrics.stringWidth(text), metrics.getHeight(), text);
        }

        else if(contentSpan instanceof Table){
            List<TableRowComponent> trs = new ArrayList<>();
            int currentY = y;

            if(((Table)contentSpan).getRows().size() != 0){
                // compose all the rows of the table
                // starting from the y coordinate -> always adds the height of the current row = y coordinate of next row
                for(TableRow row: ((Table)contentSpan).getRows()){
                    int currentX = x;
                    List<Component> components = new ArrayList<>();

                    // compose all the cells of the row
                    // starting from the x coordinate -> always add the width of the cell = x coordinate of next row
                    for(Contentspan e: row.getElements()){
                        Component ec = toComponent(currentX, currentY, e, metrics);
                        currentX += ec.getWidth();
                        components.add(ec);
                    }

                    TableRowComponent tr = new TableRowComponent(x, currentY, components);
                    trs.add(tr);
                    currentY += tr.getHeight();
                }
            }

            return new TableComponent(x, y, trs);
        }
        else if(contentSpan instanceof Submit){
            String buttonText = "Submit";
            return new ButtonComponent(buttonText, x, y, metrics.stringWidth(buttonText), metrics.getHeight());
        }
        else if(contentSpan instanceof TextInputField){
            return new TextInputFieldComponent(x, y, 30, metrics, ((TextInputField)contentSpan).getName());
        }
        else if(contentSpan instanceof Form){
            Component content = toComponent(x, y, ((Form)contentSpan).getContent(), metrics);
            return new FormComponent(content, ((Form) contentSpan).getAction(),
                    x, y, content.getWidth(), content.getHeight());
        }
        else {
            throw new IllegalArgumentException("Contentspan cannot be converted to component");
        }
    }
}