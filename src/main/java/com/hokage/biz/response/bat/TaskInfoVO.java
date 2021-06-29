package com.hokage.biz.response.bat;

import com.hokage.persistence.dataobject.HokageFixedDateTaskDO;
import lombok.Data;

/**
 * @author yiminlin
 * @date 2021/06/30 1:44 am
 * @description task info, include task DO and task execute result detail
 **/
@Data
public class TaskInfoVO {
    private HokageFixedDateTaskVO commandVO;
    private TaskResultDetailVO taskResultDetailVO;
}
