package com.conggua.common.oss.oss.core;

import com.aliyun.oss.OSS;
import com.aliyun.oss.model.*;
import com.conggua.common.oss.oss.autoconfigure.OSSProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.SneakyThrows;

import java.io.InputStream;
import java.net.URL;
import java.util.Date;
import java.util.List;

/**
 * @author ky
 * @description
 * @date 2024-07-01 11:18
 */
@Getter
@AllArgsConstructor
public class OSSTemplate {

    private OSS ossClient;

    private OSSProperties ossProperties;

    /**
     * 创建存储空间（默认）
     */
    public void createBucket() {
        ossClient.createBucket(ossProperties.getBucketName());
    }

    /**
     * 创建存储空间
     *
     * @param bucketName
     */
    public void createBucket(String bucketName) {
        CreateBucketRequest request = new CreateBucketRequest(bucketName);
        ossClient.createBucket(request);
    }

    /**
     * 创建存储空间
     *
     * @param request
     */
    public void createBucket(CreateBucketRequest request) {
        ossClient.createBucket(request);
    }

    /**
     * 存储空间是否存在（默认）
     *
     * @return
     */
    public Boolean doesBucketExist() {
        return ossClient.doesBucketExist(ossProperties.getBucketName());
    }

    /**
     * 存储空间是否存在
     *
     * @param bucketName
     * @return
     */
    public Boolean doesBucketExist(String bucketName) {
        return ossClient.doesBucketExist(bucketName);
    }

    /**
     * 列举存储空间
     *
     * @return Bucket 集合
     */
    @SneakyThrows
    public List<Bucket> listBuckets() {
        return ossClient.listBuckets();
    }

    /**
     * 删除存储空间（默认）
     */
    public void deleteBucket() {
        ossClient.deleteBucket(ossProperties.getBucketName());
    }

    /**
     * 删除存储空间
     *
     * @param bucketName
     */
    public void deleteBucket(String bucketName) {
        ossClient.deleteBucket(bucketName);
    }

    /**
     * 简单上传（默认）
     *
     * @param key
     * @param input
     * @return
     */
    public PutObjectResult putObject(String key, InputStream input) {
        PutObjectRequest request = new AppendObjectRequest(ossProperties.getBucketName(), key, input);
        return ossClient.putObject(request);
    }

    /**
     * 简单上传
     *
     * @param bucketName
     * @param key
     * @param input
     * @return
     */
    public PutObjectResult putObject(String bucketName, String key, InputStream input) {
        PutObjectRequest request = new AppendObjectRequest(bucketName, key, input);
        return ossClient.putObject(request);
    }

    /**
     * 简单上传
     *
     * @param request
     * @return
     */
    public PutObjectResult putObject(PutObjectRequest request) {
        return ossClient.putObject(request);
    }

    /**
     * 简单下载（默认）
     *
     * @param key
     * @return
     */
    public InputStream getObject(String key) {
        GetObjectRequest request = new GetObjectRequest(ossProperties.getBucketName(), key);
        OSSObject object = ossClient.getObject(request);
        return object.getObjectContent();
    }

    /**
     * 简单下载
     *
     * @param bucketName
     * @param key
     */
    public InputStream getObject(String bucketName, String key) {
        GetObjectRequest request = new GetObjectRequest(bucketName, key);
        OSSObject object = ossClient.getObject(request);
        return object.getObjectContent();
    }

    /**
     * 简单下载
     *
     * @param request
     * @return
     */
    public InputStream getObject(GetObjectRequest request) {
        OSSObject object = ossClient.getObject(request);
        return object.getObjectContent();
    }

    /**
     * 查询存储对象是否存在（默认）
     *
     * @param key
     * @return
     */
    public Boolean doesObjectExist(String key) {
        GenericRequest request = new GenericRequest(ossProperties.getBucketName(), key);
        return ossClient.doesObjectExist(request);
    }

    /**
     * 查询存储对象是否存在
     *
     * @param bucketName
     * @param key
     * @return
     */
    public Boolean doesObjectExist(String bucketName, String key) {
        GenericRequest request = new GenericRequest(bucketName, key);
        return ossClient.doesObjectExist(request);
    }

    /**
     * 查询存储对象是否存在
     *
     * @param request
     * @return
     */
    public Boolean doesObjectExist(GenericRequest request) {
        return ossClient.doesObjectExist(request);
    }

    /**
     * 删除文件或目录（目录必须为空）（默认）
     *
     * @param key
     */
    public void deleteObject(String key) {
        GenericRequest request = new GenericRequest(ossProperties.getBucketName(), key);
        ossClient.deleteObject(request);
    }

    /**
     * 删除文件或目录（目录必须为空）
     *
     * @param bucketName
     * @param key
     */
    public void deleteObject(String bucketName, String key) {
        GenericRequest request = new GenericRequest(bucketName, key);
        ossClient.deleteObject(request);
    }

    /**
     * 删除文件或目录（目录必须为空）
     *
     * @param request
     */
    public void deleteObject(GenericRequest request) {
        ossClient.deleteObject(request);
    }

    /**
     * 生成预签名 URL（默认）
     *
     * @param key
     * @return
     */
    public URL generatePresignedUrl(String key) {
        Long presignedUrlExpiration = ossProperties.getPresignedUrlExpiration();
        Date expiration = new Date(System.currentTimeMillis() + presignedUrlExpiration);
        return ossClient.generatePresignedUrl(ossProperties.getBucketName(), key, expiration);
    }

    /**
     * 生成预签名 URL
     *
     * @param bucketName
     * @param key
     * @param expiration
     * @return
     */
    public URL generatePresignedUrl(String bucketName, String key, Date expiration) {
        return ossClient.generatePresignedUrl(bucketName, key, expiration);
    }

    /**
     * 生成预签名 URL
     *
     * @param request
     * @return
     */
    public URL generatePresignedUrl(GeneratePresignedUrlRequest request) {
        return ossClient.generatePresignedUrl(request);
    }
}
