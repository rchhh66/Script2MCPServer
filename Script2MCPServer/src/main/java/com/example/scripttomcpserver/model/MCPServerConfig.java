package com.example.scripttomcpserver.model;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Map;

public class MCPServerConfig {
    @NotEmpty(message = "机器域名不能为空")
    private String machineDomain;

    @NotEmpty(message = "脚本路径不能为空")
    private String scriptPath;

    @NotEmpty(message = "启动命令不能为空")
    private String startCommand;

    private Map<String, String> parameters;

    @NotEmpty(message = "部署机器IP不能为空")
    private String deployMachineIp;

    @NotEmpty(message = "MCP Server名称不能为空")
    private String serverName;

    // getter and setter
    public String getMachineDomain() {
        return machineDomain;
    }

    public void setMachineDomain(String machineDomain) {
        this.machineDomain = machineDomain;
    }

    public String getScriptPath() {
        return scriptPath;
    }

    public void setScriptPath(String scriptPath) {
        this.scriptPath = scriptPath;
    }

    public String getStartCommand() {
        return startCommand;
    }

    public void setStartCommand(String startCommand) {
        this.startCommand = startCommand;
    }

    public Map<String, String> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, String> parameters) {
        this.parameters = parameters;
    }

    public String getDeployMachineIp() {
        return deployMachineIp;
    }

    public void setDeployMachineIp(String deployMachineIp) {
        this.deployMachineIp = deployMachineIp;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    @Override
    public String toString() {
        return "MCPServerConfig{"
                + "machineDomain='" + machineDomain + '\''
                + ", scriptPath='" + scriptPath + '\''
                + ", startCommand='" + startCommand + '\''
                + ", parameters=" + parameters
                + ", deployMachineIp='" + deployMachineIp + '\''
                + ", serverName='" + serverName + '\''
                + '}';
    }
}