INSERT INTO users(mail,password,given_name,family_name,gender) VALUES ('test@beeeon.cz', 'beeeonPass1','tester', 'dummy', 'male');      
INSERT INTO push_notification_service(user_id, service_reference_id) VALUES 
((SELECT user_id FROM users WHERE mail = 'test@beeeon.cz'), 'referenceid');      
      