package testUtility;

import dto.PlayersDto;
import dtoDirectors.RequestGetPlayerByIdDtoDirector;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;

import java.util.List;

public class PlayersUtility {

    public static JsonPath getListPlayers() {
        return RestAssured.given()
                .when()
                .get("player/get/all")
                .then().log().all()
                .extract()
                .body()
                .jsonPath();
    }

    public static List<PlayersDto> getListPlayersDto() {
        return RestAssured.given()
                .when()
                .get("player/get/all")
                .then().log().all()
                .extract()
                .body()
                .jsonPath()
                .getList("players", PlayersDto.class);
    }

    public static JsonPath getPlayersByIdDto(int id) {
        return RestAssured.given()
                .when()
                .body(new RequestGetPlayerByIdDtoDirector().idPlayer(id))
                .post("player/get")
                .then().log().all()
                .extract()
                .body()
                .jsonPath();
    }
}
