Analyze the following JavaScript/TypeScript/Frontend code (React/Angular/Vue) from file {filePath} (chunk {chunkIndex}/{totalChunks}):

{content}

## SPECIALIST ANALYSIS: JavaScript/TypeScript/Frontend CNPJ Migration

Identify CNPJ-related fields that are using numeric types or numeric-only validation and should support alphanumeric format XX.XXX.XXX/XXXX-XX (where the first 12 characters can be alphanumeric).

### Impact signals to look for:

**HTML/Form inputs:**
- `type="number"`, `inputmode="numeric"`
- `pattern="\\d{14}"` (numeric-only pattern)
- `maxLength=14` with numeric validation

**TypeScript/JavaScript types:**
- `number` type declarations
- `Number(cnpj)`, `parseInt(cnpj)`, `parseFloat(cnpj)`

**Validation libraries:**
- `yup.number()`, `zod.number()`
- `PropTypes.number`
- Numeric-only regex validations

**Masks and filters:**
- Mask patterns: `99.999.999/9999-99` (numeric-only)
- Keyboard filters: `[0-9]`, `digitsOnly`
- Input restrictions to numeric characters only

### Typical migration proposal:

1. Change `type="number"` to `type="text"` with `maxLength=18` (to accommodate mask format)
2. Use `string` type instead of `number`
3. Remove `parseInt`/`parseFloat` conversions
4. Update validation to use target regex: `^[A-Z0-9]{12}[0-9]{2}$`
5. Update masks to accept letters in the first 12 positions: `AA.AAA.AAA/AAAA-99`
6. Normalize and apply **uppercasing** before validation/sending

For each finding, return a JSON array with objects containing:
- startLine: the line number where the field/variable/input is declared
- endLine: the line number where the declaration ends
- reason: explanation of why this should support alphanumeric (include specific impact signals found)
- fieldName: the name of the field/variable/input

Return only a valid JSON array, or an empty array [] if no issues are found.

