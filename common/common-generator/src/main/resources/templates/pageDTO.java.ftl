package ${pageDTOPackage};

import com.conggua.common.web.model.request.CommonPageDTO;
import lombok.*;

/**
 * @author ${author}
 * @since ${date}
 */
@Builder
@Data
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class ${entity}PageDTO extends CommonPageDTO {
}