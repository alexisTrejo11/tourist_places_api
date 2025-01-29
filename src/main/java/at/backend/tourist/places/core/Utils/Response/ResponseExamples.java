package at.backend.tourist.places.core.Utils.Response;

import at.backend.tourist.places.core.SwaggerHelper.ExampleObject;
import at.backend.tourist.places.modules.User.DTOs.UserDTO;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class ResponseExamples {
    public static final String SUCCESS_EXAMPLE = """
        {
          "success": true,
          "data": %s,
          "message": "%s successfully Fetched",
          "status_code": 200
        }
        """;

    public static final String NOT_FOUND_EXAMPLE = """
        {
          "success": false,
          "data": null,
          "message": "%s not Found",
          "status_code": 404
        }
        """;


    public static Map<String, ExampleObject> getExamples(Class<?> dataType, String entity) {
        Map<String, ExampleObject> examples = new HashMap<>();

        // Add standard examples based on dataType
        examples.put("200", ExampleObject.of("Success Response",
                String.format(SUCCESS_EXAMPLE, getDataExample(dataType), entity)));
        examples.put("404", ExampleObject.of("Not Found",
                String.format(NOT_FOUND_EXAMPLE, entity)));

        return examples;
    }

    private static String getDataExample(Class<?> dataType) {
        if (dataType == UserDTO.class) {
            return """
                {
                  "id": 1,
                  "name": "John Doe",
                  "email": "john@example.com"
                }
                """;
        }
        return "null";
    }
}