ALTER TABLE sub_goal
    MODIFY slot_num INT NOT NULL;

ALTER TABLE sub_goal
    ADD CONSTRAINT chk_sub_goal_slot_range CHECK (slot_num BETWEEN 1 AND 8);

ALTER TABLE sub_goal
    ADD CONSTRAINT uq_main_goal_slot UNIQUE (main_goal_id, slot_num);