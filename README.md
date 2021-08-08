这是一个简单的代码生成器，一键生成springboot工程，减少程序员繁琐的工作。springboot版本是2.3.4，orm层框架使用的是spring-data-jpa。生成的都是简单的代码，目前尚有很多未完善。生成的代码都是简单易懂的，可在这基础上进行二次开发。

快速开始

要求环境或工具：jdk1.8、maven、IDE工具（idea或者eclipse等）、mysql5.7以上

## 1.通过git拉取代码
```
git@github.com:1726581875/xmz.git
```

## 2.profile.xml对配置进行修改
找到src/main/java下的profile.xml文件

- 配置项一，修改连接数据库的参数，**url、username、password**改成你自己对应的。

```xml
 <!--配置数据源 -->
<dataSource>
    <driver>com.mysql.cj.jdbc.Driver</driver>
    <url>jdbc:mysql://localhost:3306/blog?characterEncoding=UTF-8&serverTimezone=GMT%2B8</url>
    <username>root</username>
    <password>root</password>
  </dataSource>
```

- 配置项二，配置你数据库所有表的表名和对应实体类名

```xml
 <!-- 配置数据库表名和实体名映射 -->
  <tables> 
      <!--article表，对应实体类名Article -->
    <table>
      <tableName>article</tableName>
      <entityName>Article</entityName>
    </table>
    <!--collection表，对应实体类名Collection -->
    <table>
      <tableName>collection</tableName>
      <entityName>Collection</entityName>
    </table>     
 </tables>
```

- 配置项三，生产的springboot项目的基本配置

  1、配置项目的路径（在哪个位置生成），不用事先建好目录，但是也可以先创建目录。

  2、配好groupId和artifactId, （*一般来说groupId是组织域名反写+项目名，artifactId一般都是项目名-模块名*），这里我只有一个模块,为了简单groupId约定为组织域名反写，groupId为项目名。例如我的配置groupId为com.small.chili      artifactId为blog。

  3、可选配置，可采取默认配置，配置对应包的名称。默认实体类包名是entity,假如你想换个名字可以把<entityPackage>标签里的名称换成其他的名字。

```xml
<project>
    <!-- 1、项目路径 -->
     <projectPath>E:\xmz\</projectPath> 
    <!-- 2、maven相关配置-->
     <maven>
        <groupId>com.small.chili</groupId> <!-- maven工程的groupId -->
        <artifactId>blog</artifactId> <!-- artifactId -->
     </maven>
     <!-- 3、可选，配置要创建的对应包名(前缀都是groupId + artifactId) -->
     <package>
       <entityPackage>entity</entityPackage>
       <daoPackage>dao</daoPackage>
       <servicePackage>service</servicePackage>
       <controllerPackage>controller</controllerPackage>
       <utilPackage>util</utilPackage>
       <dtoPackage>dto</dtoPackage>
       <voPackage>vo</voPackage>
     </package>
     
</project>
```

还有个可选配置就是根标签的名字，<xmz></xmz>可以改成自己的拼音缩写或者其他名，它会对应到生成类的注释里的author。

## 3.运行

找到zhangyu.fool.generate包下了MainRunner.java类，配置代码生成目标·路径，点击运行即可



## 代码生成器二次开发说明

使用的是freamaker模板引擎，核心代码就几行，我封装在TemplateFactory接口里。传入的参数是模板路径、模板名、生成模板目标路径（全路径，包括要生成的文件名）、模板参数。

```
	default void generateByTemplate(String templatePath, String templateName, String destPath, Map<String, Object> paramMap) {
		File templateDir = new File(templatePath);
		if (!templateDir.exists()) {
			log.error("====模板路径 {}  不存在，结束====");
			return;
		}			
		try (FileWriter fileWriter = new FileWriter(destPath);
				BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {
			// 读模板
			Configuration config = new Configuration(Configuration.VERSION_2_3_29);
			config.setObjectWrapper(new DefaultObjectWrapper(Configuration.VERSION_2_3_29));
			config.setDirectoryForTemplateLoading(templateDir);
			Template template = config.getTemplate(templateName + ".ftl");
			// 写
			template.process(paramMap, bufferedWriter);
			bufferedWriter.flush();
		} catch (Exception e) {
			log.error("====加载模板{} 生成代码失败，发生异常{}====", templateName, e);
		}
	}

```

工程的factory包是负责加载各个模板类的生成，所有的factory类都继承TemplateFactory接口。最核心的类是MavenProjectFactory,负责生成maven工程，和调度各个工厂类生成对应代码。util包负责为factory类提供辅助性支持。
