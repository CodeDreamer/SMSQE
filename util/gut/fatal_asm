; Fatal error handling     1988  Tony Tebby

        section gen_util

        xdef    gu_fatal
        xdef    gu_fater

        xref    gu_ncol
        xref    gu_fbcur
        xref    gu_nl
        xref    gu_die

        include 'dev8_keys_qlv'

;+++
; Called when program wishes to die: writes error, waits for key then dies.
;
;       d0 c    error code
;       a0 c    channel
;---
gu_fater
        move.w  ut.werms,a2              ; write error message
        jsr     (a2)
;+++
; Called when program wishes to die: waits for key then dies.
;
;       d0 c    error code
;       a0 c    channel
;---
gu_fatal
        move.l  d0,d3                    ; error return

        jsr     gu_ncol                  ; move on a bit (removes <NL>)
        jsr     gu_fbcur                 ; fetch byte with cursor
        jsr     gu_nl                    ; now <NL>
        move.l  d3,d0
        jmp     gu_die
        end
