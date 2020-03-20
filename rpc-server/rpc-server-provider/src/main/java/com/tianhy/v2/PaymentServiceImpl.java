package com.tianhy.v2;

import com.tianhy.service.IPaymentService;

/**
 * {@link}
 *
 * @Desc: 注解的方式执行此方法
 * @Author: thy
 * @CreateTime: 2019/6/23
 **/
@RpcService(value = IPaymentService.class)
public class PaymentServiceImpl implements IPaymentService {
    @Override
    public void doPay() {
        System.out.println("doPay...");
    }
}
