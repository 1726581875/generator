
<#list tableSqlList as tableSql>
-- ----------------------------
-- Table structure for `${tableSql.tableName}`
-- ----------------------------
DROP TABLE IF EXISTS `${tableSql.tableName}`;
${tableSql.tableSql};


</#list>
