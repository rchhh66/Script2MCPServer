package com.example.mcpserver;

import com.jcraft.jsch.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

/**
 * SSH工具类，用于执行远程命令
 * 符合MCP Server规范的SSH操作工具
 */
@Component
public class SSHUtils {

    private static final Logger logger = LoggerFactory.getLogger(SSHUtils.class);
    private final int timeout;

    /**
     * 构造函数
     * @param timeout SSH连接超时时间(毫秒)
     */
    public SSHUtils(@Value("$\{ssh.connection.timeout:5000}") int timeout) {
        this.timeout = timeout;
    }

    /**
     * 执行SSH命令
     * @param host 主机名
     * @param username 用户名
     * @param password 密码
     * @param command 命令
     * @return 命令执行结果
     * @throws JSchException SSH连接异常
     * @throws IOException IO异常
     */
    public String executeCommand(String host, String username, String password, String command) throws JSchException, IOException {
        logger.info("Executing SSH command on {}: {}", host, command);

        JSch jsch = new JSch();
        Session session = jsch.getSession(username, host, 22);
        session.setPassword(password);

        // 设置SSH连接属性
        Properties config = new Properties();
        config.put("StrictHostKeyChecking", "no");
        session.setConfig(config);
        session.setTimeout(timeout);
        session.connect();

        try {
            // 打开执行命令的通道
            ChannelExec channelExec = (ChannelExec) session.openChannel("exec");
            channelExec.setCommand(command);

            // 获取命令输出
            InputStream inputStream = channelExec.getInputStream();
            channelExec.connect();

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
                StringBuilder output = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    output.append(line).append("\n");
                }
                logger.info("SSH command execution completed");
                return output.toString();
            } finally {
                channelExec.disconnect();
            }
        } finally {
            session.disconnect();
        }
    }

    /**
     * 使用密钥文件执行SSH命令
     * @param host 主机名
     * @param username 用户名
     * @param privateKeyPath 私钥文件路径
     * @param command 命令
     * @return 命令执行结果
     * @throws JSchException SSH连接异常
     * @throws IOException IO异常
     */
    public String executeCommandWithKey(String host, String username, String privateKeyPath, String command) throws JSchException, IOException {
        logger.info("Executing SSH command with key on {}: {}", host, command);

        JSch jsch = new JSch();
        jsch.addIdentity(privateKeyPath);
        Session session = jsch.getSession(username, host, 22);

        // 设置SSH连接属性
        Properties config = new Properties();
        config.put("StrictHostKeyChecking", "no");
        session.setConfig(config);
        session.setTimeout(timeout);
        session.connect();

        try {
            // 打开执行命令的通道
            ChannelExec channelExec = (ChannelExec) session.openChannel("exec");
            channelExec.setCommand(command);

            // 获取命令输出
            InputStream inputStream = channelExec.getInputStream();
            channelExec.connect();

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
                StringBuilder output = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    output.append(line).append("\n");
                }
                logger.info("SSH command execution with key completed");
                return output.toString();
            } finally {
                channelExec.disconnect();
            }
        } finally {
            session.disconnect();
        }
    }
}