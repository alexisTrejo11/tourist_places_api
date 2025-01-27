package at.backend.tourist.places.core.SwaggerHelper;


public class ApiResponseExamples {
    public static final String USER_FOUND = """
        {
          "success": true,
          "data": {
            "id": 1,
            "name": "John Doe",
            "email": "john@example.com",
            "role": "USER"
          },
          "message": "User data successfully Fetched",
          "status_code": 200
        }
    """;

    public static final String USER_NOT_FOUND = """
        {
          "success": false,
          "data": null,
          "message": "User not Found",
          "status_code": 404
        }
    """;

    public static final String UNAUTHORIZED_ACCESS = """
        {
          "success": false,
          "data": null,
          "message": "Unauthorized access",
          "status_code": 401
        }
    """;
}
