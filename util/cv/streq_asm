; Compare strings for equality          v0.00   May 1988  J.R.Oakley  QJUMP

        section cv

        include 'dev8_keys_err'

        xref    cv_lctab

        xdef    cv_streq
        xdef    cv_ssteq

;+++
; Compare two sub strings for equality: no indication of lexical ordering
; is given in the case of inequality.  The comparison converts each
; character to lower case before comparing.
;
;       Registers:
;               Entry                           Exit
;       D0      length to compare               0 or ERR.ITNF
;       A0      sub string 0 base               preserved
;       A1      sub string 1 base               preserved
;---
cv_ssteq
steqreg reg     d1/d2/a0/a1/a2
        movem.l steqreg,-(sp)
        lea     cv_lctab(pc),a2          ; point to lowercase table
        moveq   #0,d1                    ; make index word OK and set Z
        bra.s   uce_cmpe

;+++
; Compare two strings for equality: no indication of lexical ordering
; is given in the case of inequality.  The comparison converts each
; character to lower case before comparing.
;
;       Registers:
;               Entry                           Exit
;       D0                                      0 or ERR.ITNF
;       A0      string 0 base                   preserved
;       A1      string 1 base                   preserved
;---
cv_streq
        move.w  (a0),d0                  ; get length
        cmp.w   (a1),d0                  ; are they the same?
        bne.s   uce_exnf                 ; no

        movem.l steqreg,-(sp)
        lea     cv_lctab(pc),a2          ; point to lowercase table
        moveq   #0,d1                    ; make index word OK
        cmp.w   (a0)+,(a1)+              ; advance to chars and set Z
        bra.s   uce_cmpe
uce_cmpl
        move.b  (a0)+,d1
        move.b  0(a2,d1.w),d2            ; get lowercased char from string 0
        move.b  (a1)+,d1
        cmp.b   0(a2,d1.w),d2            ; is it same as lowercased string 1?
uce_cmpe
        dbne    d0,uce_cmpl              ; go until NE or done
        movem.l (sp)+,steqreg
        bne.s   uce_exnf
        moveq   #0,d0                    ; equal
uce_exit
        rts
uce_exnf
        moveq   #err.itnf,d0
        bra.s   uce_exit

        end
