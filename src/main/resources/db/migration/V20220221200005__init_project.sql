INSERT INTO healthy.project (name, code, created_at, created_by, last_modified_at, last_modified_by, deleted) VALUES('健康未来', 'health_future', '2022-02-21 10:12:23', 'admin', NULL, NULL, 0);

ALTER TABLE healthy.baby ADD project_id BIGINT UNSIGNED DEFAULT 0 NULL COMMENT '所属项目ID';

ALTER TABLE healthy.baby_history ADD project_id BIGINT UNSIGNED NULL COMMENT '所属项目ID';

ALTER TABLE healthy.carer ADD project_id BIGINT UNSIGNED NULL COMMENT '所属项目ID';

ALTER TABLE healthy.carer_history ADD project_id BIGINT UNSIGNED NULL COMMENT '所属项目ID';

ALTER TABLE healthy.community_house_worker ADD project_id BIGINT UNSIGNED NULL COMMENT '所属项目ID';

ALTER TABLE healthy.community_house_worker_history ADD project_id BIGINT UNSIGNED NULL COMMENT '所属项目ID';

ALTER TABLE healthy.curriculum ADD project_id BIGINT UNSIGNED NULL COMMENT '所属项目ID';

ALTER TABLE healthy.curriculum_history ADD project_id BIGINT UNSIGNED NULL COMMENT '所属项目ID';

ALTER TABLE healthy.lesson ADD project_id BIGINT UNSIGNED NULL COMMENT '所属项目ID';

ALTER TABLE healthy.lesson_history ADD project_id BIGINT UNSIGNED NULL COMMENT '所属项目ID';

ALTER TABLE healthy.questionnaire ADD project_id BIGINT UNSIGNED NULL COMMENT '所属项目ID';

ALTER TABLE healthy.questionnaire_history ADD project_id BIGINT UNSIGNED NULL COMMENT '所属项目ID';

ALTER TABLE healthy.module ADD project_id BIGINT UNSIGNED NULL COMMENT '所属项目ID';

ALTER TABLE healthy.module_history ADD project_id BIGINT UNSIGNED NULL COMMENT '所属项目ID';

ALTER TABLE healthy.user ADD project_id BIGINT UNSIGNED NULL COMMENT '所属项目ID';

ALTER TABLE healthy.user_history ADD project_id BIGINT UNSIGNED NULL COMMENT '所属项目ID';

ALTER TABLE healthy.visit ADD project_id BIGINT UNSIGNED NULL COMMENT '所属项目ID';

ALTER TABLE healthy.visit_history ADD project_id BIGINT UNSIGNED NULL COMMENT '所属项目ID';

