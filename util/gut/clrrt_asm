; General IO utility: clear to end of line   V2.00    1988  Tony Tebby

        section gen_util

        xdef    gu_clrrt

        xref    gu_iowp

        include 'dev8_keys_qdos_io'

;+++
; This routine clears to end of line
;
;       a0 c  p channel ID
;       error returns standard
;---
gu_clrrt
        moveq   #iow.clrr,d0             ; clear to right
        bra.s   gu_iowp  
        end
