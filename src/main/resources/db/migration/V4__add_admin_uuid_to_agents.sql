ALTER TABLE agents ADD COLUMN admin_uuid UUID;
ALTER TABLE agents ADD CONSTRAINT fk_agents_admin FOREIGN KEY (admin_uuid) REFERENCES administrators(admin_uuid); 