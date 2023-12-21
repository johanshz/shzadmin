package co.com.online_trainer.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ResponseSuccessEnum {
    STATUS_CODE_SUCCESS("200","Approvals consulted successfully");

    private final String code;
    private final String description;
}
