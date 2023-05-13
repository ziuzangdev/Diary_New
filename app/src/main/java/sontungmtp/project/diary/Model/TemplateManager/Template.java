package sontungmtp.project.diary.Model.TemplateManager;

/**
 * This class defines a template with the fields you requested, including a background color (represented as a string), an image ID (taken from the drawable resource folder), a button color (also represented as a string), a name, and content. It also includes getter and setter methods for each of the fields, which allow you to retrieve and set the values of these fields from outside the class.
 */
public class Template {
    // Background color of the template, represented as a string in hexadecimal format
    private String backgroundColor;

    // ID of the image to be displayed, taken from the drawable resource folder
    private int imageId;

    // Color of the button, represented as a string in hexadecimal format
    private String buttonColor;

    // Name of the template
    private String name;

    // Content of the template
    private String content;

    //HTML data for this template
    private String htmlData;

    // Getters and setters for each of the fields
    public String getHtmlData() {
        return htmlData;
    }

    public void setHtmlData(String htmlData) {
        this.htmlData = htmlData;
    }

    public String getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public String getButtonColor() {
        return buttonColor;
    }

    public void setButtonColor(String buttonColor) {
        this.buttonColor = buttonColor;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Template() {
    }

    public Template(String backgroundColor, int imageId, String buttonColor, String name, String content, String htmlData) {
        this.backgroundColor = backgroundColor;
        this.imageId = imageId;
        this.buttonColor = buttonColor;
        this.name = name;
        this.content = content;
        this.htmlData = htmlData;
    }
}
