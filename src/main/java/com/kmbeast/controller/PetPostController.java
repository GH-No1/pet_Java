package com.kmbeast.controller;

import com.kmbeast.aop.Pager;
import com.kmbeast.context.LocalThreadHolder;
import com.kmbeast.pojo.api.ApiResult;
import com.kmbeast.pojo.api.Result;
import com.kmbeast.pojo.dto.PetPostQueryDto;
import com.kmbeast.pojo.em.IsAuditEnum;
import com.kmbeast.pojo.entity.PetPost;
import com.kmbeast.pojo.vo.PetPostListItemVO;
import com.kmbeast.pojo.vo.PetPostVO;
import com.kmbeast.service.PetPostService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 宠物经验帖子控制器
 */
@RestController
@RequestMapping("/pet-post")
public class PetPostController {

    @Resource
    private PetPostService petPostService;

    /**
     * 宠物经验帖子新增
     *
     * @param petPost 实体数据
     * @return Result<String> 通用返回封装类
     */
    @ResponseBody
    @PostMapping(value = "/save")
    public Result<String> save(@RequestBody PetPost petPost) {
        return petPostService.saveEntity(petPost);
    }

    /**
     * 宠物经验帖子审核
     *
     * @param id 主键ID
     * @return Result<String> 通用返回封装类
     */
    // 优化后的接口：支持传递审核状态和理由
    @ResponseBody
    @PutMapping("/audit/{id}")
    public Result<String> audit(
            @PathVariable Integer id,
            @RequestParam Integer isAudit,
            @RequestParam(required = false) String auditReason) {

        // 参数校验
        if (id == null || isAudit == null) {
            return ApiResult.error("帖子ID和审核状态不能为空");
        }

        // 区分审核类型调用不同服务方法
        if (IsAuditEnum.PASSED.getStatus().equals(isAudit)) {
            // 审核通过
            return petPostService.audit(id);
        } else if (IsAuditEnum.REJECTED.getStatus().equals(isAudit)) {
            // 审核未通过（校验理由）
            if (auditReason == null || auditReason.trim().isEmpty()) {
                return ApiResult.error("未通过审核需填写理由");
            }
            return petPostService.auditReject(id, auditReason);
        } else {
            return ApiResult.error("无效的审核状态（1=通过，2=未通过）");
        }
    }

    /**
     * 宠物经验帖子修改
     *
     * @param petPost 实体数据
     * @return Result<String> 通用返回封装类
     */
    @ResponseBody
    @PutMapping(value = "/update")
    public Result<String> updateEntity(@RequestBody PetPost petPost) {
        return petPostService.updateEntity(petPost);
    }



    /**
     * 宠物经验帖子删除
     *
     * @param id 主键ID
     * @return Result<String> 通用返回封装类
     */
    @ResponseBody
    @DeleteMapping(value = "/{id}")
    public Result<String> deleteById(@PathVariable Integer id) {
        petPostService.removeById(id);
        return ApiResult.success();
    }

    /**
     * 通过ID查询宠物经验帖子信息
     *
     * @param id 主键ID
     * @return Result<PetPostVO> 通用返回封装类
     */
    @Pager
    @ResponseBody
    @GetMapping(value = "/getById/{id}")
    public Result<PetPostVO> getById(@PathVariable Integer id) {
        return petPostService.getById(id);
    }

    /**
     * 查询用户自己的宠物经验帖子信息列表
     *
     * @param petPostQueryDto 查询条件类
     * @return Result<List < PetPostListItemVO>> 通用返回封装类
     */
    @Pager
    @ResponseBody
    @PostMapping(value = "/listUser")
    public Result<List<PetPostListItemVO>> listUser(@RequestBody PetPostQueryDto petPostQueryDto) {
        petPostQueryDto.setUserId(LocalThreadHolder.getUserId()); // 设置用户ID，为了数据隔离
        return petPostService.list(petPostQueryDto);
    }

    /**
     * 查询物经验帖子信息列表
     *
     * @param petPostQueryDto 查询条件类
     * @return Result<List < PetPostListItemVO>> 通用返回封装类
     */
    @Pager
    @ResponseBody
    @PostMapping(value = "/list")
    public Result<List<PetPostListItemVO>> list(@RequestBody PetPostQueryDto petPostQueryDto) {
        return petPostService.list(petPostQueryDto);
    }

    /**
     * 查询用户收藏的宠物经验帖子信息
     *
     * @return Result<List < PetListItemVO>> 通用返回封装类
     */
    @ResponseBody
    @GetMapping(value = "/saveList")
    public Result<List<PetPostListItemVO>> saveList() {
        return petPostService.saveList();
    }

}