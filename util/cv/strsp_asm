* Compare strings to first non-alphanum v0.00   May 1988  J.R.Oakley  QJUMP
*
        section cv
*
        include 'dev8_keys_k'
*
        xref    cv_cttab
        xref    cv_lctab
*
        xdef    cv_strsp
*+++
* Compare two strings for equality: no indication of lexical ordering
* is given in the case of inequality.  The comparison converts each
* character to lower case before comparing, and stops as soon as it reaches 
* the first non-alphanumeric character.  The non-alphanumeric character
* doesn't have to match, but does have to occur
*
*       Registers:
*               Entry                           Exit
*       D0                                      0 or ERR.ITNF
*       A0      string 0 base                   preserved
*       A1      string 1 base                   preserved
*---
cv_streq
steqreg reg     d1/d2/a0-a3
        movem.l steqreg,-(sp)
        move.w  (a0)+,d0                ; get a length
        move.w  (a1)+,d1                ; and the other
        cmp.w   d0,d1                   ; use the shorter
        bge.s   uce_ltab                ; we are
        move.w  d1,d0                   ; ...now
uce_ltab
        lea     cv_lctab(pc),a2         ; point to lowercase table
        lea     cv_cttab(pc),a3         ; and character types
        moveq   #0,d1                   ; make index word OK
        bra.s   uce_cmpe
uce_cmpl
        move.b  (a0)+,d1                ; char from string 0
        move.b  0(a3,d1.w),d2           ; what sort is it?
        subq.b  #k.dig09,d2             ; digit?
        beq.s   uce_cmp                 ; yes
        subq.b  #k.lclet-k.dig09,d2     ; lower case letter?
        beq.s   uce_cmp                 ; yes
        subq.b  #k.uclet-k.lclet,d2     ; upper case letter?
        bne.s   uce_nalf                ; no, check we've bombed in other string
uce_cmp
        move.b  0(a2,d1.w),d2           ; lowercase char from string 0
        move.b  (a1)+,d1
        cmp.b   0(a2,d1.w),d2           ; is it same as lowercased string 1?
uce_cmpe
        dbne    d0,uce_cmpl             ; go until NE or done
uce_popr
        movem.l (sp)+,steqreg
        bne.s   uce_exnf
        moveq   #0,d0                   ; equal
uce_exit
        rts
uce_exnf
        moveq   #err.itnf,d0
        bra.s   uce_exit
*
uce_nalf
        move.b  (a1)+,d1                ; char from string 1
        move.b  0(a3,d1.w),d2           ; what sort is it?
        assert  k.nonpr,0
        beq.s   uce_popr                ; non-printing, OK
        subq.b  #k.lclet-k.dig09,d2     ; lower case letter?
        beq.s   uce_popr                ; yes
        subq.b  #k.uclet-k.lclet,d2     ; upper case letter?
        bra.s   uce_popr                ; CCR OK, set D0 as required
*
        end

