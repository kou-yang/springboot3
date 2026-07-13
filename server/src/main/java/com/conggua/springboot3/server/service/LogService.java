package com.conggua.springboot3.server.service;

import com.conggua.common.web.model.response.CommonPage;
import com.conggua.common.web.service.BaseService;
import com.conggua.springboot3.server.model.dto.LogPageDTO;
import com.conggua.springboot3.server.model.dto.LogSaveDTO;
import com.conggua.springboot3.server.model.dto.LogUpdateDTO;
import com.conggua.springboot3.server.model.entity.Log;
import com.conggua.springboot3.server.model.vo.LogDetailVO;
import com.conggua.springboot3.server.model.vo.LogPageVO;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author kouyang
 * @since 2026-07-10
 */
public interface LogService extends BaseService<Log> {

    /**
     * 保存
     * @param dto
     */
    Log save(LogSaveDTO dto);

    /**
     * 删除
     * @param id
     */
    void remove(String id);

    /**
     * 修改
     * @param dto
     */
    Log update(LogUpdateDTO dto);

    /**
     * 分页查询
     * @param dto
     * @return
     */
    CommonPage<LogPageVO> page(LogPageDTO dto);

    /**
     * 根据id查询详情
     * @param id
     * @return
     */
    LogDetailVO getDetail(String id);

    /**
     * 下载模板
     * @return
     */
    ResponseEntity<Resource> downloadTemplate();

    /**
     * 导入
     * @param file
     */
    void im(MultipartFile file);
}
