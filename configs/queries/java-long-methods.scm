; Query to find long Java methods (more than 20 lines)
; This is a simple test query for Java parser

; Long methods (more than 20 lines) - using line count
(method_declaration
  (modifiers) @modifiers
  name: (identifier) @method_name
  body: (block) @method_body
  (#match? @method_body ".*\n.*\n.*\n.*\n.*\n.*\n.*\n.*\n.*\n.*\n.*\n.*\n.*\n.*\n.*\n.*\n.*\n.*\n.*\n.*")
) @violation
