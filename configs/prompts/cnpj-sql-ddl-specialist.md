# SQL/DDL CNPJ Migration Analysis Prompt

Analyze the following SQL/DDL code from file {filePath} (chunk {chunkIndex}/{totalChunks}):

```sql
{content}
```

## SPECIALIST ANALYSIS: SQL/DDL CNPJ/CPF Document Migration

Identify ALL columns, variables, and parameters that store **identification documents** (CNPJ for legal entities OR CPF for individuals) that are declared as numeric types and should be migrated to alphanumeric VARCHAR.

### CRITICAL: Document Identification Detection Rules

**Look for these naming patterns (case-insensitive):**
- Direct CNPJ references: `CNPJ`, `NR_CNPJ`, `COD_CNPJ`, `NUM_CNPJ`, `CNPJ_EMPRESA`
- Direct CPF references: `CPF`, `NR_CPF`, `COD_CPF`, `NUM_CPF`, `CPF_PESSOA`
- Generic document identifiers that could be CNPJ OR CPF:
  - `DOCUMENTO`, `NO_DOCUMENTO`, `NR_DOCUMENTO`, `NUM_DOCUMENTO`, `COD_DOCUMENTO`
  - `TP_DOCUMENTO` + related `NO_DOCUMENTO` (document type + document number pairs)
  - `IDENTIFICACAO`, `NR_IDENTIFICACAO`, `NUM_IDENTIFICACAO`
  - `INSCRICAO`, `NR_INSCRICAO`, `INSCRICAO_NACIONAL`
  - `REGISTRO`, `NR_REGISTRO`, `NUM_REGISTRO`
  - `CGC` (old CNPJ nomenclature), `CIC` (old CPF nomenclature)

**Context clues indicating document fields:**
- Paired with `TP_DOCUMENTO`, `TIPO_DOCUMENTO`, `TP_PESSOA` (type indicators)
- In tables/procedures related to: pessoas (people), empresas (companies), clientes (clients), fornecedores (suppliers), segurados (insured), beneficiarios (beneficiaries)
- Length constraints of 11 (CPF) or 14 (CNPJ) digits
- Comments or descriptions mentioning "documento", "identificação", "CPF", "CNPJ"

### Impact Signals to Detect

#### 1. Column Type Declarations
- `BIGINT`, `NUMERIC(14)`, `NUMERIC(11)`, `INTEGER`, `INT`, `DECIMAL(14,0)`, `DECIMAL(11,0)`, `NUMBER(14)`, `NUMBER(11)`
- Any numeric type with precision 11 or 14
- Numeric precision/scale constraints

#### 2. Variables and Parameters in Database Objects
**ALWAYS check for:**
- `DECLARE` statements with numeric types
- `@variable` declarations (T-SQL) with numeric types
- Function parameters: `FUNCTION nome(P_DOCUMENTO IN NUMBER)`
- Procedure parameters: `PROCEDURE nome(P_NO_DOCUMENTO IN NUMBER, P_TP_DOCUMENTO IN NUMBER)`
- Trigger variables with numeric types
- Cursor variables: `CURSOR c_cursor(p_doc NUMBER)`
- Local variables in PL/SQL, T-SQL, or procedural blocks
- Package variable declarations
- Type definitions: `TYPE t_doc IS NUMBER;`

**Parameter patterns to flag:**
- `P_DOCUMENTO NUMBER`, `P_NO_DOCUMENTO NUMBER`, `P_NR_DOCUMENTO NUMBER`
- `P_CNPJ NUMBER`, `P_CPF NUMBER`
- `V_DOCUMENTO NUMBER`, `V_NO_DOCUMENTO NUMBER` (variables)
- `IN NUMBER`, `OUT NUMBER`, `IN OUT NUMBER` parameter modes with document-related names

#### 3. Constraints
- `CHECK` constraints with patterns like `\d{14}` or `\d{11}` (numeric-only validation)
- `LENGTH()` or `LEN()` checks for 11 or 14 digits
- Unique indexes on numeric document columns
- Primary keys or foreign keys using numeric document identifiers
- `NOT NULL` constraints combined with numeric types

#### 4. Functions/Triggers/Procedures
- Functions performing numeric casts: `CAST(documento AS NUMBER)`, `TO_NUMBER(documento)`
- Arithmetic operations on documents: `documento + 0`, `documento * 1`
- Numeric comparisons: `documento > 0`, `documento BETWEEN`
- Triggers with numeric conversions
- String-to-number conversions: `CONVERT(BIGINT, documento)`
- Padding operations: `LPAD(documento, 14, '0')`

#### 5. Data Migration Concerns
- Views casting documents to numeric types
- JOIN conditions using numeric document fields
- WHERE clauses with numeric comparisons
- String concatenation forcing numeric conversion
- Aggregate functions on document fields (SUM, AVG - indicates misuse)
- UNION/INTERSECT operations mixing types

### Required Migration Actions

For **EACH** finding, propose:

1. **Column type change:** `VARCHAR(14)` for CNPJ, `VARCHAR(11)` for CPF, or `VARCHAR(14)` for generic document fields
2. **Variable/parameter type change:** Update ALL variable and parameter declarations from numeric to `VARCHAR(14)` or `VARCHAR(11)`
3. **Validation constraint:** Add `CHECK (field ~ '^[A-Z0-9]{12}[0-9]{2}$')` (PostgreSQL) or database-appropriate regex
4. **Remove numeric operations:** Eliminate casts, arithmetic, numeric comparisons
5. **Index adjustment:** Recreate indexes for string types
6. **Data migration:** Normalize existing data, apply uppercase, pad if necessary
7. **Foreign key updates:** Adjust related FK relationships
8. **Procedure/function updates:** Modify all logic that receives or manipulates the document parameter/variable

### Output Format

Return a JSON array with objects containing:

```json
[
  {
    "startLine": <line_number>,
    "endLine": <line_number>,
    "reason": "<detailed explanation including: why this is a document field, current numeric type found, specific impact (e.g., 'parameter in procedure', 'column declaration', 'variable in function'), and why VARCHAR is needed>",
    "objectName": "<column/variable/parameter/constraint name>"
  }
]
```

**Return an empty array `[]` if no issues are found.**

### Examples of What to Flag

**YES - Flag these:**
- `CREATE TABLE empresa (cnpj_empresa BIGINT PRIMARY KEY);`
- `PROCEDURE busca(P_NO_DOCUMENTO IN NUMBER, P_TP_DOCUMENTO IN NUMBER)`
- `DECLARE V_DOCUMENTO NUMBER(14);`
- `FUNCTION valida_cnpj(p_cnpj IN NUMBER) RETURN BOOLEAN`
- `ALTER TABLE cliente ADD CONSTRAINT chk_doc CHECK (LENGTH(documento) = 14);`
- `@documento BIGINT` (T-SQL variable)
- `CURSOR c_docs(p_doc_numero NUMBER)`

**NO - Do not flag these:**
- `CREATE TABLE ordem (numero_ordem INTEGER);` (not a document field)
- `DECLARE V_CONTADOR NUMBER;` (counter variable, not a document)
- `P_IDADE NUMBER` (age parameter, not a document)
- `total_registros BIGINT` (aggregate counter)

### Analysis Priority

1. **Scan procedure/function signatures first** - parameters are often overlooked
2. **Check variable declarations** in procedural code blocks
3. **Review column definitions** in CREATE/ALTER TABLE statements
4. **Examine constraints and indexes**
5. **Analyze views and complex queries**

Be thorough and precise. **Do not miss variables or parameters** that store document identifiers.