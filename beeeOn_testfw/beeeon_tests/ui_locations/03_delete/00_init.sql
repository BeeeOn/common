INSERT INTO gateway (gateway_id, home_name, home_timezone) VALUES (100, 'home_one', 1);

INSERT INTO users(mail,password,given_name,family_name,gender) VALUES ('test@beeeon.cz', 'beeeonPass1','tester', 'dummy', 'male'); 

INSERT INTO user_gateway(user_id, gateway_id, permission) VALUES 
((SELECT user_id FROM users WHERE mail = 'test@beeeon.cz'), 100, 'owner');    

INSERT INTO location(location_id, gateway_id, kind, location_name) VALUES 
(1000000000, 100, 1, 'loc1');    
