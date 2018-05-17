; General IO utility: move to next column   V2.00    1988  Tony Tebby

        section gen_util

        xdef    gu_ncol

        xref    gu_iowp

        include 'dev8_keys_qdos_io'

;+++
; This routine moves to next column
;
;       a0 c  p channel ID
;       error returns standard
;---
gu_ncol
        moveq   #iow.ncol,d0             ; next column
        bra.s   gu_iowp
        end

