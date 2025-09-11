package com.conggua.common.generator;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.rules.DbColumnType;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import com.baomidou.mybatisplus.generator.fill.Column;

import java.sql.Types;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: kouyang
 * @description:
 * @date: 2025-08-23 11:41
 */
public class Generator {

    private static final String SRC_JAVA = "src/main/java";
    private static final String SRC_RESOURCES = "src/main/resources";

    public static void create(String url, String username, String password, List<String> tables, String module, String parentPackage) {
        FastAutoGenerator.create(url, username, password)
            .globalConfig(builder -> {
                builder.author("kouyang")
                    .enableSpringdoc()
                    // 指定输出目录(/xxx/xxx/src/main/java)
                    .outputDir(String.join("/", System.getProperty("user.dir"), module, SRC_JAVA));
            })
            .dataSourceConfig(builder ->
                builder.typeConvertHandler((globalConfig, typeRegistry, metaInfo) -> {
                    int typeCode = metaInfo.getJdbcType().TYPE_CODE;
                    if (typeCode == Types.SMALLINT) {
                        // 自定义类型转换
                        return DbColumnType.INTEGER;
                    }
                    return typeRegistry.getColumnType(metaInfo);
                })
            )
            .packageConfig(builder ->
                // 设置父包名(com.xxx.xxx)
                builder.parent(parentPackage)
                    .entity("model.entity")
                    // 设置mapperXml生成路径
                    .pathInfo(Collections.singletonMap(
                        OutputFile.xml,
                        String.join("/", System.getProperty("user.dir"), module, SRC_RESOURCES, "mapper"))
                    )
            )
            .strategyConfig(builder ->
                // 设置需要生成的表名
                builder.addInclude(tables)
                    .entityBuilder()
                        .enableLombok()
                        .enableTableFieldAnnotation()
                        .addTableFills(
                            new Column("create_time", FieldFill.INSERT),
                            new Column("update_time", FieldFill.INSERT_UPDATE),
                            new Column("createBy", FieldFill.INSERT),
                            new Column("updateBy", FieldFill.INSERT_UPDATE)
                        )
                        .javaTemplate("/templates/entity.java")
                    .mapperBuilder()
                        .enableBaseResultMap()
                        .enableBaseColumnList()
                        .mapperTemplate("/templates/mapper.java")
                        .mapperXmlTemplate("/templates/mapper.xml")
                    .serviceBuilder()
                        .formatServiceFileName("%sService")
                        .formatServiceImplFileName("%sServiceImpl")
                        .serviceTemplate("/templates/service.java")
                        .serviceImplTemplate("/templates/serviceImpl.java")
                    .controllerBuilder()
                        .enableRestStyle()
                        .formatFileName("%sController")
                        .template("/templates/controller.java")
            )
            .injectionConfig(builder -> {
                Map<String,Object> customMap = new HashMap<>();
                customMap.put("saveDTOPackage", parentPackage + ".model.dto");
                customMap.put("updateDTOPackage", parentPackage + ".model.dto");
                customMap.put("pageDTOPackage", parentPackage + ".model.dto");
                customMap.put("voPackage", parentPackage + ".model.vo");
                builder.customMap(customMap);

                builder.customFile(b -> b
                    .fileName("SaveDTO.java")
                    .templatePath("/templates/saveDTO.java.ftl")
                    .packageName("model.dto")
                )
                .customFile(b -> b
                    .fileName("UpdateDTO.java")
                    .templatePath("/templates/updateDTO.java.ftl")
                    .packageName("model.dto")
                )
                .customFile(b -> b
                    .fileName("PageDTO.java")
                    .templatePath("/templates/pageDTO.java.ftl")
                    .packageName("model.dto")
                )
                .customFile(b -> b
                    .fileName("PageVO.java")
                    .templatePath("/templates/pageVO.java.ftl")
                    .packageName("model.vo")
                )
                .customFile(b -> b
                        .fileName("DetailVO.java")
                        .templatePath("/templates/detailVO.java.ftl")
                        .packageName("model.vo")
                );
            })
            .templateEngine(new FreemarkerTemplateEngine())
            .execute();
    }
}
