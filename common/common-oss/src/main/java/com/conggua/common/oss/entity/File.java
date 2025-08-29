package com.conggua.common.oss.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class File {

    /**
     * 文件路径
     */
    private String ossFilePath;

    /**
     * 原始文件名
     */
    private String originalFileName;

    /**
     * 新文件名
     */
    private String newFileName;
}