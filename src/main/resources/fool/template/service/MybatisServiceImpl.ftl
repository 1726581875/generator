package ${servicePkName}.impl;

import ${entityPkName}.${entityName};
import ${daoPkName}.${entityName}Mapper;
import ${voPkName}.PageVO;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * @author xiaomingzhang
 * @date 2021/6/29
 */
@Service
public class ${entityName}ServiceImpl {

    @Resource
    private ${entityName}Mapper ${entityNameLow}Mapper;

    /**
     * 根据Id查找
     *
     * @param id
     * @return 如果找不到返回null
     */
    public ${entityName} findById(${keyType} id) {
        Assert.notNull(id, "id 不能为null");
        return ${entityNameLow}Mapper.findById(id);
    }

    /**
     * 条件分页查询
     *
     * @param matchObject 匹配对象
     * @param pageIndex   第几页
     * @param pageSize    每页大小
     * @return
     */
    public PageVO<${entityName}> findPage(${entityName} matchObject, Integer pageIndex, Integer pageSize) {
        //使用pageHelper分页
        PageHelper.startPage(pageIndex, pageSize, "create_time DESC");
        List<${entityName}> ${entityNameLow}List = ${entityNameLow}Mapper.findByCondition(matchObject);
        return new PageVO<>(${entityNameLow}List);
    }

    /**
     * 插入数据
     *
     * @param ${entityNameLow}
     * @return 返回成功数
     */
    public Integer insert(${entityName} ${entityNameLow}) {
        Assert.notNull(${entityNameLow}, "插入表的对象不能为null");
        return ${entityNameLow}Mapper.insert(${entityNameLow});
    }

    /**
     * 插入或更新数据
     * 说明:如果参数带id表示是更新，否则就是插入
     *
     * @param ${entityNameLow}
     * @return 返回成功数
     */
    public Integer insertOrUpdate(${entityName} ${entityNameLow}) {
        Assert.notNull(${entityNameLow}, "插入表的对象不能为null");
        // id不为空，表示更新操作
        if (Objects.isNull(${entityNameLow}.getId())) {
            return this.updateByIdSelective(${entityNameLow});
        }
        return this.insert(${entityNameLow});
    }


    /**
     * 选择性更新
     *
     * @param ${entityNameLow}
     * @return 返回成功条数
     */
    public Integer updateByIdSelective(${entityName} ${entityNameLow}) {
        // 入参校验
        Assert.notNull(${entityNameLow}, "更新的对象不能为null");
        Assert.notNull(${entityNameLow}, "更新的对象id不能为null");
        return ${entityNameLow}Mapper.updateByIdSelective(${entityNameLow});
    }


    public Integer deleteById(${keyType} id) {
        Assert.notNull(id, "根据id删除，id不能为null");
        return this.deleteAllByIds(Arrays.asList(id));
    }

    /**
     * 批量删除
     *
     * @param ${entityNameLow}IdList id list
     * @return 删除条数
     */
    public Integer deleteAllByIds(List<${keyType}> ${entityNameLow}IdList) {
        Assert.notEmpty(${entityNameLow}IdList, "删除id List不能为空");
        return ${entityNameLow}Mapper.deleteByIds(${entityNameLow}IdList);
    }

}
