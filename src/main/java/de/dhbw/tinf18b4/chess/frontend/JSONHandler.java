package de.dhbw.tinf18b4.chess.frontend;

import org.jetbrains.annotations.NotNull;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * Handles some basic functionality for the JSON communication with the client via the websocket
 */
class JSONHandler {

    /**
     * build the template string according to the template
     * <p>
     * should be edited before sending
     *
     * @return the JSONObject template
     */
    @SuppressWarnings("unchecked")
    @NotNull
    static JSONObject buildAnswerTemplate() {
        JSONObject template = new JSONObject();

        template.put("content", "");
        template.put("value", "");

        return template;
    }

    /**
     * Parse a json string to a json object
     *
     * @param message the json string
     * @return the json object
     * @throws ParseException on invalid format
     */
    @NotNull
    static JSONObject parseMessage(@NotNull String message) throws ParseException {
        JSONParser parser = new JSONParser();
        return (JSONObject) parser.parse(message);
    }
}
