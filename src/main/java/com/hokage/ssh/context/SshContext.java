package com.hokage.ssh.context;

import com.alibaba.fastjson.JSON;
import com.hokage.websocket.domain.TerminalSize;
import lombok.Data;

/**
 * @author yiminlin
 */
@Data
public class SshContext {
    /**
     * server id, primary key
     */
    private Long id;
    /**
     * server ip
     */
    private String ip;
    /**
     * ssh port
     */
    private String sshPort;
    /**
     * ssh account
     */
    private String account;
    /**
     * login type
     */
    private Integer loginType;
    /**
     * password
     */
    private String passwd;

    private TerminalSize size;

    @Override
    public String toString() {
        return "{" +
                "id=" + id +
                ", ip=" + ip +
                ", sshPort=" + sshPort +
                ", account=" + account +
                ", loginType=" + loginType +
                ", size=" + size +
                '}';
    }
}
