;; Detects comments containing "CNPJ" (case-insensitive)
(
  (line_comment) @violation
  (#match? @violation "(?i).*cnpj.*")
)

;; Also matches block comments
(
  (block_comment) @violation
  (#match? @violation "(?i).*cnpj.*")
)

