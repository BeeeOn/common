INSERT INTO gateway (gateway_id, home_name, home_timezone) VALUES (100, 'home_one', 1);

INSERT INTO users(mail,password,given_name,family_name,gender) VALUES ('test@beeeon.cz', 'beeeonPass1','tester', 'dummy', 'male'); 

INSERT INTO user_gateway(user_id, gateway_id, permission) VALUES 
((SELECT user_id FROM users WHERE mail = 'test@beeeon.cz'), 100, 'owner');    
INSERT INTO device (device_euid, device_type, device_name, refresh, battery, quality, init, involved, measured_at, location_id, gateway_id)
 VALUES (201, 0, 'dev201', 20, 30, 40, 1, 10001, 10011, null, 100);
 
INSERT INTO module (device_euid, module_id, measured_value)
 VALUES (201, 0, 110);
INSERT INTO module (device_euid, module_id, measured_value)
 VALUES (201, 1, 102);
 
 
INSERT INTO log (device_euid, module_id, measured_value, measured_at)
 VALUES (201, 0, 10, 0);
INSERT INTO log (device_euid, module_id, measured_value, measured_at)
 VALUES (201, 0, 11, 100);
INSERT INTO log (device_euid, module_id, measured_value, measured_at)
 VALUES (201, 1, 10, 0);
INSERT INTO log (device_euid, module_id, measured_value, measured_at)
 VALUES (201, 1, 11, 100);
INSERT INTO log (device_euid, module_id, measured_value, measured_at)
 VALUES (201, 1, 12, 200);
INSERT INTO log (device_euid, module_id, measured_value, measured_at)
 VALUES (201, 1, 13, 300);
INSERT INTO log (device_euid, module_id, measured_value, measured_at)
 VALUES (201, 1, 14, 400);
INSERT INTO log (device_euid, module_id, measured_value, measured_at)
 VALUES (201, 1, 15, 425);
INSERT INTO log (device_euid, module_id, measured_value, measured_at)
 VALUES (201, 1, 1000, 450);
 
 