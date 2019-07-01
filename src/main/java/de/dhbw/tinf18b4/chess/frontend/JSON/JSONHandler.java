package de.dhbw.tinf18b4.chess.frontend.JSON;

import org.json.simple.JSONObject;

/**
 * @author Leonhard Gahr
 */
public class JSONHandler {

    /**
     * build the template string according to:
     * <pre>
     * <code>
     * {
     * 'status': {
     * 'code': 200,
     * 'message': 'OK'
     * }
     * }
     * </code>
     * </pre>
     * The template should be edited before sending
     *
     * @return the JSONObject template
     */
    public static JSONObject buildAnswerTemplate() {
        JSONObject template = new JSONObject();

        JSONObject status = new JSONObject();
        status.put("code", 200);
        status.put("message", "OK");

        template.put("status", status);

        return template;
    }
}
