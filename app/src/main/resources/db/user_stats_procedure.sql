SELECT 
    ROW_NUMBER() OVER (ORDER BY gsr.ended_at ASC) AS game_number,
    au.username AS opponent_name,
    CASE 
        WHEN gsr.winner_player_id = 2 THEN TRUE 
        ELSE FALSE 
    END AS you_win,
    ROUND(EXTRACT(EPOCH FROM (gsr.ended_at - gsr.started_at))) AS duration
FROM 
    game_session_record gsr
JOIN 
    app_user au 
ON 
    au.id = CASE 
        WHEN gsr.second_player_id = 2 THEN gsr.first_player_id 
        ELSE gsr.second_player_id 
    END
WHERE 
    gsr.first_player_id = 2 OR gsr.second_player_id = 2
ORDER BY 
    gsr.ended_at DESC
LIMIT 10;
