; Lower case string conversion       V2.01    1986  Tony Tebby   QJUMP

        section cv

        xdef    cv_lostr

        xref    cv_lctab

;+++
; Convert string to lower case
;
;       Registers:
;               Entry                           Exit
;       a1      pointer to string
;       all other registers preserved
;       status arbitrary
;---
cv_lostr
cvls.reg reg    d0/d1/a1
        movem.l cvls.reg,-(sp)
        moveq   #0,d1                    ; clean working reg
        move.w  (a1)+,d0                 ; length of string
        bra.s   cvls_eloop
cvls_loop
        move.b  (a1),d1                  ; next character
        move.b  cv_lctab(pc,d1.w),(a1)+  ; ... upper case
cvls_eloop
        dbra    d0,cvls_loop

        movem.l (sp)+,cvls.reg
        rts
        end
