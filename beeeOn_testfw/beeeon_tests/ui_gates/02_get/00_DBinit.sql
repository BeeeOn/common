INSERT INTO gateway (gateway_id, home_name, home_timezone, altitude) VALUES (100, 'home_one', 1, 120);

INSERT INTO users(mail,password, family_name) VALUES ('test@beeeon.cz', 'beeeonPass1','name');
INSERT INTO users(mail,password) VALUES ('test2@beeeon.cz', 'beeeonPass2');

INSERT INTO user_gateway(user_id, gateway_id, permission) VALUES 
  ( (SELECT user_id FROM users WHERE mail='test@beeeon.cz') , 100, 'owner');
  INSERT INTO user_gateway(user_id, gateway_id, permission) VALUES 
  ( (SELECT user_id FROM users WHERE mail='test2@beeeon.cz') , 100, 'admin');
   