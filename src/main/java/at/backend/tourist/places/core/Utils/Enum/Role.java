package at.backend.tourist.places.core.Utils.Enum;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Enum representing user roles in the system")
public enum Role {
    @Schema(description = "Administrator role with full access")
    ADMIN,

    @Schema(description = "Editor role with permission to modify content")
    EDITOR,

    @Schema(description = "Viewer role with read-only access")
    VIEWER
}
