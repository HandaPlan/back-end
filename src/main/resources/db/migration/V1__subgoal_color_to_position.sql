ALTER TABLE sub_goal
ADD COLUMN slot_num INT;

UPDATE sub_goal
SET slot_num = CASE color
    WHEN 'COLOR_1' THEN 1
    WHEN 'COLOR_2' THEN 2
    WHEN 'COLOR_3' THEN 3
    WHEN 'COLOR_4' THEN 4
    WHEN 'COLOR_5' THEN 5
    WHEN 'COLOR_6' THEN 6
    WHEN 'COLOR_7' THEN 7
    WHEN 'COLOR_8' THEN 8
    ELSE 0
END;