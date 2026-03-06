package ${importListenerPackage};

import ${package.Parent}.model.dto.im.${entity}ImportDTO;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.fesod.sheet.context.AnalysisContext;
import org.apache.fesod.sheet.event.AnalysisEventListener;
import org.apache.commons.collections4.CollectionUtils;
import com.conggua.common.base.exception.BusinessException;
import com.conggua.common.base.exception.CommonErrorEnum;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ${author}
 * @since ${date}
 */
@Slf4j
@NoArgsConstructor
public class ${entity}ImportListener extends AnalysisEventListener<${entity}ImportDTO>{

    private final List<String> errors = new ArrayList<>();
    private final List<${entity}ImportDTO> list = new ArrayList<>();

    @Override
    public void invoke(${entity}ImportDTO dto, AnalysisContext analysisContext) {
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