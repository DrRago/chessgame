package de.dhbw.tinf18b4.chess.frontend.JSON;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * @author Leonhard Gahr
 */
public class JSONHandler {

    /**
     * build the template string according to
     * The template should be edited before sending
     *
     * @return the JSONObject template
     */
    public static JSONObject buildAnswerTemplate() {
        JSONObject template = new JSONObject();

        template.put("content", "");
        template.put("value", "");

        return template;
    }

    public static JSONObject buildAnswerTemplate(String content, String value) {
        JSONObject template = new JSONObject();

        template.put("content", content);
        template.put("value", value);

        return template;
    }

    public static JSONObject parseMessage(String message) throws ParseException {
        JSONParser parser = new JSONParser();
        return (JSONObject) parser.parse(message);
    }
}
