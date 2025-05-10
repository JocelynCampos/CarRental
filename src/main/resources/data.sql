INSERT INTO users (social_security_number, first_name, last_name, address, email, phone_number) VALUES
( '19850101-1234', 'Anna', 'Svensson', 'Eduvägen 40', 'anna.svensson@gmail.com', '070-9638527'),
( '19900215-5678', 'Erik', 'Johansson', 'Granvägen 50', 'erik.j@outlook.se', '072-3692587'),
( '19751530-9101', 'Maria', 'Lindberg', 'Granitvägen 1', 'lindberg1@live.se', '076-7418529'),
( '19881122-3456', 'Johan', 'Karlsson', 'Karlavägen 6', 'ksson@live.com', '073-3579510'),
( '19950505-7890', 'Elin', 'Andersson', 'Askgatan 85', 'el.an@hotmail.com', '070-9517531');

INSERT INTO cars (price_per_day, brand, model, registration_number, status) VALUES
( 542, 'Toyota', 'Corolla', 'UET267', 'BOOKED'),
( 629, 'Mercedes Benz', 'A180', 'GYC001', 'BOOKED'),
( 659, 'MG', 'EHS', 'RFK46B', 'SERVICE'),
( 719, 'Volvo', 'XC40', 'ZDH02L', 'FREE'),
( 899, 'BMW', 'I4', 'WMB007', 'FREE'),
( 499, 'Toyota', 'Yaris', 'ABC123', 'BOOKED');

INSERT INTO booking (total_cost, date_when_picked_up, date_when_turned_in, car_id, user_id) VALUES
( 1084, '2025-04-01', '2025-04-03', 1, 1), -- Toyota, Anna
( 1887, '2025-04-05', '2025-04-08', 2, 2), -- Mercedes, Erik
( 2636, '2025-04-10', '2025-04-14', 6, 3); -- Toyota, Maria

SELECT 1;