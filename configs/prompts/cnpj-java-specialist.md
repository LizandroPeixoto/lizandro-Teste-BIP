Analyze the following Java/Kotlin code from file {filePath} (chunk {chunkIndex}/{totalChunks}):

{content}

## SPECIALIST ANALYSIS: Java/Kotlin CNPJ Migration

Identify CNPJ-related fields that are declared as numeric types and should be migrated to alphanumeric String to support the format XX.XXX.XXX/XXXX-XX (where the first 12 characters can be alphanumeric).

### Impact signals to look for:

**Type declarations:**
- `long`, `Long`, `int`, `Integer`, `BigInteger`, `BigDecimal`, `Number`

**Validation annotations:**
- `@Pattern(regexp="^\\d{14}$")`
- `@Digits`
- `@JsonFormat(shape=NUMBER)`
- `@Column(precision=14, scale=0)`

**Numeric conversions:**
- `Long.parseLong(cnpj)`
- `new BigInteger(cnpj)`
- `NumberUtils` conversions
- `@Type` annotations with numeric types
- `AttributeConverter` with numeric conversions

**Embedded SQL:**
- `BIGINT`, `NUMERIC(14)`, `INTEGER` column types
- Numeric casts in queries
- `CHECK` constraints with numeric patterns

### Typical migration proposal:

1. Change field type to **String**
2. Remove numeric coercions and arithmetic comparisons
3. Adopt validation pipeline with target regex: `^[A-Z0-9]{12}[0-9]{2}$`
4. Adjust JPA: use `@Column(length=14)` instead of `precision/scale`
5. Update Jackson/Gson: ensure serialization as string

For each finding, return a JSON array with objects containing:
- startLine: the line number where the field/variable is declared
- endLine: the line number where the declaration ends
- reason: explanation of why this should be String (include specific impact signals found)
- fieldName: the name of the field/variable

Return only a valid JSON array, or an empty array [] if no issues are found.

