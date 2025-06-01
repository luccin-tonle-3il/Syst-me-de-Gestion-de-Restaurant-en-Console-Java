-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Hôte : 127.0.0.1
-- Généré le : lun. 02 juin 2025 à 00:11
-- Version du serveur : 10.4.32-MariaDB
-- Version de PHP : 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de données : `restaurant_db`
--

-- --------------------------------------------------------

--
-- Structure de la table `commande`
--

CREATE TABLE `commande` (
  `id_commande` int(11) NOT NULL,
  `id_table` int(10) UNSIGNED NOT NULL,
  `date_commande` datetime NOT NULL,
  `etat` varchar(20) DEFAULT 'Nouvelle',
  `montant_total` decimal(10,2) DEFAULT 0.00
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `commande`
--

INSERT INTO `commande` (`id_commande`, `id_table`, `date_commande`, `etat`, `montant_total`) VALUES
(3, 4, '2025-06-01 20:59:13', 'Nouvelle', 0.00);

-- --------------------------------------------------------

--
-- Structure de la table `commande_item`
--

CREATE TABLE `commande_item` (
  `commande_id` int(11) NOT NULL,
  `menu_item_id` int(11) NOT NULL,
  `quantite` int(11) NOT NULL CHECK (`quantite` > 0)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `commande_item`
--

INSERT INTO `commande_item` (`commande_id`, `menu_item_id`, `quantite`) VALUES
(3, 3, 4);

-- --------------------------------------------------------

--
-- Structure de la table `menu_item`
--

CREATE TABLE `menu_item` (
  `id` int(11) NOT NULL,
  `nom` varchar(100) NOT NULL,
  `prix` double NOT NULL,
  `type` enum('plat','boisson') NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `menu_item`
--

INSERT INTO `menu_item` (`id`, `nom`, `prix`, `type`) VALUES
(1, 'Pizza Margherita', 10.5, 'plat'),
(1, 'Coca-Cola', 3, 'boisson'),
(2, 'Spaghetti Bolognese', 12, 'plat'),
(2, 'Eau minérale', 2, 'boisson'),
(3, 'okok salee', 15, 'plat');

-- --------------------------------------------------------

--
-- Structure de la table `paiement`
--

CREATE TABLE `paiement` (
  `id_paiement` int(11) NOT NULL,
  `id_commande` int(11) NOT NULL,
  `montant` decimal(10,2) NOT NULL,
  `date_paiement` datetime DEFAULT current_timestamp(),
  `mode_paiement` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `paiement`
--

INSERT INTO `paiement` (`id_paiement`, `id_commande`, `montant`, `date_paiement`, `mode_paiement`) VALUES
(1, 3, 60.00, '2025-06-01 20:59:58', 'Especes');

-- --------------------------------------------------------

--
-- Structure de la table `reservations`
--

CREATE TABLE `reservations` (
  `reservation_id` int(10) UNSIGNED NOT NULL,
  `table_id` int(10) UNSIGNED NOT NULL,
  `client_nom` varchar(100) DEFAULT NULL,
  `date_reservation` date NOT NULL,
  `heure_reservation` time NOT NULL,
  `statut` enum('Confirmée','Annulée','Terminée') NOT NULL DEFAULT 'Confirmée'
) ;

--
-- Déchargement des données de la table `reservations`
--

INSERT INTO `reservations` (`reservation_id`, `table_id`, `client_nom`, `date_reservation`, `heure_reservation`, `statut`) VALUES
(1, 1, 'luccin', '2025-06-06', '18:30:00', 'Confirmée');

-- --------------------------------------------------------

--
-- Structure de la table `tables`
--

CREATE TABLE `tables` (
  `table_id` int(10) UNSIGNED NOT NULL,
  `numero` int(11) NOT NULL,
  `capacite` int(11) NOT NULL CHECK (`capacite` > 0),
  `etat` varchar(20) NOT NULL CHECK (`etat` in ('DISPONIBLE','OCCUPEE','RESERVEE'))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `tables`
--

INSERT INTO `tables` (`table_id`, `numero`, `capacite`, `etat`) VALUES
(1, 1, 4, 'DISPONIBLE'),
(2, 2, 2, 'OCCUPEE'),
(3, 3, 6, 'RESERVEE'),
(4, 4, 4, 'DISPONIBLE');

--
-- Index pour les tables déchargées
--

--
-- Index pour la table `commande`
--
ALTER TABLE `commande`
  ADD PRIMARY KEY (`id_commande`),
  ADD KEY `fk_table_commande` (`id_table`);

--
-- Index pour la table `commande_item`
--
ALTER TABLE `commande_item`
  ADD PRIMARY KEY (`commande_id`,`menu_item_id`),
  ADD KEY `menu_item_id` (`menu_item_id`);

--
-- Index pour la table `menu_item`
--
ALTER TABLE `menu_item`
  ADD PRIMARY KEY (`id`,`type`);

--
-- Index pour la table `paiement`
--
ALTER TABLE `paiement`
  ADD PRIMARY KEY (`id_paiement`),
  ADD KEY `fk_paiement_commande` (`id_commande`);

--
-- Index pour la table `reservations`
--
ALTER TABLE `reservations`
  ADD PRIMARY KEY (`reservation_id`),
  ADD KEY `table_id` (`table_id`);

--
-- Index pour la table `tables`
--
ALTER TABLE `tables`
  ADD PRIMARY KEY (`table_id`),
  ADD UNIQUE KEY `numero` (`numero`);

--
-- AUTO_INCREMENT pour les tables déchargées
--

--
-- AUTO_INCREMENT pour la table `paiement`
--
ALTER TABLE `paiement`
  MODIFY `id_paiement` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT pour la table `reservations`
--
ALTER TABLE `reservations`
  MODIFY `reservation_id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT pour la table `tables`
--
ALTER TABLE `tables`
  MODIFY `table_id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- Contraintes pour les tables déchargées
--

--
-- Contraintes pour la table `commande`
--
ALTER TABLE `commande`
  ADD CONSTRAINT `fk_table_commande` FOREIGN KEY (`id_table`) REFERENCES `tables` (`table_id`);

--
-- Contraintes pour la table `commande_item`
--
ALTER TABLE `commande_item`
  ADD CONSTRAINT `commande_item_ibfk_1` FOREIGN KEY (`commande_id`) REFERENCES `commande` (`id_commande`) ON DELETE CASCADE,
  ADD CONSTRAINT `commande_item_ibfk_2` FOREIGN KEY (`menu_item_id`) REFERENCES `menu_item` (`id`) ON DELETE CASCADE;

--
-- Contraintes pour la table `paiement`
--
ALTER TABLE `paiement`
  ADD CONSTRAINT `fk_paiement_commande` FOREIGN KEY (`id_commande`) REFERENCES `commande` (`id_commande`);

--
-- Contraintes pour la table `reservations`
--
ALTER TABLE `reservations`
  ADD CONSTRAINT `reservations_ibfk_1` FOREIGN KEY (`table_id`) REFERENCES `tables` (`table_id`) ON DELETE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
