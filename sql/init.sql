create table sys_category
(
    id          varchar(64)             not null comment 'id'
        primary key,
    module      int                     null comment '模块',
    parent_id   varchar(64) default '0' null comment '父级ID',
    name        varchar(50)             null comment '名称',
    note        varchar(255)            null comment '备注',
    create_by   varchar(64)             null comment '创建人',
    create_time datetime                null comment '创建时间',
    update_by   varchar(64)             null comment '更新人',
    update_time datetime                null comment '更新时间',
    constraint sys_category_pk
        unique (module, name, parent_id)
) comment '通用分类' row_format = COMPACT;

create table sys_permission
(
    id          varchar(32)                           not null comment '主键id'
        primary key,
    parent_id   varchar(32) default '0'               null comment '父id',
    name        varchar(15)                           not null comment '名称',
    code        varchar(20)                           not null comment '权限标识符',
    type        varchar(10)                           not null comment '资源类型：menu-菜单，button-按钮，api-接口，dataScope-数据权限',
    sort        int         default 0                 null comment '排序值',
    icon        varchar(50)                           null comment '图标',
    url         varchar(100)                          null comment 'URL路径',
    method      varchar(5)                            null comment 'HTTP方法',
    attrs       text                                  null comment '扩展属性（JSON格式，按type约定结构）',
    status      tinyint     default 1                 null comment '状态：0-禁用，1-启用',
    remark      varchar(50)                           null comment '备注',
    create_time datetime    default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time datetime    default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    constraint sys_permission_pk
        unique (code, type)
) comment '权限' charset = utf8mb4;

create table sys_role
(
    id          varchar(32)                         not null comment '主键id'
        primary key,
    name        varchar(15)                         not null comment '名称',
    code        varchar(20)                         not null comment '编码',
    remark      varchar(50)                         null comment '备注',
    create_time timestamp default CURRENT_TIMESTAMP null comment '创建时间',
    update_time timestamp default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间',
    constraint sys_role_pk
        unique (code)
) comment '角色';

create table sys_role_permission
(
    id            varchar(32) charset latin1 default ''                not null comment '主键id'
        primary key,
    type          varchar(10)                                          null comment '资源类型：menu-菜单，button-按钮，api-接口，dataScope-数据权限',
    role_id       varchar(32)                                          not null comment '角色id',
    permission_id varchar(32)                                          not null comment '权限id',
    create_time   timestamp                  default CURRENT_TIMESTAMP null comment '创建时间',
    constraint sys_role_permission_pk
        unique (role_id, permission_id)
) comment '角色权限';

create index sys_role_permission_permission_id_index
    on sys_role_permission (permission_id);

create table sys_user
(
    id          varchar(32) default '' not null comment '主键id'
        primary key,
    account     varchar(64)            null comment '账号',
    password    varchar(64)            null comment '密码',
    user_name   varchar(20)            null comment '用户名',
    phone       varchar(20)            null comment '手机号',
    gender      tinyint(1)             null comment '性别',
    avatar      text                   null comment '头像',
    create_time datetime               null comment '创建时间',
    update_time datetime               null comment '更新时间',
    constraint sys_user_account_uindex
        unique (account)
) comment '用户';

create table sys_user_role
(
    id          varchar(32)                         not null comment '主键id'
        primary key,
    user_id     varchar(32)                         not null comment '用户id',
    role_id     varchar(32)                         not null comment '角色id',
    create_time timestamp default CURRENT_TIMESTAMP null comment '创建时间',
    constraint sys_user_role_pk
        unique (user_id, role_id)
) comment '用户角色';

create index sys_user_role_role_id_index
    on sys_user_role (role_id);

create table sys_log
(
    id       varchar(64) not null comment 'id'
        primary key,
    trace_id varchar(64) null comment 'trace_id',
    level    varchar(10) null comment '日志级别',
    time     datetime    null comment '时间',
    content  text        null comment '日志内容'
) comment '日志' row_format = COMPACT;

create index sys_log_time_index
    on sys_log (time);

create index sys_log_trace_id_index
    on sys_log (trace_id);