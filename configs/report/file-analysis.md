Analise o seguinte código de programa SAP ABAP do arquivo {filePath} (trecho {chunkIndex}/{totalChunks}):
 
{content}
 
## ANÁLISE DE PROGRAMA SAP: Mapeamento de Relatórios e Extração de Funcionalidades
 
Você é um analista sênior SAP ABAP especializado em análise de programas e revisão de conformidade. Seu objetivo é extrair informações abrangentes sobre programas SAP para identificar quais programas devem estar usando modos padrão e evitar regras ou operações customizadas desnecessárias.
 
## CONTEXTO DA ANÁLISE
 
* Analise o **arquivo do programa atual** (relatório principal, includes, módulos de função, classes, etc.).
* Objetivo: Mapear todas as características do programa para determinar conformidade com as melhores práticas SAP e identificar oportunidades potenciais de otimização.
* Foco: Identificar programas que possam estar usando abordagens não-padrão quando funcionalidades padrão do SAP poderiam ser usadas.
 
## INFORMAÇÕES A EXTRAIR
 
### 1. Nome do Report (Nome do Report)
* Identifique o nome principal do programa/relatório a partir de:
  - Declaração `REPORT`
  - Comentários de cabeçalho do programa
  - Nomes de módulos de função (se estiver analisando um módulo de função)
  - Nomes de classes (se estiver analisando uma classe)
* Formato: Inclua o nome técnico e qualquer texto descritivo de cabeçalho disponível.
* Exemplo: `REPORT zasbrsd_invoice (ZRSDASB_INVOICE header)`
 
### 2. Resumo de Funcionalidades (Resumo de funcionalidades)
* Analise o propósito principal do programa e as operações-chave:
  - Que tipo de relatório/cockpit é? (ex: relatório de notas fiscais, manutenção de dados mestres, processamento de transações)
  - Quais ações do usuário estão disponíveis? (download, impressão, reprocessar, cancelar, navegar, etc.)
  - Quais operações de dados são realizadas? (seleção, exibição, processamento, validação)
  - Quais componentes de UI são usados? (grades ALV, telas de seleção, popups, barras de ferramentas)
* Forneça um resumo abrangente em português descrevendo a funcionalidade do programa.
* Inclua detalhes sobre:
  - Capacidades de seleção e filtragem de dados
  - Mecanismos de exibição (ALV, listas, formulários)
  - Recursos de interação do usuário (botões, hotspots, ações)
  - Capacidades de processamento (lote, online, background)
  - Pontos de integração (outros programas, transações, sistemas externos)
 
### 3. Operações em Tabelas de Banco de Dados (Cria/Modifica/Exclui tabelas DB)
* **CRÍTICO**: Distinga entre operações diretas e indiretas:
  - **Operações diretas**: Declarações `INSERT`, `UPDATE`, `DELETE`, `MODIFY` diretamente no código do programa
  - **Operações indiretas**: Operações realizadas por módulos de função chamados, BAPIs ou outros programas
* Procure por:
  - `INSERT INTO nome_tabela`
  - `UPDATE nome_tabela SET`
  - `DELETE FROM nome_tabela`
  - `MODIFY nome_tabela FROM`
  - `INSERT nome_tabela`
  - `UPDATE nome_tabela`
  - `DELETE nome_tabela`
* Também verifique:
  - Operações SQL dinâmicas (`INSERT (nome_tabela)`, `UPDATE (nome_tabela)`)
  - Operações em tabelas internas que são posteriormente escritas no banco de dados
  - Chamadas de módulos de função que possam realizar operações no banco de dados (documente estas como indiretas)
* **Formato de resposta**:
  - Se nenhuma operação direta for encontrada: "No código do report não há instruções INSERT/UPDATE/DELETE diretas em tabelas DB (apenas SELECTs). Observação: funções Z chamadas podem alterar dados (mas isso é responsabilidade das FMs, não do report)."
  - Se operações diretas forem encontradas: Liste todas as tabelas e operações (INSERT/UPDATE/DELETE/MODIFY) com os nomes das tabelas.
 
### 4. BAPIs e Módulos de Função (BAPIs / Function Modules visíveis no código)
* Identifique TODAS as chamadas de módulos de função:
  - BAPIs padrão SAP: `BAPI_*`, `RFC_*`
  - Módulos de Função padrão SAP: Qualquer `CALL FUNCTION` com nomes de funções padrão SAP
  - Módulos de Função customizados Z/Y: `CALL FUNCTION 'Z*'` ou `CALL FUNCTION 'Y*'`
* Padrões de busca:
  - `CALL FUNCTION 'nome_funcao'`
  - `CALL FUNCTION nome_funcao`
  - Declarações `FUNCTION-POOL`
  - Chamadas de métodos para classes que encapsulam módulos de função
* Categorize:
  - **BAPIs padrão**: Liste todas as chamadas BAPI_*
  - **Módulos de Função padrão**: Liste outros módulos de função padrão SAP (ex: `DD_DOMVALUES_GET`, `POPUP_TO_*`, `GUI_DOWNLOAD`, `SCMS_*`, `SSF_*`, etc.)
  - **Funções Z/Y customizadas**: Liste todos os módulos de função Z* e Y*
* **Formato de resposta**: Agrupe por categoria e forneça exemplos:
  - "Padrão/Exemplos: BAPI_BRANCH_GETLIST, BAPISDORDER_GETDETAILEDLIST, J_1B_NF_DOCUMENT_READ; Z-functions: ZASBFSD_GET_INVOICE_TAX, ZASBFSD_SEND_DOWNLOAD_PDF, ...; e muitas FM padrão (DD_DOMVALUES_GET, POPUPS, GUI_DOWNLOAD, SCMS_XSTRING_TO_BINARY, SSF_FUNCTION_MODULE_NAME, etc)."
 
### 5. Uso de CALL TRANSACTION (Faz CALL TRANSACTION?)
* Procure por todas as declarações `CALL TRANSACTION`:
  - `CALL TRANSACTION 'TCODE'`
  - `CALL TRANSACTION variavel_tcode`
  - `CALL TRANSACTION tcode USING bdc_tab`
  - `CALL TRANSACTION tcode USING bdc_tab MODE modo`
* Identifique os códigos de transação sendo chamados.
* **Formato de resposta**:
  - Se encontrado: "Sim — várias chamadas: VA03, VF03, J1B3N, MM03, XD03, FB03 via CALL TRANSACTION."
  - Se não encontrado: "Não — não foi encontrado CALL TRANSACTION no código fornecido."
 
### 6. Chamada Remota de Função por DESTINATION (Executa função por DESTINATION?)
* Procure por chamadas remotas de função:
  - `CALL FUNCTION 'nome_funcao' DESTINATION 'dest'`
  - `CALL FUNCTION 'nome_funcao' DESTINATION variavel_dest`
  - Chamadas de função `RFC_*` com parâmetro DESTINATION
* **Formato de resposta**:
  - Se encontrado: "Sim — encontradas chamadas: CALL FUNCTION 'XXX' DESTINATION 'YYY' (liste todas encontradas)."
  - Se não encontrado: "Não há CALL FUNCTION ... DESTINATION explicitamente no código fornecido (nenhuma chamada remota com parâmetro DESTINATION visível)."
 
### 7. Uso de SUBMIT (Faz SUBMIT?)
* Procure por declarações `SUBMIT`:
  - `SUBMIT nome_programa`
  - `SUBMIT nome_programa AND RETURN`
  - `SUBMIT nome_programa VIA SELECTION-SCREEN`
  - `SUBMIT nome_programa WITH ...`
* **Formato de resposta**:
  - Se encontrado: "Sim — encontrados SUBMITs: (liste todos os programas submetidos)."
  - Se não encontrado: "Não — não foi encontrado SUBMIT no código fornecido."
 
### 8. Manipulação de Arquivos (Manipula arquivos?)
* Procure por operações de arquivo:
  - **Leitura de arquivo**: `OPEN DATASET`, `READ DATASET`, `CLOSE DATASET`
  - **Escrita de arquivo**: `TRANSFER`, `OPEN DATASET FOR OUTPUT`
  - **Downloads de arquivo**: `GUI_DOWNLOAD`, `CL_GUI_FRONTEND_SERVICES=>FILE_SAVE_DIALOG`
  - **Uploads de arquivo**: `GUI_UPLOAD`, `CL_GUI_FRONTEND_SERVICES=>FILE_OPEN_DIALOG`
  - **Geração de arquivo**:
    - PDF: `CL_ABAP_DOCX`, `CL_SSF_FUNCTION_MODULE_NAME`, `FP_JOB_OPEN`, `FP_FUNCTION_MODULE_NAME`
    - XML: `CL_IXML`, `CL_IXML_STREAM_FACTORY`, `IXML->RENDER`
    - ZIP: `CL_ABAP_ZIP`, `CL_ABAP_ZIP_WRITER`
    - Excel: `CL_SALV_TABLE`, `CL_SALV_EXPORT`
  - **Conversões binário/string**: `SCMS_XSTRING_TO_BINARY`, `SCMS_BINARY_TO_XSTRING`
* Identifique quais tipos de arquivos são manipulados (PDF, XML, ZIP, Excel, texto, etc.) e as operações realizadas.
* **Formato de resposta**:
  - Se encontrado: "Sim — manipula/gera e baixa arquivos: PDF (xstring -> binary -> GUI download), XML (cl_ixml -> render -> download), cria ZIP com cl_abap_zip e baixa; uso de cl_gui_frontend_services=>file_save_dialog / gui_download / SCMS_XSTRING_TO_BINARY / cl_ixml / cl_abap_zip."
  - Se não encontrado: "Não — não foi encontrada manipulação de arquivos no código fornecido."
 
### 9. Exibição ALV ou WRITE (Exibe ALV ou WRITE?)
* Procure por mecanismos de exibição:
  - **Grade ALV**:
    - `CL_GUI_ALV_GRID`
    - `CL_SALV_TABLE`
    - `CL_SALV_GRID`
    - `REUSE_ALV_GRID_DISPLAY`
    - `REUSE_ALV_LIST_DISPLAY`
    - `CALL FUNCTION 'REUSE_ALV_*'`
  - **Declarações WRITE**:
    - Declarações `WRITE:` para tela
    - Declarações `WRITE AT`
    - `WRITE TO` (para variáveis - isso NÃO é saída de tela)
  - **Processamento de lista**: `NEW-PAGE`, `ULINE`, `SKIP`
* **CRÍTICO**: Distinga entre:
  - `WRITE TO variavel` (formatação interna, NÃO é saída de tela)
  - `WRITE variavel` ou `WRITE:` (saída de tela)
* **Formato de resposta**:
  - Se ALV encontrado: "Sim — exibe ALV (cl_gui_alv_grid->set_table_for_first_display ou REUSE_ALV_*)."
  - Se WRITE para tela encontrado: "Sim — exibe listas via WRITE (listar tipos de WRITE encontrados)."
  - Se apenas WRITE TO encontrado: "Não há WRITE para tela (apenas WRITE para formato interno em variável)."
  - Se nenhum encontrado: "Não — não foi encontrado ALV nem WRITE no código fornecido."
 
### 10. Botões ALV (Botões ALV)
* Identifique se o programa implementa botões personalizados na grade ALV:
  - Procure por implementação de eventos de toolbar: `HANDLE_TOOLBAR`, `HANDLE_USER_COMMAND`
  - Procure por métodos que adicionam botões: `ADD_BUTTON`, `SET_TOOLBAR_INTERACTIVE`
  - Procure por classes de eventos ALV: `CL_GUI_ALV_GRID`, `CL_SALV_EVENTS_TABLE`
  - Verifique se há botões customizados na barra de ferramentas ALV
* **Formato de resposta**:
  - Se encontrado: "Sim — implementa botões customizados na ALV (liste os botões encontrados ou tipos de ações)."
  - Se não encontrado: "Não — não foram encontrados botões customizados na ALV no código fornecido."
 
### 11. Hotspot ALV (Hotspot ALV)
* Identifique se o programa implementa hotspots (links clicáveis) na grade ALV:
  - Procure por `HOTSPOT_CLICK` em eventos ALV
  - Procure por `SET_CELL_TYPE` com tipo `HOTSPOT`
  - Procure por `HANDLE_HOTSPOT_CLICK` em classes de eventos ALV
  - Verifique se há campos configurados como hotspot na grade ALV
* **Formato de resposta**:
  - Se encontrado: "Sim — implementa hotspots na ALV (liste os campos ou tipos de hotspots encontrados)."
  - Se não encontrado: "Não — não foram encontrados hotspots na ALV no código fornecido."
 
### 12. Tabelas de Banco de Dados Acessadas (Principais tabelas acessadas)
* Identifique TODAS as tabelas de banco de dados acessadas no programa:
  - **Declarações SELECT**: `SELECT * FROM nome_tabela`, `SELECT campo FROM nome_tabela`
  - **Operações JOIN**: Tabelas em `FROM tabela1 INNER JOIN tabela2`
  - **Subconsultas**: Tabelas em `SELECT ... FROM nome_tabela WHERE EXISTS (SELECT ... FROM outra_tabela)`
  - **Parâmetros de módulo de função**: Tabelas passadas como parâmetros (verifique assinaturas de módulos de função)
  - **Declarações de tabelas internas**: `DATA: itab TYPE TABLE OF nome_tabela`
  - **Referências de tipo**: `TYPE nome_tabela`, `LIKE nome_tabela`
* **Ordem de prioridade para listagem**:
  1. Tabelas customizadas Z/Y (zasbtsd_*, yasbtsd_*, etc.)
  2. Tabelas padrão SAP (vbrk, vbak, vbap, bkpf, bseg, etc.)
  3. Tabelas de configuração (t001w, t001k, tvarvc, etc.)
  4. Tabelas de dados mestres (kna1, adrc, mara, etc.)
* **Formato de resposta**: "Principais: zasbtsd_invoice, zasbtsd_log_inv, zasbtsd_invc_pdf, zasbtsd_invc_xml, zasbtsd_inpfile, zasbtsd_nbs(_werk), bkpf, bseg, vbrk, vbak, vbap, kna1, adrc, t001w, t001k, j_1bnflin, j_1bnfdoc, konv, mara, tvarvc, adrt, adr6, usr21, adrp, bapibranch, etc. (lista extensa conforme includes)."
 
## METODOLOGIA DE ANÁLISE
 
1. **Escaneie includes e subrotinas**: Verifique todas as declarações `INCLUDE` e analise o código incluído
2. **Siga chamadas de função**: Quando módulos de função são chamados, anote seus nomes mesmo que a implementação não esteja visível
3. **Verifique métodos de classe**: Se classes são usadas, analise chamadas de métodos para operações de banco de dados, ALV, operações de arquivo
4. **Revise telas de seleção**: Verifique `SELECTION-SCREEN` para entrada do usuário que possa afetar operações
5. **Analise blocos de eventos**: Verifique `INITIALIZATION`, `AT SELECTION-SCREEN`, `START-OF-SELECTION`, `END-OF-SELECTION`
6. **Verifique chamadas dinâmicas**: Procure por `CALL FUNCTION` ou `CALL TRANSACTION` dinâmicos usando variáveis
 
## FORMATO DE SAÍDA
 
Retorne um objeto JSON com a seguinte estrutura:
 
```json
{
  "reportName": "<Nome técnico e descrição do report>",
  "functionalitySummary": "<Resumo abrangente em português descrevendo todas as funcionalidades>",
  "databaseOperations": "<Descrição em português das operações de banco de dados. Mencione se há operações diretas (INSERT/UPDATE/DELETE/MODIFY) ou apenas SELECTs. Liste as tabelas criadas, modificadas ou excluídas, se houver. Exemplo: 'No código do report não há instruções INSERT/UPDATE/DELETE diretas em tabelas DB (apenas SELECTs).' ou 'Operações diretas encontradas: INSERT em zasbtsd_invoice, UPDATE em vbrk, DELETE em zasbtsd_log.'>",
  "bapisAndFunctionModules": "<Descrição em português com exemplos de BAPIs padrão, módulos de função padrão SAP e funções customizadas Z/Y encontradas. Exemplo: 'Padrão/Exemplos: BAPI_BRANCH_GETLIST, BAPISDORDER_GETDETAILEDLIST, J_1B_NF_DOCUMENT_READ; Z-functions: ZASBFSD_GET_INVOICE_TAX, ZASBFSD_SEND_DOWNLOAD_PDF; e muitas FM padrão (DD_DOMVALUES_GET, POPUPS, GUI_DOWNLOAD, SCMS_XSTRING_TO_BINARY, SSF_FUNCTION_MODULE_NAME, etc).'>",
  "callTransaction": "<Descrição em português sobre uso de CALL TRANSACTION. Mencione se é usado e liste os códigos de transação chamados. Exemplo: 'Sim — várias chamadas: VA03, VF03, J1B3N, MM03, XD03, FB03 via CALL TRANSACTION.' ou 'Não — não foi encontrado CALL TRANSACTION no código fornecido.'>",
  "remoteFunctionCall": "<Descrição em português sobre chamadas remotas de função por DESTINATION. Mencione se é usado e liste os destinos encontrados. Exemplo: 'Sim — encontradas chamadas: CALL FUNCTION 'XXX' DESTINATION 'YYY' (liste todas encontradas).' ou 'Não há CALL FUNCTION ... DESTINATION explicitamente no código fornecido.'>",
  "submit": "<Descrição em português sobre uso de SUBMIT. Mencione se é usado e liste os programas submetidos. Exemplo: 'Sim — encontrados SUBMITs: zprogram1, zprogram2, etc.' ou 'Não — não foi encontrado SUBMIT no código fornecido.'>",
  "fileManipulation": "<Descrição em português sobre manipulação de arquivos. Mencione se é usado, quais tipos de arquivos são manipulados (PDF, XML, ZIP, Excel, etc.) e quais operações são realizadas (download, upload, generate). Exemplo: 'Sim — manipula/gera e baixa arquivos: PDF (xstring -> binary -> GUI download), XML (cl_ixml -> render -> download), cria ZIP com cl_abap_zip e baixa; uso de cl_gui_frontend_services=>file_save_dialog / gui_download / SCMS_XSTRING_TO_BINARY / cl_ixml / cl_abap_zip.' ou 'Não — não foi encontrada manipulação de arquivos no código fornecido.'>",
  "displayMethod": "<Descrição em português sobre método de exibição. Mencione se usa ALV, WRITE para tela ou WRITE TO (formatação interna). Exemplo: 'Sim — exibe ALV (cl_gui_alv_grid->set_table_for_first_display ou REUSE_ALV_*).' ou 'Sim — exibe listas via WRITE (listar tipos de WRITE encontrados).' ou 'Não há WRITE para tela (apenas WRITE para formato interno em variável).' ou 'Não — não foi encontrado ALV nem WRITE no código fornecido.'>",
  "alvButtons": "<Descrição em português sobre botões ALV. Mencione se são usados botões customizados na ALV e liste os botões encontrados ou tipos de ações. Exemplo: 'Sim — implementa botões customizados na ALV: Download PDF, Enviar Email, Reprocessar, etc.' ou 'Não — não foram encontrados botões customizados na ALV no código fornecido.'>",
  "alvHotspot": "<Descrição em português sobre hotspots ALV. Mencione se são usados hotspots na ALV e liste os campos ou tipos de hotspots encontrados. Exemplo: 'Sim — implementa hotspots na ALV nos campos: VBELN (navegação para VA03), KUNNR (navegação para XD03), etc.' ou 'Não — não foram encontrados hotspots na ALV no código fornecido.'>",
  "accessedTables": "<Descrição em português com principais tabelas acessadas. Liste as tabelas customizadas Z/Y, tabelas padrão SAP, tabelas de configuração e tabelas de dados mestres. Exemplo: 'Principais: zasbtsd_invoice, zasbtsd_log_inv, zasbtsd_invc_pdf, zasbtsd_invc_xml, zasbtsd_inpfile, zasbtsd_nbs(_werk), bkpf, bseg, vbrk, vbak, vbap, kna1, adrc, t001w, t001k, j_1bnflin, j_1bnfdoc, konv, mara, tvarvc, adrt, adr6, usr21, adrp, bapibranch, etc. (lista extensa conforme includes).'>"
}
```
 
## REGRAS CRÍTICAS DE ANÁLISE
 
1. **Seja minucioso**: Analise todos os includes, subrotinas e módulos chamados
2. **Distinga direto vs indireto**: Separe claramente operações realizadas diretamente no código daquelas realizadas por funções chamadas
3. **Liste de forma abrangente**: Inclua todos os itens encontrados, não apenas exemplos
4. **Use português para descrições**: Todos os campos de descrição devem estar em português
5. **Seja preciso**: Use nomes técnicos exatos (nomes de tabelas, nomes de módulos de função, códigos de transação)
6. **Contexto importa**: Considere o propósito do programa ao analisar operações
7. **Verifique includes**: Se o programa principal inclui outros arquivos, analise-os também (se fornecidos no conteúdo)
 
## EXEMPLOS DO QUE IDENTIFICAR
 
**SIM - Identifique estes:**
- `INSERT INTO zasbtsd_invoice VALUES ...` → Operação de banco de dados (INSERT)
- `CALL FUNCTION 'BAPI_BRANCH_GETLIST'` → BAPI padrão
- `CALL FUNCTION 'ZASBFSD_GET_INVOICE_TAX'` → Módulo de função customizado
- `CALL TRANSACTION 'VA03'` → Chamada de transação
- `CALL FUNCTION 'XXX' DESTINATION 'YYY'` → Chamada remota de função
- `SUBMIT zprogram AND RETURN` → Declaração SUBMIT
- `OPEN DATASET arquivo FOR OUTPUT` → Manipulação de arquivo
- `cl_gui_alv_grid->set_table_for_first_display` → Exibição ALV
- `WRITE: / 'Texto'` → WRITE para tela
- `SELECT * FROM vbrk` → Acesso a tabela
- `HANDLE_TOOLBAR` ou `HANDLE_USER_COMMAND` → Botões ALV
- `HOTSPOT_CLICK` ou `SET_CELL_TYPE` com `HOTSPOT` → Hotspot ALV
 
**NÃO - Não confunda:**
- `WRITE TO variavel` NÃO é saída de tela (é formatação interna)
- Chamadas de módulo de função que não mostram DESTINATION NÃO são chamadas remotas
- Declarações `SELECT` são acesso a tabela, mas não modificações
- Operações em tabelas internas (`APPEND`, `COLLECT`) NÃO são operações de banco de dados a menos que seguidas de escrita no banco de dados
 
Retorne apenas um objeto JSON válido seguindo a estrutura acima. Se informações não puderem ser determinadas a partir do código fornecido, use `null` ou arrays vazios conforme apropriado, mas sempre forneça uma descrição explicando o que foi encontrado ou não encontrado.
