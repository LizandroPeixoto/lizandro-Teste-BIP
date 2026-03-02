;; Detects local variable declarations with CNPJ-related names
;; Matches: cnpj, doc, documento, inscricao, inscricaoFederal, taxId, taxpayer, companyId, idFiscal, registration, emitter, remetente, destinatario, raizCnpj, baseCnpj, filial, dv
(
  (local_variable_declaration
    type: (_) @type
    declarator: (variable_declarator
      name: (identifier) @name
    )
  ) @violation
  (#match? @name "(?i)(cnpj|doc|documento|inscricao|inscricaoFederal|taxId|taxpayer|companyId|idFiscal|registration|emitter|remetente|destinatario|raizCnpj|baseCnpj|filial|dv)")
)

