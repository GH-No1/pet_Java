package com.kmbeast.controller;


import com.kmbeast.aop.Pager;
import com.kmbeast.pojo.api.Result;
import com.kmbeast.pojo.dto.PetTypeQueryDto;
import com.kmbeast.pojo.entity.PetType;
import com.kmbeast.service.PetTypeService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/pet-type")
public class PetTypeController {

    @Resource//jdk
    //@Autowired// spring
    private PetTypeService petTypeService;


    //宠物类别新增
    //petType   实体数据
    //Result<String>  通用返回封装类  在pojo.api包下
    @ResponseBody//转化数据为json  下面的RequestBody是转化为PetType对象
    @PostMapping(value = "/save")
    public Result<String> save(@RequestBody PetType petType){
        return petTypeService.save(petType);
    }


    //宠物类别修改
    //petType   实体数据
    //Result<String>  通用返回封装类  在pojo.api包下
    @ResponseBody//转化数据为json  下面的RequestBody是转化为PetType对象
    @PostMapping(value = "/update")
    public Result<String> update(@RequestBody PetType petType){
        return petTypeService.update(petType);
    }

    //宠物类别删除
    //id  主键ID
    //Result<String>  通用返回封装类  在pojo.api包下
    @ResponseBody//转化数据为json  下面的RequestBody是转化为PetType对象
    @DeleteMapping(value = "/{id}")
    public Result<String> deleteById(@PathVariable Integer id){
        return petTypeService.deleteById(id);
    }



    //宠物类别查询
    //
    //Result<List<PetType>>  通用返回封装类  在pojo.api包下
    @ResponseBody//转化数据为json  下面的RequestBody是转化为PetType对象
    @PostMapping(value = "/query")
    @Pager  //分页在aop
    public Result<List<PetType>> query(@RequestBody PetTypeQueryDto petTypeQueryDto){
        return petTypeService.query(petTypeQueryDto);
    }
}
