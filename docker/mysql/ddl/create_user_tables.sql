-- 유저
create table users (
    user_id bigint not null auto_increment,
    password varchar(255),
    user_name varchar(255),
    o_auth_channel_type varchar(255) not null,
    user_type varchar(255) not null,
    last_login_at datetime,
    email varchar(255) not null,
    deleted_at datetime,
    created_at datetime,
    updated_at datetime,
    primary key (user_id)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;

-- User recent search word
create table recent_search_words (
    recent_search_word_id bigint not null auto_increment,
    email bigint not null
    searchWord varchar(255) not null,
    created_at datetime,
    primary key (recent_search_word_id)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;

-- jwt refresh token
create table user_refresh_tokens (
    user_key bigint not null auto_increment,
    refresh_token varchar(255) not null,
    primary key (user_key)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;