package com.conggua.common.web.model.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * @author ky
 * @description 主键id
 * @date 2024-04-22 17:33
 */
@Data
public class PrimaryKeyDTO {

    @NotBlank
    private String id;
}
