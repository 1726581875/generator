package ${servicePkName}.impl;

import ${entityPkName}.${entityName};
import ${daoPkName}.${entityName}Repository;
import ${utilPkName}.CopyUtil;
import ${voPkName}.PageVO;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.*;
import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

/**
 * @author ${author.author}
 * @date: ${author.date}
 */
@Service
public class ${entityName}ServiceImpl {

    @Resource
    private ${entityName}Repository ${entityNameLow}Repository;

    /**
     * 根据Id查找
     * @param id
     * @return 如果找不到返回null
     */
    public ${entityName} findById(${keyType} id){
        Optional<${entityName}> optional = ${entityNameLow}Repository.findById(id);
        if(!optional.isPresent()){
            return null;
        }
        return optional.get();
    }

    public List<${entityName}> findAll(){
        return ${entityNameLow}Repository.findAll();
    }

    /**
     * 根据匹配条件查询所有
     * @param matchObject
     * @return
     */
    public List<${entityName}> findAllByCondition(${entityName} matchObject){
        return ${entityNameLow}Repository.findAll(Example.of(matchObject));
    }

    /**
     * 条件分页查询
     * @param matchObject 匹配对象
     * @param pageIndex 第几页
     * @param pageSize 每页大小
     * @return
     */
    public PageVO<${entityName}> findPage(${entityName} matchObject, Integer pageIndex, Integer pageSize){
        // 1、构造条件
         // 1.1 设置匹配策略，name属性模糊查询，startsWith右模糊(name%)/contains全模糊(%name%)
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("name", match -> match.startsWith());
         // 1.2 构造匹配条件Example对象
        Example<${entityName}> example = Example.of(matchObject,matcher);

        // 2、 构造分页参数 ,第几页,每页大小
        Pageable pageable = PageRequest.of(pageIndex - 1, pageSize);
        // 3、 传入条件、分页参数，调用方法
        Page<${entityName}> ${entityNameLow}Page = ${entityNameLow}Repository.findAll(example, pageable);
        //获取page对象里的list
        List<${entityName}> ${entityNameLow}List = ${entityNameLow}Page.getContent();
        /* 4. 封装到自定义分页结果 */
        PageVO<${entityName}> pageVO = new PageVO<>();
        pageVO.setContent(${entityNameLow}List);
        pageVO.setPageIndex(pageIndex);
        pageVO.setPageSize(pageSize);
        pageVO.setPageCount(${entityNameLow}Page.getTotalPages());
        return pageVO;
    }

    /**
     * 插入数据
     * @param ${entityNameLow}
     * @return 返回成功数
     */
    public Integer insert(${entityName} ${entityNameLow}){
        if (${entityNameLow} == null) {
            throw new IllegalArgumentException("插入表的对象不能为null");
        }
        ${entityName} new${entityName} = ${entityNameLow}Repository.save(${entityNameLow});
        return new${entityName} == null ? 0 : 1;
    }

    /**
     * 插入或更新数据
     * 说明:如果参数带id表示是更新，否则就是插入
     * @param ${entityNameLow}
     * @return 返回成功数
     */
    public Integer insertOrUpdate(${entityName} ${entityNameLow}){
        if (${entityNameLow} == null) {
            throw new IllegalArgumentException("插入表的对象不能为null");
        }
        // id不为空，表示更新操作
        if(${entityNameLow}.get${entityKey}() != null){
          return this.update(${entityNameLow});
        }
        ${entityName} new${entityName} = ${entityNameLow}Repository.save(${entityNameLow});
        return new${entityName} == null ? 0 : 1;
    }


    /**
     *  选择性更新
     * @param ${entityNameLow}
     * @return 返回成功条数
     */
    public Integer update(${entityName} ${entityNameLow}){
        // 入参校验
        if(${entityNameLow} == null || ${entityNameLow}.get${entityKey}() == null){
            throw new IllegalArgumentException("更新的对象不能为null");
        }
        // 是否存在
        Optional<${entityName}> optional = ${entityNameLow}Repository.findById(${entityNameLow}.get${entityKey}());
        if(!optional.isPresent()){
            throw new RuntimeException("找不到id为"+ ${entityNameLow}.get${entityKey}() +"的${entityName}");
        }
        ${entityName} db${entityName} = optional.get();
        //把不为null的属性拷贝到db${entityName}
        CopyUtil.notNullCopy(${entityNameLow}, db${entityName});
        //执行保存操作
        ${entityName} update${entityName} = ${entityNameLow}Repository.save(db${entityName});
        return update${entityName} == null ? 0 : 1;
    }


    public Integer deleteById(${keyType} id){
        ${entityNameLow}Repository.deleteById(id);
        return  findById(id) == null ? 1 : 0;
    }

    /**
     * 批量删除
     * @param ${entityNameLow}IdList  id list
     * @return 删除条数
     */
    public Integer deleteAllByIds(List<${keyType}> ${entityNameLow}IdList){
        List<${entityName}> del${entityName}List = ${entityNameLow}Repository.findAllById(${entityNameLow}IdList);
        ${entityNameLow}Repository.deleteInBatch(del${entityName}List);
        return del${entityName}List.size();
    }


}
