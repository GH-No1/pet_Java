package com.kmbeast.controller;


import com.kmbeast.aop.Pager;
import com.kmbeast.pojo.api.Result;
import com.kmbeast.pojo.dto.PetQueryDto;
import com.kmbeast.pojo.entity.Pet;

import com.kmbeast.pojo.vo.PetListItemVO;
import com.kmbeast.pojo.vo.PetVO;
import com.kmbeast.service.PetService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;


//宠物信息

@RestController
@RequestMapping("/pet")
public class PetController {

    @Resource//jdk自带
    //@Autowired// spring
    private PetService petService;


    //宠物新增
    //pet   实体数据
    //Result<String>  通用返回封装类  在pojo.api包下
    @ResponseBody//转化数据为json  下面的RequestBody是转化为Pet对象
    @PostMapping(value = "/save")
    public Result<String> save(@RequestBody Pet pet){
        return petService.save(pet);
    }


    //宠物修改

    @ResponseBody
    @PutMapping(value = "/update")
    public Result<String> update(@RequestBody Pet pet){
        return petService.update(pet);
    }

    //宠物删除
    //id  主键ID
    @ResponseBody
    @DeleteMapping(value = "/{id}")
    public Result<String> deleteById(@PathVariable Integer id){
        return petService.deleteById(id);
    }


    //通过id查询宠物信息
    @ResponseBody
    @GetMapping(value = "/{id}")
    public Result<PetVO> getById(@PathVariable Integer id){
        return petService.getById(id);
    }
    //通过id查询宠物信息(游客版)
    @ResponseBody
    @GetMapping(value = "un/{id}")
    public Result<PetVO> getByIdUn(@PathVariable Integer id){
        return petService.getByIdUn(id);
    }

    //宠物查询
    @ResponseBody
    @PostMapping(value = "/list")
    @Pager  //分页在aop
    public Result<List<PetListItemVO>> list(@RequestBody PetQueryDto petQueryDto){
        return petService.list(petQueryDto);
    }




    //查询手动推荐的宠物信息,类似于banner轮播图效果
    @ResponseBody
    @GetMapping(value = "/recommend")
    public Result<List<PetListItemVO>> recommend() {
        return petService.recommend();
    }

    /**
     * 智能推荐宠物信息
     * @param count 期望拿到的条数
     * @return Result<List < PetListItemVO>> 通用返回封装类
     */
    @ResponseBody
    @GetMapping(value = "/autoRecommend/{count}")
    public Result<List<PetListItemVO>> autoRecommend(@PathVariable Integer count) {
        return petService.autoRecommend(count);
    }


    /**
     * 查询用户收藏的宠物信息
     *
     * @return Result<List < PetListItemVO>> 通用返回封装类
     */
    @ResponseBody
    @GetMapping(value = "/saveList")
    public Result<List<PetListItemVO>> saveList() {
        return petService.saveList();
    }
}
