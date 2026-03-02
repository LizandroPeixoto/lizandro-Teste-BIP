;; Encontra: private Integer codDocumento;
(
  (field_declaration
    (modifiers) @mods
    type: (_) @type         ;; coringa para qualquer nó de tipo
    declarator: (variable_declarator
      name: (identifier) @name
    )
  ) @violation
  (#match? @mods "\\bprivate\\b")   ;; precisa ser private
  (#match? @type "\\bInteger\\b")   ;; texto do tipo contém 'Integer'
  (#eq? @name "codDocumento")       ;; nome exato do campo
)
