# 一个基于规则的简单金融诈骗识别Demo


## 作用
依据定义在配置文件的金额阈值和风险名单发现交易中的可能涉及诈骗的交易，将其检测出来通过多种手段记录并通知

## 快速入门

### 如何运行
该项目用 maven 构建的 springboot 项目，基础依赖  java SDK 21, maven 3.5+.  
1. java SDK 下载 [地址](https://docs.aws.amazon.com/corretto/latest/corretto-21-ug/downloads-list.html)  
2. Maven 下载 [地址](https://maven.apache.org/download.cgi)  
3. 下载并安装好以上基础依赖后，如果在国内（大陆环境还可能需要配置maven的repository 镜像），克隆本仓库代码到本地
4. 进入到项目根目录， 执行 `mvn clean package`，会打包成 jar，在 target 目录中
5. 将该jar放到任意目录下即可使用 `java -jar *.jar`来运行该项目

### 视频说明
[![Watch the video](https://vumbnail.com/1031088467.jpg)](https://vimeo.com/1031088467)

### 调整配置
当你需要调整 **金额阈值** 和 **涉及诈骗名单** 时，开发、测试环境本地验证时请在配置文件`src/main/resources/application-dev.properties`下请调整 
```
## over amount
rule.thresholdAmount=200000
## names
rule.suspiciousAccounts=gaiby,mountA,masterOne
```
当需要生产部署时，请配置认证信息、新的队列和生产上的规则到`src/main/resources/application-pro.properties`文件中
```
aws.sqs.queueUrl= 【我是占位符】
aws.res.apiKey= 【我是占位符】
aws.res.apiSecret=【我是占位符】
## over amount
rule.thresholdAmount=200000
## names
rule.suspiciousAccounts=gaiby,mountA,masterOne
```
### Docker 部署
- 国内部署可能有障碍，请参考互联网搜索加镜像源同步镜像。
- 启动命令
  ```
  docker run -d -p 8080:8080 --restart always wallen/rt_fraud_detect:latest --spring.profiles.active=dev 
  ```
### k8s 部署
详见[部署清单](./deployment.yaml)

## 项目详情
- [需求详情](./doc/requriements.md)
- [项目结构](./doc/detail.md)
## 安全问题
pom xml中移除处理了有安全问题的依赖，应随时定义注意安全问题，避免引入有安全问题的依赖。

## 后期扩展方向
- 实现将规则可以通过数据库配置，方便变更；
- 增加后台管理，可以通过鉴权后的web页面方便业务人员更新规则；
- 进一步考虑多个规则组合的情况，支持规则组合检测，例如，转给 xxx 且金额是 xxx 是可能涉及诈骗。
