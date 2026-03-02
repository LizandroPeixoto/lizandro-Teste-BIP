;; Detects method parameters with CNPJ-related names
;; Matches: cnpj, doc, documento, inscricao, inscricaoFederal, taxId, taxpayer, companyId, idFiscal, registration, emitter, remetente, destinatario, raizCnpj, baseCnpj, filial, dv
(
  (formal_parameter
    type: (_) @type
    name: (identifier) @name
  ) @violation
  (#match? @name "(?i)(cnpj|doc|documento|inscricao|inscricaoFederal|taxId|taxpayer|companyId|idFiscal|registration|emitter|remetente|destinatario|raizCnpj|baseCnpj|filial|dv)")
)

