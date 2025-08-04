package com.example.scripttomcpserver.service;

import com.example.scripttomcpserver.model.MCPServerConfig;
import java.io.IOException;
import java.util.Map;

public interface MCPServerGeneratorService {
    /**
     * 生成MCP Server代码
     * @param config MCP Server配置
     * @return 生成的代码文件路径
     * @throws IOException 当文件操作失败时
     */
    String generateMCPServerCode(MCPServerConfig config) throws IOException;

    /**
     * 初始化部署机器的SSH密钥
     * @param deployMachineIp 部署机器IP
     * @param targetMachineDomain 目标机器域名
     * @return 是否初始化成功
     */
    boolean initializeSshKey(String deployMachineIp, String targetMachineDomain);

    /**
     * 打包MCP Server代码
     * @param codePath 代码路径
     * @return 打包后的文件路径
     * @throws IOException 当打包失败时
     */
    String packageMCPServer(String codePath) throws IOException;

    /**
     * 部署MCP Server到指定机器
     * @param packagePath 包路径
     * @param deployMachineIp 部署机器IP
     * @param serverName 服务器名称
     * @return 部署结果
     */
    Map<String, Object> deployMCPServer(String packagePath, String deployMachineIp, String serverName);
}