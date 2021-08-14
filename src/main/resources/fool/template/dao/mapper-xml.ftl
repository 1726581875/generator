<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">

<mapper namespace="${basePackagePath}.dao.${entityName}Mapper">

<#if isMyBatis ! false>
    <select id="findById" resultType="${basePackagePath}.model.entity.${entityName}">
        select * from `${tableName}` where `id` = <#noparse>#</#noparse>{id}
    </select>

    <insert id="insertOne">
        insert into `${tableName}`(<#list fieldList as field>`${field.name}`<#if field_index + 1 != fieldList?size>,</#if> </#list>)
        value(<#list fieldList as field><#noparse>#{</#noparse>${field.nameHump}} <#if field_index + 1 != fieldList?size>,</#if></#list>)
    </insert>


    <update id="updateByIdSelective">
        update `${tableName}`
        <set>
        <#list fieldList as field>
            <if test="${field.nameHump} != null">
                `${field.name}` = <#noparse>#{</#noparse>${field.nameHump}},
            </if>
        </#list>
        </set>
        where id =  <#noparse>#</#noparse>{id}
    </update>

    <delete id="deleteByIds">
        delete from `${tableName}` where id in
        <foreach collection="idList" open="(" item="id" separator="," close=")">
            <#noparse>#</#noparse>{id}
        </foreach>
    </delete>


    <select id="findByCondition" resultType="${basePackagePath}.model.entity.${entityName}">
        select * from `${tableName}`
        <where>
            1 = 1
         <#list fieldList as field>
            <#if field.isFuzzy ! false>
            <if test="${field.nameHump} != null and ${field.nameHump} != ''">
                and `${field.name}` like concat('%',<#noparse>#{</#noparse>${field.nameHump}},'%')
            </if>
            <#else>
            <if test="${field.nameHump} != null">
                and `${field.name}` = <#noparse>#{</#noparse>${field.nameHump}}
            </if>
          </#if>
        </#list>
        </where>
    </select>
</#if>

</mapper>
