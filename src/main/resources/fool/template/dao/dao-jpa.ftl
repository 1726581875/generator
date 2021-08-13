package ${daoPackageName};
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ${entityPackageName}.${entityName};

/**
 * @author ${author.author}
 * @date ${author.date}
 * @see JpaRepository 支持常用增删查改
 * @see JpaSpecificationExecutor 支持多条件分页
 */
public interface ${entityName}Repository extends JpaRepository<${entityName}, ${keyType}>,JpaSpecificationExecutor<${entityName}> {

}
