;; Detects string literals containing "CNPJ" (case-insensitive)
(
  (string_literal) @violation
  (#match? @violation "(?i).*cnpj.*")
)

