; Add Extension to filename     V2.00    1990  Tony Tebby   QJUMP

        section cv

        xdef    cv_addex

        include 'dev8_keys_hdr'
        include 'dev8_keys_err'

;+++
; Copies string, adding extension (_xxx). If the extension makes the name
; longer than a permitted filename, the name is truncated.
;
;       Registers:
;               Entry                           Exit
;       a0      pointer to result               preserved
;       a1      pointer to source               preserved
;       a2      pointer to new extension        preserved
;       all other registers preserved
;       status standard (err.inam if new length > hdr.nmln)
;---
cv_addex
cvax.reg reg    d1/d2/d3/a0/a1/a2
        movem.l cvax.reg,-(sp)
        move.w  (a1)+,d1                 ; old name length
        move.w  (a2)+,d2                 ; length of new extension
        move.w  d1,d3
        add.w   d2,d3                    ; total length
        moveq   #hdr.nmln,d0             ; name length
        sub.w   d3,d0                    ; name too long?
        bge.s   cvax_ok

        add.w   d0,d3                    ; name shorter
        add.w   d0,d2                    ; extension shorter
        moveq   #err.inam,d0
        bra.s   cvax_copy

cvax_ok
        moveq   #0,d0

cvax_copy
        move.w  d3,(a0)+                 ; ... string length

        bra.s   cvax_nmend
cvax_nmloop
        move.b  (a1)+,(a0)+
cvax_nmend
        dbra    d1,cvax_nmloop

        bra.s   cvax_exend
cvax_exloop
        move.b  (a2)+,(a0)+
cvax_exend
        dbra    d2,cvax_exloop

        tst.l   d0
        movem.l (sp)+,cvax.reg
        rts
        end
