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
          "message": "User data successfully fetched",
          "status_code": 200
        }
    """;

    public static final String SUCCESS = """
        {
          "success": true,
          "data":  null,
          "message": "Success Message",
          "status_code": 200
        }
    """;

    public static final String NOT_FOUND = """
        {
          "success": false,
          "data": null,
          "message": "Not found Message",
          "status_code": 404
        }
    """;

    public static final String CREATED = """
        {
          "success": false,
          "data": {
         \s
          },
          "message": "Created Message",
          "status_code": 201
        }
   \s""";

    public static final String UNAUTHORIZED_ACCESS = """
        {
          "success": false,
          "data": null,
          "message": "Unauthorized access",
          "status_code": 401
        }
    """;

    public static final String BAD_REQUEST = """
        {
          "success": false,
          "data": null,
          "message": "Bad request, invalid parameters",
          "status_code": 400
        }
    """;

    public static final String INTERNAL_SERVER_ERROR = """
        {
          "success": false,
          "data": null,
          "message": "Internal server error, please try again later",
          "status_code": 500
        }
    """;

    public static final String FORBIDDEN = """
        {
          "success": false,
          "data": null,
          "message": "Access forbidden, insufficient permissions",
          "status_code": 403
        }
    """;

    public static final String CONFLICT = """
        {
          "success": false,
          "data": null,
          "message": "Conflict, resource already exists",
          "status_code": 409
        }
    """;

    public static final String NO_CONTENT = """
        {
          "success": true,
          "data": null,
          "message": "No content to return",
          "status_code": 204
        }
    """;

    public static final String ACCEPTED = """
        {
          "success": true,
          "data": null,
          "message": "Request accepted but not yet processed",
          "status_code": 202
        }
    """;

    public static final String NOT_MODIFIED = """
        {
          "success": true,
          "data": null,
          "message": "The resource has not been modified",
          "status_code": 304
        }
    """;
}
