; Convert a word integer to ASCII in a buffer   1993 Jochen Merz

        section utility

        xdef    ut_intrasc      ; integer to ASCII, right-aligned

;+++
; Convert a word integer to ASCII in a buffer, right-aligned.
;
;               Entry                           Exit
;       d1.w    value
;       a1      ptr to buffer                   ptr before value in buffer
;---
ut_intrasc
asc.reg reg     d2-d3
        movem.l asc.reg,-(sp)
        moveq   #1,d2           ; reset digit count
try_div
        cmp.w   #10,d1          ; another digit left?
        bls.s   all_divd        ; no
        addq.w  #1,d2           ; another digit
        and.l   #$ffff,d1
        divu    #10,d1
        move.l  d1,-(sp)
        bra.s   try_div
all_divd
        move.w  d2,d3           ; keep digit counter
        neg.w   d2
        lea     5(a1,d2.w),a1   ; point to digit 1
        swap    d1
ins_loop
        swap    d1
        add.b   #'0',d1
        move.b  d1,(a1)+        ; insert digit
        subq.w  #1,d3           ; all done?
        beq.s   all_done
        move.l  (sp)+,d1
        bra.s   ins_loop
all_done
        add.w   d2,a1
        movem.l (sp)+,asc.reg
        rts
        end
