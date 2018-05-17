; General IO utility: fetch byte with cursor   V2.00    1988  Tony Tebby

        section gen_util

        xdef    gu_fbcur

        xref    gu_iow

        include 'dev8_keys_qdos_io'

;+++
; This routine does fetch byte with cursor enabled
;
;       d1  r   byte fetched
;       a0 c  p channel ID
;       a1    p
;       error returns standard
;---
gu_fbcur
        move.l  a1,-(sp)
        moveq   #iow.ecur,d0             ; enable cursor
        bsr.s   gu_iow
        moveq   #iob.fbyt,d0
        bsr.s   gu_iow                   ; fetch byte
        movem.l d0/d1,-(sp)
        moveq   #iow.dcur,d0             ; and disable cursor
        bsr.s   gu_iow
        movem.l (sp)+,d0/d1/a1           ; restore byte read and status
        tst.l   d0
        rts
        end
