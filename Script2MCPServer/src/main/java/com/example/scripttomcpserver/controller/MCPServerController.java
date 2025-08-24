package com.example.scripttomcpserver.controller;

import com.example.scripttomcpserver.model.MCPServerConfig;
import com.example.scripttomcpserver.service.MCPServerGeneratorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/mcp-server")
public class MCPServerController {

    private static final Logger logger = LoggerFactory.getLogger(MCPServerController.class);

    @Autowired
    private MCPServerGeneratorService mcpServerGeneratorService;

    /**
     * 创建并部署MCP Server
     * @param config MCP Server配置
     * @return 部署结果
     */
    @PostMapping("/create-and-deploy")
    public ResponseEntity<Map<String, Object>> createAndDeployMCPServer(@Valid @RequestBody MCPServerConfig config) {
        logger.info("收到创建并部署MCP Server的请求: {}", config);

        try {
            // 1. 初始化部署机器的SSH密钥
            boolean sshKeyInitialized = mcpServerGeneratorService.initializeSshKey(
                    config.getDeployMachineIp(), config.getMachineDomain());
            if (!sshKeyInitialized) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(Map.of("success", false, "message", "SSH密钥初始化失败"));
            }

            // 2. 生成MCP Server代码
            String codePath = mcpServerGeneratorService.generateMCPServerCode(config);

            // 3. 打包MCP Server
            String packagePath = mcpServerGeneratorService.packageMCPServer(codePath);

            // 4. 部署MCP Server
            Map<String, Object> deployResult = mcpServerGeneratorService.deployMCPServer(
                    packagePath, config.getDeployMachineIp(), config.getServerName());

            if ((boolean) deployResult.getOrDefault("success", false)) {
                return ResponseEntity.ok(deployResult);
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(deployResult);
            }

        } catch (IOException e) {
            logger.error("创建并部署MCP Server失败", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("success", false, "message", "操作失败: " + e.getMessage()));
        }
    }

    /**
     * 仅生成MCP Server代码
     * @param config MCP Server配置
     * @return 生成结果
     */
    @PostMapping("/generate-code")
    public ResponseEntity<Map<String, Object>> generateMCPServerCode(@Valid @RequestBody MCPServerConfig config) {
        logger.info("收到生成MCP Server代码的请求: {}", config);

        try {
            String codePath = mcpServerGeneratorService.generateMCPServerCode(config);
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "MCP Server代码生成成功",
                    "codePath", codePath
            ));
        } catch (IOException e) {
            logger.error("生成MCP Server代码失败", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("success", false, "message", "生成代码失败: " + e.getMessage()));
        }
    }

    /**
     * 初始化SSH密钥
     * @param config MCP Server配置
     * @return 初始化结果
     */
    @PostMapping("/initialize-ssh")
    public ResponseEntity<Map<String, Object>> initializeSshKey(@Valid @RequestBody MCPServerConfig config) {
        logger.info("收到初始化SSH密钥的请求: {}", config);

        boolean initialized = mcpServerGeneratorService.initializeSshKey(
                config.getDeployMachineIp(), config.getMachineDomain());

        if (initialized) {
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "SSH密钥初始化成功"
            ));
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("success", false, "message", "SSH密钥初始化失败"));
        }
    }

    /**
     * 部署MCP Server
     * @param config MCP Server配置
     * @return 部署结果
     */
    @PostMapping("/deploy")
    public ResponseEntity<Map<String, Object>> deployMCPServer(@Valid @RequestBody MCPServerConfig config) {
        logger.info("收到部署MCP Server的请求: {}", config);

        try {
            // 假设代码已经生成并打包，这里简化处理
            String packagePath = "/path/to/generated/mcp-server.jar"; // 实际应用中应该从请求中获取
            Map<String, Object> deployResult = mcpServerGeneratorService.deployMCPServer(
                    packagePath, config.getDeployMachineIp(), config.getServerName());

            if ((boolean) deployResult.getOrDefault("success", false)) {
                return ResponseEntity.ok(deployResult);
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(deployResult);
            }
        } catch (Exception e) {
            logger.error("部署MCP Server失败", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("success", false, "message", "部署失败: " + e.getMessage()));
        }
    }
}