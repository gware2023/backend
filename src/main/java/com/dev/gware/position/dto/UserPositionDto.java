package com.dev.gware.position.dto;

import com.dev.gware.position.domain.UserPosition;
import lombok.Data;

@Data
public class UserPositionDto {
    private Long positionId;
    private String positionName;
    private String positionDescription;

    public UserPosition toEntity() {
        UserPosition userPosition = new UserPosition();
        userPosition.setPositionId(this.positionId);
        userPosition.setPositionName(this.positionName);
        userPosition.setPositionDescription(this.positionDescription);

        return userPosition;
    }

    public static UserPositionDto fromEntity(UserPosition userPosition) {
        UserPositionDto userPositionDTO = new UserPositionDto();
        userPositionDTO.setPositionId(userPosition.getPositionId());
        userPositionDTO.setPositionName(userPosition.getPositionName());
        userPositionDTO.setPositionDescription(userPosition.getPositionDescription());
        // Set other fields...
        return userPositionDTO;
    }
}
