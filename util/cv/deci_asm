* Convert ASCII to integer              v0.00   May 1988  J.R.Oakley  QJUMP
*
        section cv
*
        include 'dev8_keys_err'
        include 'dev8_keys_k'
*
        xref    cv_cttab
*
        xdef    cv_decil
        xdef    cv_deciw
*+++
* Convert ASCII in a buffer to a number.  Leading spaces are stripped.
*
*       Registers:
*               Entry                           Exit
*       D0                                      0 or ERR.IEXP
*       D1.l                                    number
*       D7      end of buffer                   preserved
*       A0      buffer                          updated
*---
cv_deciw
        bsr.s   cv_decil                ; convert to long
        bne.s   ucw_exit                ; oops
        move.l  d1,d0
        ext.l   d0
        cmp.l   d1,d0                   ; is it OK as a word
        bne.s   ucw_exov
        moveq   #0,d0
ucw_exit
        rts
ucw_exov
        moveq   #err.ovfl,d0
        bra.s   ucw_exit
*+++
* Convert ASCII in a buffer to a number.  Leading spaces are stripped.
*
*       Registers:
*               Entry                           Exit
*       D0                                      0 or ERR.IEXP
*       D1.l                                    number
*       D7      end of buffer                   preserved
*       A0      buffer                          updated
*---
cv_decil
decreg  reg     d2/d3/a2
        movem.l decreg,-(sp)
        moveq   #k.space,d0
        lea     cv_cttab,a2
ucd_spcl
        cmp.l   a0,d7                   ; off end?
        beq.s   ucd_exxp                ; yes, error in expression
        cmp.b   (a0)+,d0                ; space?
        beq.s   ucd_spcl                ; yes, try again
*
        moveq   #0,d3                   ; number is 0
        move.b  -1(a0),d0               ; this may be sign, or not
        moveq   #1,d2                   ; assume +ve
        cmp.b   #'+',d0                 ; is it?
        beq.s   ucd_ndig                ; yes, convert from next digit
        cmp.b   #'-',d0                 ; no, negative?
        bne.s   ucd_cdig                ; no, better be a digit!
        neg.l   d2                      ; yes, sign is -ve
ucd_ndig
        cmp.l   a0,d7                   ; off end?
        beq.s   ucd_excv                ; yes, stop
        move.b  (a0)+,d0                ; no, get a digit
ucd_cdig
        cmp.b   #k.dig09,(a2,d0.w)      ; is it a digit
        bne.s   ucd_exbs                ; no, backspace
        clr.w   d2                      ; yes, flag number valid
        add.l   d3,d3                   ; number = 2 * number
        bvs.s   ucd_exxp                ; whoops
        move.l  d3,d1
        add.l   d1,d1
        bvs.s   ucd_exxp
        add.l   d1,d1                   ; and number * 8
        bvs.s   ucd_exxp
        add.l   d1,d3                   ; thus number * 10
        bvs.s   ucd_exxp
        sub.b   #k.zero,d0              ; make it in 0..9
        add.l   d0,d3                   ; and accumulate it
        bvc.s   ucd_ndig                ; go round again if OK
ucd_exxp
        moveq   #err.iexp,d0
        bra.s   ucd_exit
ucd_exbs
        subq.l  #1,a0
ucd_excv
        tst.w   d2                      ; get valid flag
        bne.s   ucd_exxp                ; not valid
        moveq   #0,d0                   ; valid, no problem
        tst.l   d2                      ; negative?
        bpl.s   ucd_exit                ; no
        neg.l   d3                      ; yes
ucd_exit
        move.l  d3,d1
        tst.l   d0
        movem.l (sp)+,decreg
        rts
*
        end
