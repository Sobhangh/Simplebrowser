package domain;

public class Form extends Contentspan {
    /**
     * Action of the form
     */
    private String action;

    private Contentspan content;

    /**
     * Constructor for Form class
     * @param action
     * @param content
     */
    public Form(String action, Contentspan content){
        setAction(action);
        setContent(content);
    }

    /**
     * Getter for action of form
     * @return action attribute of form
     */
    public String getAction(){
        return this.action;
    }

    /**
     * Getter for content of form
     * @return content of form
     */
    public Contentspan getContent(){
        return this.content;
    }

    /**
     * Setter for action of form
     * @param action
     * @throws IllegalArgumentException if given action is null
     */
    public void setAction(String action){
        if(action == null){
            throw new IllegalArgumentException("Action cannot be null.");
        }
        this.action = action;
    }

    /**
     * Setter for content of form
     * @param content
     * @throws  IllegalArgumentException if given content is null
     */
    public void setContent(Contentspan content){
        if(content == null){
            throw new IllegalArgumentException("Content cannot be null");
        }
        this.content = content;
    }

    @Override
    public String getHtmlString(){
        return "<form action=\"" + this.action + "\">" + this.content.getHtmlString() + "</form>";
    }

}
