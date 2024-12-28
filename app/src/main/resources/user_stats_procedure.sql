CREATE OR REPLACE PROCEDURE getGameStatsForUser(
    IN player_id INTEGER,
    IN query_limit INTEGER
)
LANGUAGE plpgsql
AS $$
BEGIN
    -- Check if player_id is NULL
    IF player_id IS NULL THEN
      RAISE EXCEPTION 'player_id cannot be NULL';
      RETURN;
    END IF;
    
    -- The main query
    SELECT 
        ROW_NUMBER() OVER (ORDER BY gsr.ended_at ASC) AS game_number,
        au.username AS opponent_name,
        CASE 
            WHEN gsr.winner_player_id = player_id THEN TRUE 
            ELSE FALSE 
        END AS you_win,
        ROUND(EXTRACT(EPOCH FROM (gsr.ended_at - gsr.started_at))) AS duration
    FROM 
        game_session_record gsr
    JOIN 
        app_user au 
    ON 
        au.id = CASE 
            WHEN gsr.second_player_id = player_id THEN gsr.first_player_id 
            ELSE gsr.second_player_id 
        END
    WHERE 
        gsr.first_player_id = player_id OR gsr.second_player_id = player_id
    ORDER BY 
        gsr.ended_at DESC
    LIMIT query_limit;
END;
$$;
