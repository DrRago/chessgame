package de.dhbw.tinf18b4.chess.frontend.JSON;

import org.jetbrains.annotations.NotNull;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * Handles some basic functionality for the JSON communication with the client via the websocket
 */
public class JSONHandler {

    /**
     * build the template string according to the template
     * <p>
     * should be edited before sending
     *
     * @return the JSONObject template
     */
    public static JSONObject buildAnswerTemplate() {
        return buildAnswerTemplate("", "");
    }

    /**
     * build the template string according to the template
     *
     * @param content the content field of the json object
     * @param value   the value field of the json object
     * @return the JSONObject template
     */
    public static JSONObject buildAnswerTemplate(@NotNull String content, @NotNull String value) {
        JSONObject template = new JSONObject();

        template.put("content", content);
        template.put("value", value);

        return template;
    }

    /**
     * Parse a json string to a json object
     *
     * @param message the json string
     * @return the json object
     * @throws ParseException on invalid format
     */
    public static JSONObject parseMessage(@NotNull String message) throws ParseException {
        JSONParser parser = new JSONParser();
        return (JSONObject) parser.parse(message);
    }
}
