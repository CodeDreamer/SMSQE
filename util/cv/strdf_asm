* Compare strings for equality          v0.00   May 1988  J.R.Oakley  QJUMP
*
        section cv
*
        include 'dev8_keys_err'
*
        xref    cv_lctab
*
        xdef    cv_strdf
*+++
* Compare two strings for equality: no indication of lexical ordering
* is given in the case of inequality.  The comparison converts each
* character to lower case before comparing. 
*
*       Registers:
*               Entry                           Exit
*       D0                                      lsw/msw pos of diff, 0 if all =
*       A0      string 0 base                   preserved
*       A1      string 1 base                   preserved
*---
cv_strdf
steqreg reg     d1-d4/a0/a1/a2
        movem.l steqreg,-(sp)
        move.w  (a0)+,d0                ; length 0
        move.w  (a1)+,d1                ; length 1
        move.l  #$ffff,d4               ; zero the MSW if equal
        cmp.w   d0,d1                   ; choose the shorter
        bgt.s   uce_ltab                ; we have it
        bne.s   uce_set1                ; it's string 1
        moveq   #0,d4                   ; the same, keep neither half if equal
        bra.s   uce_ltab
uce_set1
        move.w  d1,d0                   ; it's string 1
        swap    d4                      ; zero LSW if equal
uce_ltab
        move.w  d0,d3                   ; keep initial length
        lea     cv_lctab(pc),a2         ; point to lowercase table
        moveq   #0,d1                   ; make index word OK
        bra.s   uce_cmpe
uce_cmpl
        move.b  (a0)+,d1
        move.b  0(a2,d1.w),d2           ; get lowercased char from string 0
        move.b  (a1)+,d1
        cmp.b   0(a2,d1.w),d2           ; is it same as lowercased string 1?
uce_cmpe
        dbne    d0,uce_cmpl             ; go until NE or done
        sub.w   d0,d3                   ; first failure was here
        move.w  d3,d2
        swap    d2
        move.w  d3,d2                   ; same in both
        tst.w   d0                      ; was there a failure at all?
        bpl.s   uce_exit                ; yes
        and.l   d4,d2                   ; no, zero the relevant difference
uce_exit
        move.l  d2,d0                   ; result where we want it 
        movem.l (sp)+,steqreg
        rts
*
        end
