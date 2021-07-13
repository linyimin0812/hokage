package com.hokage.ssh.command;

import com.hokage.ssh.enums.LsOptionEnum;
import com.hokage.ssh.enums.OsTypeEnum;

import java.util.List;

/**
 * @author yiminlin
 * @date 2021/05/28 1:59 am
 * @description common command definition
 **/
public interface Command {
    /**
     * specify os type
     * @return os type
     */
    OsTypeEnum os();

    /**
     * list directory contents
     * @param dir directory to list
     * @param optionList command option
     * @return ls command execute result content
     */
    String ls(String dir, List<String> optionList);

    /**
     * acquire memory information
     * @return memory information
     */
    String memInfo();

    /**
     * acquire server bandwidth
     * @return server bandwidth json string
     */
    String bandwidth();

    /**
     * acquire process information
     * @return process information
     */
    String process();

    /**
     * calculate download transfer rate
     * @return download transfer rate
     */
    String downloadTransferRate();

    /**
     * acquire dir path
     * @param dir may be relative path or absolute path
     * @return absolute path
     */
    String pwd(String dir);

    /**
     * acquire cpu information
     * @return cpu information
     */
    String cpuInfo();

    /**
     * acquire last login account
     * @return lastlog command
     */
    String lastLog();

    /**
     * acquire disk partition information
     * @return disk partition information
     */
    String df();

    /**
     * acquire arp cache information
     * @return arp cache information
     */
    String arp();

    /**
     * acquire connection information
     * @return connection information
     */
    String netstat();

    /**
     * create account
     * @param account account
     * @param passwd password
     * @return create account command
     */
    String addUser(String account, String passwd);

    /**
     * delete account
     * @param account account
     * @return del account command
     */
    String delUser(String account);

    /**
     * execute report bash script
     * @return report script execute command
     */
    String report();
}
