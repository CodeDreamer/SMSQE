; General IO utility: newline     V2.00    1988  Tony Tebby

        section gen_util

        xdef    gu_nl

        xref    gu_iow

        include 'dev8_keys_qdos_io'
        include 'dev8_keys_k'

;+++
; This routine sends a newline
;
;       a0 c  p channel ID
;       error returns standard
;---
gu_nl
        movem.l d1/a1,-(sp)
        moveq   #k.nl,d1                 ; send newline
        moveq   #iob.sbyt,d0 
        bsr.s   gu_iow
        movem.l (sp)+,d1/a1
        rts
        end
