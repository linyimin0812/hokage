package com.hokage.biz.service.impl;

import com.hokage.biz.converter.server.ConverterTypeEnum;
import com.hokage.biz.converter.server.ServerDOConverter;
import com.hokage.biz.enums.server.ServerStatusEnum;
import com.hokage.biz.response.home.HomeDetailMeta;
import com.hokage.biz.response.home.HomeDetailVO;
import com.hokage.biz.response.server.HokageServerVO;
import com.hokage.biz.service.HomeService;
import com.hokage.common.ServiceResponse;
import com.hokage.persistence.dao.HokageServerDao;
import com.hokage.persistence.dao.HokageServerMetricDao;
import com.hokage.persistence.dao.HokageSubordinateServerDao;
import com.hokage.persistence.dataobject.HokageServerMetricDO;
import com.hokage.persistence.dataobject.HokageSubordinateServerDO;
import com.hokage.ssh.enums.MetricTypeEnum;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author yiminlin
 * @date 2021/07/19 9:22 pm
 * @description home service
 **/
@Service
public class HomeServiceImpl implements HomeService {
    private HokageServerDao serverDao;
    private HokageSubordinateServerDao subordinateServerDao;
    private HokageServerMetricDao metricDao;

    @Autowired
    public void setServerDao(HokageServerDao serverDao) {
        this.serverDao = serverDao;
    }

    @Autowired
    public void setSubordinateServerDao(HokageSubordinateServerDao subordinateServerDao) {
        this.subordinateServerDao = subordinateServerDao;
    }

    @Autowired
    public void setMetricDao(HokageServerMetricDao metricDao) {
        this.metricDao = metricDao;
    }

    @Override
    public ServiceResponse<HomeDetailVO> homeDetail() {
        ServiceResponse<HomeDetailVO> response = new ServiceResponse<>();
        List<HokageServerVO> serverVOList = serverDao.selectAll().stream()
                .map(serverDO -> ServerDOConverter.converter2VO(serverDO, ConverterTypeEnum.all))
                .collect(Collectors.toList());

        HomeDetailMeta totalVO = this.totalDetail(serverVOList);
        HomeDetailMeta availableVO = this.availableDetail(serverVOList);
        HomeDetailMeta accountVO = this.accountDetail(serverVOList);

        HomeDetailVO detailVO = new HomeDetailVO(totalVO, availableVO, accountVO);

        return response.success(detailVO);

    }

    @Override
    public ServiceResponse<String> acquireMostBusyServerIp() {
        ServiceResponse<String> response = new ServiceResponse<>();
        long end = System.currentTimeMillis();
        long start = end - 10 * 60 * 1000;
        List<HokageServerMetricDO> metricDOList = metricDao.queryByTimeInterval(start, end);
        Map<String, List<HokageServerMetricDO>> metricMap = metricDOList
                .stream()
                .collect(Collectors.groupingBy(HokageServerMetricDO::getServer));

        Pair<String, Double> pair = null;
        for (Map.Entry<String, List<HokageServerMetricDO>> entry : metricMap.entrySet()) {
            double sum = entry.getValue()
                    .stream()
                    .filter(metricDO -> MetricTypeEnum.cpu.getValue().equals(metricDO.getType()))
                    .mapToDouble(HokageServerMetricDO::getValue)
                    .sum();

            if (Objects.isNull(pair) || pair.getValue() < sum) {
                pair = Pair.of(entry.getKey(), sum);
            }
        }
        String serverIp = Objects.isNull(pair) ? StringUtils.EMPTY : pair.getKey();
        return response.success(serverIp);
    }

    private HomeDetailMeta accountDetail(List<HokageServerVO> serverVOList) {
        Map<Long, HokageServerVO> serverMap = serverVOList
                .stream()
                .collect(Collectors.toMap(HokageServerVO::getId, Function.identity(), (o1, o2) -> o1));

        List<HokageSubordinateServerDO> subordinateServerDOList = subordinateServerDao.selectAll();
        List<HokageServerVO> accountServerVOList = subordinateServerDOList.stream()
                .map(subServerDO -> {
                    HokageServerVO serverVO = new HokageServerVO();
                    BeanUtils.copyProperties(serverMap.get(subServerDO.getServerId()), serverVO);
                    serverVO.setAccount(subServerDO.getAccount());
                    serverVO.setLoginType(subServerDO.getLoginType());
                    return serverVO;
                }).collect(Collectors.toList());

        return this.assembleDetailMeta(accountServerVOList);
    }

    private HomeDetailMeta assembleDetailMeta(List<HokageServerVO> serverVOList) {
        HomeDetailMeta meta = new HomeDetailMeta();
        meta.setTotal(serverVOList.size());

        Map<String, List<String>> map = serverVOList.stream()
                .map(HokageServerVO::getServerGroupList)
                .filter(serverGroupList -> !CollectionUtils.isEmpty(serverGroupList))
                .flatMap(Collection::stream)
                .collect(Collectors.groupingBy(Function.identity()));
        Map<String, Integer> groupInfo = new HashMap<>();
        for (Map.Entry<String, List<String>> entry : map.entrySet()) {
            groupInfo.put(entry.getKey(), entry.getValue().size());
        }
        meta.setGroupInfo(groupInfo);

        return meta;
    }

    private HomeDetailMeta availableDetail(List<HokageServerVO> serverVOList) {
        List<HokageServerVO> availableVOList = serverVOList
                .stream()
                .filter(serverVO -> ServerStatusEnum.online.getStatus().equals(serverVO.getStatus()))
                .collect(Collectors.toList());
        return this.assembleDetailMeta(availableVOList);
    }

    private HomeDetailMeta totalDetail(List<HokageServerVO> serverVOList) {
        return this.assembleDetailMeta(serverVOList);
    }
}
