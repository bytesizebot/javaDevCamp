INSERT INTO product(
	 name,description, price, image_url)
	VALUES
	('Retail Short Term Insurance', 'Provides cover for short-term products for individuals - Electronics, Household Items, Jewellery, Cars etc.', 500.0, 'http://fixed.image.com'),
    ('Retail Long-Term Insurance', 'Provides cover for longer term products individuals - household insurance, life insurance etc.', 1000.00, 'http://notice.image.com'),
    ('Commercial Short Term Insurance', 'Provides cover for short-term products for commercial entities - Printers, Company Cars, Theft, etc.', 5000.00, 'http://life.image.com'),
    ('Commercial Long-Term Insurance', 'Provides cover for longer term products - office insurance, employee benefit insurance, etc', 10000.00, 'http://life.image.com'),
    ('Device Contract', 'Allows the customer to take out a device on contract - such as a phone, laptop etc.', 850.00, 'http://life.image.com'),
    ('Short-Term Investment Product', 'Provides a way for customers to invest their money over a short period of time - 32 day fixed deposit etc.', 2500.00, 'http://life.image.com'),
    ('Long-Term investment Product', 'Provides a way for users to invest their money over the long term - Retirement / Annuity Funds, Unit Trusts etc', 5000.00, 'http://life.image.com'),
    ('Islamic Investment Product', 'Provides a way for Islamic customers to invest their money', 5000.00, 'http://life.image.com'),
    ('VIP Investment Product', 'Provides an Investment product for VIP customers Over 150 Million Net-Asset Value', 20000.00, 'http://life.image.com')

INSERT INTO fulfilment_type(
	 product_id, name, description)
	VALUES
	(1, 'C', 'Requires all of the checks in Type B, as well as the Marital Status Check & Credit Check'),
    (2, 'C', 'Requires all of the checks in Type B, as well as the Marital Status Check & Credit Check'),
    (3, 'C', 'Requires all of the checks in Type B, as well as the Marital Status Check & Credit Check'),
    (4, 'C', 'Requires all of the checks in Type B, as well as the Marital Status Check & Credit Check'),
    (5, 'A', 'Smallest unit of checks. This Just requires KYC to be completed.'),
    (6, 'B', 'Requires all of the checks in Type A, as well as the Fraud Check, Living Status Check and Duplicate ID Check'),
    (7, 'B', 'Requires all of the checks in Type A, as well as the Fraud Check, Living Status Check and Duplicate ID Check'),
    (8, 'B', 'Requires all of the checks in Type A, as well as the Fraud Check, Living Status Check and Duplicate ID Check'),
    (9, 'B', 'Requires all of the checks in Type A, as well as the Fraud Check, Living Status Check and Duplicate ID Check')
