package com.conggua.common.web.service.oss;

import com.conggua.common.web.model.vo.PresignedUrlVo;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author ky
 * @description
 * @date 2024-07-08 11:17
 */
public interface COSService extends CommonOSSService {

    /**
     * 简单上传
     * @param file
     * @param folder xxx xxx/xxx
     * @return
     */
    String simpleUpload(MultipartFile file, String folder);

    /**
     * 简单上传
     * @param file
     * @param folder xxx xxx/xxx
     * @return
     */
    String simpleUpload(String tenantId, MultipartFile file, String folder);

    /**
     * 删除对象
     * @param urlList
     */
    void deleteObject(List<String> urlList);

    /**
     * 生成带过期时间访问的URL
     * @param keyList
     * @return
     */
    List<PresignedUrlVo> generatePresignedUrl(List<String> keyList);
}
