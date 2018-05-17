; Command line interpreter           V2.00   1989  Tony Tebby

        section gen_util

        xdef    gu_cli

        xref    cv_locas

        include 'dev8_keys_err'

;+++
; Command line interpreter.
;
; This routine interpret the parameters of a 'command line'. The line is
; passed as a standard string, and is processed with the aid of a table of
; definitions:
;
;       character  ' '    string is optional
;                  '+'    string is required
;
;       character  ' '    position dependent parameter
;                  other  keyed parameter
;
;       word       pointer to flag set if parameter found (rel a5)
;       word       pointer to buffer for parameter string (rel a5)
;       word       max length of parameter string
;
; The table is terminated with a zero word.
;
;       Registers:
;               Entry                           Exit
;       D0                                      0 or error code
;       A1      pointer to command line
;       A2      pointer to definitions
;       A5      pointer to results area
;---
gu_cli
gcl.reg reg     d1/d2/a0/a1/a2
        movem.l gcl.reg,-(sp)

        moveq   #0,d0                  ; set error code
        movem.l (sp)+,gcl.reg
        rts

        end
