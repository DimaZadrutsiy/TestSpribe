package enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RoleType {
    SUPERVISOR("supervisor"),
    ADMIN("admin"),
    USER("user");

    private String status;
}
