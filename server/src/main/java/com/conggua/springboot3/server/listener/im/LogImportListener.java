package com.conggua.springboot3.server.listener.im;

import com.conggua.common.base.exception.BusinessException;
import com.conggua.common.base.exception.CommonErrorEnum;
import com.conggua.springboot3.server.model.dto.im.LogImportDTO;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.fesod.sheet.context.AnalysisContext;
import org.apache.fesod.sheet.event.AnalysisEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * @author kouyang
 * @since 2026-07-10
 */
@Slf4j
@NoArgsConstructor
public class LogImportListener extends AnalysisEventListener<LogImportDTO>{

    private final List<String> errors = new ArrayList<>();
    private final List<LogImportDTO> list = new ArrayList<>();

    @Override
    public void invoke(LogImportDTO dto, AnalysisContext analysisContext) {
        list.add(dto);
    }

    @SneakyThrows
    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        if (CollectionUtils.isNotEmpty(errors)) {
            throw new BusinessException(CommonErrorEnum.PARAMS_ERROR, "导入失败：" + String.join(";", errors));
        }
    }
}