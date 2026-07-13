package com.conggua.springboot3.server.service.impl;

import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.conggua.common.base.util.CRUDUtil;
import com.conggua.common.base.util.CollStreamUtils;
import com.conggua.common.web.model.response.CommonPage;
import com.conggua.springboot3.server.listener.im.LogImportListener;
import com.conggua.springboot3.server.mapper.LogMapper;
import com.conggua.springboot3.server.model.dto.LogPageDTO;
import com.conggua.springboot3.server.model.dto.LogSaveDTO;
import com.conggua.springboot3.server.model.dto.LogUpdateDTO;
import com.conggua.springboot3.server.model.dto.im.LogImportDTO;
import com.conggua.springboot3.server.model.entity.Log;
import com.conggua.springboot3.server.model.vo.LogDetailVO;
import com.conggua.springboot3.server.model.vo.LogPageVO;
import com.conggua.springboot3.server.service.LogService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.fesod.sheet.FesodSheet;
import org.springframework.beans.BeanUtils;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;

/**
 * @author kouyang
 * @since 2026-07-10
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class LogServiceImpl extends ServiceImpl<LogMapper, Log> implements LogService {

    @Override
    public Log save(LogSaveDTO dto) {
        Log entity = new Log();
        BeanUtils.copyProperties(dto, entity);
        this.save(entity);
        return entity;
    }

    @Override
    public void remove(String id) {
        boolean success = this.removeById(id);
        CRUDUtil.validateDeleteSuccess(success);
    }

    @Override
    public Log update(LogUpdateDTO dto) {
        Log entity = new Log();
        BeanUtils.copyProperties(dto, entity);
        this.updateById(entity);
        return entity;
    }

    @Override
    public CommonPage<LogPageVO> page(LogPageDTO dto) {
        Page<Log> page = dto.startMpPage(Log.class);
        LambdaQueryChainWrapper<Log> wrapper = lambdaQuery();
        page = wrapper.page(page);
        // entity转vo
        List<LogPageVO> voList = this.entityList2PageVOList(page.getRecords());
        return CommonPage.restPage(voList, page.getTotal());
    }

    @Override
    public LogDetailVO getDetail(String id) {
        Log entity = this.getById(id);
        return this.entity2DetailVO(entity);
    }

    public List<LogPageVO> entityList2PageVOList(List<Log> entityList) {
        if (CollectionUtils.isEmpty(entityList)) {
            return Collections.emptyList();
        }
        return CollStreamUtils.toList(entityList, entity -> {
            LogPageVO vo = new LogPageVO();
            BeanUtils.copyProperties(entity, vo);
            return vo;
        });
    }

    public LogDetailVO entity2DetailVO(Log entity) {
        if (entity == null) {
            return null;
        }
        LogDetailVO vo = new LogDetailVO();
        BeanUtils.copyProperties(entity, vo);
        return vo;
    }

    @Override
    public ResponseEntity<Resource> downloadTemplate() {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("template/import/导入模版.xlsx");
        Assert.notNull(inputStream, "File not found");
        ContentDisposition contentDisposition = ContentDisposition.builder("attachment")
                .filename("导入模版.xlsx", StandardCharsets.UTF_8)
                .build();
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition.toString())
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(new InputStreamResource(inputStream));
    }

    @SneakyThrows
    @Override
    public void im(MultipartFile file) {
        FesodSheet.read(file.getInputStream(), LogImportDTO.class, new LogImportListener())
                .sheet()
                .doRead();
    }
}