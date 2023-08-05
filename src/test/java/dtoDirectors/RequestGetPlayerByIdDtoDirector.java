package dtoDirectors;

import dto.RequestGetPlayerByIdDto;
import io.restassured.path.json.JsonPath;
import testUtility.PlayersUtility;

import java.util.List;

import static dto.RequestGetPlayerByIdDto.*;

public class RequestGetPlayerByIdDtoDirector {

    public RequestGetPlayerByIdDto idFirstPlayer() {
        JsonPath listPlayers = PlayersUtility.getListPlayers();
        List<Integer> listId = listPlayers.get("players.id");
        int firstIdPlayer = listId.get(0);
         return builder()
                 .playerId(firstIdPlayer)
                 .build();
    }

    public RequestGetPlayerByIdDto idLastPlayer() {
        JsonPath listPlayers = PlayersUtility.getListPlayers();
        List<Integer> listId = listPlayers.get("players.id");
        int lastIdPlayer = listId.get(listId.size()-1);
        return builder()
                .playerId(lastIdPlayer)
                .build();
    }

    public RequestGetPlayerByIdDto idPlayer(int id) {
        return builder()
                .playerId(id)
                .build();
    }
}
