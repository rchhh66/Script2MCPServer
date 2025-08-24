# MCP Server 自动生成平台

## 项目简介
这是一个用于自动生成、打包和部署MCP Server的平台。通过该平台，您可以轻松地将远程机器上的脚本封装为MCP Server，并通过Docker进行部署。

## 核心功能
1. 自动生成MCP Server代码
2. SSH密钥初始化
3. 代码打包
4. Docker部署
5. 提供友好的Web界面

## 技术栈
- 后端: Java, Spring Boot, Maven
- 前端: HTML, CSS, JavaScript, Bootstrap
- 模板引擎: FreeMarker
- SSH连接: JSch
- 容器化: Docker

## 项目结构
```
script-to-mcp-server/
├── pom.xml                   # Maven配置文件
├── README.md                 # 项目说明
├── src/main/java/
│   └── com/example/scripttomcpserver/
│       ├── ScriptToMCPServerApplication.java  # 主应用类
│       ├── config/           # 配置类
│       ├── controller/       # REST控制器
│       ├── model/            # 数据模型
│       └── service/          # 业务服务
├── src/main/resources/
│   ├── application.properties  # 应用配置
│   ├── static/               # 静态资源
│   └── templates/            # FreeMarker模板
└── src/test/                 # 测试代码
```

## 使用方法
1. 克隆项目到本地
2. 使用Maven构建项目: `mvn clean install`
3. 运行应用: `java -jar target/script-to-mcp-server-1.0-SNAPSHOT.jar`
4. 访问Web界面: http://localhost:8080
5. 在界面上填写MCP Server配置信息
6. 点击相应按钮执行操作

## 配置说明
在 `application.properties` 文件中可以配置以下参数:
- `server.port`: 应用服务器端口
- `ssh.connection.timeout`: SSH连接超时时间
- `docker.host`: Docker主机地址
- `docker.api.version`: Docker API版本
- `mcp.server.template.directory`: 模板文件目录

## 注意事项
1. 确保部署机器上已安装Docker
2. 确保具有部署机器和目标机器的SSH访问权限
3. 生成的MCP Server默认监听8080端口
4. 首次使用需要初始化SSH密钥

## 扩展建议
1. 添加用户认证和授权机制
2. 实现MCP Server的监控和日志收集
3. 支持更多的部署选项和配置
4. 添加批量操作功能
5. 优化前端界面和用户体验