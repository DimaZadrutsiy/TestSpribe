package dto;

import lombok.*;

@Getter
@Setter
@Builder
public class UpdatePlayerDto {
    private Integer age;
    private String gender;
    private String login;
    private String password;
    private String role;
    private String screenName;
}
