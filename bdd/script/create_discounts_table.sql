-- Création de la table des remises globales
CREATE TABLE discounts (
    id INT AUTO_INCREMENT PRIMARY KEY,
    rate DECIMAL(5,2) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Insertion d'un taux de remise initial de 0%
INSERT INTO discounts (rate) VALUES (10.00); 