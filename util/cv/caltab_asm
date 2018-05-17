; SMS2 calendar tables                  v0.00   Mar 1988  J.R.Oakley  QJUMP

        section calendar

        xdef    cv_caltb
        xdef    cv_calst

        xref    cv_english
        xref    cv_deutsch
        xref    cv_francais
        xref    cv_papal
;+++
; Select calendar table
;
;       d0 cr   word table number 0 to n / long pointer to table!
;       status arbitrary
;---
cv_calst
        move.l  a0,-(sp)
        cmp.w   cv_caltb,d0              ; out of range
        bls.s   ccs_set
        moveq   #0,d0
ccs_set
        add.w   d0,d0                    ; indexed by words
        lea     cv_caltb,a0              ; table
        add.w   2(a0,d0.w),a0            ; offset
        move.w  (a0)+,d0                 ; ... length of title string
        add.w   d0,a0
        and.w   #1,d0
        add.w   d0,a0                    ; rounded up
        move.l  a0,d0                    ; result
        move.l  (sp)+,a0
        rts

;+++
; Table of calendar tables
;---
cv_caltb
        dc.w    3
        dc.w    cv_english-cv_caltb
        dc.w    cv_deutsch-cv_caltb
        dc.w    cv_francais-cv_caltb
        dc.w    cv_papal-cv_caltb
*
        end
