; General IO utility: send standard string  V2.00    1988  Tony Tebby

        section gen_util

        xdef    gu_sstrg

        xref    gu_iow

        include 'dev8_keys_qdos_io'

;+++
; This routine sends a standard string
;
;       a0 c  p channel ID
;       a1 c  p pointer to string
;       error returns standard
;---
gu_sstrg
        movem.l d1/d2/a1,-(sp)
        move.w  (a1)+,d2
        moveq   #iob.smul,d0
        bsr.s   gu_iow
        movem.l (sp)+,d1/d2/a1
        rts
        end
