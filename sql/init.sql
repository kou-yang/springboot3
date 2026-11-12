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
    deleted     datetime                null comment '逻辑删除',
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
    deleted     datetime                              null comment '逻辑删除',
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
    deleted     datetime                          null comment '逻辑删除',
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
    account     varchar(64)            not null comment '账号',
    password    varchar(64)            null comment '密码',
    user_name   varchar(20)            null comment '用户名',
    phone       varchar(20)            null comment '手机号',
    gender      tinyint(1)             null comment '性别',
    avatar      text                   null comment '头像',
    type        tinyint    default 1   null comment '用户类型：0-后台，1-移动端',
    status      tinyint(1) default 1   null comment '状态：0-禁用，1-启用',
    create_time datetime               null comment '创建时间',
    update_time datetime               null comment '更新时间',
    deleted     datetime               null comment '逻辑删除',
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
    content  text        null comment '日志内容',
    user_id  varchar(64) null comment '操作用户id'
)
    comment '日志' row_format = COMPACT;

create index sys_log_time_index
    on sys_log (time);

create index sys_log_trace_id_index
    on sys_log (trace_id);

-- 初始管理员账号：admin / 1，首次登录后请及时修改密码
insert into sys_user (id, account, password, user_name, type, create_time)
values ('1', 'admin', '$2a$12$lp/sf4ubdsnQlpUEnQ8YO.s4J3RMx4rI/AWI6rS3hUkKlbpS57rZ.', '管理员', 0, now());

-- 初始角色：管理员（持有全部接口权限）
insert into sys_role (id, name, code, remark)
values ('1', '管理员', 'admin', '初始角色，持有全部接口权限');

insert into sys_user_role (id, user_id, role_id)
values ('1', '1', '1');

-- 接口权限（与 Controller 上 @PreAuth 的权限码一一对应）
insert into sys_permission (id, name, code, type)
values ('1', '用户保存', 'user:save', 'api'),
       ('2', '用户删除', 'user:delete', 'api'),
       ('3', '用户更新', 'user:update', 'api'),
       ('4', '用户分页查询', 'user:page', 'api'),
       ('5', '用户详情', 'user:detail', 'api'),
       ('6', '角色保存', 'role:save', 'api'),
       ('7', '角色删除', 'role:delete', 'api'),
       ('8', '角色更新', 'role:update', 'api'),
       ('9', '角色分页查询', 'role:page', 'api'),
       ('10', '角色详情', 'role:detail', 'api'),
       ('11', '角色查询全部', 'role:all', 'api'),
       ('12', '权限保存', 'perm:save', 'api'),
       ('13', '权限删除', 'perm:delete', 'api'),
       ('14', '权限更新', 'perm:update', 'api'),
       ('15', '权限分页查询', 'perm:page', 'api'),
       ('16', '权限详情', 'perm:detail', 'api'),
       ('17', '权限查询全部', 'perm:all', 'api'),
       ('18', '用户角色保存', 'userRole:save', 'api'),
       ('19', '用户角色删除', 'userRole:delete', 'api'),
       ('20', '用户角色更新', 'userRole:update', 'api'),
       ('21', '用户角色分页查询', 'userRole:page', 'api'),
       ('22', '用户角色详情', 'userRole:detail', 'api'),
       ('23', '用户角色查询全部', 'userRole:all', 'api'),
       ('24', '分配角色', 'userRole:distribute', 'api'),
       ('25', '查询用户角色', 'userRole:roles', 'api'),
       ('26', '角色权限保存', 'rolePerm:save', 'api'),
       ('27', '角色权限删除', 'rolePerm:delete', 'api'),
       ('28', '角色权限分页查询', 'rolePerm:page', 'api'),
       ('29', '分配权限', 'rolePerm:distribute', 'api'),
       ('30', '查询角色权限', 'rolePerm:permissions', 'api'),
       ('31', '分类保存', 'category:save', 'api'),
       ('32', '分类删除', 'category:delete', 'api'),
       ('33', '分类更新', 'category:update', 'api'),
       ('34', '分类分页查询', 'category:page', 'api'),
       ('35', '分类详情', 'category:detail', 'api'),
       ('36', '分类查询全部', 'category:all', 'api'),
       ('37', '日志删除', 'log:delete', 'api'),
       ('38', '日志批量删除', 'log:batchDelete', 'api'),
       ('39', '日志分页查询', 'log:page', 'api'),
       ('40', '日志游标分页查询', 'log:pageCursor', 'api');

-- 管理员角色绑定全部接口权限
insert into sys_role_permission (id, type, role_id, permission_id)
values ('rp01', 'api', '1', '1'), ('rp02', 'api', '1', '2'), ('rp03', 'api', '1', '3'), ('rp04', 'api', '1', '4'),
       ('rp05', 'api', '1', '5'), ('rp06', 'api', '1', '6'), ('rp07', 'api', '1', '7'), ('rp08', 'api', '1', '8'),
       ('rp09', 'api', '1', '9'), ('rp10', 'api', '1', '10'), ('rp11', 'api', '1', '11'), ('rp12', 'api', '1', '12'),
       ('rp13', 'api', '1', '13'), ('rp14', 'api', '1', '14'), ('rp15', 'api', '1', '15'), ('rp16', 'api', '1', '16'),
       ('rp17', 'api', '1', '17'), ('rp18', 'api', '1', '18'), ('rp19', 'api', '1', '19'), ('rp20', 'api', '1', '20'),
       ('rp21', 'api', '1', '21'), ('rp22', 'api', '1', '22'), ('rp23', 'api', '1', '23'), ('rp24', 'api', '1', '24'),
       ('rp25', 'api', '1', '25'), ('rp26', 'api', '1', '26'), ('rp27', 'api', '1', '27'), ('rp28', 'api', '1', '28'),
       ('rp29', 'api', '1', '29'), ('rp30', 'api', '1', '30'), ('rp31', 'api', '1', '31'), ('rp32', 'api', '1', '32'),
       ('rp33', 'api', '1', '33'), ('rp34', 'api', '1', '34'), ('rp35', 'api', '1', '35'), ('rp36', 'api', '1', '36'),
       ('rp37', 'api', '1', '37'), ('rp38', 'api', '1', '38'), ('rp39', 'api', '1', '39'), ('rp40', 'api', '1', '40');