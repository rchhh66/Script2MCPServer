# 基础镜像
# MCP Server Dockerfile
# 基于官方OpenJDK 11镜像
FROM openjdk:11.0.16-jre-slim-bullseye

# 维护者信息
LABEL maintainer="MCP Server Team <team@example.com>"

# 创建非root用户运行应用
RUN addgroup --system mcp && adduser --system --group mcp

# 设置工作目录
WORKDIR /app

# 复制JAR文件到容器中
COPY target/${serverName}-mcp-server.jar app.jar

# 配置SSH客户端
RUN apt-get update && apt-get install -y --no-install-recommends openssh-client && \n\
    rm -rf /var/lib/apt/lists/*

# 创建SSH目录并设置权限
RUN mkdir -p /home/mcp/.ssh && \n\
    chown -R mcp:mcp /home/mcp/.ssh && \n\
    chmod 700 /home/mcp/.ssh

# 暴露端口
EXPOSE 8080

# 添加健康检查
HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 \
    CMD wget --no-verbose --tries=1 --spider http://localhost:8080/actuator/health || exit 1

# 切换到非root用户
USER mcp

# 设置环境变量
ENV JAVA_OPTS="-Xms256m -Xmx512m"

# 设置启动命令
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]