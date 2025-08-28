ALTER TABLE main_goal
ADD CONSTRAINT fk_main_goal_member
FOREIGN KEY (member_id) REFERENCES member(member_id)
ON DELETE RESTRICT;

ALTER TABLE sub_goal
ADD CONSTRAINT fk_sub_goal_main_goal
FOREIGN KEY (main_goal_id) REFERENCES main_goal(main_goal_id)
ON DELETE CASCADE;

ALTER TABLE daily_action
ADD CONSTRAINT fk_daily_action_sub_goal
FOREIGN KEY (sub_goal_id) REFERENCES sub_goal(sub_goal_id)
ON DELETE CASCADE;

ALTER TABLE daily_progress
ADD CONSTRAINT fk_daily_progress_daily_action
FOREIGN KEY (daily_action_id) REFERENCES daily_action(daily_action_id)
ON DELETE CASCADE;