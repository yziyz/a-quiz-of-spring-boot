--
-- PostgreSQL database dump
--

-- Dumped from database version 9.6.3
-- Dumped by pg_dump version 9.6.3

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;
SET row_security = off;

--
-- Name: plpgsql; Type: EXTENSION; Schema: -; Owner: 
--

CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;


--
-- Name: EXTENSION plpgsql; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';


SET search_path = public, pg_catalog;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- Name: cities; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE cities (
    city_code integer NOT NULL,
    city_name text NOT NULL
);


ALTER TABLE cities OWNER TO postgres;

--
-- Name: users; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE users (
    id bigint NOT NULL,
    uuid character varying(36) NOT NULL,
    user_name character varying(240) NOT NULL,
    email character varying(50) NOT NULL,
    password character varying(96) NOT NULL,
    city_code integer NOT NULL
);


ALTER TABLE users OWNER TO postgres;

--
-- Name: users_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE users_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE users_id_seq OWNER TO postgres;

--
-- Name: users_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE users_id_seq OWNED BY users.id;


--
-- Name: users id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY users ALTER COLUMN id SET DEFAULT nextval('users_id_seq'::regclass);


--
-- Data for Name: cities; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY cities (city_code, city_name) FROM stdin;
110000	北京市
120000	天津市
130100	石家庄市
130200	唐山市
130300	秦皇岛市
130400	邯郸市
130500	邢台市
130600	保定市
130700	张家口市
130800	承德市
130900	沧州市
131000	廊坊市
131100	衡水市
139001	定州市
139002	辛集市
140100	太原市
\.


--
-- Data for Name: users; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY users (id, uuid, user_name, email, password, city_code) FROM stdin;
1	f89975b0-58a5-4148-8db1-c99cc9586f3f	Mike	mike@gmail.com	cf6b5f5ef865ff56521e7a496a9b9a4ffeb1cb1aa888fc01abfbe27f8bfdc6da7c8cce4d05244765aa6e63efade74fd5	120000
2	8bb69022-e8db-4410-bf84-7746d55cecbe	李笑天	mike@gmail.com	cf6b5f5ef865ff56521e7a496a9b9a4ffeb1cb1aa888fc01abfbe27f8bfdc6da7c8cce4d05244765aa6e63efade74fd5	120000
3	4344262b-165e-48a2-9916-470e4b0a41a8	王天	wangtian@gmail.com	f0cce3567b1a70e051a14a8a13ad264041ca22c24395d9c3237863b7e485e239678a3f1ffff4cc991fcc50ba6011cd75	130400
4	ee51cc04-c25a-4e09-8b8c-ba6e04cc68f5	Tim	tim@gmail.com	f0cce3567b1a70e051a14a8a13ad264041ca22c24395d9c3237863b7e485e239678a3f1ffff4cc991fcc50ba6011cd75	130100
5	137867af-68bc-4060-9e24-150bcedaba54	Kiven	kiven@gmail.com	c6fd2b45fe68c3e1ee529d69d841ebf019d0b6879c217b7185639d5aad322aa701e248170abf8a8975e3bc7744a594ca	130500
\.


--
-- Name: users_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('users_id_seq', 5, true);


--
-- Name: cities cities_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY cities
    ADD CONSTRAINT cities_pkey PRIMARY KEY (city_code);


--
-- Name: users users_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY users
    ADD CONSTRAINT users_pkey PRIMARY KEY (uuid);


--
-- Name: cities_city_name_index; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX cities_city_name_index ON cities USING btree (city_name);


--
-- Name: users users_city_code_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY users
    ADD CONSTRAINT users_city_code_fkey FOREIGN KEY (city_code) REFERENCES cities(city_code);


--
-- PostgreSQL database dump complete
--

