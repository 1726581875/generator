package ${controllerPkName};

import ${servicePkName}.${entityName}Service;
import ${entityPkName}.${entityName};
import ${voPkName}.RespResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * @author ${Author.author}
 * @date: ${Author.date}
 */
@RestController
@RequestMapping("/${entityNameLow}s")
public class ${entityName}Controller {

    @Autowired
    private ${entityName}Service ${entityNameLow}Service;

    /**
     * 分页查询${entityNameLow}接口
     * @param matchObject
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @GetMapping("/list")
    public RespResult findByPage(${entityName} matchObject,
                                 @RequestParam(value = "pageIndex", defaultValue = "1") Integer pageIndex,
                                 @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {

        return RespResult.success(${entityNameLow}Service.findPage(matchObject, pageIndex, pageSize));
    }

    /**
     * 插入或更新${entityNameLow}
     * @param ${entityNameLow}
     * @return
     */
    @PostMapping("/save")
    public RespResult insertOrUpdate(@RequestBody ${entityName} ${entityNameLow}) {
        Integer flag = ${entityNameLow}Service.insertOrUpdate(${entityNameLow});
        if (flag == 0) {
            return RespResult.fail("新增${entityName}失败");
        }
        return RespResult.success("新增${entityName}成功");
    }

    /**
     * 删除${entityNameLow}接口
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public RespResult delete(@PathVariable ${keyType} id) {
        Integer flag = ${entityNameLow}Service.deleteById(id);
        if (flag == 0) {
            return RespResult.fail("删除${entityName}失败");
        }
        return RespResult.success("删除${entityName}成功");
    }

    /**
     * 批量删除${entityNameLow}接口
     * @param ${entityNameLow}IdList
     * @return
     */
    @PostMapping("/batch/delete")
    public RespResult deleteMultiple(@RequestBody List<${keyType}> ${entityNameLow}IdList) {
        ${entityNameLow}Service.deleteAllByIds(${entityNameLow}IdList);
        return RespResult.success("批量删除${entityName}成功");
    }

}