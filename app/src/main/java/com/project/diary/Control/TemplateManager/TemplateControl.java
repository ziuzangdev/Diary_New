package com.project.diary.Control.TemplateManager;
import com.project.diary.Model.TemplateManager.Template;
import com.project.diary.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A final class that manages a collection of default templates as static fields and methods.
 *
 * Default templates can be added to the collection using the {@link #addTemplate(String, Template)} method.
 */
public final class TemplateControl {
    private static Map<String, Template> defaultTemplates;

    /**
     * Initializes the static map of default templates with some default templates.<br>
     * Template data:<br>
     * The first parameter is the name of the template, which is used as the key in the map.<br>
     * The second parameter is an instance of the Template class, which includes the following fields:<br>
     * - Background color: "#FFFFFF" (white)<br>
     * - Image ID: R.drawable.default_image (taken from the drawable resource folder)<br>
     * - Button color: "#000000" (black)<br>
     * - Name: "Default"<br>
     * - Content: "This is the default template."<br>
     * - Html Data: Data of this Template
     */
    static {
        defaultTemplates = new HashMap<>();
        defaultTemplates.put("Quick Start Guide", new Template(
                "#EBF6FB",
                R.drawable.template_diary_img,
                "#3EBDC6",
                "Quick Start Guide",
                "Follow the instructions to write your date easily",
                "How is the weather today?<br><br><br><br><br>Where did you go today? Meet who? Anything interesting?<br><br><br><br><br>How are you feeling today? Is there a summary?<br><br>"
        ));
    }

    /**
     * Private constructor to prevent instantiation of this class.
     */
    private TemplateControl() {}

    /**
     * Adds a default template to the collection.
     *
     * @param name the name of the template to add
     * @param template the template to add
     */
    public static void addTemplate(String name, Template template) {
        defaultTemplates.put(name, template);
    }

    /**
     * Retrieves a default template by its name.
     *
     * @param name the name of the template to retrieve
     * @return the default template with the specified name, or null if no such template exists
     */
    public static Template getTemplate(String name) {
        return defaultTemplates.get(name);
    }

    /**
     * Returns a list of the names of the default templates in the collection.
     *
     * @return a list of the names of the default templates
     */
    public static ArrayList<String> getTemplateNames() {
        return new ArrayList<>(defaultTemplates.keySet());
    }

    /**
     * Returns the size of the default templates collection.
     *
     * @return the size of the default templates collection
     */
    public static int getTemplateCount() {
        return defaultTemplates.size();
    }

    /**
     * Returns the default template at the specified position in the collection.
     *
     * @param index the position of the template to retrieve
     * @return the default template at the specified position, or null if the index is out of bounds
     */
    public static Template getTemplateAt(int index) {
        List<String> templateNames = new ArrayList<>(defaultTemplates.keySet());
        if (index < 0 || index >= templateNames.size()) {
            return null;
        }
        return defaultTemplates.get(templateNames.get(index));
    }
}
