INSERT INTO gateway (gateway_id, home_name, home_timezone) VALUES (100, 'home_one', 1);

INSERT INTO users(mail,password) VALUES ('test@beeeon.cz', 'beeeonPass1');
   
INSERT INTO user_gateway(user_id, gateway_id, permission) VALUES 
  ( (SELECT user_id FROM users WHERE mail='test@beeeon.cz') , 100, 'owner');