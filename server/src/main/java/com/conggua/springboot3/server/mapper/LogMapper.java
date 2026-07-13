package com.conggua.springboot3.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.conggua.springboot3.server.model.entity.Log;

import java.time.LocalDateTime;

/**
 * @author kouyang
 * @since 2026-07-10
 */
public interface LogMapper extends BaseMapper<Log> {

    /**
     * 删除过期日志
     * @param thresholdTime
     * @return
     */
    int deleteByExpiredTime(LocalDateTime thresholdTime);
}
