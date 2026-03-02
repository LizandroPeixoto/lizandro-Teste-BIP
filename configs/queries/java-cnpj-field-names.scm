;; Detects field declarations with CNPJ-related names
;; Matches: cnpj, doc, documento, inscricao, inscricaoFederal, taxId, taxpayer, companyId, idFiscal, registration, emitter, remetente, destinatario, raizCnpj, baseCnpj, filial, dv
(
  (field_declaration
    (modifiers) @mods
    type: (_) @type
    declarator: (variable_declarator
      name: (identifier) @name
    )
  ) @violation
  (#match? @name "(?i)(cnpj|doc|documento|inscricao|inscricaoFederal|taxId|taxpayer|companyId|idFiscal|registration|emitter|remetente|destinatario|raizCnpj|baseCnpj|filial|dv)")
)

