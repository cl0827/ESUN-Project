DELIMITER //

CREATE PROCEDURE add_comment(
    IN p_user_id INT,
    IN p_post_id INT,
    IN p_content TEXT
)
BEGIN
    INSERT INTO comment (user_id, post_id, content, created_at)
    VALUES (p_user_id, p_post_id, p_content, NOW());
END //

DELIMITER ;