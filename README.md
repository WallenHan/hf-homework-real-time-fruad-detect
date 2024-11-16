# 一个基于规则的简单金融诈骗识别Demo


## 作用
依据定义在配置文件的金额阈值和风险名单发现交易中的可能涉及诈骗的交易，将其检测出来通过多种手段记录并通知

## 如何运行
该项目用 maven 构建的 springboot 项目，基础依赖  java SDK 21, maven 3.5+.  
1. java SDK 下载[地址](https://docs.aws.amazon.com/corretto/latest/corretto-21-ug/downloads-list.html)  
2. Maven 下载 [地址](https://maven.apache.org/download.cgi)  
3. 下载并安装好以上基础依赖后，如果在国内（大陆环境还可能需要配置maven的repository 镜像），克隆本仓库代码到本地
4. 进入到项目根目录， 执行 `mvn clean package`，会打包成 jar，在 target 目录中
5. 将该jar放到任意目录下即可使用 `java -jar *.jar`来运行该项目

### 微调版
当你需要调整 **金额阈值** 和 **涉及诈骗名单** 时，开发环境配置文件下请调整 
```
## over amount
rule.thresholdAmount=200000
## names
rule.suspiciousAccounts=gaiby,mountA,masterOne
```
### Docker 版本
待补充
### k8s 版本
待补充
## 下一步规划
