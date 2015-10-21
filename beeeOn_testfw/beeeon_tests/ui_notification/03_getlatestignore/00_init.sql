INSERT INTO users(mail,password,given_name,family_name,gender) VALUES ('test@beeeon.cz', 'beeeonPass1','tester', 'dummy', 'male');      
INSERT INTO notifications(text, level, timestamp, fk_user_id, read, name) VALUES 
('notif', 2, 1000 (SELECT user_id FROM users WHERE mail = 'test@beeeon.cz'), f, 'name');      
INSERT INTO notifications(text, level, timestamp, fk_user_id, read, name) VALUES 
('notif2', 2, 1010 (SELECT user_id FROM users WHERE mail = 'test@beeeon.cz'), f, 'name2');      
      