package zhangyu.fool.generator.main.writer.doc;

import zhangyu.fool.generator.dao.DatabaseDAO;
import zhangyu.fool.generator.main.annotation.Writer;
import zhangyu.fool.generator.main.enums.WriterEnum;
import zhangyu.fool.generator.main.model.param.CommonParam;
import zhangyu.fool.generator.model.mysql.TableSql;
import zhangyu.fool.generator.service.DatabaseServiceImpl;
import zhangyu.fool.generator.util.FileUtil;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author xiaomingzhang
 * @date 2021/8/9
 */
@Writer(type = WriterEnum.SQL_FILE)
public class SqlScriptWriter extends AbstractDocWriter {

    private static final String PATH = TEMPLATE_BASE_PATH + "/resources/sql/";

    private static final String NAME = "sql";

    @Override
    public void write(String destPath) {
        FileUtil.checkAndCreateDir(destPath);
        String destFullPath =  destPath + File.separator + File.separator + "db.sql";
        Set<String> tableNameList = DatabaseServiceImpl.getTableNameMap().keySet();
        List<TableSql> tableSqlList = tableNameList.stream().map(name -> DatabaseDAO.getOne(TableSql.getSQL(name),TableSql.class)).collect(Collectors.toList());
        Map<String, Object> paramMap = new HashMap<>(1);
        paramMap.put("tableSqlList", tableSqlList);
        this.writeByTemplate(PATH, NAME, destFullPath, paramMap);
        log.info("sql脚本文件生成完成");
    }

    @Override
    public CommonParam buildParam(String tableName, String entityName) {

        return null;
    }



}
