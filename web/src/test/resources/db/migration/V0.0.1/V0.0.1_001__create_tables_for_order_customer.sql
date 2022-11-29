/*
--  Copyright 2022 berni3.
--
--  Licensed under the Apache License, Version 2.0 (the "License");
--  you may not use this file except in compliance with the License.
--  You may obtain a copy of the License at
--
--       http://www.apache.org/licenses/LICENSE-2.0
--
--  Unless required by applicable law or agreed to in writing, software
--  distributed under the License is distributed on an "AS IS" BASIS,
--  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
--  See the License for the specific language governing permissions and
--  limitations under the License.
*/

/*
--  Author:  berni3
--  Created: Nov 27, 2022
--
--  Description:
--    Create tables for Order, and Customer entities
--
--  Effect:
--    Tables, sequences, constraints and indexes are created
*/

create table CustomerEntity (
    id bigint not null,
    version integer not null,
    companyName varchar(255),
    contactName varchar(255),
    contactTitle varchar(255),
    customerID varchar(255),
    fax varchar(255),
    address varchar(255),
    city varchar(255),
    country varchar(255),
    postalcode varchar(255),
    region varchar(255),
    phone varchar(255),
    primary key (id)
);

create table OrderEntity (
    id bigint not null,
    version integer not null,
    customerID varchar(255),
    employeeID varchar(255),
    orderDate varchar(255),
    requiredDate varchar(255),
    freight varchar(255),
    shipAddress varchar(255),
    shipCity varchar(255),
    shipCountry varchar(255),
    shipName varchar(255),
    shipPostalcode varchar(255),
    shipRegion varchar(255),
    shipVia varchar(255),
    shippedDate varchar(255),
    primary key (id)
);