package com.fanqielaile.toms.dto;

import com.fanqielaile.toms.model.UserInfo;

/**
 * Created by wangdayin on 2015/5/21.
 */
public class UserInfoDto extends UserInfo {
    //所属公司
    private String companyName;
    //是否有管理的客栈
    private boolean isHaveInn;
    //查看订单时是否被选择
    private boolean selected;

    public boolean getIsHaveInn() {
        return isHaveInn;
    }

    public void setIsHaveInn(boolean isHaveInn) {
        this.isHaveInn = isHaveInn;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}
}
