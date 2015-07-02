package com.fanqielaile.toms.service;

import com.fanqie.core.dto.ParamDto;
import com.fanqielaile.toms.dto.UserInfoDto;
import com.fanqielaile.toms.enums.ChannelSource;
import com.fanqielaile.toms.model.Order;
import com.fanqielaile.toms.model.UserInfo;
import com.fanqielaile.toms.support.util.JsonModel;
import net.sf.json.JSONObject;

import java.util.Map;

/**
 * DESC :
 *
 * @author : 番茄木-ZLin
 * @data : 2015/5/29
 * @version: v1.0.0
 */
public interface IOrderService {


    /**
     * 订单来源、概况
     * @param paramDto
     * @param userInfo
     * @return
     * @throws Exception
     */
    Map<String,Object> findOrderSourceDetail(ParamDto paramDto,UserInfo userInfo) throws Exception;

    /**
     * 创建订单
     *
     * @param xmlStr
     */
    Order addOrder(String xmlStr, ChannelSource channelSource) throws Exception;

    /**
     * 取消订单
     *
     * @param xmlStr
     * @param channelSource
     * @throws Exception
     */
    JsonModel cancelOrder(String xmlStr, ChannelSource channelSource) throws Exception;

    /**
     * 付款成功回调
     *
     * @param xmlStr
     * @param channelSource
     * @return
     * @throws Exception
     */
    JsonModel paymentSuccessCallBack(String xmlStr, ChannelSource channelSource) throws Exception;

    /**
     * 查询订单状态
     *
     * @param xmlStr
     * @param channelSource
     * @return
     */
    Map<String, String> findOrderStatus(String xmlStr, ChannelSource channelSource) throws Exception;
}
