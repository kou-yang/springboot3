package com.conggua.common.oss.cos.core;

import com.conggua.common.oss.cos.autoconfigure.COSProperties;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.model.*;
import com.qcloud.cos.transfer.TransferManager;
import com.qcloud.cos.transfer.TransferManagerConfiguration;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author ky
 * @description
 * @date 2024-07-05 14:11
 */
@Getter
@AllArgsConstructor
public class COSTemplate {

    private COSClient cosClient;

    private COSProperties cosProperties;

    /**
     * 创建存储桶（默认）
     */
    public void createBucket() {
        CreateBucketRequest request = new CreateBucketRequest(cosProperties.getBucket());
        cosClient.createBucket(request);
    }

    /**
     * 创建存储桶
     *
     * @param bucketName
     */
    public void createBucket(String bucketName) {
        CreateBucketRequest request = new CreateBucketRequest(bucketName);
        cosClient.createBucket(request);
    }

    /**
     * 创建存储桶
     *
     * @param request
     */
    public void createBucket(CreateBucketRequest request) {
        cosClient.createBucket(request);
    }

    /**
     * 检索存储桶（默认）
     *
     * @return
     */
    public Boolean doesBucketExist() {
        return cosClient.doesBucketExist(cosProperties.getBucket());
    }

    /**
     * 检索存储桶
     *
     * @param bucketName
     * @return
     */
    public Boolean doesBucketExist(String bucketName) {
        return cosClient.doesBucketExist(bucketName);
    }

    /**
     * 删除存储桶（默认）
     */
    public void deleteBucket() {
        DeleteBucketRequest request = new DeleteBucketRequest(cosProperties.getBucket());
        cosClient.deleteBucket(request);
    }

    /**
     * 删除存储桶
     *
     * @param bucketName
     */
    public void deleteBucket(String bucketName) {
        DeleteBucketRequest request = new DeleteBucketRequest(bucketName);
        cosClient.deleteBucket(request);
    }

    /**
     * 删除存储桶
     *
     * @param request
     */
    public void deleteBucket(DeleteBucketRequest request) {
        cosClient.deleteBucket(request);
    }

    /**
     * 查询存储桶列表
     *
     * @return
     */
    public List<Bucket> listBuckets() {
        TransferManagerConfiguration transferManagerConfiguration = new TransferManagerConfiguration();
        return cosClient.listBuckets();
    }

    /**
     * 查询存储桶列表
     *
     * @param request
     * @return
     */
    public List<Bucket> listBuckets(ListBucketsRequest request) {
        return cosClient.listBuckets(request);
    }

    /**
     * 创建目录（默认）
     *
     * @param folder     /xxx/xxx
     * @return
     */
    public PutObjectResult createFolder(String folder) {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(0);
        PutObjectRequest request = new PutObjectRequest(cosProperties.getBucket(), folder, inputStream, objectMetadata);
        return cosClient.putObject(request);
    }

    /**
     * 创建目录
     *
     * @param bucketName
     * @param folder     /xxx/xxx
     * @return
     */
    public PutObjectResult createFolder(String bucketName, String folder) {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(0);
        PutObjectRequest request = new PutObjectRequest(bucketName, folder, inputStream, objectMetadata);
        return cosClient.putObject(request);
    }

    /**
     * 上传对象（默认）
     *
     * @param key
     * @param inputStream
     * @return
     */
    public PutObjectResult putObject(String key, InputStream inputStream) {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        PutObjectRequest request = new PutObjectRequest(cosProperties.getBucket(), key, inputStream, objectMetadata);
        return cosClient.putObject(request);
    }

    /**
     * 上传对象
     *
     * @param bucketName
     * @param key
     * @param inputStream
     * @return
     */
    public PutObjectResult putObject(String bucketName, String key, InputStream inputStream) {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        PutObjectRequest request = new PutObjectRequest(bucketName, key, inputStream, objectMetadata);
        return cosClient.putObject(request);
    }

    /**
     * 上传对象
     *
     * @param request
     * @return
     */
    public PutObjectResult putObject(PutObjectRequest request) {
        return cosClient.putObject(request);
    }

    /**
     * 下载对象（默认）
     *
     * @param key
     * @return
     */
    public InputStream getObject(String key) {
        GetObjectRequest request = new GetObjectRequest(cosProperties.getBucket(), key);
        COSObject object = cosClient.getObject(request);
        return object.getObjectContent();
    }

    /**
     * 下载对象
     *
     * @param bucketName
     * @param key
     * @return
     */
    public InputStream getObject(String bucketName, String key) {
        GetObjectRequest request = new GetObjectRequest(bucketName, key);
        COSObject object = cosClient.getObject(request);
        return object.getObjectContent();
    }

    /**
     * 下载对象
     *
     * @param request
     * @return
     */
    public InputStream getObject(GetObjectRequest request) {
        COSObject object = cosClient.getObject(request);
        return object.getObjectContent();
    }

    /**
     * 判断存储对象是否存在（默认）
     *
     * @param key
     * @return
     */
    public Boolean doesObjectExist(String key) {
        return cosClient.doesObjectExist(cosProperties.getBucket(), key);
    }

    /**
     * 判断存储对象是否存在
     *
     * @param bucketName
     * @param key
     * @return
     */
    public Boolean doesObjectExist(String bucketName, String key) {
        return cosClient.doesObjectExist(bucketName, key);
    }

    /**
     * 删除文件或目录（目录必须为空）（默认）
     *
     * @param key
     */
    public void deleteObject(String key) {
        cosClient.deleteObject(cosProperties.getBucket(), key);
    }

    /**
     * 删除文件或目录（目录必须为空）
     *
     * @param bucketName
     * @param key
     */
    public void deleteObject(String bucketName, String key) {
        cosClient.deleteObject(bucketName, key);
    }

    /**
     * 删除文件或目录（目录必须为空）
     *
     * @param request
     */
    public void deleteObject(DeleteObjectRequest request) {
        cosClient.deleteObject(request);
    }

    /**
     * 获取访问url（默认）
     *
     * @param key
     * @return
     */
    public URL getObjectUrl(String key) {
        GetObjectRequest request = new GetObjectRequest(cosProperties.getBucket(), key);
        return cosClient.getObjectUrl(request);
    }

    /**
     * 获取访问url
     *
     * @param bucketName
     * @param key
     * @return
     */
    public URL getObjectUrl(String bucketName, String key) {
        GetObjectRequest request = new GetObjectRequest(bucketName, key);
        return cosClient.getObjectUrl(request);
    }

    /**
     * 获取访问url
     *
     * @param request
     * @return
     */
    public URL getObjectUrl(GetObjectRequest request) {
        return cosClient.getObjectUrl(request);
    }

    /**
     * 生成预签名 URL（默认）
     *
     * @param key
     * @return
     */
    public URL generatePresignedUrl(String key) {
        Long presignedUrlExpiration = cosProperties.getPresignedUrlExpiration();
        Date expiration = new Date(System.currentTimeMillis() + presignedUrlExpiration);
        return cosClient.generatePresignedUrl(cosProperties.getBucket(), key, expiration);
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
        return cosClient.generatePresignedUrl(bucketName, key, expiration);
    }

    /**
     * 生成预签名 URL
     *
     * @param request
     * @return
     */
    public URL generatePresignedUrl(GeneratePresignedUrlRequest request) {
        return cosClient.generatePresignedUrl(request);
    }

    /**
     * 创建 TransferManager
     *
     * @return
     */
    public TransferManager createTransferManager() {
        ExecutorService threadPool = Executors.newFixedThreadPool(32);
        TransferManager transferManager = new TransferManager(cosClient, threadPool);
        TransferManagerConfiguration transferManagerConfiguration = new TransferManagerConfiguration();
        transferManagerConfiguration.setMultipartUploadThreshold(cosProperties.getMultipartUploadThreshold());
        transferManagerConfiguration.setMinimumUploadPartSize(cosProperties.getMinimumUploadPartSize());
        transferManager.setConfiguration(transferManagerConfiguration);
        return transferManager;
    }

    /**
     * 关闭 TransferManager
     * 指定参数为 true, 则同时会关闭 transferManager 内部的 COSClient 实例
     * 指定参数为 false, 则不会关闭 transferManager 内部的 COSClient 实例
     *
     * @param transferManager
     */
    public void shutdownTransferManager(TransferManager transferManager) {
        transferManager.shutdownNow(false);
    }
}
