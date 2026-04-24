
--
-- TOC entry 228 (class 1259 OID 16471)
-- Name: contract_document; Type: TABLE; Schema: public; Owner: user
--

CREATE TABLE public.contract_document (
    document_id uuid NOT NULL,
    created_at timestamp(6) without time zone,
    file_name character varying(255),
    file_path character varying(255)
);



--
-- TOC entry 211 (class 1259 OID 16394)
-- Name: eligibility; Type: TABLE; Schema: public; Owner: user
--

CREATE TABLE public.eligibility (
    eligibility_id bigint NOT NULL,
    customer_id bigint,
    product_id bigint,
    result boolean
);


--
-- TOC entry 209 (class 1259 OID 16384)
-- Name: flyway_schema_history; Type: TABLE; Schema: public; Owner: user
--

CREATE TABLE public.flyway_schema_history (
    installed_rank integer NOT NULL,
    version character varying(50),
    description character varying(200) NOT NULL,
    type character varying(20) NOT NULL,
    script character varying(1000) NOT NULL,
    checksum integer,
    installed_by character varying(100) NOT NULL,
    installed_on timestamp without time zone DEFAULT now() NOT NULL,
    execution_time integer NOT NULL,
    success boolean NOT NULL
);


--
-- TOC entry 213 (class 1259 OID 16400)
-- Name: fulfilment_type; Type: TABLE; Schema: public; Owner: user
--

CREATE TABLE public.fulfilment_type (
    fulfilment_type_id bigint NOT NULL,
    description character varying(255),
    name character varying(255),
    product_id bigint
);


--
-- TOC entry 215 (class 1259 OID 16408)
-- Name: order; Type: TABLE; Schema: public; Owner: user
--

CREATE TABLE public."order" (
    order_id bigint NOT NULL,
    contract_url character varying(255),
    created_at timestamp(6) without time zone,
    customer_id bigint,
    order_status character varying(255),
    CONSTRAINT order_order_status_check CHECK (((order_status)::text = ANY ((ARRAY['APPROVED'::character varying, 'PENDING'::character varying, 'DECLINED'::character varying])::text[])))
);


--
-- TOC entry 217 (class 1259 OID 16417)
-- Name: order_item; Type: TABLE; Schema: public; Owner: user
--

CREATE TABLE public.order_item (
    order_item_id bigint NOT NULL,
    order_id bigint,
    product_id bigint
);


--
-- TOC entry 219 (class 1259 OID 16423)
-- Name: product; Type: TABLE; Schema: public; Owner: user
--

CREATE TABLE public.product (
    product_id bigint NOT NULL,
    description character varying(255),
    image_url character varying(255),
    name character varying(255),
    price real,
    fulfilment_type_fulfilment_type_id bigint
);


--
-- TOC entry 221 (class 1259 OID 16431)
-- Name: profile; Type: TABLE; Schema: public; Owner: user
--

CREATE TABLE public.profile (
    profile_id bigint NOT NULL,
    customer_type_id bigint,
    email_address character varying(255) NOT NULL,
    first_name character varying(255),
    id_number character varying(255),
    last_name character varying(255),
    user_id bigint NOT NULL
);


--
-- TOC entry 223 (class 1259 OID 16439)
-- Name: qualifying_accounts; Type: TABLE; Schema: public; Owner: user
--

CREATE TABLE public.qualifying_accounts (
    qualifying_accounts_id bigint NOT NULL,
    account_id bigint,
    product_id bigint
);

--
-- TOC entry 225 (class 1259 OID 16445)
-- Name: qualifying_customer_types; Type: TABLE; Schema: public; Owner: user
--

CREATE TABLE public.qualifying_customer_types (
    qualifying_customer_types_id bigint NOT NULL,
    customer_types_id bigint,
    product_id bigint
);


--
-- TOC entry 227 (class 1259 OID 16451)
-- Name: user; Type: TABLE; Schema: public; Owner: user
--

CREATE TABLE public."user" (
    user_id bigint NOT NULL,
    password character varying(255),
    role character varying(255),
    username character varying(255) NOT NULL,
    CONSTRAINT user_role_check CHECK (((role)::text = ANY ((ARRAY['USER'::character varying, 'ADMIN'::character varying])::text[])))
);

--
-- TOC entry 3418 (class 0 OID 16471)
-- Dependencies: 228
-- Data for Name: contract_document; Type: TABLE DATA; Schema: public; Owner: user
--

COPY public.contract_document (document_id, created_at, file_name, file_path) FROM stdin;
\.


--
-- TOC entry 3401 (class 0 OID 16394)
-- Dependencies: 211
-- Data for Name: eligibility; Type: TABLE DATA; Schema: public; Owner: user
--

COPY public.eligibility (eligibility_id, customer_id, product_id, result) FROM stdin;
\.


--
-- TOC entry 3399 (class 0 OID 16384)
-- Dependencies: 209
-- Data for Name: flyway_schema_history; Type: TABLE DATA; Schema: public; Owner: user
--

COPY public.flyway_schema_history (installed_rank, version, description, type, script, checksum, installed_by, installed_on, execution_time, success) FROM stdin;
\.


--
-- TOC entry 3403 (class 0 OID 16400)
-- Dependencies: 213
-- Data for Name: fulfilment_type; Type: TABLE DATA; Schema: public; Owner: user
--

COPY public.fulfilment_type (fulfilment_type_id, description, name, product_id) FROM stdin;
\.


--
-- TOC entry 3405 (class 0 OID 16408)
-- Dependencies: 215
-- Data for Name: order; Type: TABLE DATA; Schema: public; Owner: user
--

COPY public."order" (order_id, contract_url, created_at, customer_id, order_status) FROM stdin;
\.


--
-- TOC entry 3407 (class 0 OID 16417)
-- Dependencies: 217
-- Data for Name: order_item; Type: TABLE DATA; Schema: public; Owner: user
--

COPY public.order_item (order_item_id, order_id, product_id) FROM stdin;
\.


--
-- TOC entry 3409 (class 0 OID 16423)
-- Dependencies: 219
-- Data for Name: product; Type: TABLE DATA; Schema: public; Owner: user
--

COPY public.product (product_id, description, image_url, name, price, fulfilment_type_fulfilment_type_id) FROM stdin;
\.


--
-- TOC entry 3411 (class 0 OID 16431)
-- Dependencies: 221
-- Data for Name: profile; Type: TABLE DATA; Schema: public; Owner: user
--

COPY public.profile (profile_id, customer_type_id, email_address, first_name, id_number, last_name, user_id) FROM stdin;
\.


--
-- TOC entry 3413 (class 0 OID 16439)
-- Dependencies: 223
-- Data for Name: qualifying_accounts; Type: TABLE DATA; Schema: public; Owner: user
--

COPY public.qualifying_accounts (qualifying_accounts_id, account_id, product_id) FROM stdin;
\.


--
-- TOC entry 3415 (class 0 OID 16445)
-- Dependencies: 225
-- Data for Name: qualifying_customer_types; Type: TABLE DATA; Schema: public; Owner: user
--

COPY public.qualifying_customer_types (qualifying_customer_types_id, customer_types_id, product_id) FROM stdin;
\.


--
-- TOC entry 3417 (class 0 OID 16451)
-- Dependencies: 227
-- Data for Name: user; Type: TABLE DATA; Schema: public; Owner: user
--

COPY public."user" (user_id, password, role, username) FROM stdin;
\.


--
-- TOC entry 3426 (class 0 OID 0)
-- Dependencies: 210
-- Name: eligibility_eligibility_id_seq; Type: SEQUENCE SET; Schema: public; Owner: user
--

SELECT pg_catalog.setval('public.eligibility_eligibility_id_seq', 1, false);


--
-- TOC entry 3427 (class 0 OID 0)
-- Dependencies: 212
-- Name: fulfilment_type_fulfilment_type_id_seq; Type: SEQUENCE SET; Schema: public; Owner: user
--

SELECT pg_catalog.setval('public.fulfilment_type_fulfilment_type_id_seq', 1, false);


--
-- TOC entry 3428 (class 0 OID 0)
-- Dependencies: 216
-- Name: order_item_order_item_id_seq; Type: SEQUENCE SET; Schema: public; Owner: user
--

SELECT pg_catalog.setval('public.order_item_order_item_id_seq', 1, false);


--
-- TOC entry 3429 (class 0 OID 0)
-- Dependencies: 214
-- Name: order_order_id_seq; Type: SEQUENCE SET; Schema: public; Owner: user
--

SELECT pg_catalog.setval('public.order_order_id_seq', 1, false);


--
-- TOC entry 3430 (class 0 OID 0)
-- Dependencies: 218
-- Name: product_product_id_seq; Type: SEQUENCE SET; Schema: public; Owner: user
--

SELECT pg_catalog.setval('public.product_product_id_seq', 1, false);


--
-- TOC entry 3431 (class 0 OID 0)
-- Dependencies: 220
-- Name: profile_profile_id_seq; Type: SEQUENCE SET; Schema: public; Owner: user
--

SELECT pg_catalog.setval('public.profile_profile_id_seq', 1, false);


--
-- TOC entry 3432 (class 0 OID 0)
-- Dependencies: 222
-- Name: qualifying_accounts_qualifying_accounts_id_seq; Type: SEQUENCE SET; Schema: public; Owner: user
--

SELECT pg_catalog.setval('public.qualifying_accounts_qualifying_accounts_id_seq', 1, false);


--
-- TOC entry 3433 (class 0 OID 0)
-- Dependencies: 224
-- Name: qualifying_customer_types_qualifying_customer_types_id_seq; Type: SEQUENCE SET; Schema: public; Owner: user
--

SELECT pg_catalog.setval('public.qualifying_customer_types_qualifying_customer_types_id_seq', 1, false);


--
-- TOC entry 3434 (class 0 OID 0)
-- Dependencies: 226
-- Name: user_user_id_seq; Type: SEQUENCE SET; Schema: public; Owner: user
--

SELECT pg_catalog.setval('public.user_user_id_seq', 1, false);


--
-- TOC entry 3252 (class 2606 OID 16477)
-- Name: contract_document contract_document_pkey; Type: CONSTRAINT; Schema: public; Owner: user
--

ALTER TABLE ONLY public.contract_document
    ADD CONSTRAINT contract_document_pkey PRIMARY KEY (document_id);


--
-- TOC entry 3222 (class 2606 OID 16398)
-- Name: eligibility eligibility_pkey; Type: CONSTRAINT; Schema: public; Owner: user
--

ALTER TABLE ONLY public.eligibility
    ADD CONSTRAINT eligibility_pkey PRIMARY KEY (eligibility_id);


--
-- TOC entry 3219 (class 2606 OID 16391)
-- Name: flyway_schema_history flyway_schema_history_pk; Type: CONSTRAINT; Schema: public; Owner: user
--

ALTER TABLE ONLY public.flyway_schema_history
    ADD CONSTRAINT flyway_schema_history_pk PRIMARY KEY (installed_rank);


--
-- TOC entry 3224 (class 2606 OID 16406)
-- Name: fulfilment_type fulfilment_type_pkey; Type: CONSTRAINT; Schema: public; Owner: user
--

ALTER TABLE ONLY public.fulfilment_type
    ADD CONSTRAINT fulfilment_type_pkey PRIMARY KEY (fulfilment_type_id);


--
-- TOC entry 3230 (class 2606 OID 16421)
-- Name: order_item order_item_pkey; Type: CONSTRAINT; Schema: public; Owner: user
--

ALTER TABLE ONLY public.order_item
    ADD CONSTRAINT order_item_pkey PRIMARY KEY (order_item_id);


--
-- TOC entry 3228 (class 2606 OID 16415)
-- Name: order order_pkey; Type: CONSTRAINT; Schema: public; Owner: user
--

ALTER TABLE ONLY public."order"
    ADD CONSTRAINT order_pkey PRIMARY KEY (order_id);


--
-- TOC entry 3232 (class 2606 OID 16429)
-- Name: product product_pkey; Type: CONSTRAINT; Schema: public; Owner: user
--

ALTER TABLE ONLY public.product
    ADD CONSTRAINT product_pkey PRIMARY KEY (product_id);


--
-- TOC entry 3236 (class 2606 OID 16437)
-- Name: profile profile_pkey; Type: CONSTRAINT; Schema: public; Owner: user
--

ALTER TABLE ONLY public.profile
    ADD CONSTRAINT profile_pkey PRIMARY KEY (profile_id);


--
-- TOC entry 3244 (class 2606 OID 16443)
-- Name: qualifying_accounts qualifying_accounts_pkey; Type: CONSTRAINT; Schema: public; Owner: user
--

ALTER TABLE ONLY public.qualifying_accounts
    ADD CONSTRAINT qualifying_accounts_pkey PRIMARY KEY (qualifying_accounts_id);


--
-- TOC entry 3246 (class 2606 OID 16449)
-- Name: qualifying_customer_types qualifying_customer_types_pkey; Type: CONSTRAINT; Schema: public; Owner: user
--

ALTER TABLE ONLY public.qualifying_customer_types
    ADD CONSTRAINT qualifying_customer_types_pkey PRIMARY KEY (qualifying_customer_types_id);


--
-- TOC entry 3238 (class 2606 OID 16466)
-- Name: profile uk3fmyv5j3ttnt0p2oipomf3624; Type: CONSTRAINT; Schema: public; Owner: user
--

ALTER TABLE ONLY public.profile
    ADD CONSTRAINT uk3fmyv5j3ttnt0p2oipomf3624 UNIQUE (id_number);


--
-- TOC entry 3234 (class 2606 OID 16462)
-- Name: product uk7o3jtgk0hljipmclmevvt73hh; Type: CONSTRAINT; Schema: public; Owner: user
--

ALTER TABLE ONLY public.product
    ADD CONSTRAINT uk7o3jtgk0hljipmclmevvt73hh UNIQUE (fulfilment_type_fulfilment_type_id);


--
-- TOC entry 3240 (class 2606 OID 16464)
-- Name: profile ukbmg2f8yioubfem4nfqlxwxnx0; Type: CONSTRAINT; Schema: public; Owner: user
--

ALTER TABLE ONLY public.profile
    ADD CONSTRAINT ukbmg2f8yioubfem4nfqlxwxnx0 UNIQUE (email_address);


--
-- TOC entry 3242 (class 2606 OID 16468)
-- Name: profile ukc1dkiawnlj6uoe6fnlwd6j83j; Type: CONSTRAINT; Schema: public; Owner: user
--

ALTER TABLE ONLY public.profile
    ADD CONSTRAINT ukc1dkiawnlj6uoe6fnlwd6j83j UNIQUE (user_id);


--
-- TOC entry 3226 (class 2606 OID 16460)
-- Name: fulfilment_type ukm1i4i3710l55f8732qe6iig7b; Type: CONSTRAINT; Schema: public; Owner: user
--

ALTER TABLE ONLY public.fulfilment_type
    ADD CONSTRAINT ukm1i4i3710l55f8732qe6iig7b UNIQUE (product_id);


--
-- TOC entry 3248 (class 2606 OID 16470)
-- Name: user uksb8bbouer5wak8vyiiy4pf2bx; Type: CONSTRAINT; Schema: public; Owner: user
--

ALTER TABLE ONLY public."user"
    ADD CONSTRAINT uksb8bbouer5wak8vyiiy4pf2bx UNIQUE (username);


--
-- TOC entry 3250 (class 2606 OID 16458)
-- Name: user user_pkey; Type: CONSTRAINT; Schema: public; Owner: user
--

ALTER TABLE ONLY public."user"
    ADD CONSTRAINT user_pkey PRIMARY KEY (user_id);


--
-- TOC entry 3220 (class 1259 OID 16392)
-- Name: flyway_schema_history_s_idx; Type: INDEX; Schema: public; Owner: user
--

CREATE INDEX flyway_schema_history_s_idx ON public.flyway_schema_history USING btree (success);


--
-- TOC entry 3254 (class 2606 OID 16488)
-- Name: order_item fk551losx9j75ss5d6bfsqvijna; Type: FK CONSTRAINT; Schema: public; Owner: user
--

ALTER TABLE ONLY public.order_item
    ADD CONSTRAINT fk551losx9j75ss5d6bfsqvijna FOREIGN KEY (product_id) REFERENCES public.product(product_id);


--
-- TOC entry 3259 (class 2606 OID 16508)
-- Name: qualifying_customer_types fk9i4xx1quu7t1g9i7y1c4c5eup; Type: FK CONSTRAINT; Schema: public; Owner: user
--

ALTER TABLE ONLY public.qualifying_customer_types
    ADD CONSTRAINT fk9i4xx1quu7t1g9i7y1c4c5eup FOREIGN KEY (product_id) REFERENCES public.product(product_id);


--
-- TOC entry 3257 (class 2606 OID 16498)
-- Name: profile fkawh070wpue34wqvytjqr4hj5e; Type: FK CONSTRAINT; Schema: public; Owner: user
--

ALTER TABLE ONLY public.profile
    ADD CONSTRAINT fkawh070wpue34wqvytjqr4hj5e FOREIGN KEY (user_id) REFERENCES public."user"(user_id);


--
-- TOC entry 3256 (class 2606 OID 16493)
-- Name: product fkf1sednhl9yevgofc4mpyjhi3b; Type: FK CONSTRAINT; Schema: public; Owner: user
--

ALTER TABLE ONLY public.product
    ADD CONSTRAINT fkf1sednhl9yevgofc4mpyjhi3b FOREIGN KEY (fulfilment_type_fulfilment_type_id) REFERENCES public.fulfilment_type(fulfilment_type_id);


--
-- TOC entry 3253 (class 2606 OID 16478)
-- Name: fulfilment_type fki2k09ueu5978lnhe1kuediv6a; Type: FK CONSTRAINT; Schema: public; Owner: user
--

ALTER TABLE ONLY public.fulfilment_type
    ADD CONSTRAINT fki2k09ueu5978lnhe1kuediv6a FOREIGN KEY (product_id) REFERENCES public.product(product_id);


--
-- TOC entry 3258 (class 2606 OID 16503)
-- Name: qualifying_accounts fkrpnc0jpj0mofty5ph75bjper4; Type: FK CONSTRAINT; Schema: public; Owner: user
--

ALTER TABLE ONLY public.qualifying_accounts
    ADD CONSTRAINT fkrpnc0jpj0mofty5ph75bjper4 FOREIGN KEY (product_id) REFERENCES public.product(product_id);


--
-- TOC entry 3255 (class 2606 OID 16483)
-- Name: order_item fkt6wv8m7eshksp5kp8w4b2d1dm; Type: FK CONSTRAINT; Schema: public; Owner: user
--

ALTER TABLE ONLY public.order_item
    ADD CONSTRAINT fkt6wv8m7eshksp5kp8w4b2d1dm FOREIGN KEY (order_id) REFERENCES public."order"(order_id);


--
-- TOC entry 3425 (class 0 OID 0)
-- Dependencies: 4
-- Name: SCHEMA public; Type: ACL; Schema: -; Owner: user
--

REVOKE USAGE ON SCHEMA public FROM PUBLIC;
GRANT ALL ON SCHEMA public TO PUBLIC;


-- Completed on 2026-04-24 06:52:52

--
-- PostgreSQL database dump complete
--

\unrestrict cfJKuQD3Bn5cyEyEtFGWNZVf39YNrCA5zmA0VzsMhzIhYAvje8ts6Qt6i2pGRQw

