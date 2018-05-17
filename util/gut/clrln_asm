; General IO utility: clear line   V2.00    1988  Tony Tebby

        section gen_util

        xdef    gu_clrln

        xref    gu_iowp

        include 'dev8_keys_qdos_io'

;+++
; This routine clears line
;
;       a0 c  p channel ID
;       error returns standard
;---
gu_clrln
        moveq   #iow.clrl,d0             ; clear to line
        bra.s   gu_iowp  
        end
