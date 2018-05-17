; Procedure return floating point   V2.00     1988   Tony Tebby   QJUMP

        section hotkey

        xdef    hot_rtfp
        xdef    hot_rter

        xref    ut_chkri
        xref    ut_lfloat
        xref    ut_retfp

;+++
; Return floating point value of d1
;
;       d1 c    error return
;       status returns 0
;---
hot_rtfp
        move.l  d1,d0
;+++
; Return error from function
;
;       d0 c    error return
;       status returns 0
;---
hot_rter
        move.l  d0,-(sp)
        moveq   #6,d1
        jsr     ut_chkri                 ; check for room on stack
        move.l (sp)+,d1
        jsr     ut_lfloat                ; float error
        jmp     ut_retfp                 ; and return it
        end
