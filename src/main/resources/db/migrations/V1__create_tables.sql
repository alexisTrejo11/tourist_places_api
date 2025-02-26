-- Create tables
CREATE TABLE activities (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    description TEXT NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    duration VARCHAR(255) NOT NULL,
    place_id BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE countries (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    capital VARCHAR(255) NOT NULL,
    currency VARCHAR(255) NOT NULL,
    language VARCHAR(255) NOT NULL,
    population BIGINT NOT NULL,
    area DOUBLE PRECISION NOT NULL,
    continent VARCHAR(50) NOT NULL,
    flag_image VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT continent_check CHECK (continent IN ('AFRICA', 'ASIA', 'EUROPE', 'AMERICA', 'OCEANIA'))
);

CREATE TABLE tourist_places (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    description TEXT NOT NULL,
    rating DOUBLE PRECISION NOT NULL DEFAULT 0.0,
    image VARCHAR(255),
    opening_hours VARCHAR(255),
    price_range VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    country_id BIGINT,
    category_id BIGINT NOT NULL
);

CREATE TABLE place_categories (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE reviews (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    ratings DOUBLE PRECISION NOT NULL,
    comment TEXT NOT NULL,
    author VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    place_id BIGINT
);

CREATE TABLE users (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(255),
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255),
    provider VARCHAR(255),
    is_activated BOOLEAN NOT NULL DEFAULT false,
    joined_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    last_login TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    role VARCHAR(50) CHECK (role IN('VIEWER', 'EDITOR', 'ADMIN'))
);

CREATE TABLE tourist_place_list (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    user_id BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE place_list_tourist_places(
    place_list_id BIGINT NOT NULL,
    tourist_place_id BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (place_list_id, tourist_place_id)
);

-- Add Relationships
ALTER TABLE tourist_place_list
    ADD CONSTRAINT fk_user_id
    FOREIGN KEY (user_id)
    REFERENCES users(id)
    ON DELETE CASCADE;

ALTER TABLE place_list_tourist_places
    ADD CONSTRAINT fk_place_list_id
    FOREIGN KEY (place_list_id)
    REFERENCES tourist_place_list(id)
    ON DELETE CASCADE;

ALTER TABLE place_list_tourist_places
    ADD CONSTRAINT fk_tourist_place_id
    FOREIGN KEY (tourist_place_id)
    REFERENCES tourist_places(id)
    ON DELETE CASCADE;

ALTER TABLE activities
    ADD CONSTRAINT fk_place_id FOREIGN KEY (place_id) REFERENCES tourist_places(id) ON DELETE CASCADE;

ALTER TABLE tourist_places
    ADD CONSTRAINT fk_country_id FOREIGN KEY (country_id) REFERENCES countries(id) ON DELETE SET NULL;

ALTER TABLE tourist_places
    ADD CONSTRAINT fk_category_id FOREIGN KEY (category_id) REFERENCES place_categories(id) ON DELETE CASCADE;

ALTER TABLE reviews
    ADD CONSTRAINT fk_place FOREIGN KEY (place_id) REFERENCES tourist_places(id);