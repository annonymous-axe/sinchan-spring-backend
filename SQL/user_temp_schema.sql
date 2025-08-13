drop database if exists sinchan_temp;

create database sinchan_temp;

use sinchan_temp;

create table users(
	user_id integer primary key auto_increment,
    email varchar(255) not null unique,
    password varchar(255) not null,
    active bit not null,
    contact_number varchar(255),
    firm_name varchar(255),
    first_name varchar(255),
    last_name varchar(255),
    gst_number varchar(255),
    address varchar(255)
)engine=InnoDB auto_increment = 1001;

create table roles(
	email varchar(255) not null,
    role varchar(50) not null,
    
    primary key (email, role),
    foreign key (email) references users(email)
	on delete cascade
	on update cascade    
);

create table farmers(
	id integer,
    user_id integer not null,
	contact_no varchar(255) ,
	email varchar(255) ,
	farmer_name varchar(255) ,
	address varchar(255) ,
	sanch varchar(255) ,
	aadhar_id varchar(255) ,
	farmer_id varchar(255) ,
	district_id varchar(255) ,
	tehsil_id varchar(255),
    
    primary key (id, user_id),
    foreign key (user_id) references users(user_id)
	on delete cascade
	on update cascade    
);

create table districts(
	district_id int primary key,
    district_name varchar(255)
);

create table tehsils(
	district_id int,
    tehsil_id int,
    tehsil_name varchar(255),
    primary key(district_id, tehsil_id),
    foreign key (district_id) references districts(district_id)
	on delete cascade
	on update cascade
);

create table if not exists categories(
	id int not null auto_increment,
    user_id int not null,
    name varchar(255) not null,
    primary key (id, user_id),
    foreign key (user_id) references users(user_id) on delete cascade on update cascade
)engine = InnoDB;

create table if not exists manufacturers (
	id int auto_increment,
    user_id int not null,
	name varchar(255),
    primary key (id, user_id),
    foreign key (user_id) references users(user_id) on delete cascade on update cascade
)ENGINE = InnoDB;

create table if not exists items (
	id int not null,
    user_id int not null,
	category_id int not null,
	name varchar(255) not null,
	unit varchar(255) ,
	gst_rate float,
	index category_id (category_id asc) ,
    primary key (id, user_id),
	foreign key (category_id) references categories (id) 
    on delete cascade
	on update cascade,
    foreign key (user_id) references users(user_id)
	on delete cascade
	on update cascade
)engine = InnoDB;

create table if not exists item_manufacturer_details (
	id int auto_increment,
	item_id int not null,
    user_id int not null,
	manufacturer_id int not null,
	rate float not null,
	cml_number varchar(255),
	quontity int,
    primary key (id, item_id, user_id),
	foreign key (item_id, user_id) references items (id, user_id)
	on delete cascade
	on update cascade,
    foreign key (user_id) references users(user_id)
	on delete cascade
	on update cascade
)engine = InnoDB;

create table purchase_orders (
  id int ,
  user_id int,
  po_number varchar(255) ,
  supplier_name varchar(255) ,
  purchase_date datetime(6) ,
  bill_number varchar(255) ,
  created_at timestamp null default current_timestamp,
  primary key(id, user_id),
  foreign key (user_id) references users(user_id) on delete cascade on update cascade
) ENGINE=InnoDB;

create table purchase_order_items (
  id int auto_increment,
  user_id int,
  purchase_order_id int not null,
  item_id int not null,
  manufacturer_id int not null,
  quantity int not null,
  rate float not null,
  total_amount varchar(255) ,
  category_id int not null,
  cml_number varchar(255) ,
  primary key (id, purchase_order_id, user_id),
  foreign key (purchase_order_id, user_id) references purchase_orders (id, user_id) on delete cascade on update cascade,
  foreign key (item_id) references items (id) on delete cascade on update cascade,
  foreign key (manufacturer_id) references manufacturers (id)
) engine=InnoDB;

create table invoice (
  id int not null,
  user_id int not null,
  farmer int,
  email varchar(255) ,
  phone varchar(255) ,
  address varchar(255) ,
  created_at timestamp default current_timestamp,
  manufacturer_id int not null,
  grand_total double ,
  district_id varchar(255) ,
  sanch varchar(255) ,
  tehsil_id varchar(255) ,
  aadhar_id varchar(255) ,
  farmer_id varchar(255) ,
  primary key (id, user_id),
  foreign key (user_id) references users(user_id) on delete cascade on update cascade,
  foreign key (farmer) references farmers(id) on delete cascade on update cascade
) engine=InnoDB;

create table quotation (
  id int not null,
  user_id int not null,
  customer_name varchar(255),
  email varchar(255) ,
  phone varchar(255) ,
  address varchar(255) ,
  created_at timestamp default current_timestamp,
  manufacturer_id int not null,
  grand_total double ,
  district_id varchar(255) ,
  sanch varchar(255) ,
  tehsil_id varchar(255) ,
  aadhar_id varchar(255) ,
  farmer_id varchar(255) ,
  primary key (id, user_id),
  foreign key (user_id) references users(user_id) on delete cascade on update cascade
) engine=InnoDB;

create table invoice_items (
  id int not null auto_increment,
  user_id int not null,
  invoice_id int ,
  quotation_id int ,
  quantity int ,
  unit varchar(255) ,
  rate double ,
  total double ,
  item_id int ,
  category_id int ,
  cml_number varchar(255) ,
  primary key (id, user_id) ,
  foreign key (invoice_id, user_id) references invoice(id, user_id) ,
  foreign key (quotation_id, user_id) references quotation(id, user_id),
  foreign key (user_id) references users(user_id) on delete cascade on update cascade
) ENGINE=InnoDB;

insert into users(email, password, active) values('admin123@gmail.com', '$2y$10$yupTWmZ.qFDM7jmlGhp08ezw6wiDGXlh0pu42Zqah5Z895gEHDQEi', 1);
insert into users(email, password, active) values('sadmin123@gmail.com', '$2y$10$yupTWmZ.qFDM7jmlGhp08ezw6wiDGXlh0pu42Zqah5Z895gEHDQEi', 1);

insert into roles(email, role) values('admin123@gmail.com', 'ROLE_ADMIN');
insert into roles(email, role) values('sadmin123@gmail.com', 'ROLE_ADMIN');

insert into districts values (1,'Ahmednagar'),(2,'Akola'),(3,'Amravati'),(4,'Aurangabad'),(5,'Beed'),(6,'Bhandara'),(7,'Buldhana'),(8,'Chandrapur'),(9,'Dhule'),(10,'Gadchiroli'),(11,'Gondia'),(12,'Hingoli'),(13,'Jalgaon'),(14,'Jalna'),(15,'Kolhapur'),(16,'Latur'),(17,'Mumbai City'),(18,'Mumbai Suburban'),(19,'Nagpur'),(20,'Nanded'),(21,'Nandurbar'),(22,'Nashik'),(23,'Osmanabad'),(24,'Palghar'),(25,'Parbhani'),(26,'Pune'),(27,'Raigad'),(28,'Ratnagiri'),(29,'Sangli'),(30,'Satara'),(31,'Sindhudurg'),(32,'Solapur'),(33,'Thane'),(34,'Wardha'),(35,'Washim'),(36,'Yavatmal');

insert into tehsils (tehsil_id, tehsil_name, district_id) values(5,'Ambegaon',26),(31,'Andheri',18),(23,'Baglan',22),(8,'Baramati',26),(12,'Bhor',26),(32,'Borivali',18),(21,'Chandwad',22),(10,'Daund',26),(26,'Deola',22),(17,'Dindori',22),(1,'Haveli',26),(16,'Igatpuri',22),(9,'Indapur',26),(4,'Junnar',26),(22,'Kalwan',22),(6,'Khed',26),(30,'Kurla',18),(20,'Malegaon',22),(3,'Mawal',26),(2,'Mulshi',26),(33,'Mumbai City',17),(27,'Nandgaon',22),(14,'Nashik',22),(18,'Niphad',22),(28,'Peint',22),(25,'Peth',22),(13,'Purandar',26),(7,'Shirur',26),(15,'Sinnar',22),(29,'Surgana',22),(24,'Trimbakeshwar',22),(11,'Velhe',26),(19,'Yeola',22);

insert into categories (id, user_id, name) values (3, 1001,'  इनलाईन लॅटरल'),(13, 1001,'एन्ड कॅप'),(17, 1001,'एफ. टी. ए.'),(14, 1001,'एल. बो.'),(9, 1001,'कंट्रोल व्हॉल्व'),(16, 1001,'कपलिंग'),(11, 1001,'जी. टी. ओ.'),(12, 1001,'जॉईनेर'),(15, 1001,'टी.'),(4, 1001,'प्लेन लॅटरल'),(20, 1001,'फिटिंग अँड असेसरीज'),(10, 1001,'फ्लश व्हॉल्व'),(1, 1001,'मेन लाईन'),(18, 1001,'रेड्युसर'),(8, 1001,'व्हेंचुरी'),(2, 1001,'सब मेन लाईन'),(6, 1001,'सॅण्ड फिल्टर'),(19, 1001,'सोल्युशन'),(5, 1001,'स्क्रीन फिल्टर'),(7, 1001,'हायड्रोसायक्लोन फिल्टर');
insert into categories (id, user_id, name) values (3, 1002,'  इनलाईन लॅटरल'),(13, 1002,'एन्ड कॅप'),(17, 1002,'एफ. टी. ए.'),(14, 1002,'एल. बो.'),(9, 1002,'कंट्रोल व्हॉल्व'),(16, 1002,'कपलिंग'),(11, 1002,'जी. टी. ओ.'),(12, 1002,'जॉईनेर'),(15, 1002,'टी.'),(4, 1002,'प्लेन लॅटरल'),(20, 1002,'फिटिंग अँड असेसरीज'),(10, 1002,'फ्लश व्हॉल्व'),(1, 1002,'मेन लाईन'),(18, 1002,'रेड्युसर'),(8, 1002,'व्हेंचुरी'),(2, 1002,'सब मेन लाईन'),(6, 1002,'सॅण्ड फिल्टर'),(19, 1002,'सोल्युशन'),(5, 1002,'स्क्रीन फिल्टर'),(7, 1002,'हायड्रोसायक्लोन फिल्टर');

-- insert into manufacturers(id, user_id, name) values (4,1001,'DripCo'),(1,1001,'Jain'),(2,1001,'Skipper'),(3,1001,'Venuka');
-- insert into manufacturers(id, user_id, name) values (4,1002,'DripCo'),(1,1002,'Jain'),(2,1002,'Skipper'),(3,1002,'Venuka'),(5,1002,'Bhide');