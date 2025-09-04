INSERT INTO products (name, price, stock) VALUES
('Laptop Gaming', 1200.00, 10),
('Smartphone Pro', 800.00, 15),
('Tablet Elite', 400.00, 20),
('Auriculares Bluetooth', 100.00, 30),
('Teclado Mecánico', 150.00, 25),
('Mouse Inalámbrico', 80.00, 40),
('Monitor 4K', 600.00, 8),
('Impresora Laser', 300.00, 12),
('Disco Duro SSD', 200.00, 18),
('Memoria RAM 16GB', 120.00, 22);

-- Insertar órdenes de ejemplo
INSERT INTO orders (date, total, status) VALUES
('2024-01-15 10:30:00', 2240.00, 'CONFIRMED'),
('2024-01-16 14:45:00', 630.00, 'PROCESSED'),
('2024-01-17 09:15:00', 1800.00, 'PENDING'),
('2024-01-18 16:20:00', 390.00, 'CONFIRMED'),
('2024-01-19 11:00:00', 3120.00, 'PROCESSED');

-- Insertar items para la orden 1 (Laptop + Mouse + Auriculares)
INSERT INTO order_items (order_id, product_id, quantity, subtotal) VALUES
(1, 1, 1, 1200.00),  -- Laptop Gaming
(1, 6, 2, 160.00),   -- Mouse Inalámbrico (2 unidades)
(1, 4, 3, 300.00),   -- Auriculares Bluetooth (3 unidades)
(1, 10, 1, 120.00),  -- Memoria RAM 16GB
(1, 9, 1, 200.00);   -- Disco Duro SSD

-- Insertar items para la orden 2 (Tablet + Teclado)
INSERT INTO order_items (order_id, product_id, quantity, subtotal) VALUES
(2, 3, 1, 400.00),   -- Tablet Elite
(2, 5, 1, 150.00),   -- Teclado Mecánico
(2, 4, 2, 200.00);   -- Auriculares Bluetooth (2 unidades)

-- Insertar items para la orden 3 (Smartphone + Monitor)
INSERT INTO order_items (order_id, product_id, quantity, subtotal) VALUES
(3, 2, 2, 1600.00),  -- Smartphone Pro (2 unidades)
(3, 7, 1, 600.00);   -- Monitor 4K

-- Insertar items para la orden 4 (Impresora + Accesorios)
INSERT INTO order_items (order_id, product_id, quantity, subtotal) VALUES
(4, 8, 1, 300.00),   -- Impresora Laser
(4, 6, 1, 80.00),    -- Mouse Inalámbrico
(4, 4, 1, 100.00);   -- Auriculares Bluetooth

-- Insertar items para la orden 5 (Varios productos - para probar descuentos)
INSERT INTO order_items (order_id, product_id, quantity, subtotal) VALUES
(5, 1, 2, 2400.00),  -- Laptop Gaming (2 unidades)
(5, 2, 1, 800.00),   -- Smartphone Pro
(5, 3, 1, 400.00),   -- Tablet Elite
(5, 4, 2, 200.00),   -- Auriculares Bluetooth (2 unidades)
(5, 5, 1, 150.00),   -- Teclado Mecánico
(5, 6, 1, 80.00),    -- Mouse Inalámbrico
(5, 7, 1, 600.00);   -- Monitor 4K