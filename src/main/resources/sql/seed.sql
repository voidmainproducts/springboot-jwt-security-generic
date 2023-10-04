CREATE TABLE `_user` (
                         `id` int NOT NULL,
                         `email` varchar(255) DEFAULT NULL,
                         `firstname` varchar(255) DEFAULT NULL,
                         `lastname` varchar(255) DEFAULT NULL,
                         `password` varchar(255) DEFAULT NULL,
                         `role` varchar(255) DEFAULT NULL,
                         PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `_user_seq` (
    `next_val` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


CREATE TABLE `token` (
                         `id` int NOT NULL,
                         `expired` bit(1) NOT NULL,
                         `revoked` bit(1) NOT NULL,
                         `token` varchar(255) DEFAULT NULL,
                         `token_type` varchar(255) DEFAULT NULL,
                         `user_id` int DEFAULT NULL,
                         PRIMARY KEY (`id`),
                         UNIQUE KEY `UK_pddrhgwxnms2aceeku9s2ewy5` (`token`),
                         KEY `FKiblu4cjwvyntq3ugo31klp1c6` (`user_id`),
                         CONSTRAINT `FKiblu4cjwvyntq3ugo31klp1c6` FOREIGN KEY (`user_id`) REFERENCES `_user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;-- jwt_security.token_seq definition

CREATE TABLE `token_seq` (
    `next_val` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


