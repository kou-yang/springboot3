package com.conggua.common.generator;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.DbColumnType;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import com.baomidou.mybatisplus.generator.fill.Column;

import java.io.File;
import java.sql.Types;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author: kouyang
 * @description:
 * @date: 2025-08-23 11:41
 */
public class Generator {

    private static final String SRC_JAVA = "src/main/java";
    private static final String SRC_RESOURCES = "src/main/resources";

    public static void create(Boolean overwrite, String url, String username, String password, List<String> tables, String module, String parentPackage) {
        if (Boolean.TRUE.equals(overwrite)) {
            if (!confirmOverwrite()) {
                // 用户取消，直接退出
                return;
            }
        }

        FastAutoGenerator.create(url, username, password)
            .globalConfig(builder -> {
                builder.author("kouyang")
                    .enableSpringdoc()
                    .disableOpenDir()
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
                        .logicDeleteColumnName("deleted")
                        .addTableFills(
                            new Column("create_time", FieldFill.INSERT),
                            new Column("update_time", FieldFill.INSERT_UPDATE),
                            new Column("create_by", FieldFill.INSERT),
                            new Column("update_by", FieldFill.INSERT_UPDATE)
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

                // 覆盖模式
                if (overwrite) {
                    builder.beforeOutputFile((tableInfo, objectMap) -> {
                        // 获取所有即将生成的文件路径（根据策略配置）
                        List<String> filePaths = getAllGeneratedFilePaths(tableInfo, module, parentPackage);
                        for (String filePath : filePaths) {
                            File file = new File(filePath);
                            if (file.exists()) {
                                boolean deleted = file.delete();
                                if (deleted) {
                                    System.out.println("🗑️ 已删除旧文件（覆盖模式）: " + filePath);
                                } else {
                                    System.err.println("❌ 删除失败: " + filePath);
                                }
                            }
                        }
                    });
                }

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

    private static List<String> getAllGeneratedFilePaths(TableInfo tableInfo, String module, String parentPackage) {
        List<String> paths = new ArrayList<>();

        String javaDir = String.join("/", System.getProperty("user.dir"), module, SRC_JAVA);
        String resourceDir = String.join("/", System.getProperty("user.dir"), module, SRC_RESOURCES);

        String entityPackagePath = parentPackage.replace(".", "/");
        String mapperPackagePath = (parentPackage + ".mapper").replace(".", "/");
        String servicePackagePath = (parentPackage + ".service").replace(".", "/");
        String serviceImplPackagePath = (parentPackage + ".service.impl").replace(".", "/");
        String controllerPackagePath = (parentPackage + ".controller").replace(".", "/");

        String className = tableInfo.getEntityName();

        // 1. Entity
        paths.add(javaDir + "/" + entityPackagePath + "/model/entity/" + className + ".java");

        // 2. Mapper
        paths.add(javaDir + "/" + mapperPackagePath + "/" + className + "Mapper.java");

        // 3. Mapper XML
        paths.add(resourceDir + "/mapper/" + className + "Mapper.xml");

        // 4. Service
        paths.add(javaDir + "/" + servicePackagePath + "/" + className + "Service.java");

        // 5. ServiceImpl
        paths.add(javaDir + "/" + serviceImplPackagePath + "/" + className + "ServiceImpl.java");

        // 6. Controller
        paths.add(javaDir + "/" + controllerPackagePath + "/" + className + "Controller.java");

        // 7. 自定义 DTO/VO
        paths.add(javaDir + "/" + entityPackagePath + "/model/dto/" + className + "SaveDTO.java");
        paths.add(javaDir + "/" + entityPackagePath + "/model/dto/" + className + "UpdateDTO.java");
        paths.add(javaDir + "/" + entityPackagePath + "/model/dto/" + className + "PageDTO.java");
        paths.add(javaDir + "/" + entityPackagePath + "/model/vo/" + className + "PageVO.java");
        paths.add(javaDir + "/" + entityPackagePath + "/model/vo/" + className + "DetailVO.java");

        return paths;
    }

    private static boolean confirmOverwrite() {
        AtomicReference<String> userInput = new AtomicReference<>("");
        AtomicBoolean hasInput = new AtomicBoolean(false);

        System.out.println("⚠️ 覆盖模式已启用！请在 10 秒内输入确认：");
        System.out.print("输入 Y 确认覆盖，N 取消生成（超时 10s 自动取消）：");

        Thread inputThread = new Thread(() -> {
            try (Scanner scanner = new Scanner(System.in)) {
                if (scanner.hasNextLine()) {
                    String line = scanner.nextLine().trim();
                    userInput.set(line);
                    hasInput.set(true);
                }
            }
        });
        inputThread.setDaemon(true);
        inputThread.start();

        try {
            inputThread.join(10_000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return false;
        }

        if (!hasInput.get()) {
            System.out.println("\n⏳ 超时未输入，已取消生成。");
            return false;
        }

        String input = userInput.get();
        if ("Y".equalsIgnoreCase(input)) {
            System.out.println("✅ 已确认，开始覆盖生成...");
            return true;
        } else if ("N".equalsIgnoreCase(input)) {
            System.out.println("❌ 用户已取消生成。");
            return false;
        } else {
            System.out.println("❓ 无效输入（\"" + input + "\"），已取消生成。");
            return false;
        }
    }
}
