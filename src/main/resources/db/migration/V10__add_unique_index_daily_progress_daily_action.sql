CREATE UNIQUE INDEX uq_progress_action_date
    ON daily_progress(daily_action_id, checked_date);

DROP INDEX idx_progress_action ON daily_progress;