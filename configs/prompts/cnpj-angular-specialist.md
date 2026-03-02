Analyze the following Angular/TypeScript code from file {filePath} (chunk {chunkIndex}/{totalChunks}):

{content}

## SPECIALIST ANALYSIS: Angular CNPJ Migration

Identify CNPJ-related fields, validators, masks, and formatters that are using numeric types or numeric-only validation and should support alphanumeric format XX.XXX.XXX/XXXX-XX (where the first 12 characters can be alphanumeric).

### Impact signals to look for:

**Angular Reactive Forms:**
- `FormControl` with `number` type or numeric validators
- `FormGroup` containing CNPJ fields with numeric validation
- `Validators.pattern(/\\d{14}/)` or similar numeric-only patterns
- Custom validators that only accept digits
- `valueChanges` subscriptions that convert to number

**TypeScript types and interfaces:**
- `number` type declarations for CNPJ fields
- Interface properties: `cnpj: number`, `cnpjNumber: number`, `companyCnpj: number`
- Type assertions: `as number`, `<number>`
- `Number(cnpj)`, `parseInt(cnpj)`, `parseFloat(cnpj)` conversions

**Input masks and directives:**
- Mask patterns: `99.999.999/9999-99` (numeric-only, like ngx-mask, ng2-mask)
- `[mask]="'00.000.000/0000-00'"` with numeric-only mask
- Custom mask directives that filter only digits
- `[allowedPattern]="[0-9]"` or similar restrictions

**HTML templates:**
- `type="number"` or `inputmode="numeric"` on input fields
- `pattern="\\d{14}"` attribute
- `maxLength="14"` with numeric-only validation
- `(keypress)` or `(keydown)` handlers that block non-numeric characters
- `[ngModel]` or `[(ngModel)]` with numeric type coercion

**Pipes and formatters:**
- Custom pipes that format CNPJ as numbers only
- Pipes using `Number` or numeric conversion
- `| number` pipe applied to CNPJ values

**Validation methods:**
- CNPJ validation functions that only check numeric digits
- Regex patterns like `/^\\d{14}$/` or `/^\\d{2}\\.\\d{3}\\.\\d{3}\\/\\d{4}-\\d{2}$/`
- Validation that uses `Number.isInteger()` or similar numeric checks
- Custom validators: `cnpjValidator()`, `validateCnpj()`, `isValidCnpj()` that only accept digits

**Nomenclature variations to detect:**
- `cnpj`, `cnpjNumber`, `cnpjNumero`, `companyCnpj`, `empresaCnpj`
- `cnpjEmpresa`, `numeroCnpj`, `cnpjCode`, `cnpjId`
- `documentoCnpj`, `cnpjDocument`, `cnpjDocumento`
- Any identifier containing 'cnpj' (case-insensitive) with numeric handling

**Services and utilities:**
- Service methods that format/validate CNPJ as numeric only
- Utility functions: `formatCnpj()`, `sanitizeCnpj()`, `cleanCnpj()` that remove letters
- HTTP interceptors or API services that convert CNPJ to number before sending

### Typical migration proposal:

1. Change `FormControl` type from `number` to `string`
2. Update mask patterns to accept alphanumeric: `AA.AAA.AAA/AAAA-99` or similar
3. Change input `type="number"` to `type="text"` with `maxLength=18`
4. Update validators to use alphanumeric regex: `^[A-Z0-9]{12}[0-9]{2}$`
5. Remove `Number()`, `parseInt()`, `parseFloat()` conversions
6. Update custom validators to accept alphanumeric format
7. Normalize and apply **uppercasing** before validation/sending
8. Update pipes to handle string format instead of number
9. Modify mask directives to allow letters in first 12 positions

For each finding, return a JSON array with objects containing:
- startLine: the line number where the field/variable/input/validator is declared
- endLine: the line number where the declaration ends
- reason: explanation of why this should support alphanumeric (include specific impact signals found and Angular-specific context)
- fieldName: the name of the field/variable/input/validator
- component: Angular-specific context (e.g., "FormControl", "Validator", "Pipe", "Mask", "Template")

Return only a valid JSON array, or an empty array [] if no issues are found.

