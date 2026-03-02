Analyze this SAP ABAP file and provide a structured summary focusing on the information needed for comprehensive program analysis and compliance review.

File: {filePath}

File content:
{content}

## PART 1: FILE IDENTIFICATION

First, identify what type of file this is:
- **Report/Program**: Look for `REPORT` statement, program name, and header comments
- **Function Module**: Look for `FUNCTION` statement, function pool name
- **Class**: Look for `CLASS` statement, class name, methods
- **Include**: Look for `INCLUDE` statement or if it's a dependency file
- **Other**: Identify any other SAP ABAP object type

Provide:
- **File Type**: Report / Function Module / Class / Include / Other
- **Technical Name**: The exact technical name (e.g., ZASBRSD_INVOICE, ZASBFSD_GET_INVOICE_TAX)
- **Description**: Any header text or description found in the code
- **Purpose**: Brief description of what this file does

## PART 2: DATA EXTRACTION FOR ANALYSIS REPORT

Extract the following information that will be used in the detailed analysis report. Include specific code snippets and line references when relevant:

### 1. Database Operations (INSERT/UPDATE/DELETE/MODIFY)
- **Direct operations**: Find all `INSERT INTO`, `UPDATE`, `DELETE FROM`, `MODIFY` statements directly in the code
- **Indirect operations**: Note any function modules or BAPIs called that might perform database operations
- **Code snippets**: Include the actual statements with table names
- Format: List each operation type, table name, and context (e.g., "INSERT INTO zasbtsd_invoice at line 245")

### 2. BAPIs and Function Modules
- **BAPIs**: Find all `CALL FUNCTION 'BAPI_*'` or `CALL FUNCTION 'RFC_*'`
- **Standard Function Modules**: Find all `CALL FUNCTION` with standard SAP function names (non-Z/Y)
- **Custom Function Modules**: Find all `CALL FUNCTION 'Z*'` or `CALL FUNCTION 'Y*'`
- **Code snippets**: Include the CALL FUNCTION statements with parameters if relevant
- Format: Categorize and list all function modules found

### 3. CALL TRANSACTION Usage
- Find all `CALL TRANSACTION` statements
- Extract transaction codes being called
- Include code snippets showing the CALL TRANSACTION statements

### 4. Remote Function Calls (DESTINATION)
- Find all `CALL FUNCTION ... DESTINATION` statements
- Extract destination names and function names
- Include code snippets

### 5. SUBMIT Usage
- Find all `SUBMIT` statements
- Extract program names being submitted
- Include code snippets

### 6. File Manipulation
- **File operations**: `OPEN DATASET`, `READ DATASET`, `TRANSFER`, `CLOSE DATASET`
- **Download/Upload**: `GUI_DOWNLOAD`, `GUI_UPLOAD`, `CL_GUI_FRONTEND_SERVICES`
- **File generation**: PDF (`CL_SSF_*`, `FP_*`), XML (`CL_IXML`), ZIP (`CL_ABAP_ZIP`), Excel (`CL_SALV_EXPORT`)
- **Conversions**: `SCMS_XSTRING_TO_BINARY`, `SCMS_BINARY_TO_XSTRING`
- Include code snippets and identify file types being handled

### 7. Display Methods (ALV/WRITE)
- **ALV**: `CL_GUI_ALV_GRID`, `CL_SALV_TABLE`, `REUSE_ALV_*`
- **WRITE for screen**: `WRITE:`, `WRITE AT` (NOT `WRITE TO` which is internal formatting)
- **List processing**: `NEW-PAGE`, `ULINE`, `SKIP`
- Include code snippets and distinguish between screen output and internal formatting

### 8. ALV Buttons
- Find `HANDLE_TOOLBAR`, `HANDLE_USER_COMMAND` event handlers
- Find `ADD_BUTTON`, `SET_TOOLBAR_INTERACTIVE` methods
- Include code snippets showing button implementations

### 9. ALV Hotspots
- Find `HOTSPOT_CLICK` events
- Find `SET_CELL_TYPE` with `HOTSPOT` type
- Find `HANDLE_HOTSPOT_CLICK` methods
- Include code snippets

### 10. Database Tables Accessed
- Find all `SELECT` statements (including JOINs and subqueries)
- Find table references in `TYPE TABLE OF`, `LIKE`, `DATA ... TYPE`
- Find tables passed as parameters to function modules
- **Priority order**: 
  1. Custom Z/Y tables (zasbtsd_*, yasbtsd_*, etc.)
  2. Standard SAP tables (vbrk, vbak, vbap, bkpf, bseg, etc.)
  3. Configuration tables (t001w, t001k, tvarvc, etc.)
  4. Master data tables (kna1, adrc, mara, etc.)
- Include code snippets showing SELECT statements

### 11. Includes and Dependencies
- Find all `INCLUDE` statements
- Note any dependencies on other programs, classes, or function modules
- List included files and their purposes

### 12. Key Code Patterns
- Selection screens (`SELECTION-SCREEN`)
- Event blocks (`INITIALIZATION`, `AT SELECTION-SCREEN`, `START-OF-SELECTION`, `END-OF-SELECTION`)
- Dynamic calls (using variables for `CALL FUNCTION` or `CALL TRANSACTION`)
- Error handling patterns
- Business logic patterns

## OUTPUT FORMAT

Provide a structured summary with:
1. **File Identification**: Type, name, description, purpose
2. **Extracted Data**: For each category above, list what was found with code snippets and line references
3. **Code Snippets**: Include relevant code excerpts that demonstrate the findings
4. **Missing Information**: Note if certain information cannot be determined from the provided code

Focus on extracting concrete, specific information that will be used in the detailed analysis report. Include actual code snippets and line numbers when possible to support the analysis.

