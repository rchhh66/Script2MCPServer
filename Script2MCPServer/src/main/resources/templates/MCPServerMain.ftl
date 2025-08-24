package com.example.mcpserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import protocol.modelcontext.mcp.McpServer;
import protocol.modelcontext.mcp.annotation.McpFunction;
import protocol.modelcontext.mcp.model.McpRequest;
import protocol.modelcontext.mcp.model.McpResponse;
import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
public class ${serverName}Server extends McpServer {

    private static final Logger logger = LoggerFactory.getLogger(${serverName}Server.class);

    public static void main(String[] args) {
        SpringApplication.run(${serverName}Server.class, args);
        logger.info("${serverName} MCP Server started successfully");
    }

    @McpFunction(name = "execute_script")
    public McpResponse executeScript(McpRequest request) {
        logger.info("Received MCP request to execute script with params: {}", request.getParams());

        McpResponse response = new McpResponse();
        try {
            // 构建完整的命令
            String command = buildCommand(request.getParams());
            logger.info("Executing command: {}", command);

            // 执行SSH命令 (这里应该使用官方SDK提供的工具类或重新实现)
            String output = executeRemoteCommand("${machineDomain}", command);

            response.setSuccess(true);
            response.setResult(output);
            logger.info("Script execution successful");
        } catch (Exception e) {
            logger.error("Script execution failed", e);
            response.setSuccess(false);
            response.setError(e.getMessage());
        }
        return response;
    }

    private String buildCommand(Map<String, Object> params) {
        StringBuilder commandBuilder = new StringBuilder();
        commandBuilder.append("cd ${scriptPath} && ");
        commandBuilder.append("${startCommand}");

        // 添加参数
        <#if parameters?? && (parameters?size > 0)>
            <#list parameters as paramName, paramDefault>
                Object ${paramName}Value = params.getOrDefault("${paramName}", "${paramDefault}");
                commandBuilder.append(" --${paramName} " + ${paramName}Value);
            </#list>
        </#if>

        return commandBuilder.toString();
    }

    private String executeRemoteCommand(String machineDomain, String command) {
        // 这里应该使用官方SDK提供的SSH工具或重新实现
        // 为简化示例，这里返回模拟结果
        return "Command executed successfully on " + machineDomain + ": " + command;
    }
}