package com.hokage.biz.converter.file;

import com.hokage.biz.converter.Converter;
import com.hokage.biz.response.file.HokageFileProperty;
import com.hokage.ssh.domain.FileProperty;
import com.hokage.util.FileUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

/**
 * @author yiminlin
 * @date 2021/06/04 10:21 pm
 * @description ssh file property to web file property
 **/
@Component
public class SshProperty2WebProperty implements Converter<FileProperty, HokageFileProperty> {
    @Override
    public HokageFileProperty doForward(FileProperty sshFileProperTy) {
        HokageFileProperty property = new HokageFileProperty();

        property.setName(sshFileProperTy.getName());
        property.setOwner(sshFileProperTy.getOwner());
        property.setGroup(sshFileProperTy.getGroup());
        property.setPermission(sshFileProperTy.getTypeAndPermission());
        property.setSize(FileUtil.humanReadable(sshFileProperTy.getSize()));

        if (StringUtils.startsWith(sshFileProperTy.getTypeAndPermission(), "d")) {
            property.setType("directory");
        } else {
            property.setType("file");
        }

        property.setLastAccessTime(StringUtils.substring(sshFileProperTy.getLastAccessTime(), 0, "yyyy-MM-dd HH:mm:ss".length()));

        return property;
    }

    @Override
    public FileProperty doBackward(HokageFileProperty hokageFileProperty) {
        return null;
    }
}
