package at.backend.tourist.places.core.SwaggerHelper;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ExampleObject {
    private String name;
    private String description;

    public ExampleObject(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public static ExampleObject of(String name, String description) {
        return new ExampleObject(name, description);
    }

}
