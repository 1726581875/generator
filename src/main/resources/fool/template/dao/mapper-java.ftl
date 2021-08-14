package ${basePackagePath}.dao;

import ${basePackagePath}.model.entity.${entityName};
<#if isMyBatisPlus ! false>
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
</#if>
import org.apache.ibatis.annotations.Mapper;
<#if isMyBatis ! false>
import org.apache.ibatis.annotations.Param;
import java.util.List;
</#if>
/**
 * @author ${author.author}
 * @date ${author.date}
 */
@Mapper
public interface ${entityName}Mapper<#if isMyBatisPlus ! false> extends BaseMapper<${entityName}></#if> {
<#if isMyBatis ! false>
    /**
     * 根据id查询
     *
     * @param id
     * @return
     */
    ${entityName} findById(${keyType} id);

    /**
     * 条件查询
     *
     * @param ${entityNameLow}
     * @return
     */
    List<${entityName}> findByCondition(${entityName} ${entityNameLow});

    /**
     * 插入
     *
     * @param ${entityNameLow}
     * @return
     */
    int insertOne(@Param("${entityNameLow}") ${entityName} ${entityNameLow});

    /**
     * 根据id选择性更新
     *
     * @param manager
     * @return
     */
    int updateByIdSelective(${entityName} manager);

    /**
     * 根据id列表删除
     *
     * @param idList
     * @return
     */
    int deleteByIds(List<${keyType}> idList);
</#if>

}
