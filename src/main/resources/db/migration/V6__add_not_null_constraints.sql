ALTER TABLE member
    MODIFY email varchar(255) NOT NULL,
    MODIFY password varchar(255) NOT NULL,
    MODIFY nickname varchar(100) NOT NULL,
    MODIFY member_status enum('ACTIVITY','WITHDRAW') NOT NULL,
    MODIFY member_role enum('ROLE_ADMIN','ROLE_USER') NOT NULL;

ALTER TABLE main_goal
    MODIFY main_goal_name VARCHAR(255) NOT NULL,
    MODIFY is_representative TINYINT(1) NOT NULL,
    MODIFY main_goal_status enum('ACTIVITY','ATTAINMENT','PAUSE') NOT NULL,
    MODIFY member_id BIGINT NOT NULL;

ALTER TABLE sub_goal
    MODIFY sub_goal_name VARCHAR(255) NOT NULL,
    MODIFY is_store TINYINT(1) NOT NULL,
    MODIFY main_goal_id BIGINT NOT NULL;

ALTER TABLE daily_action
    MODIFY daily_action_title VARCHAR(255) NOT NULL,
    MODIFY target_num INT NOT NULL,
    MODIFY is_store TINYINT(1) NOT NULL,
    MODIFY sub_goal_id BIGINT NOT NULL;

ALTER TABLE daily_progress
    MODIFY checked_date DATE NOT NULL,
    MODIFY daily_action_id BIGINT NOT NULL;