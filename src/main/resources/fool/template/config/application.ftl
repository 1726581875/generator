#使用端口
server:
  port: 9002

#配置数据库连接参数
spring:
  datasource:
    driver-class-name: ${driver}
    url: ${url}
    username: ${username}
    password: ${password}
  #前后端传参时间格式化
  jackson:
    date-format: yyyy-MM-dd HH:mm:ss
<#if isJpa ! true>
#执行数据库操作时，控制台打印sql语句    
  jpa:
    show-sql: true
    properties:
      hibernate:
      format_sql: true
    hibernate:
      ddl-auto: update
</#if>
<#if !isJpa ! false>
#mybatis配置文件位置
mybatis:
  #指定xml文件存放路径
  mapper-locations: classpath:mapper/*.xml
  #驼峰命名转换
  configuration:
    map-underscore-to-camel-case: true

#mybatis设置日志级别为trace，输出sql执行过程
logging:
  level:
    cn.edu.lingnan.docman.mapper: trace
</#if>