package com.kmbeast.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kmbeast.context.LocalThreadHolder;
import com.kmbeast.mapper.AddressMapper;
import com.kmbeast.pojo.api.ApiResult;
import com.kmbeast.pojo.api.Result;
import com.kmbeast.pojo.dto.AddressQueryDto;
import com.kmbeast.pojo.em.IsDefaultEnum;
import com.kmbeast.pojo.entity.Address;
import com.kmbeast.service.AddressService;
import com.kmbeast.utils.AssertUtils;
import com.kmbeast.utils.ValidationUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;


@Service
public class AddressServiceImpl extends ServiceImpl<AddressMapper, Address> implements AddressService {



    @Override
    public Result<String> saveEntity(Address address) {
        judgeParam(address);
        address.setUserId(LocalThreadHolder.getUserId()); // 设置用户ID
        address.setCreateTime(LocalDateTime.now()); // 设置当前时间
        // 默认地址只能有一项
        defaultAddress(address);
        save(address);
        return ApiResult.success("收货地址设置成功");
    }


    private void judgeParam(Address address) {
        // 非空校验
        AssertUtils.hasText(address.getAddressee(), "收件人不能为空");
        AssertUtils.hasText(address.getDetail(), "地址详情不能为空");
        AssertUtils.hasText(address.getConcatPhone(), "联系电话不能为空");
        AssertUtils.notNull(address.getIsDefault(), "请设置是否是默认地址");
        // 校验手机号是否符合要求
        AssertUtils.isTrue(ValidationUtils.isValidChineseMobile(address.getConcatPhone()), "手机号不符合要求，请认真填写");
        AssertUtils.isTrue(address.getDetail().length() < 100, "地址详情上限100字哦");
    }


    private void defaultAddress(Address address) {
        // 默认地址只能有一项
        if (address.getIsDefault()) {
            // 1. 将之前用户名下已经维护的默认地址设置为非默认地址
            AddressQueryDto addressQueryDto = new AddressQueryDto();
            addressQueryDto.setUserId(LocalThreadHolder.getUserId()); // 设置上用户ID
            addressQueryDto.setIsDefault(IsDefaultEnum.DEFAULT_ADDRESS.getStatus()); // 设置成默认地址
            List<Address> addressList = this.baseMapper.list(addressQueryDto);
            // 2. 存在默认地址，先把原来的默认地址设置为非默认地址
            if (!addressList.isEmpty()) {
                Address userDefaultAddress = addressList.get(0);
                userDefaultAddress.setIsDefault(IsDefaultEnum.NO_DEFAULT_ADDRESS.getStatus());
                updateById(userDefaultAddress);
            }
        }
    }


    @Override
    public Result<String> update(Address address) {
        judgeParam(address); // 参数教研
        defaultAddress(address); // 默认收货地址处理
        updateById(address); // 收货地址数据更新
        return ApiResult.success("收货地址修改成功");
    }


    @Override
    public Result<List<Address>> query(AddressQueryDto addressQueryDto) {
        List<Address> addressList = this.baseMapper.list(addressQueryDto);
        Integer count = this.baseMapper.listCount(addressQueryDto);
        return ApiResult.success(addressList, count);
    }
}