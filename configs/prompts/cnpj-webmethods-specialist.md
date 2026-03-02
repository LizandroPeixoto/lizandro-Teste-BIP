Analyze the following WebMethods code from file {filePath} (chunk {chunkIndex}/{totalChunks}):

{content}

## SPECIALIST ANALYSIS: WebMethods CNPJ Migration

Identify CNPJ-related fields, variables, parameters, and database columns that are declared as numeric types and should be migrated to alphanumeric String/VARCHAR to support the format XX.XXX.XXX/XXXX-XX (where the first 12 characters can be alphanumeric).

### Impact signals to look for:

**Java code (.java files):**
- Variables/fields declared as `Long`, `Integer`, `BigInteger`, `BigDecimal`, `long`, `int` with names containing "cnpj", "CNPJ", "cpfCnpj", "documento", "identificacao"
- SQL queries embedded in Java strings with numeric CNPJ columns: `SELECT ... WHERE cnpj = ?` or `WHERE no_cnpj = ?`
- PreparedStatement parameters bound to numeric CNPJ fields
- ResultSet getLong/getInt calls for CNPJ columns
- Numeric type conversions: `Long.parseLong(cnpj)`, `Integer.parseInt(cnpj)`, `new Long(cnpj)`
- Database field mappings with numeric types (JPA/Hibernate annotations with numeric precision)

**Flow XML files (.flow.xml):**
- Database adapter invocations (`adap:`) referencing stored procedures/functions with CNPJ parameters
- Field mappings in MAP nodes that reference numeric CNPJ fields
- Input/output document types with numeric CNPJ fields
- References to database fields with names like `NO_CNPJ`, `CD_CNPJ`, `CNPJ`, `CNPJ_CPF`, `DOCUMENTO`
- Field type declarations as `integer`, `long`, `number`, `numeric` for CNPJ-related fields

**Node Definition files (.ndf):**
- Field definitions with `field_type` as `integer`, `long`, `number`, `numeric`, `decimal` for CNPJ-related fields
- Field names containing "cnpj", "CNPJ", "cpfCnpj", "documento", "identificacao" with numeric types
- Adapter service signatures (sig_in/sig_out) with numeric CNPJ parameters
- Stored procedure parameter definitions with numeric types for CNPJ
- Output cursor fields from database queries with numeric CNPJ columns

**Interface Definition files (.idf):**
- Interface field definitions with numeric types for CNPJ-related fields
- Document type definitions with numeric CNPJ fields

**Database adapter references:**
- Stored procedure calls (SPS) with numeric CNPJ parameters
- Function calls with numeric CNPJ return types or parameters
- Database field references in adapter configurations
- Connection/query configurations referencing numeric CNPJ columns

**Variable and parameter declarations:**
- Variables in Java code with numeric types for CNPJ
- Pipeline variables (`$variable`) with numeric types for CNPJ
- Service input/output parameters with numeric CNPJ types
- Local variables in flow services with numeric CNPJ types

### Typical migration proposal:

1. **Java code:**
   - Change variable/field types from `Long`/`Integer`/`BigInteger` to `String`
   - Update SQL queries to use VARCHAR/CHAR instead of numeric types
   - Change ResultSet calls from `getLong()`/`getInt()` to `getString()`
   - Remove numeric parsing: replace `Long.parseLong(cnpj)` with string normalization
   - Update PreparedStatement from `setLong()`/`setInt()` to `setString()`

2. **Flow XML:**
   - Update field type mappings from `integer`/`long`/`number` to `string`
   - Update document type definitions to use `string` for CNPJ fields
   - Modify adapter service invocations to pass CNPJ as string

3. **Node Definition files (.ndf):**
   - Change `field_type` from `integer`/`long`/`number`/`numeric`/`decimal` to `string`
   - Update adapter service signatures to use string for CNPJ parameters
   - Modify stored procedure parameter definitions to VARCHAR(14)

4. **Database layer:**
   - Update database column types from `BIGINT`/`NUMERIC(14)`/`INTEGER` to `VARCHAR(14)` or `CHAR(14)`
   - Add validation pattern: `^[A-Z0-9]{12}[0-9]{2}$`
   - Update stored procedures/functions to use VARCHAR for CNPJ parameters
   - Review and update all database queries, views, and triggers

5. **Validation and normalization:**
   - Add string validation with pattern `^[A-Z0-9]{12}[0-9]{2}$`
   - Normalize CNPJ values: remove formatting, uppercase letters
   - Update masks/formatters to accept alphanumeric format (AA.AAA.AAA/AAAA-99)

For each finding, return a JSON array with objects containing:
- startLine: the line number where the field/variable/parameter/definition is declared or referenced
- endLine: the line number where the declaration/reference ends
- reason: explanation of why this should be String/VARCHAR (include specific impact signals found and file type context)
- objectName: the name of the field/variable/parameter/column/definition

Return only a valid JSON array, or an empty array [] if no issues are found.

