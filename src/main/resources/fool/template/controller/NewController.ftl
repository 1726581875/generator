package ${controllerPkName};

import ${servicePkName}.${entityName}Service;
import ${entityPkName}.${entityName};
import ${voPkName}.RespResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author xiaomingzhang
 * @date 2021/7/30
 */
@RestController
@RequestMapping("/${entityNameLow}")
public class ${entityName}Controller {

    @Autowired
    private ${entityName}Service ${entityNameLow}Service;


    @GetMapping("/list")
    public RespResult findByPage(${entityName} match,
                                 @RequestParam(value = "pageIndex", defaultValue = "1") Integer pageIndex,
                                 @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {

        return RespResult.success(${entityNameLow}Service.listByPage(match, pageIndex, pageSize));
    }


    @PostMapping("/save")
    public RespResult insertOrUpdate(@RequestBody ${entityName} ${entityNameLow}) {
        ${entityNameLow}Service.insertOrUpdate(${entityNameLow});
        return RespResult.success();
    }


    @PostMapping("/batch/delete")
    public RespResult deleteMultiple(@RequestBody List<Long> ids) {
        ${entityNameLow}Service.batchDelete(ids);
        return RespResult.success();
    }


}
