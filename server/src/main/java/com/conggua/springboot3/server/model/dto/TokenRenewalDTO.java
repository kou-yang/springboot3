package com.conggua.springboot3.server.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * @author ky
 * @description
 * @date 2024-05-07 13:55
 */
@Data
public class TokenRenewalDTO {

    @NotBlank
    private String refreshToken;
}
