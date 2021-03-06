package com.fanqielaile.toms.model.fc;

import com.fanqie.support.OtaRequest;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Created by wangdayin on 2015/9/24.
 * 同步价格计划接口请求对象
 */
@XmlRootElement(name = "Request")
@XmlType(propOrder = {"header", "syncRatePlanRequest"})
public class SyncRatePlanRequest extends OtaRequest {
    //xml头部
    private Header header;
    //价格计划请求对象信息
    private SyncRatePlanRequestInfo syncRatePlanRequest;

    public SyncRatePlanRequest() {
    }

    public SyncRatePlanRequest(Header header, SyncRatePlanRequestInfo syncRatePlanRequest) {
        this.header = header;
        this.syncRatePlanRequest = syncRatePlanRequest;
    }

    @XmlElement(name = "Header")
    public Header getHeader() {
        return header;
    }

    public void setHeader(Header header) {
        this.header = header;
    }

    @XmlElement(name = "SyncRatePlanRequest")
    public SyncRatePlanRequestInfo getSyncRatePlanRequest() {
        return syncRatePlanRequest;
    }

    public void setSyncRatePlanRequest(SyncRatePlanRequestInfo syncRatePlanRequest) {
        this.syncRatePlanRequest = syncRatePlanRequest;
    }
}
