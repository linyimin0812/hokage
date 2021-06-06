package com.hokage.ssh.component;

import com.hokage.ssh.SshClient;
import com.hokage.ssh.enums.JSchChannelType;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.Session;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Objects;

/**
 * @author yiminlin
 * @date 2021/06/06 11:53 pm
 * @description jsch sftp channel component for downloading file from server
 **/
@Slf4j
@Component
public class SshSftpComponent {

    @Value("${file.management.download.rate.byte}")
    private int downloadRate;

    /**
     * download a file, and write stream to os
     * @param client ssh client
     * @param file file path
     * @param os output stream
     */
    public void download(SshClient client, String file, OutputStream os) {
        ChannelSftp sftp = null;
        try {
            Session session = client.getSessionOrCreate();
            sftp = (ChannelSftp) session.openChannel(JSchChannelType.SFTP.getValue());
            sftp.connect();
            InputStream in = sftp.get(file);
            byte[] buf = new byte[downloadRate];
            int length = 0;
            do {
                length = in.read(buf, 0, buf.length);
                if (length > 0) {
                    os.write(buf, 0, length);
                }
                os.flush();
            } while (length > 0);
        } catch (Exception e) {
            log.error("SshSftpComponent.download error: {}", e.getMessage());
        } finally {
            if (Objects.nonNull(sftp)) {
                sftp.disconnect();
            }
        }
    }
}
