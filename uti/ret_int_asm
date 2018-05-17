; Return integer to SuperBASIC Interpreter

        section utility

        xdef    bi_rtint

        include dev8_keys_bv
        include dev8_keys_qlv
;+++
; Return integer to SuperBASIC Interpreter.
; A function using this routine should load the value into d1 and jump to it.
;
;               Entry                           Exit
;       d1.w    parameter
;       a1      arithmetic stack pointer
;       a6      SuperBASIC master pointer
;---
bi_rtint
        move.w  d1,-(sp)
        moveq   #2,d1
        move.w  qa.resri,a2
        jsr     (a2)
        move.l  bv_rip(a6),a1
        subq.l  #2,a1
        move.l  a1,bv_rip(a6)
        move.w  (sp)+,(a6,a1.l)
        moveq   #3,d4
        moveq   #0,d0
        rts

        end
