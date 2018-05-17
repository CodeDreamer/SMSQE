; Upper case string/conversion       V2.00    1986  Tony Tebby   QJUMP

        section cv

        xdef    cv_upstr
        xref    cv_uctab

;+++
; Convert string to upper case
;
;       Registers:
;               Entry                           Exit
;       a1      pointer to string
;       all other registers preserved
;       status arbitrary
;---
cv_upstr
cvus.reg reg    d0/d1/a1
        movem.l cvus.reg,-(sp)
        moveq   #0,d1                    ; clean working reg
        move.w  (a1)+,d0                 ; length of string
        bra.s   cvus_eloop
cvus_loop
        move.b  (a1),d1                  ; next character
        move.b  cv_uctab(pc,d1.w),(a1)+  ; ... upper case
cvus_eloop
        dbra    d0,cvus_loop

        movem.l (sp)+,cvus.reg
        rts
        end
