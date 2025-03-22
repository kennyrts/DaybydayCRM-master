-- Ajout de la colonne api_token à la table users
ALTER TABLE users ADD COLUMN api_token VARCHAR(80) NULL DEFAULT NULL AFTER password;
