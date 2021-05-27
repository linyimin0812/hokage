package com.hokage.websocket.domain;

import lombok.Data;

/**
 * @author yiminlin
 * @date 2021/05/28 12:30 am
 * @description terminal resize property
 **/
@Data
public class TerminalSize {
    private Integer cols;
    private Integer rows;
}
