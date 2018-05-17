; Remove Extension from filename     V2.00    1990  Tony Tebby   QJUMP

        section cv

        xdef    cv_remex
        xref    cv_locas

;+++
; Copies string, removing extension (_xxx) if exists
;
;       Registers:
;               Entry                           Exit
;       d0                                      0
;       a0      pointer to result               preserved
;       a1      pointer to source               preserved
;       a2      pointer to old extension (lc)   preserved
;       all other registers preserved
;---
cv_remex
cvrx.reg reg    d1/d2/d3/a0/a1/a2
        movem.l cvrx.reg,-(sp)
        move.w  (a1)+,d2                 ; old name length
        move.w  (a2)+,d3                 ; length of old extension
        ble.s   cvrx_copy

        move.w  d2,d0
        sub.w   d3,d0                    ; possible new length
        ble.s   cvrx_copy                ; ... none

cvrx_cmp
        move.b  (a1,d0.w),d1             ; next character
        jsr     cv_locas
        cmp.b   (a2)+,d1                 ; matches?
        bne.s   cvrx_copy                ; ... no
        addq.w  #1,d0
        cmp.w   d2,d0                    ; end of string yet?
        blt.s   cvrx_cmp                 ; ... no

        sub.w   d3,d2                    ; copy this much

cvrx_copy
        move.w  d2,(a0)+                 ; ... string length
        bra.s   cvrx_cpend
cvrx_cploop
        move.b  (a1)+,(a0)+
cvrx_cpend
        dbra    d2,cvrx_cploop

        moveq   #0,d0
        movem.l (sp)+,cvrx.reg
        rts
        end
