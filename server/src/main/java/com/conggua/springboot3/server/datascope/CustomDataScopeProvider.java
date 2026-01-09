package com.conggua.springboot3.server.datascope;

import com.conggua.common.web.provider.DataScopeProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author: kouyang
 * @description:
 * @date: 2025-12-03 15:29
 */
@Component
@RequiredArgsConstructor
public class CustomDataScopeProvider implements DataScopeProvider {

    @Override
    public List<String> getPermissions() {
        return List.of("self");
    }

    @Override
    public String getCurrentUserId() {
        return "1";
    }
}
