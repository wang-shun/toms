package com.fanqielaile.toms.controller;

import com.fanqie.util.HttpClientUtil;
import com.fanqielaile.toms.enums.ChannelSource;
import com.fanqielaile.toms.enums.OrderMethod;
import com.fanqielaile.toms.helper.OrderMethodHelper;
import com.fanqielaile.toms.model.Order;
import com.fanqielaile.toms.model.Result;
import com.fanqielaile.toms.model.UserInfo;
import com.fanqielaile.toms.service.IOrderService;
import com.fanqielaile.toms.service.IUserInfoService;
import com.fanqielaile.toms.support.exception.TomsRuntimeException;
import com.fanqielaile.toms.support.util.JsonModel;
import com.fanqielaile.toms.support.util.XmlDeal;
import com.tomato.framework.log.annotation.Log;
import com.tomato.framework.log.annotation.LogModule;
import com.tomato.framework.log.client.BusinLogClient;
import com.tomato.framework.log.client.SysLogClient;
import com.tomato.log.model.BusinLog;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/**
 * Created by wangdayin on 2015/6/19.
 * OTA对接接口
 */
@Controller
@RequestMapping("")
public class OTAManageController extends BaseController {
    private static Logger logger = LoggerFactory.getLogger(OTAManageController.class);
    @Resource
    private IOrderService orderService;
    @Resource
    private IUserInfoService userInfoService;
    @Resource
    private Md5PasswordEncoder passwordEncoder;
    @Resource
    private BusinLogClient businLogClient;
    /**
     * 淘宝调用的接口
     *
     * @return
     */
    @RequestMapping("taobaoService")
    @ResponseBody
    public Object TBService(HttpServletRequest request) throws Exception {
        String xmlStr = HttpClientUtil.convertStreamToString(request.getInputStream());
        Result result = new Result();
        if (StringUtils.isNotEmpty(xmlStr)) {
            //接口调用验证用户
            UserInfo userNameAndPassword = OrderMethodHelper.getUserNameAndPassword(xmlStr);
            UserInfo userInfo = (UserInfo) userInfoService.loadUserByUsername(userNameAndPassword.getUsername());
            if (null != userInfo) {
                //验证用户密码
                if (userInfo.getPassword().equals(passwordEncoder.encodePassword(userNameAndPassword.getPassword(), ""))) {
                    //得到跟节点
                    logger.info("xml参数：" + xmlStr);
                    String rootElementString = XmlDeal.getRootElementString(xmlStr);
                    //根据根节点判断执行的方法
                    if (rootElementString.equals(OrderMethod.BookRQ.name())) {
                        //创建订单
                        Order order = orderService.addOrder(xmlStr, ChannelSource.TAOBAO);
                        result.setResultCode("0");
                        result.setMessage(order.getId());
                    } else if (rootElementString.equals(OrderMethod.CancelRQ.name())) {
                        //取消订单
                        JsonModel jsonModel = orderService.cancelOrder(xmlStr, ChannelSource.TAOBAO);
                        if (jsonModel.isSuccess()) {
                            result.setResultCode("0");
                            result.setMessage("取消订单成功");
                        } else {
                            result.setResultCode("-209");
                            result.setMessage(jsonModel.getMessage());
                        }
                    } else if (rootElementString.equals(OrderMethod.PaySuccessRQ.name())) {
                        //付款成功回调
                        JsonModel jsonModel = orderService.paymentSuccessCallBack(xmlStr, ChannelSource.TAOBAO);
                        if (jsonModel.isSuccess()) {
                            result.setResultCode("0");
                            result.setMessage("付款成功");
                        } else {
                            result.setResultCode("-400");
                            result.setMessage(jsonModel.getMessage());
                        }
                        //查询订单状态
                    } else if (rootElementString.equals(OrderMethod.QueryStatusRQ.name())) {
                        Map<String, String> orderStatus = orderService.findOrderStatus(xmlStr, ChannelSource.TAOBAO);
                        result.setMessage(orderStatus.get("message"));
                        result.setResultCode(orderStatus.get("code"));
                        if (StringUtils.isNotEmpty(orderStatus.get("status"))) {
                            result.setStatus(orderStatus.get("status"));
                        }
                    } else {
                        logger.error("xml参数错误");
                    }
                } else {
                    logger.error("创建订单失败,验证用户不通过", userInfo);
                    result.setMessage("创建订单失败,验证用户不通过");
                    result.setResultCode("-400");
                }
            } else {
                logger.error("创建订单失败,用户不存在", userInfo);
                result.setMessage("创建订单失败,用户不存在");
                result.setResultCode("-400");
            }
        } else {
            logger.error("创建订单失败，原因：参数不正确", xmlStr);
            result.setMessage("创建订单失败，原因：参数不正确");
            result.setResultCode("-400");
        }
        logger.info("返回淘宝的xml值=>" + result.toString());
        return result;
    }
}
