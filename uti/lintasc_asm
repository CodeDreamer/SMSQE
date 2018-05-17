; Convert a long integer to ASCII into a buffer         1993 Jochen Merz

        section utility

        xdef    ut_lintasc

;+++
; Convert a long integer to ASCII into a buffer. The maximum value
; is 999.999.999. A dirty way of doing it, but very quick.
;
;               Entry                           Exit
;       d1.l    value                           length
;       a1      ptr to buffer                   updated ptr to buffer
;---
ut_lintasc
lasc.reg reg    d2-d3
        movem.l lasc.reg,-(sp)
        moveq   #0,d2                   ; character count
        move.l  #100000000,d3
        bsr.s   try_div
        move.l  #10000000,d3
        bsr.s   try_div
        move.l  #1000000,d3
        bsr.s   try_div
        move.l  #100000,d3
        bsr.s   try_div
        move.l  #10000,d3
        bsr.s   try_div
        move.l  #1000,d3
        bsr.s   try_div
        moveq   #100,d3
        bsr.s   try_div
        moveq   #10,d3
        bsr.s   try_div
        add.b   #'0',d1
        addq.b  #1,d2
        move.b  d1,(a1)+
        move.l  d2,d1
        movem.l (sp)+,d2-d3
        rts

try_div
        cmp.l   d3,d1                   ; value larger than digit?
        blt.s   no_div                  ; no!
        moveq   #'0',d0                 ; reset character/counter
try_loop
        addq.b  #1,d0
        sub.l   d3,d1
        cmp.l   d3,d1                   ; still bigger?
        bge.s   try_loop                ; yes, try again!
        addq.b  #1,d2                   ; another digit
        move.b  d0,(a1)+                ; insert character
        rts

no_div
        tst.b   d2                      ; any character in buffer?
        beq.s   no_char                 ; no!
        addq.b  #1,d2                   ; otherwise 0 must be buffered
        move.b  #'0',(a1)+
no_char
        rts
        end
