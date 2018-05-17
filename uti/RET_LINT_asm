; Return long integer to SuperBASIC Interpreter

        section utility

        xdef    bi_rtlin

        include dev8_keys_qlv
        include dev8_keys_bv
;+++
; Return long integer to SuperBASIC Interpreter
; A function using this routine should load the value into d1 and jump to it.
;
;               Entry                           Exit
;       d1.l    parameter
;       a1      arithmetic stack pointer
;       a6      SuperBASIC master pointer
;---
bi_rtlin
        move.l  d1,d4
        moveq   #$18,d1
        move.w  qa.resri,a2
        jsr     (a2)
        move.l  bv_rip(a6),a1
        subq.l  #2,a1
        swap    d4
        move.w  d4,d5
        move.w  d4,(a6,a1.l)
        move.w  qa.op,a2
        moveq   #qa.float,d0
        jsr     (a2)
        tst.w   d5
        bpl.s   nosign
        subq.l  #6,a1
        move.w  #$813,(a6,a1.l)
        move.l  #$10000000,2(a6,a1.l)
        moveq   #qa.add,d0
        jsr     (a2)
nosign  subq.l  #6,a1
        move.w  #$813,(a6,a1.l)
        move.l  #$10000000,2(a6,a1.l)
        moveq   #qa.mul,d0
        jsr     (a2)
        swap    d4
        move.w  d4,d5
        subq.l  #2,a1
        move.w  d4,(a6,a1.l)
        moveq   #qa.float,d0
        jsr     (a2)
        tst.w   d5
        bpl.s   posnum
        subq.l  #6,a1
        move.w  #$813,(a6,a1.l)
        move.l  #$10000000,2(a6,a1.l)
        moveq   #qa.add,d0
        jsr     (a2)
posnum  moveq   #qa.add,d0
        jsr     (a2)
        move.l  a1,bv_rip(a6)
        moveq   #2,d4
        rts

        end
