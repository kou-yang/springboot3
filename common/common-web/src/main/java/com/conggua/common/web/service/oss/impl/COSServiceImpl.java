package com.conggua.common.web.service.oss.impl;

import com.conggua.common.base.util.CollStreamUtils;
import com.conggua.common.oss.cos.autoconfigure.COSProperties;
import com.conggua.common.oss.cos.core.COSTemplate;
import com.conggua.common.web.model.vo.PresignedUrlVo;
import com.conggua.common.web.service.oss.COSService;
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
 * @date 2024-07-08 11:18
 */
@Service
public class COSServiceImpl implements COSService {

    @Autowired(required = false)
    private COSTemplate cosTemplate;

    @SneakyThrows
    @Override
    public String simpleUpload(MultipartFile file, String folder) {
        String originalFilename = file.getOriginalFilename();
        // 生成存储对象key
        Assert.notNull(originalFilename, "FileName must not be null");
        if (StringUtils.isBlank(folder)) {
            folder = this.generateFolder();
        }
        String fileName = this.generateFileName(folder, originalFilename);
        // 存储对象
        cosTemplate.putObject(fileName, file.getInputStream());
        return fileName;
    }

    @SneakyThrows
    @Override
    public String simpleUpload(String tenantId, MultipartFile file, String folder) {
        String originalFilename = file.getOriginalFilename();
        // 生成存储对象key
        Assert.notNull(originalFilename, "FileName must not be null");
        Assert.notNull(tenantId, "tenantId must not be null");
        if (StringUtils.isBlank(folder)) {
            folder = this.generateFolder(tenantId);
        }
        String fileName = this.generateFileName(folder, originalFilename);
        // 存储对象
        cosTemplate.putObject(fileName, file.getInputStream());
        return fileName;
    }

    @Override
    public void deleteObject(List<String> urlList) {
        urlList.forEach(cosTemplate::deleteObject);
    }

    @Override
    public List<PresignedUrlVo> generatePresignedUrl(List<String> keyList) {
        return CollStreamUtils.toList(keyList, key -> {
            PresignedUrlVo vo = new PresignedUrlVo();
            vo.setKey(key);
            URL presignedUrl = cosTemplate.generatePresignedUrl(key);
            vo.setPresignedUrl(presignedUrl.toString());
            return vo;
        });
    }

    @Override
    public String url2Key(String url) {
        COSProperties cosProperties = cosTemplate.getCosProperties();
        String bucket = cosProperties.getBucket();
        String region = cosProperties.getRegion();
        String target = "https://" + bucket + ".cos." + region + ".myqcloud.com/";
        url = url.replace(target, "");
        url = url.substring(0, url.indexOf("?"));
        return url;
    }
}
