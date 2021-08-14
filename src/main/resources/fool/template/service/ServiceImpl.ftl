package ${basePackagePath}.service.impl;

import ${basePackagePath}.service.${entityName}Service;
import ${basePackagePath}.model.entity.${entityName};
<#if isJpa ! false>
import ${basePackagePath}.dao.${entityName}Repository;
import org.springframework.data.domain.*;
<#elseif isMyBatisPlus ! false>
import ${basePackagePath}.dao.${entityName}Mapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
<#elseif isMyBatis ! false>
import ${basePackagePath}.dao.${entityName}Mapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
</#if>
import ${basePackagePath}.util.CopyUtil;
import ${basePackagePath}.model.PageVO;

import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * @author ${author.author}
 * @date: ${author.date}
 */
@Service
public class ${entityName}ServiceImpl implements ${entityName}Service {
   <#if isJpa ! false>

    @Resource
    private ${entityName}Repository ${entityNameLow}Repository;
   <#elseif isMyBatisPlus || isMyBatis ! false>

    @Resource
    private ${entityName}Mapper ${entityNameLow}Mapper;
   </#if>

    @Override
    public PageVO<${entityName}> listByPage(${entityName} match, Integer pageIndex, Integer pageSize) {
        <#if isJpa ! false>
        // 设置匹配策略，name属性模糊查询，startsWith右模糊(name%)/contains全模糊(%name%)
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("name", ExampleMatcher.GenericPropertyMatcher::contains);
        // 构造匹配条件Example对象
        Example<${entityName}> example = Example.of(match, matcher);
        Pageable pageable = PageRequest.of(pageIndex - 1, pageSize);
        Page<${entityName}> ${entityNameLow}Page = ${entityNameLow}Repository.findAll(example, pageable);
        <#elseif isMyBatisPlus ! false>
        Page<${entityName}> ${entityNameLow}Page = ${entityNameLow}Mapper.selectPage(new Page<>(pageIndex, pageSize), new QueryWrapper<>(match));
        <#elseif isMyBatis ! false>
        //使用pageHelper分页
        PageHelper.startPage(pageIndex, pageSize);
        List<${entityName}> ${entityNameLow}List = ${entityNameLow}Mapper.findByCondition(match);
        PageInfo<${entityName}> ${entityNameLow}Page = new PageInfo<>(${entityNameLow}List);
        </#if>
        return new PageVO<>(${entityNameLow}Page);
    }


    @Override
    public void insertOrUpdate(${entityName} ${entityNameLow}) {
        <#-- 选择插入方法 -->
        // id为空，表示是插入操作
        if (Objects.isNull(${entityNameLow}.get${entityKey}())) {
          <#if isJpa ! false>
            ${entityNameLow}Repository.save(${entityNameLow});
          <#elseif isMyBatisPlus ! false>
            ${entityNameLow}Mapper.insert(${entityNameLow});
          <#elseif isMyBatis ! false>
            ${entityNameLow}Mapper.insertOne(${entityNameLow});
          </#if>
            return;
        }
     <#-- 选择更新方法 -->
     <#if isJpa ! false>
        // 更新前先判断是否存在
        Optional<${entityName}> optional = ${entityNameLow}Repository.findById(${entityNameLow}.getId());
        if (!optional.isPresent()) {
            throw new RuntimeException("找不到id为" + ${entityNameLow}.getId() + "的${entityName}");
        }
        ${entityName} db${entityName} = optional.get();
        // 把不为null的属性拷贝到更新对象里
        CopyUtil.notNullCopy(${entityNameLow}, db${entityName});
        // 执行保存操作
        ${entityNameLow}Repository.save(db${entityName});
     <#elseif isMyBatisPlus ! false>
        ${entityNameLow}Mapper.updateById(${entityNameLow});
     <#elseif isMyBatis ! false>
        ${entityNameLow}Mapper.updateByIdSelective(${entityNameLow});
     </#if>
    }

    @Override
    public void batchDelete(List<${keyType}> ids) {
    <#if isJpa ! false>
        List<${entityName}> del${entityName}List = ${entityNameLow}Repository.findAllById(ids);
        if (!CollectionUtils.isEmpty(del${entityName}List)) {
            ${entityNameLow}Repository.deleteInBatch(del${entityName}List);
        }
    <#elseif isMyBatisPlus ! false>
        ${entityNameLow}Mapper.deleteBatchIds(ids);
     <#elseif isMyBatis ! false>
        ${entityNameLow}Mapper.deleteByIds(ids);
    </#if>
    }


}
