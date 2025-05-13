# Projet de Système de Gestion de Restaurant en Console Java

## Aperçu
Dans ce projet, vous allez créer un système de gestion de restaurant en console où les utilisateurs pourront gérer les éléments du menu, les commandes, les tables, les réservations et le personnel. Le système devra être implémenté sous forme d’application console en Java, en utilisant les concepts de programmation orientée objet et des patrons de conception.

## Exigences

### Fonctionnalités du Système
1. **Gestion du menu** (ajouter, supprimer, modifier, afficher les éléments)
2. **Gestion des tables** (ajouter, supprimer, consulter l’état des tables)
3. **Gestion des commandes** (créer, modifier, annuler, finaliser les commandes)
4. **Système de réservation** (créer, modifier, annuler des réservations)
5. **Gestion du personnel** (ajouter, supprimer, affecter le personnel aux tables/sections)
6. **Facturation et gestion des paiements**
7. **Suivi des stocks d’ingrédients**
8. **Fonctionnalités de reporting** (rapports de ventes, plats les plus commandés, etc.)
9. **Persistance de l’état du système** (fonctionnalité de sauvegarde/chargement)

### Exigences Techniques

#### Concepts POO
1. **Agrégation** : Implémenter des relations où des objets contiennent d’autres objets (par exemple, un restaurant contenant des tables et des éléments de menu, des commandes contenant des éléments de menu)
2. **Cohésion** : S’assurer que les classes ont des responsabilités bien définies et des méthodes cohérentes
3. **Héritage** : Créer des hiérarchies de classes (par exemple, différents types d’éléments de menu, rôles de personnel)
4. **Encapsulation** : Utiliser les modificateurs d’accès appropriés et les méthodes getter/setter
5. **Polymorphisme** : Implémenter la redéfinition de méthodes pour des comportements spécifiques

#### Patrons de Conception
1. **Patron Factory** : Pour créer différents types d’éléments de menu, de commandes ou de rapports
2. **Patron Observer** : Pour les notifications (par exemple, quand une commande est prête, quand le stock est bas)
3. **Patron State** : Pour gérer les états des commandes (nouvelle, en cours, prête, livrée, payée)
4. **Patron Strategy** : Pour différentes stratégies de tarification ou méthodes de traitement des paiements
5. **Patron Singleton** : Pour un accès global à la base de données du restaurant ou à la configuration
6. **Patron Facade** : Pour fournir une interface simplifiée aux sous-systèmes complexes du restaurant

### Interface Utilisateur
- Interface textuelle dans la console
- Instructions claires pour les utilisateurs
- Système de commandes intuitif (par exemple : `ajouter plat`, `créer commande`, `réserver table`)

## Livrables
1. Le code source du système de gestion de restaurant
2. Un bref document expliquant :
    - Comment chaque concept POO a été implémenté
    - Comment chaque patron de conception a été utilisé
    - Les instructions pour utiliser le système