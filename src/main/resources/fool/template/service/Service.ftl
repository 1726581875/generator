package ${servicePkName};

import ${entityPkName}.${entityName};
import ${voPkName}.PageVO;

import java.util.List;

/**
 * @author xiaomingzhang
 * @date 2021/7/29
 */
public interface ${entityName}Service {

    /**
     * 条件分页查询
     * @param match 匹配条件
     * @param pageIndex
     * @param pageSize
     * @return
     */
    PageVO<${entityName}> listByPage(${entityName} match,Integer pageIndex, Integer pageSize);

    /**
     * 插入或更新
     * @param ${entityNameLow}
     */
    void insertOrUpdate(${entityName} ${entityNameLow});

    /**
     * 批量删除
     * @param ids
     */
    void batchDelete(List<Long> ids);

}
