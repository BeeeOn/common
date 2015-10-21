INSERT INTO gateway (gateway_id, home_name, home_timezone) VALUES (100, 'home_one', 1);

INSERT INTO users(mail,password,given_name,family_name,gender) VALUES ('test@beeeon.cz', 'beeeonPass1','tester', 'dummy', 'male'); 

INSERT INTO user_gateway(user_id, gateway_id, permission) VALUES 
((SELECT user_id FROM users WHERE mail = 'test@beeeon.cz'), 100, 'owner');    

INSERT INTO location(location_id, gateway_id, kind, location_name) VALUES 
(1000000000,100, 1, 'loc1');    


INSERT INTO device (device_euid, device_type, device_name, refresh, battery, quality, init, involved, measured_at, location_id, gateway_id)
 VALUES (201, 0, 'dev201', 20, 30, 40, 0, 10001, 10011, 1000000000, 100);
INSERT INTO device (device_euid, device_type, device_name, refresh, battery, quality, init, involved, measured_at, location_id, gateway_id)
 VALUES (202, 1, 'dev202', 50, 60, 70, 0, 10002, 10012, null, 100);
 
INSERT INTO module (device_euid, module_id, measured_value)
 VALUES (201, 0, 110);
INSERT INTO module (device_euid, module_id, measured_value)
 VALUES (201, 1, 102);
INSERT INTO module (device_euid, module_id, measured_value)
 VALUES (202, 0, 103);
 INSERT INTO module (device_euid, module_id, measured_value)
 VALUES (202, 1, 104);
INSERT INTO module (device_euid, module_id, measured_value)
 VALUES (202, 2, 105);