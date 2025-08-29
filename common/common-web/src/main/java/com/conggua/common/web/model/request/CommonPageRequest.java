package com.conggua.common.web.model.request;

import lombok.Data;

/**
 * @author: kouyang
 * @description:
 * @date: 2025-02-27 17:14
 */
@Data
public class CommonPageRequest {

    private Integer pageIndex = 1;
    private Integer pageSize = 10;
}
