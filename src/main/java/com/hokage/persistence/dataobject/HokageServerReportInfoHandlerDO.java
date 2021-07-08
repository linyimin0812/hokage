package com.hokage.persistence.dataobject;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author yiminlin
 * @date 2021/07/08 9:13 am
 * @description hokage server report info handler data object
 **/
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class HokageServerReportInfoHandlerDO extends HokageBaseDO {
    private Long id;
    private String handlerIp;
    private Integer handlerPort;
    private Long startTime;
    private Integer version;
}
