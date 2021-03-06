package com.fanqielaile.toms.model.fc;

/**
 * DESC :
 *
 * @author : 番茄木-ZLin
 * @data : 2015/8/13
 * @version: v1.0.0
 */
public enum RequestType {
    //新增酒店
    addHotelMapping,
    //新增房型
    addRoomTypeMapping,
    //同步价格计划
    syncRatePlan,
    //同步价格信息接口
    syncRateInfo,
    //酒店基本信息列表查询接口
    queryHotelInfoList,
    //酒店基本信息详情查询接口- QueryHotelInfo
    queryHotelInfo,
    //订单状态推送接口
    syncOrderStatus,
    //酒店基本信息详情查询接口- getHotelInfo
    getHotelInfo,
    //价格计划删除
    deleteRatePlan,
    //删除房型映射
    deleteRoomTypeMapping,
    //删除房型映射
    deleteHotelMapping;
    }
