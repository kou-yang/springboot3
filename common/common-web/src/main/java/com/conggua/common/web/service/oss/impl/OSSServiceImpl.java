package com.conggua.common.web.service.oss.impl;

import com.conggua.common.base.util.CollStreamUtils;
import com.conggua.common.oss.oss.autoconfigure.OSSProperties;
import com.conggua.common.oss.oss.core.OSSTemplate;
import com.conggua.common.web.model.vo.PresignedUrlVo;
import com.conggua.common.web.service.oss.OSSService;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;

import java.net.URL;
import java.util.List;

/**
 * @author ky
 * @description
 * @date 2024-07-02 10:07
 */
@Service
public class OSSServiceImpl implements OSSService {

    @Autowired(required = false)
    private OSSTemplate ossTemplate;

    @SneakyThrows
    @Override
    public String simpleUpload(MultipartFile file, String folder) {
        OSSProperties ossProperties = ossTemplate.getOssProperties();
        String originalFilename = file.getOriginalFilename();
        // 生成存储对象key
        Assert.notNull(originalFilename, "FileName must not be null");
        if (StringUtils.isBlank(folder)) {
            folder = this.generateFolder();
        }
        String fileName = this.generateFileName(folder, originalFilename);
        // 存储对象
        ossTemplate.putObject(ossProperties.getBucketName(), fileName, file.getInputStream());
        return fileName;
    }

    @SneakyThrows
    @Override
    public String simpleUpload(String tenantId, MultipartFile file, String folder) {
        OSSProperties ossProperties = ossTemplate.getOssProperties();
        String originalFilename = file.getOriginalFilename();
        // 生成存储对象key
        Assert.notNull(originalFilename, "FileName must not be null");
        Assert.notNull(tenantId, "tenantId must not be null");
        if (StringUtils.isBlank(folder)) {
            folder = this.generateFolder(tenantId);
        }
        String fileName = this.generateFileName(folder, originalFilename);
        // 存储对象
        ossTemplate.putObject(ossProperties.getBucketName(), fileName, file.getInputStream());
        return fileName;
    }

    @Override
    public void deleteObject(List<String> urlList) {
        urlList.forEach(ossTemplate::deleteObject);
    }

    @Override
    public List<PresignedUrlVo> generatePresignedUrl(List<String> keyList) {
        return CollStreamUtils.toList(keyList, key -> {
            PresignedUrlVo vo = new PresignedUrlVo();
            vo.setKey(key);
            URL presignedUrl = ossTemplate.generatePresignedUrl(key);
            vo.setPresignedUrl(presignedUrl.toString());
            return vo;
        });
    }

    private String getUrl(String bucketName, String endpoint, String key) {
        return "https://" + bucketName + "." + endpoint + "/" + key;
    }

    private String url2Key(String url, String bucketName, String endpoint) {
        String target = "https://" + bucketName + "." + endpoint + "/";
        return url.replace(target, "");
    }
}
