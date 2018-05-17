; General IO utility: fetch byte   V2.00    1988  Tony Tebby

        section gen_util

        xdef    gu_fbyte

        xref    gu_iow

        include 'dev8_keys_qdos_io'

;+++
; This routine does simple fetch byte
;
;       d1  r   byte fetched
;       a0 c  p channel ID
;       a1    p
;       error returns standard
;---
gu_fbyte
        move.l  a1,-(sp)
        moveq   #iob.fbyt,d0             ; fetch byte
        bsr.s   gu_iow
        move.l  (sp)+,a1
        rts
        end
