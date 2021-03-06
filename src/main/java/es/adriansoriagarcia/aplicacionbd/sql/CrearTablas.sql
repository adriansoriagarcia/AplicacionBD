

CREATE TABLE EMPLE (
 EMP_NO NUMERIC(4) NOT NULL,
 NOMBRE VARCHAR(20) NOT NULL,
 APELLIDO VARCHAR(10),
 OFICIO VARCHAR(10),
 DIR NUMERIC(4),
 FECHA_ALT DATE,
 NUM_HIJOS SMALLINT,
 ESTADO_CIVIL CHAR(1),
 SALARIO DECIMAL(7,2),
 COMISION INTEGER,
 BAJA BOOLEAN,
 DEPT_NO NUMERIC(2),
 IMAGEN VARCHAR(30),
 CONSTRAINT ID_EMPLE_PK PRIMARY KEY (EMP_NO),
 CONSTRAINT FK_EMPLE FOREIGN KEY(DEPT_NO) REFERENCES DEPART (DEPT_NO)
);

