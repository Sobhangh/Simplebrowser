package domain;

public class TextInputField extends Input{
    /**
     * Name attribute of Text Input Field
     */
    private String name;

    /**
     * Constructor
     * @param name
     */
    public TextInputField(String name){
        setName(name);
    }

    /**
     * Getter for name attribute of Text Input Field
     * @return name attribute of Text Input Field
     */
    public String getName(){
        return this.name;
    }

    /**
     * Setter for name attribute of Text Input Field
     * @param name
     * @throws IllegalArgumentException if name is null
     */
    public void setName(String name){
        if(name == null){
            throw new IllegalArgumentException("Name cannot be null.");
        }
        this.name = name;
    }

    @Override
    public String getHtmlString(){
        return "<input type=\"text\" name=\"" + this.name + "\">";
    }
}
