package test.api;

import dto.*;
import dtoDirectors.RequestGetPlayerByIdDtoDirector;
import dtoDirectors.UpdatePlayerDtoDirector;
import enums.GenderType;
import enums.RoleType;
import io.restassured.path.json.JsonPath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import testUtility.PlayersUtility;
import utils.*;
import testUtility.Specifications;

import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;

public class SpribeTest extends BaseApiTest {
    Logger logger = LoggerFactory.getLogger(SpribeTest.class);
    private String pathToSchema = "src/test/java/test/api/schemas/";

    @Test
    public void testCheckAgeAllPlayers() {
        logger.info("Test to check the respective ages of all players");
        Specifications.installSpec(Specifications.requestSpecification(), Specifications.responseCodeOK200());
        List<PlayersDto> players = given()
                .when()
                .get("player/get/all")
                .then().log().all()
                .extract()
                .body()
                .jsonPath()
                .getList("players", PlayersDto.class);
        players.forEach(d -> Assert.assertTrue(d.getAge() >= 16 && d.getAge() <= 60));
    }

    @Test
    public void testGetAllPlayersCheckSchemaOK200() {
        logger.info("Checking the scheme of the response from the server to the getAllPlayers request");
        logger.info("The test fails because the return scheme does not match the documentation of the swagger");
        Specifications.installSpec(Specifications.requestSpecification(), Specifications.responseCodeOK200());
        given()
                .when()
                .get("player/get/all")
                .then().log().all()
                .assertThat()
                .body(checkSchema(pathToSchema + "getAllPlayersSchema.json"));
    }

    @Test
    public void testCheckRolePlayerByPlayerId() {
        logger.info("Checking that under id number 1 the user has the role of supervisor");
        String expectedRole = RoleType.SUPERVISOR.getStatus();;

        Specifications.installSpec(Specifications.requestSpecification(), Specifications.responseCodeOK200());
        JsonPath jsonPath = given()
                .when()
                .body(new RequestGetPlayerByIdDtoDirector().idFirstPlayer())
                .post("player/get")
                .then().log().all()
                .body("id", notNullValue())
                .body("login", notNullValue())
                .body("password", notNullValue())
                .body("screenName", notNullValue())
                .body("gender", notNullValue())
                .body("age", notNullValue())
                .body("role", notNullValue())
                .extract()
                .body()
                .jsonPath();
        String actualRole = jsonPath.get("role");
        Assert.assertEquals(actualRole, expectedRole, "The user has the wrong role");
    }

    @Test
    public void testCreatePlayer() {
        logger.info("Create a new player and check by its id that it is displayed in the list upon request getAllPlayers");
        String editor = RoleType.SUPERVISOR.getStatus();
        int age = IntegerGenerator.getValue(16, 60);
        String login = NameGenerator.getFullName();
        String screenName = NameGenerator.getNickname();
        String password = PasswordGenerator.getPassword();
        String gender = GenderType.FAMALE.getStatus();
        String role = RoleType.ADMIN.getStatus();
        Map<String, String> params = Map.of(
                "age", String.valueOf(age),
                "gender", gender,
                "login", login,
                "password", password,
                "role", role,
                "screenName", screenName);

        Specifications.installSpec(Specifications.requestSpecification(), Specifications.responseCodeOK200());
        JsonPath jsonPath = given()
                .when()
                .pathParam("editor", editor)
                .queryParams(params)
                .get("/player/create/{editor}")
                .then().log().all()
                .body("id", notNullValue())
                .body("login", notNullValue())
                .extract()
                .jsonPath();

        JsonPath listPlayers = PlayersUtility.getListPlayers();
        List<Integer> listId = listPlayers.get("players.id");
        int idCreatedPlayer = jsonPath.get("id");

        Assert.assertTrue(listId.contains(idCreatedPlayer), "Failed to create player");
    }

    @Test
    public void testNegativeCreatePlayerWithInvalidAgeForbidden403() {
        logger.info("Create new player with invalid age, expect 403 forbidden");
        logger.info("We get the code 400 this is an error, in the documentation there is no return of a 400 error.");
        String editor = RoleType.SUPERVISOR.getStatus();
        String login = NameGenerator.getFullName();
        String screenName = NameGenerator.getNickname();
        String password = PasswordGenerator.getPassword();
        String gender = GenderType.MALE.getStatus();
        String role = RoleType.USER.getStatus();
        Map<String, String> params = Map.of(
                "age", "61",
                "gender", gender,
                "login", login,
                "password", password,
                "role", role,
                "screenName", screenName);

        Specifications.installSpec(Specifications.requestSpecification(), Specifications.responseCodeForbidden403());
        given()
                .when()
                .pathParam("editor", editor)
                .queryParams(params)
                .get("/player/create/{editor}")
                .then().log().all();
        logger.info("We get the code 400 this is an error, in the documentation there is no return of a 400 error.");
    }

    @Test
    public void testNegativeCreatePlayerWithInvalidGenderForbidden403() {
        logger.info("Create new player with invalid gender expect 403 forbidden");
        logger.info("We get 200 code is a bug.");
        String editor = RoleType.SUPERVISOR.getStatus();
        int age = IntegerGenerator.getValue(16, 60);
        String login = NameGenerator.getFullName();
        String screenName = NameGenerator.getNickname();
        String password = PasswordGenerator.getPassword();
        String role = RoleType.USER.getStatus();
        Map<String, String> params = Map.of(
                "age", String.valueOf(age),
                "gender", "animal",
                "login", login,
                "password", password,
                "role", role,
                "screenName", screenName);

        Specifications.installSpec(Specifications.requestSpecification(), Specifications.responseCodeForbidden403());
        given()
                .when()
                .pathParam("editor", editor)
                .queryParams(params)
                .get("/player/create/{editor}")
                .then().log().all();
    }

    @Test
    public void testNegativeCreatePlayerWithInvalidPasswordForbidden403() {
        logger.info("Create new player with invalid gender expect 403 forbidden");
        logger.info("We get 200 code is a bug.");
        String editor = RoleType.SUPERVISOR.getStatus();
        int age = IntegerGenerator.getValue(16, 60);
        String login = NameGenerator.getFullName();
        String screenName = NameGenerator.getNickname();
        String password = "1";
        String gender = GenderType.MALE.getStatus();
        String role = RoleType.ADMIN.getStatus();
        Map<String, String> params = Map.of(
                "age", String.valueOf(age),
                "gender", gender,
                "login", login,
                "password", password,
                "role", role,
                "screenName", screenName);

        Specifications.installSpec(Specifications.requestSpecification(), Specifications.responseCodeForbidden403());
        given()
                .when()
                .pathParam("editor", editor)
                .queryParams(params)
                .get("/player/create/{editor}")
                .then().log().all();
    }

    @Test
    public void testUpdatePlayer() {
        logger.info("Update the player and compare his data before and after the update.");
        String editor = RoleType.SUPERVISOR.getStatus();
        Specifications.installSpec(Specifications.requestSpecification(), Specifications.responseCodeOK200());
        RequestGetPlayerByIdDto idDto = new RequestGetPlayerByIdDtoDirector().idLastPlayer();
        int idPlayer = idDto.getPlayerId();
        Specifications.installSpec(Specifications.requestSpecification(), Specifications.responseCodeOK200());
        JsonPath playerUntilUpdate = PlayersUtility.getPlayersByIdDto(idPlayer);
        UpdatePlayerDto updateData = new UpdatePlayerDtoDirector().updatePlayer();
        Specifications.installSpec(Specifications.requestSpecification(), Specifications.responseCodeOK200());
        JsonPath playerAfterUpdate = given()
                .when()
                .pathParam("editor", editor)
                .pathParam("id", idPlayer)
                .body(updateData)
                .patch("/player/update/{editor}/{id}")
                .then().log().all()
                .extract()
                .jsonPath();

        int ageUntilUpdate = playerUntilUpdate.get("age");
        String loginUntilUpdate = playerUntilUpdate.get("login");
        String passwordUntilUpdate = playerUntilUpdate.get("password");
        String screenNameUntilUpdate = playerUntilUpdate.get("screenName");
        int ageAfterUpdate = playerAfterUpdate.get("age");
        String loginAfterUpdate = playerAfterUpdate.get("login");
        String passwordAfterUpdate = playerAfterUpdate.get("password");
        String screenNameAfterUpdate = playerAfterUpdate.get("screenName");

        SoftAssert softAssert = new SoftAssert();
        softAssert.assertNotEquals(ageUntilUpdate, ageAfterUpdate, "Age not updated");
        softAssert.assertNotEquals(loginUntilUpdate, loginAfterUpdate, "Login not updated");
        softAssert.assertNotEquals(passwordUntilUpdate, passwordAfterUpdate, "Password not updated");
        softAssert.assertNotEquals(screenNameUntilUpdate, screenNameAfterUpdate, "Screen Name not updated");
    }

    @Test
    public void testNegativeUpdatePlayerNotValidData() {
        logger.info("Update the player not valid data.");
        logger.info("We expect to receive a 403 error, the data is updated and the code 200 comes.");
        String editor = RoleType.SUPERVISOR.getStatus();
        Specifications.installSpec(Specifications.requestSpecification(), Specifications.responseCodeOK200());
        RequestGetPlayerByIdDto idDto = new RequestGetPlayerByIdDtoDirector().idLastPlayer();
        int idPlayer = idDto.getPlayerId();
        Specifications.installSpec(Specifications.requestSpecification(), Specifications.responseCodeOK200());
        PlayersUtility.getPlayersByIdDto(idPlayer);
        UpdatePlayerDto updateData = new UpdatePlayerDtoDirector().updatePlayerNotValidData();
        Specifications.installSpec(Specifications.requestSpecification(), Specifications.responseCodeForbidden403());
        given()
                .when()
                .pathParam("editor", editor)
                .pathParam("id", idPlayer)
                .body(updateData)
                .patch("/player/update/{editor}/{id}")
                .then().log().all()
                .extract()
                .jsonPath();
    }

    @Test
    public void testDeletePlayer() {
        logger.info("Delete a player and check by his id that he is no longer listed in the getAllPlayers requests");
        String editor = RoleType.SUPERVISOR.getStatus();
        Specifications.installSpec(Specifications.requestSpecification(), Specifications.responseCodeOK200());
        RequestGetPlayerByIdDto idDto = new RequestGetPlayerByIdDtoDirector().idLastPlayer();
        int idPlayer = idDto.getPlayerId();
        Specifications.installSpec(Specifications.requestSpecification(), Specifications.responseCodeNoContent204());
        given()
                .when()
                .pathParam("editor", editor)
                .body(idDto)
                .delete("/player/delete/{editor}")
                .then().log().all()
                .extract()
                .jsonPath();

        Specifications.installSpec(Specifications.requestSpecification(), Specifications.responseCodeOK200());
        JsonPath listPlayers = PlayersUtility.getListPlayers();
        List<Integer> listId = listPlayers.get("players.id");

        Assert.assertFalse(listId.contains(idPlayer), "Failed to remove player");
    }

    @Test
    public void testNegativeDeletePlayerSupervisor() {
        logger.info("Delete a player with supervisor role");
        String editor = RoleType.SUPERVISOR.getStatus();
        Specifications.installSpec(Specifications.requestSpecification(), Specifications.responseCodeOK200());
        RequestGetPlayerByIdDto idDto = new RequestGetPlayerByIdDtoDirector().idFirstPlayer();
        Specifications.installSpec(Specifications.requestSpecification(), Specifications.responseCodeForbidden403());
        given()
                .when()
                .pathParam("editor", editor)
                .body(idDto)
                .delete("/player/delete/{editor}")
                .then().log().all();
    }
}
