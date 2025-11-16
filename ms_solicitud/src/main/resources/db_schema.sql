-- =====================================================================
-- LEGO - Normalization of THEME, AGE and COUNTRY
-- Base: PUBLIC.LEGO_SETS (flattened dataset)
-- Engine: H2 2.x
-- =====================================================================

-- ---------------------------------------------------------------------
-- Cleanup (idempotent)
-- ---------------------------------------------------------------------

-- ---------------------------------------------------------------------
-- Sequences (auto-increment strategy)
-- ---------------------------------------------------------------------
CREATE SEQUENCE IF NOT EXISTS SEQ_SOLICITUD_ID   START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE IF NOT EXISTS SEQ_CONTENEDOR_ID      START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE IF NOT EXISTS SEQ_CLIENTE_ID  START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE IF NOT EXISTS SEQ_TARIFA_ID    START WITH 1 INCREMENT BY 1;

-- ---------------------------------------------------------------------
-- Table: COUNTRIES
-- ID_COUNTRY (PK), CODE (indexed + unique), NAME
-- ---------------------------------------------------------------------
CREATE TABLE CLIENTES (
    DNI             INTEGER      NOT NULL,
    NOMBRE        VARCHAR(30)   NOT NULL,
    APELLIDO        VARCHAR(30) NOT NULL,
    CONSTRAINT PK_CLIENTE PRIMARY KEY (DNI)
);
------------------------------------------------------
-- Table: THEMES (catalog of themes)
-- ---------------------------------------------------------------------
CREATE TABLE CONTENEDORES (
    ID_CONTENEDOR  INTEGER      NOT NULL DEFAULT NEXT VALUE FOR SEQ_CONTENEDOR_ID,
    PESO           DOUBLE       NOT NULL,
    VOLUMEN           DOUBLE       NOT NULL,
    ESTADO           VARCHAR(30)       NOT NULL,
    TIEMPO_ESTADIA   INTEGER        NOT NULL,
    CONSTRAINT PK_CONTENEDOR PRIMARY KEY (ID_CONTENEDOR)
);

-- ---------------------------------------------------------------------
-- Table: AGE_GROUPS (catalog of age ranges)
-- CODE = dataset literal (e.g., '6-12', '12')
-- ---------------------------------------------------------------------
CREATE TABLE TARIFAS (
    ID_TARIFA  INTEGER      NOT NULL DEFAULT NEXT VALUE FOR SEQ_TARIFA_ID,
    VALORFIJOTRAMO      DOUBLE       NOT NULL,
    VALORPORVOLUMEN      DOUBLE       NOT NULL,
    VALORFIJOCOMBUSTIBLE      DOUBLE       NOT NULL,
    VALORPORESTADIA     DOBLE       NOT NULL,
    CONSTRAINT PK_TARIFA PRIMARY KEY (ID_TARIFA)
);

-- ---------------------------------------------------------------------
-- Table: LEGO_SETS (normalized destination)
-- * Surrogate PK ID_SET
-- * FKs to THEMES, AGE_GROUPS, COUNTRIES
-- ---------------------------------------------------------------------
CREATE TABLE SOLICITUDES (
    NUMERO            INTEGER       NOT NULL DEFAULT NEXT VALUE FOR SEQ_SOLICITUD_ID,
    CONTENEDOR_ID           INTEGER       NOT NULL,
    DNICLIENTE          INTEGER  NOT NULL,
    COSTO_ESTIMADO        DOUBLE,
    TIEMPO_ESTIMADO     DOUBLE,
    COSTO_FINAL       DOUBLE,
    TIEMPO_REAL       DOUBLE,
    CONSUMO_ESTIMADO        DOUBLE,
    ID_TARIFA          INTEGER       NOT NULL,
    COORDENADAS_ORIGEN      DOUBLE       NOT NULL,
    COORDENADAS_DESTINO        DOUBLE       NOT NULL,
    CONSTRAINT PK_SOLICITUD PRIMARY KEY (NUMERO),
    CONSTRAINT FK_SOLICITUD_CONTENEDOR     FOREIGN KEY (CONTENEDOR_ID)     REFERENCES CONTENEDORES(ID_CONTENEDOR),
    CONSTRAINT FK_SOLICITUD_CLIENTE  FOREIGN KEY (DNICLIENTE) REFERENCES CLIENTES(DNI),
    CONSTRAINT FK_SOLICITUD_TARIFA   FOREIGN KEY (ID_TARIFA)   REFERENCES TARIFAS(ID_TARIFA)
);

-- Suggested indexes for queries
CREATE INDEX IF NOT EXISTS IX_SOLICITUD_CLIENTE ON SOLICITUDES (DNICLIENTE);
CREATE INDEX IF NOT EXISTS IX_SOLICITUD_TARIFA ON SOLICITUDES (ID_TARIFA);
CREATE INDEX IF NOT EXISTS IX_SOLICITUD_CONTENEDOR ON SOLICITUDES (CONTENEDOR_ID);
-- =====================================================================
-- End of DDL
-- =====================================================================
