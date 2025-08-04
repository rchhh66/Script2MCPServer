package com.example.scripttomcpserver.service.impl;

import com.example.scripttomcpserver.model.MCPServerConfig;
import com.example.scripttomcpserver.service.MCPServerGeneratorService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MCPServerGeneratorServiceImplTest {

    @Autowired
    private MCPServerGeneratorService mcpServerGeneratorService;

    @Test
    void generateMCPServerCode() throws IOException {
        // 创建测试配置
        MCPServerConfig config = new MCPServerConfig();
        config.setServerName("TestServer");
        config.setMachineDomain("test-machine.dev.example.com");
        config.setScriptPath("/home/user/scripts");
        config.setStartCommand("python test_script.py");
        config.setDeployMachineIp("192.168.1.100");

        // 添加参数
        Map<String, String> params = new HashMap<>();
        params.put("input", "default_input.txt");
        params.put("output", "default_output.txt");
        config.setParameters(params);

        // 测试代码生成
        String codePath = mcpServerGeneratorService.generateMCPServerCode(config);
        assertNotNull(codePath);
        System.out.println("生成的代码路径: " + codePath);
    }

    @Test
    void initializeSshKey() {
        String deployMachineIp = "192.168.1.100";
        String targetMachineDomain = "test-machine.dev.example.com";

        boolean result = mcpServerGeneratorService.initializeSshKey(deployMachineIp, targetMachineDomain);
        assertTrue(result);
    }

    @Test
    void packageMCPServer() throws IOException {
        // 这里需要先生成代码，然后测试打包
        MCPServerConfig config = new MCPServerConfig();
        config.setServerName("TestServer");
        config.setMachineDomain("test-machine.dev.example.com");
        config.setScriptPath("/home/user/scripts");
        config.setStartCommand("python test_script.py");
        config.setDeployMachineIp("192.168.1.100");

        String codePath = mcpServerGeneratorService.generateMCPServerCode(config);
        String packagePath = mcpServerGeneratorService.packageMCPServer(codePath);

        assertNotNull(packagePath);
        System.out.println("打包后的文件路径: " + packagePath);
    }

    @Test
    void deployMCPServer() throws IOException {
        // 这里需要先生成代码并打包，然后测试部署
        MCPServerConfig config = new MCPServerConfig();
        config.setServerName("TestServer");
        config.setMachineDomain("test-machine.dev.example.com");
        config.setScriptPath("/home/user/scripts");
        config.setStartCommand("python test_script.py");
        config.setDeployMachineIp("192.168.1.100");

        String codePath = mcpServerGeneratorService.generateMCPServerCode(config);
        String packagePath = mcpServerGeneratorService.packageMCPServer(codePath);

        Map<String, Object> deployResult = mcpServerGeneratorService.deployMCPServer(
                packagePath, config.getDeployMachineIp(), config.getServerName());

        assertNotNull(deployResult);
        assertTrue((boolean) deployResult.getOrDefault("success", false));
        System.out.println("部署结果: " + deployResult);
    }
}