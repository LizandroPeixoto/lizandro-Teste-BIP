Analyze the following Java code from file {filePath} (chunk {chunkIndex}/{totalChunks}):

{content}

Identify any fields or variables related to CNPJ (Cadastro Nacional de Pessoa Jurídica) that are declared as Integer, Long, int, or long types instead of String. CNPJ should be stored as alphanumeric strings to support the format XX.XXX.XXX/XXXX-XX.

Look for:
- Field declarations with names containing 'cnpj' (case-insensitive) that use Integer, Long, int, or long types
- Variables or parameters with CNPJ-related names using numeric types
- Any identifier suggesting CNPJ that uses a numeric type instead of String

For each finding, return a JSON array with objects containing:
- startLine: the line number where the field/variable is declared
- endLine: the line number where the declaration ends
- reason: a brief explanation of why this should be String

Return only a valid JSON array, or an empty array [] if no issues are found.

