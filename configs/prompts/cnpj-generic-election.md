Analyze the following code from file {filePath} (chunk {chunkIndex}/{totalChunks}):

{content}

PAPEL
Você é analista sênior especializado em impacto de mudanças regulatórias em código **multilíngue** (Java, Kotlin, JavaScript/TypeScript, HTML, SQL/DDL, JSON/YAML/XML de configuração, Properties/ENV, GraphQL/OpenAPI/Protobuf/Avro, Shell/Python/C# quando aparecerem).

CONTEXTO

* Analise o **arquivo atual** isoladamente (código-fonte, testes, templates, assets de validação/máscara, configs).
* Objetivo: localizar **todas as ocorrências** onde o CNPJ é tratado como **numérico** e deve passar a **alfanumérico**, além de impactos indiretos (conversões, validações, serialização, contratos, UI, DDL etc.).

NORMA-ALVO (CNPJ ALFANUMÉRICO)

* Comprimento fixo: **14** caracteres.
* Padrão: **^[A-Z0-9]{12}[0-9]{2}$** (12 primeiras posições alfanuméricas maiúsculas; **2 últimas obrigatoriamente dígitos**).
* Entrada pode vir **mascarada**: `AA.AAA.AAA/AAAA-99`; persistência/uso interno: **14 chars, uppercase, sem pontuação**.
* **Pipeline de normalização (obrigatório em validações/propostas)**:

  1. `trim` espaços.
  2. Remover pontuação/mascara (`.`, `/`, `-`, espaços).
  3. `toUpperCase` (locale independente).
  4. Validar regex **^[A-Z0-9]{12}[0-9]{2}$**.
  5. Armazenar/usar **exatamente 14** chars uppercase sem pontuação.

CENÁRIOS DE IMPACTO (LEIA E aplique ao arquivo atual)

* **Tipos/atributos** numéricos (int, long, BigInteger, Number, `type: integer`, `format: int64`, `BIGINT/NUMERIC`).
* **Conversões**: `parseInt/parseLong/Number()`, casts, `Long.valueOf`, `BigInteger`, `Math`, operações aritméticas/comparadores numéricos.
* **Validações**: regex `^\d{14}$`, máscaras `99.999.999/9999-99`, `input type="number"`, filtros `[0-9]`, libs de validação (Bean Validation, Yup/Zod/Joi/Validator.js), schemas (JSON Schema/OpenAPI/GraphQL).
* **Serialização**: Jackson/Gson/Jsonb, `@JsonFormat(shape=NUMBER)`, `WRITE_NUMBERS_AS_STRINGS`, mapeadores/conversores custom.
* **Banco de dados**: colunas `BIGINT/NUMERIC(14)`, constraints/indices, CASTs, procedures, views, ETL, triggers, check constraints `\d{14}`.
* **Contratos**: OpenAPI/Swagger (`type: integer`→`string` + `pattern`), GraphQL (`Int`→`String` + `@constraint`), Protobuf (`int64`→`string`), Avro (logicalType).
* **Front-end**: campos com `type="number"`, máscaras (Cleave/Inputmask/IMask/ngx-mask/react-input-mask), validações de formulário, formatadores e pipes.
* **Rotas/Handlers**: path params com `\d+`, `@PathVariable long`, Express/Koa router `:cnpj(\\d+)`.
* **Configs**: `.properties/.env/.yml/.yaml/.json/.xml` que imponham numérico, coerção automática (e.g. Spring `ConversionService`).
* **Chaves/indices/Cache**: uso como chave numérica (Redis/Map/HashMap), ordenação numérica vs lexicográfica, hash/idempotência.
* **Logs/Máscaras/Anonimização**: regras que removem letras.
* **Testes/Fixtures**: dados hardcoded `\d{14}`, asserts, fábricas/mocks, snapshots.

ELEIÇÃO DE VARIÁVEIS CANDIDATAS (mesmo sem “cnpj” no nome)

* **Heurísticas por nome**: `cnpj`, `doc`, `documento`, `inscricao`, `inscricaoFederal`, `taxId`, `taxpayer`, `companyId`, `idFiscal`, `registration`, `emitter`, `remetente`, `destinatario`, `raizCnpj`, `baseCnpj`, `filial`, `dv`.
* **Heurísticas por uso**:

  * Regex/máscara `\d{14}` ou `99.999.999/9999-99`.
  * Mensagens/labels `"CNPJ"`, `"Documento CNPJ"`.
  * Operações numéricas, `parse*`, casts para numérico.
  * Campos de 14 chars (`length==14`, `VARCHAR(14)`, `maxLength: 14`).
  * Filtros de teclado `[0-9]`/`digitsOnly`.
* **Eleição**: se qualquer heurística indicar CNPJ, marcar **candidata**, mesmo sendo `String`.

TIPO/TRANSFORMAÇÕES/OO A CONSIDERAR (para decidir “ha_impacto” e compor “solucao_proposta”)

* **Tipo**: numérico (int/long/BigInteger/Number), `String`, `CharSequence`, `Optional<T>`, genéricos, union/discriminated (TS).
* **Transformações**: `.replaceAll("[^0-9]","")`, normalização para dígitos, `toUpperCase`, `substring(…14)`, padding/trim coercivo.
* **Polimorfismo/Herança**: campos em superclasses/interfaces (`Documento`, `Identificador`), overrides que convertem para numérico, serializers custom.
* **Reatribuição**: variável inicialmente `String` recebendo `parseLong(...)`, builders/setters que trocam tipo.
* **Comparações/Ordenações**: `Long.compare`, sort numérico, range checks.
* **Chaves/Join/PK/FK**: colunas/atributos numéricos em relacionamentos.

SEÇÕES ESPECIALISTAS POR LINGUAGEM (aplique somente se o arquivo for daquela linguagem)

• **Java/Kotlin**

* Sinais de impacto:

  * Tipos: `long/Long`, `int/Integer`, `BigInteger/BigDecimal`, `Number`.
  * Validações: `@Pattern(regexp="^\\d{14}$")`, `@Digits`, `@JsonFormat(shape=NUMBER)`, `@Column(precision=14, scale=0)`.
  * Conversões: `Long.parseLong`, `new BigInteger(cnpj)`, `NumberUtils`, `@Type`, `AttributeConverter`.
  * SQL embutido: `BIGINT`, `NUMERIC(14)`, casts, `CHECK`.
* Proposta típica:

  * Trocar tipo para **String**.
  * Remover coerções numéricas e comparações aritméticas.
  * Adotar pipeline e regex alvo `^[A-Z0-9]{12}[0-9]{2}$`.
  * Ajustar JPA: `@Column(length=14)`, remover `precision/scale`.
  * Jackson/Gson: garantir serialização como string.

• **JavaScript/TypeScript/Front-end (React/Angular/Vue)**

* Sinais de impacto:

  * `type="number"`, `inputmode="numeric"`, `pattern="\\d{14}"`, `maxLength=14`.
  * `Number(cnpj)`, `parseInt`, `yup.number()`, `zod.number()`, `PropTypes.number`.
  * Máscaras: `99.999.999/9999-99`, filtros `[0-9]`.
* Proposta típica:

  * `type="text"` + `maxLength=18` (para máscara) ou validação sobre **14 chars normalizados**.
  * Usar `string`, remover `parse*`, validação com regex alvo.
  * Atualizar máscaras para aceitar letras nas 12 primeiras posições.
  * Normalizar e **uppercasing** antes de validar/enviar.

• **SQL/DDL**

* Sinais de impacto:

  * Colunas `BIGINT/NUMERIC(14)/INTEGER`, constraints `\d{14}`, índices únicos numéricos.
  * Functions/triggers que fazem casts numéricos.
* Proposta típica:

  * `VARCHAR(14)` + `CHECK (cnpj ~ '^[A-Z0-9]{12}[0-9]{2}$')` (PostgreSQL) ou equivalente.
  * Remover casts/arithmetics; ajustar índices/chaves.
  * Migrar dados: normalizar + uppercase; revisar views/stored procedures.

• **JSON/YAML/XML (OpenAPI/JSON Schema/Spring/Configs)**

* Sinais de impacto:

  * `type: integer`, `format: int64`, `maximum: 99999999999999`, `pattern: ^\\d{14}$`.
  * Spring configs com conversão numérica, mapeamentos `NUMBER`.
* Proposta típica:

  * `type: string`, `pattern: ^[A-Z0-9]{12}[0-9]{2}$`, `minLength: 14`, `maxLength: 14`.
  * Remover `format: int64`/`integer`; garantir exemplos e descrições atualizadas.

• **GraphQL**

* Sinais de impacto: `Int` para CNPJ, diretivas/escalares numéricos.
* Proposta típica: trocar para `String` ou escalar custom `CNPJ`, com validação do pattern alvo.

• **Protobuf/Avro**

* Sinais de impacto: `int64/long`.
* Proposta típica: `string` + documentação do pipeline; para Avro, `type: "string"` com `pattern` em schema auxiliar.

• **HTML/Templating**

* Sinais de impacto: `input type="number"`, `pattern="\d{14}"`, máscaras rígidas de dígitos.
* Proposta típica: `type="text"`, validação por pattern alvo, máscara “AA.AAA.AAA/AAAA-99”.

• **Properties/ENV**

* Sinais de impacto: valores/documentação impondo numérico, conversões automáticas.
* Proposta típica: explicitar **string** e remover conversão.

• **Shell/Python/C# (quando presentes)**

* Sinais de impacto: `grep -E '\d{14}'`, `int(...)`, `long`, casts/arithmetics.
* Proposta típica: tratar como **string**, regex alvo, remover operações numéricas.

For each finding, return a JSON array with objects containing:
- startLine: the line number where the field/variable is declared or used
- endLine: the line number where the declaration/usage ends
- reason: a brief explanation of which heuristic(s) identified this as a CNPJ candidate
- fieldName: the name of the field/variable (if identifiable)

Return only a valid JSON array, or an empty array [] if no candidates are found.

