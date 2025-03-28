-- Ajout de la colonne api_token à la table users
ALTER TABLE users ADD COLUMN api_token VARCHAR(80) NULL DEFAULT NULL AFTER password;

-- Création de la table des remises globales
CREATE TABLE discounts (
    id INT AUTO_INCREMENT PRIMARY KEY,
    rate DECIMAL(5,2) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Insertion d'un taux de remise initial de 10%
INSERT INTO discounts (rate) VALUES (10.00);
