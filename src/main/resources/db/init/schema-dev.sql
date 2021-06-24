CREATE DATABASE betting;
CREATE USER betting_${user.name} PASSWORD 'betting';
CREATE SCHEMA betting_${user.name} AUTHORIZATION betting_${user.name};