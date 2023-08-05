package dtoDirectors;

import dto.UpdatePlayerDto;
import utils.IntegerGenerator;
import utils.NameGenerator;
import utils.PasswordGenerator;

import static dto.UpdatePlayerDto.*;

public class UpdatePlayerDtoDirector {

    public UpdatePlayerDto updatePlayer() {
        int age = IntegerGenerator.getValue(16, 60);
        String login = NameGenerator.getFullName();
        String screenName = NameGenerator.getNickname();
        String password = PasswordGenerator.getPassword();

        return builder()
                .age(age)
                .gender("male")
                .login(login)
                .password(password)
                .screenName(screenName)
                .role("admin")
                .build();
    }

    public UpdatePlayerDto updatePlayerNotValidData() {
        int age = 120;
        String login = NameGenerator.getFullName();
        String screenName = NameGenerator.getNickname();
        String password = "3c";

        return builder()
                .age(age)
                .gender("zero")
                .login(login)
                .password(password)
                .screenName(screenName)
                .role("supervisor")
                .build();
    }
}
