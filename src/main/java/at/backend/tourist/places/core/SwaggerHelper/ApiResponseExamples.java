package at.backend.tourist.places.core.SwaggerHelper;

public class ApiResponseExamples {

    public static final String USER = """
    {
        "success": true,
        "data": {
            "id": 1,
            "name": "John Doe",
            "email": "john@example.com",
            "role": "USER"
        },
        "message": "User retrieved",
        "status_code": 200
    }
    """;

    public static final String USERS = """
    {
        "success": true,
        "data": [
            {
                "id": 1,
                "name": "John Doe",
                "email": "john@example.com",
                "role": "USER"
            },
            {
                "id": 2,
                "name": "Admin User",
                "email": "admin@example.com",
                "role": "ADMIN"
            }
        ],
        "message": "Users retrieved",
        "status_code": 200
    }
    """;

    public static final String USER_CREATED = """
    {
        "success": true,
        "data": {
            "id": 3,
            "name": "New User",
            "email": "new@example.com",
            "role": "USER"
        },
        "message": "User created",
        "status_code": 201
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

    public static final String ACTIVITY_CREATED = """
        {
          "success": true,
          "data": {
            "id": 1,
            "name": "City Tour",
            "description": "A guided tour through the city's landmarks.",
            "price": 49.99,
            "duration": "2h",
            "tourist_place_id": 101
          },
          "message": "Activity successfully created",
          "status_code": 201
        }
    """;

    public static final String ACTIVITY = """
        {
          "success": true,
          "data": {
            "id": 1,
            "name": "City Tour",
            "description": "A guided tour through the city's landmarks.",
            "price": 49.99,
            "duration": "2h",
            "tourist_place_id": 101
          },
          "message": "Activity successfully fetched",
          "status_code": 201
        }
    """;

    public static final String COUNTRY = """
    {
      "success": true,
      "data": {
        "id": 1,
        "name": "Japan",
        "capital": "Tokyo",
        "currency": "Yen",
        "language": "Japanese",
        "population": 125800000,
        "area": 377975.0,
        "continent": "ASIA",
        "flag_image": "https://example.com/flags/japan.png"
      },
      "message": "Country successfully fetched",
      "status_code": 200
    }
    """;

    public static final String COUNTRY_CREATED = """
    {
      "success": true,
      "data": {
        "id": 1,
        "name": "Japan",
        "capital": "Tokyo",
        "currency": "Yen",
        "language": "Japanese",
        "population": 125800000,
        "area": 377975.0,
        "continent": "ASIA",
        "flag_image": "https://example.com/flags/japan.png"
      },
      "message": "Country successfully fetched",
      "status_code": 200
    }
    """;

    public static final String COUNTRIES = """
    {
      "success": true,
      "data": [
        {
          "id": 1,
          "name": "Japan",
          "capital": "Tokyo",
          "currency": "Yen",
          "language": "Japanese",
          "population": 125800000,
          "area": 377975.0,
          "continent": "ASIA",
          "flag_image": "https://example.com/flags/japan.png"
        },
        {
          "id": 2,
          "name": "Germany",
          "capital": "Berlin",
          "currency": "Euro",
          "language": "German",
          "population": 83200000,
          "area": 357022.0,
          "continent": "EUROPE",
          "flag_image": "https://example.com/flags/germany.png"
        }
      ],
      "message": "Countries successfully fetched",
      "status_code": 200
    }
    """;

    public static final String PLACE_LIST = """
    {
      "success": true,
      "data": {
        "id": 1,
        "user_id": 101,
        "name": "Favorite Destinations",
        "places": [
          {
            "id": 1,
            "name": "Eiffel Tower",
            "description": "Iconic tower in Paris, France.",
            "location": "Paris",
            "rating": 4.8
          },
          {
            "id": 2,
            "name": "Machu Picchu",
            "description": "Ancient Incan city in Peru.",
            "location": "Cusco",
            "rating": 4.9
          }
        ]
      },
      "message": "Place list successfully fetched",
      "status_code": 200
    }
    """;

    public static final String PLACE_LISTS = """
    {
      "success": true,
      "data": [
        {
          "id": 1,
          "user_id": 101,
          "name": "Favorite Destinations",
          "places": [
            {
              "id": 1,
              "name": "Eiffel Tower",
              "description": "Iconic tower in Paris, France.",
              "location": "Paris",
              "rating": 4.8
            },
            {
              "id": 2,
              "name": "Machu Picchu",
              "description": "Ancient Incan city in Peru.",
              "location": "Cusco",
              "rating": 4.9
            }
          ]
        },
        {
          "id": 2,
          "user_id": 102,
          "name": "Dream Vacations",
          "places": [
            {
              "id": 3,
              "name": "Grand Canyon",
              "description": "Massive canyon in Arizona, USA.",
              "location": "Arizona",
              "rating": 4.7
            },
            {
              "id": 4,
              "name": "Santorini",
              "description": "Beautiful island in Greece.",
              "location": "Greece",
              "rating": 4.9
            }
          ]
        }
      ],
      "message": "Place lists successfully fetched",
      "status_code": 200
    }
    """;

    public static final String REVIEW = """
    {
      "success": true,
      "data": {
        "id": 1,
        "rating": 4,
        "comment": "Great place to visit!",
        "author_id": "John Doe",
        "place_id": 101
      },
      "message": "Review successfully fetched",
      "status_code": 200
    }
    """;

    public static final String REVIEW_CREATED = """
    {
      "success": true,
      "data": {
        "id": 1,
        "rating": 4,
        "comment": "Great place to visit!",
        "author_id": "John Doe",
        "place_id": 101
      },
      "message": "Review successfully created",
      "status_code": 201
    }
    """;


    public static final String REVIEWS = """
    {
      "success": true,
      "data": [
        {
          "id": 1,
          "rating": 4,
          "comment": "Great place to visit!",
          "author_id": "John Doe",
          "place_id": 101
        },
        {
          "id": 2,
          "rating": 5,
          "comment": "Absolutely amazing experience!",
          "author_id": "Jane Smith",
          "place_id": 102
        }
      ],
      "message": "Reviews successfully fetched",
      "status_code": 200
    }
    """;

    public static final String SIGNUP_SUCCESS = """
    {
        "success": true,
        "message": "An Email will be sent to the email provided. Use that token to activate your account.",
        "status_code": 201
    }
    """;

    public static final String LOGIN_SUCCESS = """
    {
        "success": true,
        "data": {
            "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
            "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
        },
        "message": "User logged in successfully",
        "status_code": 200
    }
    """;

    public static final String LOGIN_FAILURE = """
    {
        "success": false,
        "message": "Invalid credentials",
        "status_code": 401
    }
    """;

}
