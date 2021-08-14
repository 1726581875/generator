package ${basePackagePath}.model.dto;

<#if isLombok ! false>
import lombok.Data;
</#if>
<#list javaTypeSet as type>
<#if type=='Date'>
import java.util.Date;
</#if>
<#if type=='BigDecimal'>
import java.math.BigDecimal;
</#if>
</#list>

/**
 * @author ${author.author}
 * @date: ${author.date}
 */
<#if isLombok ! false>
@Data
</#if>
public class ${entityName}DTO {
    <#list fieldList as field>
    /**
     * ${field.comment}
     */
    private ${field.javaType} ${field.nameHump};
    </#list>

<#if !isLombok ! false>
    <#list fieldList as field>
    public ${field.javaType} get${field.nameBigHump}() {
        return ${field.nameHump};
    }

    public void set${field.nameBigHump}(${field.javaType} ${field.nameHump}) {
        this.${field.nameHump} = ${field.nameHump};
    }
    </#list>

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        <#list fieldList as field>
        sb.append(", ${field.nameHump}=").append(${field.nameHump});
        </#list>
        sb.append("]");
        return sb.toString();
    }
</#if>
}