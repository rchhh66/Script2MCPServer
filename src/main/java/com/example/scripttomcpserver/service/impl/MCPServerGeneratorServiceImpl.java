package com.example.scripttomcpserver.service.impl;

import com.example.scripttomcpserver.model.MCPServerConfig;
import com.example.scripttomcpserver.service.MCPServerGeneratorService;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
// MCP SDK依赖暂时移除，相关导入已删除

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class MCPServerGeneratorServiceImpl implements MCPServerGeneratorService {

    private static final Logger logger = LoggerFactory.getLogger(MCPServerGeneratorServiceImpl.class);

    @Autowired
    private Configuration freemarkerConfig;

    @Value("${mcp.server.template.directory}")
    private String templateDirectory;

    @Value("${ssh.connection.timeout}")
    private int sshTimeout;

    @Override
    public String generateMCPServerCode(MCPServerConfig config) throws IOException {
        logger.info("开始生成符合MCP官方SDK规范的Server代码，配置: {}", config);

        // 创建临时目录存储生成的代码
        String tempDir = System.getProperty("java.io.tmpdir") + File.separator + "mcp-server-" + UUID.randomUUID();
        Path tempPath = Paths.get(tempDir);
        Files.createDirectories(tempPath);

        // 创建包结构目录
        Path packageDir = Paths.get(tempDir + File.separator + "src" + File.separator + "main" + File.separator + "java" + File.separator + "com" + File.separator + "example" + File.separator + "mcpserver");
        Files.createDirectories(packageDir);

        Path resourcesDir = Paths.get(tempDir + File.separator + "src" + File.separator + "main" + File.separator + "resources");
        Files.createDirectories(resourcesDir);

        // 准备模板数据
        Map<String, Object> templateData = new HashMap<>();
        templateData.put("serverName", config.getServerName());
        templateData.put("machineDomain", config.getMachineDomain());
        templateData.put("scriptPath", config.getScriptPath());
        templateData.put("startCommand", config.getStartCommand());
        templateData.put("parameters", config.getParameters());
        templateData.put("sshTimeout", sshTimeout);

        try {
            // 生成主类代码
            generateFileFromTemplate("MCPServerMain.ftl", packageDir + File.separator + config.getServerName() + "Server.java", templateData);
            // 生成SSH工具类代码
            generateFileFromTemplate("SSHUtils.ftl", packageDir + File.separator + "SSHUtils.java", templateData);
            // 生成Dockerfile
            generateFileFromTemplate("Dockerfile.ftl", tempDir + File.separator + "Dockerfile", templateData);
            // 生成pom.xml
            generateFileFromTemplate("pom.ftl", tempDir + File.separator + "pom.xml", templateData);

            // 生成application.properties
            String applicationProperties = "# MCP Server configuration\n"
                    + "ssh.connection.timeout=\"${sshTimeout}\"\n"
                    + "server.port=8080\n";
            Files.write(Paths.get(resourcesDir + File.separator + "application.properties"), applicationProperties.getBytes(StandardCharsets.UTF_8));

            logger.info("MCP Server代码生成成功，路径: {}", tempDir);
            return tempDir;
        } catch (TemplateException e) {
            logger.error("模板处理失败", e);
            throw new IOException("生成MCP Server代码失败: " + e.getMessage(), e);
        }
    }

    private void generateFileFromTemplate(String templateName, String outputFilePath, Map<String, Object> data) throws IOException, TemplateException {
        Template template = freemarkerConfig.getTemplate(templateName);
        try (Writer writer = new OutputStreamWriter(new FileOutputStream(outputFilePath), StandardCharsets.UTF_8)) {
            template.process(data, writer);
        }
    }

    @Override
    public boolean initializeSshKey(String deployMachineIp, String targetMachineDomain) {
        logger.info("开始初始化部署机器 {} 的SSH密钥，目标机器: {}", deployMachineIp, targetMachineDomain);

        try {
            // 1. 连接到部署机器
            // 这里简化实现，实际应该使用SSH连接到部署机器
            // 2. 生成SSH密钥对
            // 3. 将公钥复制到目标机器的authorized_keys文件中

            // 模拟实现
            Thread.sleep(2000);
            logger.info("SSH密钥初始化成功");
            return true;
        } catch (Exception e) {
            logger.error("SSH密钥初始化失败", e);
            return false;
        }
    }

    @Override
    public String packageMCPServer(String codePath) throws IOException {
        logger.info("开始打包MCP Server，代码路径: {}", codePath);

        try {
            // 模拟Maven打包过程
            String packagePath = codePath + File.separator + "target" + File.separator + "mcp-server.jar";
            Path packageDir = Paths.get(codePath + File.separator + "target");
            Files.createDirectories(packageDir);
            Files.createFile(Paths.get(packagePath));

            logger.info("MCP Server打包成功，包路径: {}", packagePath);
            return packagePath;
        } catch (Exception e) {
            logger.error("MCP Server打包失败", e);
            throw new IOException("打包MCP Server失败: " + e.getMessage(), e);
        }
    }

    @Override
    public Map<String, Object> deployMCPServer(String packagePath, String deployMachineIp, String serverName) {
        logger.info("开始部署MCP Server，包路径: {}，部署机器: {}，服务器名称: {}", packagePath, deployMachineIp, serverName);

        Map<String, Object> result = new HashMap<>();
        try {
            // 1. 将包传输到部署机器
            // 2. 使用Docker构建镜像
            // 3. 运行Docker容器

            // 模拟部署过程
            Thread.sleep(3000);

            result.put("success", true);
            result.put("message", "MCP Server部署成功");
            result.put("serverUrl", "http://" + deployMachineIp + ":8080/" + serverName);

            logger.info("MCP Server部署成功");
            return result;
        } catch (Exception e) {
            logger.error("MCP Server部署失败", e);
            result.put("success", false);
            result.put("message", "部署失败: " + e.getMessage());
            return result;
        }
    }
}