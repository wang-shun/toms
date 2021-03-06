package com.fanqielaile.toms.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.fanqie.core.dto.ParamDto;
import com.fanqie.qunar.model.Hotel;
import com.fanqie.qunar.response.QunarGetHotelInfoResponse;
import com.fanqie.util.DcUtil;
import com.fanqie.util.HttpClientUtil;
import com.fanqie.util.JacksonUtil;
import com.fanqielaile.toms.common.CommonApi;
import com.fanqielaile.toms.dao.*;
import com.fanqielaile.toms.dto.*;
import com.fanqielaile.toms.model.BangInn;
import com.fanqielaile.toms.model.Company;
import com.fanqielaile.toms.model.InnLabel;
import com.fanqielaile.toms.model.UserInfo;
import com.fanqielaile.toms.service.IBangInnService;
import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by wangdayin on 2015/5/15.
 */
@Service
public class BangInnService implements IBangInnService {
    private static final Logger log = Logger.getLogger(BangInnService.class);
    @Resource
    private BangInnDao bangInnDao;
    @Resource
    private InnLabelDao innLabelDao;
    @Resource
    private UserInfoDao userInfoDao;
    @Resource
    private CompanyDao companyDao;
    @Resource
    private IOtaInfoDao otaInfoDao;

    @Override
    public List<BangInn> findBangInnByInnLabelId(String innLabelId) {
        return this.bangInnDao.selectBangInnByInnLabelId(innLabelId,new UserInfo());
    }

    @Override
    public List<BangInn> findBangInnBuUser(UserInfo userInfo) {
        return this.bangInnDao.selectBangInnByUser(userInfo);
    }

    @Override
    public List<BangInn> findBangInnAndLabel(UserInfo userInfo) {
        List<BangInn> results = new ArrayList<>();
        List<BangInn> labels = this.bangInnDao.selectBangInnByUser(userInfo);
        //新增一个全部客栈分类，即查询当前登录用户下所有绑定的客栈
        BangInn bangInn1 = new BangInn();
        bangInn1.setInnLabelId(null);
        labels.add(bangInn1);
        if (null != labels) {
            for (BangInn bangInn : labels) {
                List<BangInn> inns = this.bangInnDao.selectBangInnByInnLabelId(bangInn.getInnLabelId(), userInfo);
                if (StringUtils.isEmpty(bangInn.getInnLabelId())) {
                    BangInn bangInn2 = new BangInn();
                    bangInn2.setInnLabelId("");
                    bangInn2.setInnLabelName("全部客栈分类");
                    bangInn2.setBangInnList(inns);
                    results.add(bangInn2);
                } else {
                    InnLabel innLabel = this.innLabelDao.selectLabelById(bangInn.getInnLabelId());
                    if (null != innLabel && null != inns) {
                        BangInn inn = new BangInn();
                        inn.setInnLabelId(innLabel.getId());
                        inn.setInnLabelName(innLabel.getLabelName());
                        inn.setBangInnList(inns);
                        results.add(inn);
                    }
                }
            }
        }
        Collections.sort(results);
        return results;
    }

    @Override
    public List<BangInnDto> findBangInnListByUserInfo(UserInfo userInfo, PageBounds pageBounds) {
        List<BangInnDto> bangInnDtoList = this.bangInnDao.selectBangInnListByUserInfo(userInfo, pageBounds);
        if (ArrayUtils.isNotEmpty(bangInnDtoList.toArray())) {
            for (BangInnDto bangInnDto : bangInnDtoList) {
                if (StringUtils.isNotEmpty(bangInnDto.getInnLabelId())) {
                    //标签
                    InnLabel innLabel = this.innLabelDao.selectLabelById(bangInnDto.getInnLabelId());
                    bangInnDto.setLabelName(innLabel.getLabelName());
                }
                if (StringUtils.isNotEmpty(bangInnDto.getUserId())) {
                    //所属管理员
                    UserInfo info = this.userInfoDao.selectUserInfoById(bangInnDto.getUserId());
                    bangInnDto.setUserName(info.getUserName());
                }
            }
        }
        return bangInnDtoList;
    }

    @Override
    public BangInnDto findBangInnById(String id) {
        BangInnDto bangInnDto = this.bangInnDao.selectBangInnById(id);
        if (null != bangInnDto) {
            if (StringUtils.isNotEmpty(bangInnDto.getInnLabelId())) {
                InnLabel innLabel = this.innLabelDao.selectLabelById(bangInnDto.getInnLabelId());
                bangInnDto.setLabelName(innLabel.getLabelName());
            }
            if (StringUtils.isNotEmpty(bangInnDto.getUserId())) {
                UserInfo userInfo = this.userInfoDao.selectUserInfoById(bangInnDto.getUserId());
                bangInnDto.setUserName(userInfo.getUserName());
            }
        }
        return bangInnDto;
    }

    @Override
    public BangInnDto findBangInnByInnIdCompanyId(String innId,String companyId) {
        return bangInnDao.selectBangInnByInnIdCompanyId(Integer.valueOf(innId),companyId);
    }

    @Override
    public void modifiyBangInn(BangInnDto bangInnDto) {
        this.bangInnDao.updateBangInn(bangInnDto);
    }

    @Override
    public List<BangInnDto> findCompanyByInnId(int innId) {
        return this.bangInnDao.selectCompanyByInnId(innId);
    }

    @Override
    public void addBanginn(BangInnDto bangInnDto) {
        this.bangInnDao.createBangInn(bangInnDto);
    }

    @Override
    public BangInn findBangInnByCompanyIdAndInnId(String companyId, int innId) {
        return this.bangInnDao.selectBangInnByCompanyIdAndInnId(companyId, innId);
    }

    @Override
    public List<BangInn> findBangInnByStringBangInn(List<BangInn> bangInnList) {
        List<BangInn> bangInns = new ArrayList<>();
        if (ArrayUtils.isNotEmpty(bangInnList.toArray())) {
            for (BangInn bangInn : bangInnList) {
                BangInnDto bangInnDto = this.bangInnDao.selectBangInnById(bangInn.getId());
                bangInns.add(bangInnDto);
            }
        }
        return bangInns;
    }

    @Override
    public List<BangInn> findBangInnByCompanyId(String companyId) {
        return this.bangInnDao.selectBangInnByCompanyId(companyId);
    }

    @Override
    public BangInn findBangInnByUserAndCode(UserInfo userInfo, String code) {
        return this.bangInnDao.selectBangInnByUserAndCode(userInfo, code);
    }

    @Override
    public List<BangInn> findBangInnImages(String companyId) throws IOException {
        List<BangInn> bangInns = this.bangInnDao.selectBangInnByCompanyId(companyId);
        Company company = this.companyDao.selectCompanyById(companyId);
        if (ArrayUtils.isNotEmpty(bangInns.toArray()) && null != company) {
            for (BangInn bangInn : bangInns) {
                //封装访问的路径与参数
                String timestamp = String.valueOf(new Date().getTime());
                String signature = DcUtil.obtMd5(company.getOtaId() + timestamp + company.getUserAccount() + company.getUserPassword());
                String url = CommonApi.getInnInfo() + "?timestamp=" + timestamp + "&otaId=" + company.getOtaId() + "&accountId=" + bangInn.getAccountId() + "&signature=" + signature;
                String response = HttpClientUtil.httpGets(url, null);
                JSONObject jsonInn = JSONObject.fromObject(response);
                if ("200".equals(jsonInn.get("status").toString()) && jsonInn.get("list") != null) {
                    InnDto omsInnDto = JacksonUtil.json2list(jsonInn.get("list").toString(), InnDto.class).get(0);
                    bangInn.setInnDto(omsInnDto);
                }
            }
        }
        return bangInns;
    }

    @Override
    public List<RoomTypeInfo> findBangInnRoomImage(BangInnDto bangInnDto) throws IOException {
        Company company = this.companyDao.selectCompanyById(bangInnDto.getCompanyId());
        List<RoomTypeInfo> result = new ArrayList<>();
        if (null != company) {
            String timestamp = String.valueOf(new Date().getTime());
            String signature = DcUtil.obtMd5(company.getOtaId() + timestamp + company.getUserAccount() + company.getUserPassword());
            String url = CommonApi.getRoomType() + "?timestamp=" + timestamp + "&from=" + new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + "&to=" + new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + "&otaId=" + company.getOtaId() + "&accountId=" + bangInnDto.getAccountId() + "&signature=" + signature;
            log.info("toms 获取oms图片url:"+url);
            String response = HttpClientUtil.httpGets(url, null);
            JSONObject jsonObject = JSONObject.fromObject(response);
            if ("200".equals(jsonObject.get("status").toString()) && jsonObject.get("list") != null) {
                //result = JacksonUtil.json2list(jsonObject.get("list").toString(), RoomTypeInfo.class);
                result = JSON.parseObject(jsonObject.get("list").toString(), new TypeReference<List<RoomTypeInfo>>(){});
                if (ArrayUtils.isNotEmpty(result.toArray())) {
                    for (RoomTypeInfo roomTypeInfo : result) {
                        if (ArrayUtils.isNotEmpty(roomTypeInfo.getImgList().toArray())) {
                            for (OmsImg omsImg : roomTypeInfo.getImgList()) {
                                omsImg.setSuffix(omsImg.getImgUrl().split("\\.")[1]);
                            }
                            //排序
                            Collections.sort(roomTypeInfo.getImgList());
                        }
                    }
                }
            }
        }
        return result;
    }

    @Override
    public InnDto selectBangInnImage(BangInnDto bangInnDto) throws IOException {
        Company company = this.companyDao.selectCompanyById(bangInnDto.getCompanyId());
        //封装访问的路径与参数
        String timestamp = String.valueOf(new Date().getTime());
        String signature = DcUtil.obtMd5(company.getOtaId() + timestamp + company.getUserAccount() + company.getUserPassword());
        String url = CommonApi.getInnInfo() + "?timestamp=" + timestamp + "&otaId=" + company.getOtaId() + "&accountId=" + bangInnDto.getAccountId() + "&signature=" + signature;
        String response = HttpClientUtil.httpGets(url, null);
        JSONObject jsonInn = JSONObject.fromObject(response);
        if ("200".equals(jsonInn.get("status").toString()) && jsonInn.get("list") != null) {
            InnDto omsInnDto = JacksonUtil.json2list(jsonInn.get("list").toString(), InnDto.class).get(0);
            if (ArrayUtils.isNotEmpty(omsInnDto.getImgList().toArray())) {
                for (OmsImg omsImg : omsInnDto.getImgList()) {
                    omsImg.setSuffix(omsImg.getImgUrl().split("\\.")[1]);
                }
            }
            Collections.sort(omsInnDto.getImgList());
            return omsInnDto;
        }
        return null;
    }

    @Override
    public BangInnDto findBangInnByInnIdCompanyId(Integer innId, String companyId) {
        return bangInnDao.selectBangInnByInnIdCompanyId(innId,companyId);
    }

    @Override
    public List<BangInnDto> findFcBangInn(BangInnDto bangInnDto,PageBounds pageBounds) {
        return  bangInnDao.selectFcBangInn(bangInnDto,pageBounds);
    }

    @Override
    public List<BangInnDto> findOTABangInn(BangInnDto bangInnDto, OtaInfoRefDto otaInfo, PageBounds pageBounds) {
        List<String> companyIdList = otaInfoDao.selectOtaByAppKey(otaInfo);
        OtaInfoRefDto infoRefDto = otaInfoDao.selectOtaInfoByAppKey(otaInfo);
        //如果infoRefDto 中的公司id不跟当前的登录者的公司id相同的时候，就要排除共同拥有的innId
        if (infoRefDto!=null && !infoRefDto.getCompanyId().equals(otaInfo.getCompanyId())){
            bangInnDto.setCompanyIdList(companyIdList);
        }
        bangInnDto.setOtaInfoId(otaInfo.getOtaInfoId());
        return bangInnDao.selectOTABangInn(bangInnDto,pageBounds);
    }

    @Override
    public List<BangInnDto> findFcBangInn(BangInnDto bangInnDto) {
        return  bangInnDao.selectFcExcelBangInn(bangInnDto);
    }

    @Override
    public List<BangInn> selectNoMatch() {
        return bangInnDao.selectNoMatch();
    }

    @Override
    public QunarGetHotelInfoResponse findBangInnListByCompanyCode(String companyCode) {
        QunarGetHotelInfoResponse result = new QunarGetHotelInfoResponse();
        List<Hotel> hotelList = this.bangInnDao.selectBangInnListByCompanyCode(companyCode);
        result.setHotelList(hotelList);
        return result;
    }

    @Override
    public List<BangInn> findClassifyByUser(UserInfo userInfo) {
        List<BangInn> results = new ArrayList<>();
        List<BangInn> labels = this.bangInnDao.selectBangInnByUser(userInfo);
        //新增一个全部客栈分类，即查询当前登录用户下所有绑定的客栈
        BangInn bangInn1 = new BangInn();
        bangInn1.setInnLabelId(null);
        labels.add(bangInn1);
        if (null != labels) {
            for (BangInn bangInn : labels) {
                if (StringUtils.isEmpty(bangInn.getInnLabelId())) {
                    BangInn bangInn2 = new BangInn();
                    bangInn2.setInnLabelId("");
                    bangInn2.setInnLabelName("全部客栈分类");
                    results.add(bangInn2);
                } else {
                    InnLabel innLabel = this.innLabelDao.selectLabelById(bangInn.getInnLabelId());
                    if (null != innLabel) {
                        BangInn inn = new BangInn();
                        inn.setInnLabelId(innLabel.getId());
                        inn.setInnLabelName(innLabel.getLabelName());
                        results.add(inn);
                    }
                }
            }
        }
        Collections.sort(results);
        return results;
    }


    @Override
    public List<BangInn> findRoomTypeByName(String innLabelId,String innName,UserInfo userInfo,PageBounds pageBounds) {
        List<BangInn> inns = this.bangInnDao.selectBangInnByName(innLabelId, innName, userInfo,pageBounds);
        return inns;
    }
}
