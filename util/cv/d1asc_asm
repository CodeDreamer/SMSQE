* Convert D1 to ASCII                   v0.00   May 1988  J.R.Oakley  QJUMP
*
        section cv
*
        xref    cv_uldiv
*
        xdef    cv_d1asc
*+++
* Convert a number of up to 8 digits into ASCII: the number is assumed
* to be unsigned.
*
*       Registers:
*               Entry                           Exit
*       D1      number to convert               smashed
*       A0      buffer                          updated
*---
cv_d1asc
d1areg  reg     d0/d2/d3
        movem.l d1areg,-(sp)
        moveq   #0,d2                   ; answer
        moveq   #0,d3                   ; number of digits
        bra.s   cnvle
cnvlp
        jsr     cv_uldiv(pc)
        or.b    d1,d2                   ; add in a digit
        lsl.l   #4,d2                   ; and shift it backwards a nibble
        move.l  d0,d1                   ; now divide quotient
cnvle
        addq.w  #1,d3                   ; one more digit
        moveq   #10,d0                  ; we want to divide by ten
        cmp.l   d1,d0                   ; is it worth it?
        ble.s   cnvlp                   ; yes
        or.b    d1,d2                   ; no, just whack in the last digit
*
        bra.s   cpde                    ; convert to ASCII in buffer
cpdl
        moveq   #$f,d0                  ; mask a nibble
        and.b   d2,d0                   ; from the list
        lsr.l   #4,d2                   ; here's the next one
        add.b   #'0',d0                 ; make it a digit
        move.b  d0,(a0)+                ; and fill it in
cpde
        dbra    d3,cpdl
        movem.l (sp)+,d1areg
        rts
*
        end
