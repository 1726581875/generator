package ${basePackagePath}.model.entity;

<#if isJpa ! false>
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import javax.persistence.Id;
</#if>
<#if isMyBatisPlus ! false>
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
</#if>
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
<#if isJpa ! false>
@DynamicInsert
@DynamicUpdate
@Entity(name = "${tableName}")
</#if>
<#if isMyBatisPlus ! false>
@TableName("${tableName}")
</#if>
public class ${entityName}{
    <#list fieldList as field>
    /**
     * ${field.comment}
     */
<#if field.keyType=='PRI' ! false>
  <#if isJpa ! false>
    @Id
  	@GeneratedValue(strategy=GenerationType.IDENTITY)
  <#elseif isMyBatisPlus ! false>
    @TableId(type = IdType.AUTO)
  </#if>
</#if>
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