-- Désactiver les contraintes de clé étrangère
SET FOREIGN_KEY_CHECKS = 0;

-- Supprimer toutes les tables
DROP TABLE IF EXISTS `absences`;
DROP TABLE IF EXISTS `activities`;
DROP TABLE IF EXISTS `appointments`;
DROP TABLE IF EXISTS `business_hours`;
DROP TABLE IF EXISTS `clients`;
DROP TABLE IF EXISTS `comments`;
DROP TABLE IF EXISTS `contacts`;
DROP TABLE IF EXISTS `credit_lines`;
DROP TABLE IF EXISTS `credit_notes`;
DROP TABLE IF EXISTS `departments`;
DROP TABLE IF EXISTS `documents`;
DROP TABLE IF EXISTS `files`;
DROP TABLE IF EXISTS `industries`;
DROP TABLE IF EXISTS `integrations`;
DROP TABLE IF EXISTS `invoice_lines`;
DROP TABLE IF EXISTS `invoices`;
DROP TABLE IF EXISTS `leads`;
DROP TABLE IF EXISTS `migrations`;
DROP TABLE IF EXISTS `notifications`;
DROP TABLE IF EXISTS `oauth_access_tokens`;
DROP TABLE IF EXISTS `oauth_auth_codes`;
DROP TABLE IF EXISTS `oauth_clients`;
DROP TABLE IF EXISTS `oauth_personal_access_clients`;
DROP TABLE IF EXISTS `oauth_refresh_tokens`;
DROP TABLE IF EXISTS `offers`;
DROP TABLE IF EXISTS `password_resets`;
DROP TABLE IF EXISTS `payments`;
DROP TABLE IF EXISTS `permission_role`;
DROP TABLE IF EXISTS `permissions`;
DROP TABLE IF EXISTS `products`;
DROP TABLE IF EXISTS `projects`;
DROP TABLE IF EXISTS `role_user`;
DROP TABLE IF EXISTS `roles`;
DROP TABLE IF EXISTS `settings`;
DROP TABLE IF EXISTS `statuses`;
DROP TABLE IF EXISTS `subscriptions`;
DROP TABLE IF EXISTS `tasks`;
DROP TABLE IF EXISTS `users`;
DROP TABLE IF EXISTS `department_user`;
DROP TABLE IF EXISTS `mails`;

-- Réactiver les contraintes de clé étrangère
SET FOREIGN_KEY_CHECKS = 1;