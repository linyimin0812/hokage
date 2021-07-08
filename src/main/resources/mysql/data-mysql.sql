# 指定表的自增字段初始值
INSERT IGNORE INTO `hokage_sequence` (gmt_create, gmt_modified, name, value) VALUES (now(), now(), 'hokage_user', '1000');
INSERT IGNORE INTO `hokage_sequence` (gmt_create, gmt_modified, name, value) VALUES (now(), now(), 'hokage_supervisor_subordinate', '1000');
INSERT IGNORE INTO `hokage_sequence` (gmt_create, gmt_modified, name, value) VALUES (now(), now(), 'hokage_server', '1000');
INSERT IGNORE INTO `hokage_sequence` (gmt_create, gmt_modified, name, value) VALUES (now(), now(), 'hokage_server_group', '1000');
INSERT IGNORE INTO `hokage_sequence` (gmt_create, gmt_modified, name, value) VALUES (now(), now(), 'hokage_subordinate_server', '1000');
INSERT IGNORE INTO `hokage_sequence` (gmt_create, gmt_modified, name, value) VALUES (now(), now(), 'hokage_supervisor_server', '1000');
INSERT IGNORE INTO `hokage_sequence` (gmt_create, gmt_modified, name, value) VALUES (now(), now(), 'hokage_task_result', '1000');
INSERT IGNORE INTO `hokage_sequence` (gmt_create, gmt_modified, name, value) VALUES (now(), now(), 'hokage_security_group', '1000');
INSERT IGNORE INTO `hokage_sequence` (gmt_create, gmt_modified, name, value) VALUES (now(), now(), 'hokage_fixed_date_task', '1000');
INSERT IGNORE INTO `hokage_sequence` (gmt_create, gmt_modified, name, value) VALUES (now(), now(), 'hokage_server_ssh_key_content', '1000');

INSERT IGNORE INTO `hokage_server_report_info_handler` (
    id,
    gmt_create,
    gmt_modified,
    handler_ip,
    start_time,
    version
) VALUES (
    1,
    now(),
    now(),
    '0.0.0.0',
    1592150400000,
    1
);

# 添加管理员
INSERT IGNORE INTO hokage.hokage_user (
    id,
    gmt_create,
    gmt_modified,
    username,
    passwd,
    role,
    email,
    is_subscribed
) VALUES (
    2,
    NOW(),
    NOW(),
    'banzhe',               # 超级管理员姓名
    '$2a$10$.b8fqqoQp8PgrO0pwU1GnegjQBjDcWSad7iFac3FmnB2UmwtlQKI2', # 密码, 使用Bcrypt加密，请在 https://www.javainuse.com/onlineBcrypt 指定生成
    100,                    # 超级管理员角色（指定为100）
    'banzhe@eaxmple.com',   # 超级管理员邮箱（用于登录）
    0                       # 是否订阅
);