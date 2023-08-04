package dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class RequestGetPlayerByIdDto {
    private Integer playerId;
}
