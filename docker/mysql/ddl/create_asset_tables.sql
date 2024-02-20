-- Asset
create table assets (
    asset_id bigint not null auto_increment,
    ticker_code varchar(255),
    stock_code varchar(255),
    name varchar(255) not null,
    country_type varchar(255),
    market_type varchar(255),
    asset_category_type varchar(255),
    created_at datetime,
    updated_at datetime,
    primary key (asset_id)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;

-- Asset
create table asset_dividends (
    asset_dividend_id bigint not null auto_increment,
    frequency_type varchar(255) not null,
    dividend decimal(10, 2) not null,
    ex_dividend_date datetime,
    pay_date datetime,
    created_at datetime,
    updated_at datetime,
    asset_id bigint not null,
    primary key (asset_dividend_id)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;

-- popular stock
create table popular_stocks (
    popular_stock_id bigint not null auto_increment,
    ticker_code varchar(255),
    stock_code varchar(255),
    name varchar(255) not null,
    primary key (popular_stocks)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;