; General IO utility: move to previous column     V2.00    1988  Tony Tebby

        section gen_util

        xdef    gu_pcol

        xref    gu_iowp

        include 'dev8_keys_qdos_io'

;+++
; This routine moves to previous column
;
;       a0 c  p channel ID
;       error returns standard
;---
gu_pcol
        moveq   #iow.pcol,d0             ; previous column
        bra.s   gu_iowp
        end
