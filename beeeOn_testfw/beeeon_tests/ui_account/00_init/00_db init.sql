INSERT INTO gateway (gateway_id, home_name, home_timezone) VALUES (1, 'home_one', 1);
INSERT INTO gateway (gateway_id, home_name, home_timezone) VALUES (2, 'home_two', -1);

INSERT INTO device (device_euid, device_type, device_name, refresh, battery, quality, init, involved, measured_at, location_id, gateway_id)
 VALUES (101, 0, 'dev101', 10, 10, 10, 1, 10000, 10010, null, 1);
INSERT INTO device (device_euid, device_type, device_name, refresh, battery, quality, init, involved, measured_at, location_id, gateway_id)
 VALUES (201, 0, 'dev201', 10, 10, 10, 1, 10000, 10010, null, 2);
INSERT INTO device (device_euid, device_type, device_name, refresh, battery, quality, init, involved, measured_at, location_id, gateway_id)
 VALUES (202, 1, 'dev202', 10, 10, 10, 1, 10000, 10010, null, 2);
 
INSERT INTO module (device_euid, module_id, measured_value)
 VALUES (101, 0, 100);
INSERT INTO module (device_euid, module_id, measured_value)
 VALUES (101, 1, 50);
INSERT INTO module (device_euid, module_id, measured_value)
 VALUES (201, 0, 100);
INSERT INTO module (device_euid, module_id, measured_value)
 VALUES (201, 1, 100);
INSERT INTO module (device_euid, module_id, measured_value)
 VALUES (202, 0, 100);
 INSERT INTO module (device_euid, module_id, measured_value)
 VALUES (202, 1, 100);
INSERT INTO module (device_euid, module_id, measured_value)
 VALUES (202, 2, 100);

INSERT INTO users(mail,password) VALUES ('test@test.cz', 'testPASS');      