package com.kmbeast.controller;

import com.kmbeast.aop.Pager;
import com.kmbeast.context.LocalThreadHolder;
import com.kmbeast.pojo.api.ApiResult;
import com.kmbeast.pojo.api.Result;
import com.kmbeast.pojo.dto.AddressQueryDto;
import com.kmbeast.pojo.entity.Address;
import com.kmbeast.service.AddressService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

//收货地址控制器
@RestController
@RequestMapping("/address")
public class AddressController {

    @Resource
    private AddressService addressService;

    @ResponseBody
    @PostMapping(value = "/save")
    public Result<String> save(@RequestBody Address address) {
        return addressService.saveEntity(address);
    }


    @ResponseBody
    @PutMapping(value = "/update")
    public Result<String> update(@RequestBody Address address) {
        return addressService.update(address);
    }

    @ResponseBody
    @DeleteMapping(value = "/{id}")
    public Result<String> deleteById(@PathVariable Integer id) {
        addressService.removeById(id);
        return ApiResult.success("删除成功");
    }

    @Pager
    @ResponseBody
    @PostMapping(value = "/queryUser")
    public Result<List<Address>> queryUser(@RequestBody AddressQueryDto addressQueryDto) {
        addressQueryDto.setUserId(LocalThreadHolder.getUserId()); // 设置用户ID，数据隔离
        return addressService.query(addressQueryDto);
    }

    @Pager
    @ResponseBody
    @PostMapping(value = "/query")
    public Result<List<Address>> query(@RequestBody AddressQueryDto addressQueryDto) {
        return addressService.query(addressQueryDto);
    }

}