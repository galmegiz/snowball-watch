-- Portfolio
create table portfolios (
    portfolio_id bigint not null auto_increment,
    user_id bigint not null,
    priority bigint not null,
    created_at datetime,
    updated_at datetime,
    primary key (portfolio_id)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;

create table portfolio_assets (
    portfolio_asset_id bigint not null auto_increment,
    count bigint not null,
    purchase_price decimal(10, 2) not null,
    currency_type varchar(255) not null,
    asset_id bigint not null,
    portfolio_id bigint not null,
    created_at datetime,
    updated_at datetime,
    primary key (portfolio_asset_id)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;