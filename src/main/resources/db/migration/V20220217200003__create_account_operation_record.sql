CREATE TABLE healthy.account_operation_record (
                                                  id BIGINT UNSIGNED auto_increment NOT NULL,
                                                  account_id BIGINT UNSIGNED NULL COMMENT '账号ID',
                                                  account_type varchar(20) NOT NULL COMMENT '账号类型(BABY,CHW,SUPERVISOR)',
                                                  close_time DATETIME NULL COMMENT '账户关闭时间',
                                                  revert_time DATETIME NULL COMMENT '恢复时间',
                                                  revert BIT NULL COMMENT '是否恢复',
                                                  created_at DATETIME NULL,
                                                  created_by varchar(64) NULL,
                                                  last_modified_by varchar(64) NULL,
                                                  last_modified_at DATETIME NULL,
                                                  deleted BIT NULL,
                                                  CONSTRAINT account_operation_record_pk PRIMARY KEY (id)
)
    ENGINE=InnoDB
DEFAULT CHARSET=utf8mb4
COLLATE=utf8mb4_bin
COMMENT='账号操作记录';