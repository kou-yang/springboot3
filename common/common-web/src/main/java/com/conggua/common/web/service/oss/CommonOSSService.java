package com.conggua.common.web.service.oss;

import cn.hutool.core.io.FileUtil;
import com.conggua.common.base.common.UserHolder;
import com.conggua.common.base.util.CollStreamUtils;
import com.conggua.common.base.util.UUIDUtils;
import com.conggua.common.web.constant.DateFormatterConstant;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.function.Function;

/**
 * @author ky
 * @description
 * @date 2024-07-08 11:02
 */
public interface CommonOSSService {

    /**
     * 生成文件夹名
     * @return 文件夹名（2024-03-03）
     */
    default String generateFolder() {
        return LocalDate.now().format(DateTimeFormatter.ofPattern(DateFormatterConstant.DEFAULT_DATE_FORMAT));
    }

    /**
     * 生成文件夹名
     * @param tenantId
     * @return 文件夹名（tenantId/2024-03-03）
     */
    default String generateFolder(String tenantId) {
        Assert.notNull(tenantId, "[Generate folder] - tenantId must not be null");
        String date = LocalDate.now().format(DateTimeFormatter.ofPattern(DateFormatterConstant.DEFAULT_DATE_FORMAT));
        return tenantId + "/" + date;
    }

    /**
     * 生成文件名
     * @param folder
     * @param originalFilename
     * @return
     */
    default String generateFileName(String folder, String originalFilename) {
        // 获取文件key组成信息
        // 文件扩展名
        String suffixName = FileUtil.extName(originalFilename);
        String format = LocalDateTime.now().format(DateTimeFormatter.ofPattern(DateFormatterConstant.DEFAULT_DATE_TIME_FORMAT));
        String[] split = format.split(" ");
        String dateStr = CollStreamUtils.join(Arrays.asList(split[0].split("-")), Function.identity(), "");
        String timeStr = CollStreamUtils.join(Arrays.asList(split[1].split(":")), Function.identity(), "");
        String userId = UserHolder.getUserId();
        String uuid8 = UUIDUtils.getUuid8();
        // 生成文件名，日期_用户ID_UUID8.后缀（20240702103625_1805422024560152578.jpg）
        String fileName = dateStr + timeStr + "_" + userId + "_" + uuid8 + "." + suffixName;
        if (StringUtils.isNotBlank(folder)) {
            fileName = folder + "/" + fileName;
        }
        return fileName;
    }

    /**
     * url转key
     * @param url
     * @return
     */
    default String url2Key(String url) {
        return url;
    }
}
